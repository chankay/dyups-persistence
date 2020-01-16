package xyz.grafana.dyupspersistence.controller;

import xyz.grafana.dyupspersistence.dto.Result;
import xyz.grafana.dyupspersistence.model.UpstreamConfig;
import xyz.grafana.dyupspersistence.model.UpstreamConfigWithBLOBs;
import xyz.grafana.dyupspersistence.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @ResponseBody
    public Result addConfig(@RequestBody UpstreamConfigWithBLOBs upstreamConfig){
        Result result = configService.create(upstreamConfig);
        return result;
    }

    @RequestMapping(value = "/config", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteConfig(@RequestParam int id){
        Result result = configService.delete(id);
        return result;
    }

    @RequestMapping(value = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Result updateConfig(@RequestBody UpstreamConfigWithBLOBs upstreamConfig){
        Result result = configService.update(upstreamConfig);
        return result;
    }

    @RequestMapping(value = "/configData", method = RequestMethod.GET)
    @ResponseBody
    public Result<UpstreamConfig> getConfigData(@RequestParam("id") Integer id){
        Result result = new Result();
        UpstreamConfig upstreamConfig = configService.selectById(id);
        result.setData(upstreamConfig);
        return result;
    }

    @RequestMapping(value = "/configList", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<UpstreamConfigWithBLOBs>> getConfigList(){
        Result result = new Result();
        List<UpstreamConfigWithBLOBs> upstreamConfigs = configService.selectConfig();
        result.setData(upstreamConfigs);
        return result;
    }
}
