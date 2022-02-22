## ve-project

#### 项目结构

* **commons-api** 第二方组件api
* **commons-service** 第二方组件接入
* **gateway-service** 服务网关
* **infrastructure** 基础设施
    * **ve-base** 公共依赖
    * **ve-feign** 微服务调用
    * **ve-file** 文件内容
    * **ve-job** 任务调度
    * **ve-rabbitmq** 消息队列
    * **ve-redis** 缓存
    * **ve-rest** web层依赖
* **message-api** 消息服务api
* **message-service** 消息服务
* **thirdparty-api** 第三方服务接入api
* **thirdparty-service** 第三方服务接入
* **user-api** 用户服务api
* **user-service** 用户服务

#### 目标:

1. 接入minio文件服务器
1. 接入七牛文件服务器
1. 接入activiti工作流引擎
1. 实现api网关,签名验签,证书颁发,对称加密
1. 设计通用的消息服务
1. 接入微信小程序内容
1. 其他基础功能...
