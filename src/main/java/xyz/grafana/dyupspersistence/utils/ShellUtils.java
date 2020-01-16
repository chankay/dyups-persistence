package xyz.grafana.dyupspersistence.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import xyz.grafana.dyupspersistence.model.RemoteConnect;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ShellUtils {
    private static final Logger logger = LoggerFactory.getLogger(ShellUtils.class);

    private static String DEFAULTCHARTSET = "UTF-8";
    private static Connection conn;

    /**
     * @throws
     * @Title: login
     * @Description: 用户名密码方式  远程登录linux服务器
     * @return: Boolean
     */
    public static Boolean login(RemoteConnect remoteConnect) {
        boolean flag = false;
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();// 连接
            flag = conn.authenticateWithPassword(remoteConnect.getUserName(), remoteConnect.getPassword());// 认证
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param remoteConnect
     * @param keyFile       一个文件对象指向一个文件，该文件包含OpenSSH密钥格式的用户的DSA或RSA私钥(PEM，不能丢失"-----BEGIN DSA PRIVATE KEY-----" or "-----BEGIN RSA PRIVATE KEY-----"标签
     * @param keyfilePass   如果秘钥文件加密 需要用该参数解密，如果没有加密可以为null
     * @return Boolean
     * @throws
     * @Title: loginByKey
     * @Description: 秘钥方式  远程登录linux服务器
     */
    public static Boolean loginByFileKey(RemoteConnect remoteConnect, File keyFile, String keyfilePass) {
        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUserName(), keyFile, keyfilePass);
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param remoteConnect
     * @param keys          一个字符[]，其中包含用户的DSA或RSA私钥(OpenSSH密匙格式，您不能丢失“----- begin DSA私钥-----”或“-----BEGIN RSA PRIVATE KEY-----“标签。char数组可以包含换行符/换行符。
     * @param keyPass       如果秘钥字符数组加密  需要用该字段解密  否则不需要可以为null
     * @return Boolean
     * @throws
     * @Title: loginByCharsKey
     * @Description: 秘钥方式  远程登录linux服务器
     */
    public static Boolean loginByCharsKey(RemoteConnect remoteConnect, char[] keys, String keyPass) {

        boolean flag = false;
        // 输入密钥所在路径
        // File keyfile = new File("C:\\temp\\private");
        try {
            conn = new Connection(remoteConnect.getIp());
            conn.connect();
            // 登录认证
            flag = conn.authenticateWithPublicKey(remoteConnect.getUserName(), keys, keyPass);
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param cmd 脚本命令
     * @throws
     * @Title: execute
     * @Description: 远程执行shll脚本或者命令
     * @return: result 命令执行完毕返回结果
     */
    public static String execute(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            // 如果为得到标准输出为空，说明脚本执行出错了
            if (StringUtils.isBlank(result)) {
                result = processStdout(session.getStderr(), DEFAULTCHARTSET);
            }
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param shell脚本或者命令
     * @return String 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     * @throws
     * @Title: executeSuccess
     * @Description: 远程执行shell脚本或者命令
     */
    public static String executeSuccess(String cmd) {
        String result = "";
        try {
            Session session = conn.openSession();// 打开一个会话
            session.execCommand(cmd);// 执行命令
            result = processStdout(session.getStdout(), DEFAULTCHARTSET);
            conn.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param in      输入流对象
     * @param charset 编码
     * @return String 以纯文本的格式返回
     * @throws
     * @Title: processStdout
     * @Description: 解析脚本执行的返回结果
     */
    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * @return String
     * @throws
     * @Title: ConnectLinux
     * @Description: 通过用户名和密码关联linux服务器
     */
    public static boolean connectLinux(String ip, String userName, String password, String commandStr) {

        logger.info("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  commandStr:"
                + commandStr);

        String returnStr = "";
        boolean result = true;

        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);
        try {
            if (login(remoteConnect)) {
                returnStr = execute(commandStr);
                System.out.println(result);
                conn.close();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * @param host       ip(其他服务器)
     * @param username   用户名(其他服务器)
     * @param password   密码(其他服务器)
     * @param remoteFile 文件位置(其他服务器)
     * @param localDir   本服务器目录
     * @return void
     * @throws IOException
     * @throws
     * @Title: scpGet
     * @Description: 从其他服务器获取文件到本服务器指定目录
     */
    public static void scpGet(String ip, String userName, String password, String remoteFile, String localDir)
            throws IOException {

        logger.info("ConnectLinuxCommand  scpGet===" + "ip:" + ip + "  userName:" + userName + "  remoteFile:"
                + remoteFile + "  localDir:" + localDir);
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);

        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localDir);
            conn.close();
        }
    }

    /**
     * @param host
     * @param username
     * @param password
     * @param localFile
     * @param remoteDir
     * @return void
     * @throws IOException
     * @throws
     * @Title: scpPut
     * @Description: 将文件复制到其他计算机中
     */
    public static void scpPut(String ip, String userName, String password, String localFile, String remoteDir)
            throws IOException {
        logger.info("ConnectLinuxCommand  scpPut===" + "ip:" + ip + "  userName:" + userName + "  localFile:"
                + localFile + "  remoteDir:" + remoteDir);

        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(ip);
        remoteConnect.setUserName(userName);
        remoteConnect.setPassword(password);

        if (login(remoteConnect)) {
            SCPClient client = new SCPClient(conn);
            client.put(localFile, remoteDir);
            conn.close();
        }
    }
}
