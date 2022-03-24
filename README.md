## ve-project

### 项目结构

* **infrastructure** 基础设施
    * **ve-base** 公共依赖
    * **ve-feign** 微服务调用
    * **ve-file** 文件内容
    * **ve-job** 任务调度
    * **ve-rabbitmq** 消息队列
    * **ve-redis** 缓存
    * **ve-rest** web层依赖
* **gateway-service** 服务网关
* **commons-api** 第二方组件api
* **commons-service** 第二方组件接入
    * minio文件上传
    * tess4j身份证ocr
    * tess4j银行卡ocr
* **message-api** 消息服务api
* **message-service** 消息服务
    * 消息通知业务
    * 阿里云短信
* **thirdgateway-api** 第三方服务接入api
* **thirdgateway-service** 第三方服务接入
    * 微信获取openid
    * 微信获取手机号
    * 微信公众号消息
    * 百度人脸比对
    * 阿里银联3要素
    * 阿里云身份证ocr
    * 阿里云银行卡ocr
* **user-api** 用户服务api
* **user-service** 用户服务
    * 微信登录
    * 手机号登录
    * 账号密码登录
    * 用户_登录表设计

### TODO:

1. graylog
2. websocket
3. 去掉swagger
4. 国际化
5. 发邮件
6. 七牛文件服务器
7. activiti工作流引擎
8. 实现api网关
9. 短信业务流程改造,远程调用(同步,实时返回调用结构,然后异步存储结果)
10. 其他基础功能...

### STARTUP:
###### 使用docker快速部署相关中间件
```
1. 一键安装docker
curl -sSL https://get.daocloud.io/docker | sh
2. 安装pgsql
docker run --name postgres -e POSTGRES_PASSWORD=ve2021.Ve -p 10101:5432 --restart=always -v /var/data/postgresql/data:/var/lib/postgresql/data -d postgres
3. 安装rabbitmq
docker run -d --name rabbitmq --restart=always -p 10102:5672 -p 10201:15672 -v /var/data/rabbitmq/data:/var/lib/rabbitmq -e RABBITMQ_DEFAULT_VHOST=local  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.9.5-management
4. 安装redis
docker run -d --name redis -p 10103:6379 --restart=always redis --requirepass ve2021.Ve
5. 安装nacos
docker  run --name nacos -d -p 10202:8848 --privileged=true --restart=always -e JVM_XMS=256m -e JVM_XMX=256m -e MODE=standalone -v /var/data/nacos/logs:/home/nacos/logs -v /var/data/nacos/init.d:/home/nacos/init.d nacos/nacos-server
6. nacos创建local命名空间
7. 目前暂时可以启动各个服务了.其他还没看
* todo...
```
