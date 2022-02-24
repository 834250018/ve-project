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
* **thirdgateway-api** 第三方服务接入api
* **thirdgateway-service** 第三方服务接入
* **user-api** 用户服务api
* **user-service** 用户服务

#### FEAT:

1. 百度人脸比对
1. 阿里银联3要素
1. 阿里云身份证ocr
1. 阿里云银行卡ocr
1. 阿里云短信
1. minio文件上传
1. 设计通用的消息服务
1. 微信登录,手机号登录,账号密码登录
1. 微信公众号消息(需开通开放平台)

#### TODO:

1. graylog
1. websocket
1. 去掉swagger
1. 国际化
1. 发邮件
1. 七牛文件服务器
1. activiti工作流引擎
1. 实现api网关,签名验签,证书颁发,对称加密
1. 短信业务流程改造,远程调用(同步,实时返回调用结构,然后异步存储结果)
1. 其他基础功能...
