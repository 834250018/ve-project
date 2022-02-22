## rabbitmq接入手册

#### 步骤1. 权限配置

```
目前message,user各模块配置对应的使用账号
账号user密码XXXXXXX
账号message密码XXXXXXXX

如果需要增加对应模块的账号,需要登录管理员账号创建用户并授予权限,权限如下
配置权限: 删掉,为空
写权限: ^(all2|模块名2).*
读权限: .*(2all|2模块名).*

比如用户模块
配置权限: 删掉,为空
写权限: ^(all2|user2).*
读权限: .*(2all|2user).*
```

#### 步骤2. 在ve-commons中的MqConstant类增加常量,定义队列名,交换机名,配对路由key名(请严格遵守命名规则)

```
 /** 
 * 命名规则:
 * 生产者2消费者_业务_queue
 * 生产者2消费者_业务_exchange
 * 生产者2消费者_业务_routing_key
 * 如果生产者或消费者不明确,则使用all替代
*/
    /**
     * 队列名
     */
    String ALL2MESSAGE_SMS_QUEUE = "all2message_sms_queue";
    /**
     * 交换机名
     */
    String ALL2MESSAGE_SMS_EXCHANGE = "all2message_sms_exchange";
    /**
     * routingKey名
     */
    String ALL2MESSAGE_SMS_ROUTING_KEY = "all2message_sms_routing_key";
    /*
```

#### 步骤3. commons-service中的RabbitConfig构造器里面持久化队列、交换机、绑定关系,并重启以创建配置

```
        // 用户行为队列,为了防止变量名过多,请代码封装到一个独立的方法
        Queue q1 = new Queue(MqConstant.ALL2MESSAGE_SMS_QUEUE, true);
        amqpAdmin.declareQueue(q1);
        DirectExchange e1 = new DirectExchange(MqConstant.ALL2MESSAGE_SMS_EXCHANGE, true, false);
        amqpAdmin.declareExchange(e1);
        Binding b1 = BindingBuilder.bind(q1).to(e1).with(MqConstant.ALL2MESSAGE_SMS_ROUTING_KEY);
        amqpAdmin.declareBinding(b1);
```

#### 步骤4. 生产者跟消费者api模块中引入rabbitMq依赖

````
        <dependency>
            <groupId>cn.ve</groupId>
            <artifactId>ve-rabbitmq</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-slf4j-impl</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
````

#### 步骤5. 配置rabbitmq账号信息(nacos)

```
spring.rabbitmq.host=
spring.rabbitmq.port=
spring.rabbitmq.username= # 不同服务使用不同账号
spring.rabbitmq.password= 
spring.rabbitmq.virtual-host=dev # 跟随环境变量
```

#### 步骤6. 创建消息载体(继承BaseMqParam,order-api或者user-api中创建,看谁依赖谁)

```
@Getter
@Setter
public class RateMqParam extends BaseMqParam {

    // todo 这里加上业务参数,在这个场景中,假设用户9527被评价,打了4分,参数共两个:userId,rate
    private Long userId; // 用户id
    private BigDecimal rate; // 评分

    @Override
    protected String bindExchange() {
        // todo 重写此方法告知交换机名称
        return MqConstant.ALL2MESSAGE_SMS_EXCHANGE;
    }
    @Override
    protected String bindRoutingKey() {
        // todo 重写此方法告知路由key
        return MqConstant.ALL2MESSAGE_SMS_ROUTING_KEY;
    }
    // 固定继承构造器
    public UserUpdatedMqParam(AmqpTemplate amqpTemplate) {
        super(amqpTemplate);
    }
}
```

#### 步骤7. 生产者发送消息

```
    @Resource
    private RabbitTemplate rabbitTemplate;
    
    public void demo() {
        // 创建消息容器
        RateMqParam rateMqParam = new RateMqParam();
        // 设置业务参数
        rateMqParam.setRate(BigDecimal.valueOf(4));
        rateMqParam.setUserId(9527L);
        // 发送mq
        rateMqParam.sendMessage();
    }
```

#### 步骤8. 消费者监听队列并实现业务

```
    // todo 下面一个监听方法仅在消费者(user)添加
    @RabbitListener(queues = "all2message_sms_queue")
    public void smsRabbitListener(Message message) {
        RateMqParam rateMqParam = (RateMqParam)SerializationUtils.deserialize(message.getBody());
        log.info(JSON.toJSONString(userUpdatedMqParam));
        // todo 如果业务依赖某个spring-bean,可以在类上面加上@DependOn注解
    }
```