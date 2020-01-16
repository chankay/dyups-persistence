package xyz.grafana.dyupspersistence.service.impl;

import xyz.grafana.dyupspersistence.dao.UpstreamConfigMapper;
import xyz.grafana.dyupspersistence.dto.Result;
import xyz.grafana.dyupspersistence.model.UpstreamConfig;
import xyz.grafana.dyupspersistence.model.UpstreamConfigWithBLOBs;
import xyz.grafana.dyupspersistence.service.ConfigService;
import xyz.grafana.dyupspersistence.utils.OkHttpUtil;
import xyz.grafana.dyupspersistence.utils.ShellUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    UpstreamConfigMapper upstreamConfigMapper;
    @Value("${dyups.remotePath}")
    String remotePath;
    @Value("${dyups.user}")
    String user;
    @Value("${dyups.password}")
    String password;
    @Value("${dyups.server}")
    String host;
    @Value("${dyups.interfacePath}")
    String inrerfacePath;
    @Value("${dyups.interfacePort}")
    String inrerfacePort;

    public Result create(UpstreamConfigWithBLOBs upstreamConfig){
        Result result = new Result();
        result.setCode(0);
        String message = "";

        try {
            int res = upstreamConfigMapper.insertSelective(upstreamConfig);
            if (res < 0) {
                result.setCode(-1);
                message = String.format(" [插入数据库失败：%s] ", res);
                result.setMessage(message);
                return result;
            }

            result = syncConfig(upstreamConfig);
        }catch (Exception e){
            message = e.getMessage();
            result.setMessage(message);
        }

        return result;
    }

    public Result update(UpstreamConfigWithBLOBs upstreamConfig){
        Result result = new Result();
        result.setCode(0);
        String message = "";

        try{
            int res = upstreamConfigMapper.updateByPrimaryKeySelective(upstreamConfig);
            if (res < 0) {
                result.setCode(-1);
                message += String.format(" [更新数据库失败：%s] ", res);
                result.setMessage(message);
                return result;
            }

            result = syncConfig(upstreamConfig);
        }catch (Exception e){
            message = e.getMessage();
            result.setMessage(message);
        }

        return result;

    }

    public UpstreamConfigWithBLOBs selectById(int configId){
        UpstreamConfigWithBLOBs upstreamConfig = upstreamConfigMapper.selectByPrimaryKey(configId);
        return upstreamConfig;
    }

    public Result delete(int configId){
        Result result = new Result();
        String message = "";

        try{
            UpstreamConfigWithBLOBs upstreamConfig = upstreamConfigMapper.selectByPrimaryKey(configId);
            int delDbResult = upstreamConfigMapper.deleteByPrimaryKey(configId);
            if(delDbResult < 0){
                message += "数据库删除失败";
            }
            String[] hostArr = host.split(";");
            for(int i=0;i<hostArr.length;i++){
                boolean delFileResult = delFile(upstreamConfig.getName(), hostArr[i]);
                if(delFileResult){
                    message += hostArr[i] + " 文件删除成功\n ";
                }else {
                    message += hostArr[i] + " 文件删除失败\n ";
                }
            }
        }catch (Exception e){
            result.setCode(-1);
            message += "异常" + e.getMessage();
            logger.error(e.getMessage());
        }

        result.setMessage(message);
        return result;
    }

    public List<UpstreamConfigWithBLOBs> selectConfig(){
        List<UpstreamConfigWithBLOBs> upstreamConfigs = upstreamConfigMapper.getConfig();
        return upstreamConfigs;
    }

    private boolean syncFile(UpstreamConfigWithBLOBs upstreamConfig, String oneHost){
        String content = "";
        content += "upstream " + upstreamConfig.getName() + "{\n";
        content += upstreamConfig.getUpstream();
        content += "\n}\n";
        content +="server {\n";
        content += upstreamConfig.getServer();
        content += "\n}";
        String filepath = remotePath + upstreamConfig.getName() + ".conf";
        String cmd = String.format("echo '''%s''' > %s", content, filepath);

        boolean res = ShellUtils.connectLinux(oneHost, user, password, cmd);
        logger.info(String.valueOf(res));
        return res;
    }

    private boolean delFile(String name, String oneHost){
        String filepath = remotePath + name + ".conf";
        String cmd = String.format("rm -rf %s", filepath);

        boolean res = ShellUtils.connectLinux(oneHost, user, password, cmd);
        logger.info(String.valueOf(res));
        return res;
    }

    private Result syncConfig(UpstreamConfigWithBLOBs upstreamConfig){
        Result result = new Result();
        String message = "";
        String[] hostArr = host.split(";");
        for(int i=0;i<hostArr.length;i++){
            String oneHost = hostArr[i];

            //调用dyups接口
            Result interfaceResult = dyupsChange(upstreamConfig.getName(), upstreamConfig.getUpstream(), oneHost);
            result.setCode(interfaceResult.getCode());

            if(interfaceResult.getCode() == 0){
                message +=  oneHost + "接口调用成功";
                boolean fileRes = syncFile(upstreamConfig, oneHost);
                if(fileRes){
                    message += ",同步配置文件成功";
                }else{
                    message += ",同步配置文件失败";
                    result.setCode(-1);
                }
            }else{
                message += oneHost + "接口调用失败:" + interfaceResult.getMessage();
            }

            message += "\n";
        }

        result.setMessage(message);
        return result;
    }

    public Result dyupsChange(String name, String upstream, String host){
        Result result = new Result();
        try{
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "text/plain");
            String url = "http://" + host + ":" + inrerfacePort + inrerfacePath + name;
            String content = upstream;
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), content);
            Response response = OkHttpUtil.httpPost(url, headerMap, requestBody);
            boolean isSuccess = response.isSuccessful();
            String responseContent = response.body().string();
            if (response != null){
                response.close();
            }

            if (isSuccess){
                logger.info(responseContent);
                result.setCode(0);
                result.setMessage(responseContent);
            } else {
                logger.error("请求失败: " + responseContent);
                result.setCode(-1);
                result.setMessage(responseContent);
            }
            return result;
        } catch (Exception e){
            logger.error(e.getMessage());
            result.setCode(-1);
            result.setMessage(e.getMessage());
            return result;
        }
    }
}
