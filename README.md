# dyups-upstream

# tengine dyups动态配置持久化
支持多服务器，多个服务器需要创建相同的用于修改配置文件的用户，配置文件目录授写权限给该用户


打包命令：mvn clean package 

dyups配置说明：
server: 服务器ip，多个用 ; 分隔
remotePath: upstream配置文件目录，需要在主配置文件中引入，例如/ext/dyups/conf.d/
interfacePath: dyups接口路径，例如：/upstream/
interfacePort: dyups接口端口，例如：8083
user: 用于修改upstream配置文件的系统用户,例如：dyups
password: 用于修改upstream配置文件的系统用户密码