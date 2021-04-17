# 配置jdk

```shell
编辑/etc/profile

添加 

export JAVA_HOME=/usr/local/jdk16/jdk1.8.0_281
export PATH=$JAVA_HOME/bin:$PATH
export JRE_HMOE=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib

测试环境
java -version


```



# 安装zk

将配置zoo.sample.cfg 改为zoo.cfg

```shell
# The number of milliseconds of each tick
# zk服务器的心跳时间
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
# 投票选举新leader的初始化时间
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
#数据目录
dataDir=/tmp/zookeeper/data
#日志目录
#dataLogDir=/tmp/zookeeper/log
# the port at which the clients will connect
# 对外端口
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1

## Metrics Providers
#
# https://prometheus.io Metrics Exporter
#metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
#metricsProvider.httpPort=7000
#metricsProvider.exportJvmInfo=true

```

启动命令

```shell
配置环境

$ source /etc/profile


. zkServer.sh   {start|start-foreground|stop|version|restart|status|print-cmd}

bin/zkServer.sh start

```

# kafka

```
1.修改config下 server.properties

2.bin/kafka-server-start.sh  config/server.properties # 启动kafka

3.查看启动 ps -ef | grep kafka
```





# kafka测试消息的生产与消费

## 1.创建一个主题

* 命令如下：

```shell
bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic cfanlei --partitions 2 --replication-factor 1
2个分区  1个副本

--zookeeper   指定kafka所连接的zookeeper 服务地址
--topic  指定了所要创建主题的名称
--partitions  指定了分区的个数
--replication-factor  指定了副本因子
--create  创建主题的动作命令
```

* 展示所有的主题

```
bin/kafka-topics.sh --zookeeper localhost:2181 --list
```

* 查看主题详情

```shell
bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic cfanlei
```

* 启动消费端接收消息

```shell
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cfanlei

生产者创建发送消息将接收
hello kafka
i m cfanlei


--bootstrap-server 指定了kafka集群的地址
--topic 指定了订阅消息的主题
```

* 生产端发送消息

```shell
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic cfanlei
--brocker-list 指定了连接的kafka集群的地址
--topic  指定了发送消息时的主题
```

## 2.java创建生产者

```java
/**
 * kafka生产者
 */
public class ProducerFastStart {
    private static final  String BROKERLIST ="192.168.106.135:9092";
    private static final String TOPIC ="cfanlei";
    public static void main(String[] args) {
        Properties properties =new Properties();

        //设置key初始化器
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,2);

        //设置值初始化器
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        //设置集群地址
        properties.put("bootstrap.servers",BROKERLIST);
        KafkaProducer<String,String> producer =new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record =new ProducerRecord<>(TOPIC,"kafka-demo","hello kafka,im cfanlei o!!");
                try{
                    producer.send(record);
                }catch (Exception e){
                    e.printStackTrace();
                }
                producer.close();
    }
}

运行错误


java.net.UnknownHostException: cfanlei
	at java.net.InetAddress.getAllByName0(InetAddress.java:1280)
	at java.net.InetAddress.getAllByName(InetAddress.java:1192)
	at java.net.InetAddress.getAllByName(InetAddress.java:1126)
	at org.apache.kafka.clients.ClientUtils.resolve(ClientUtils.java:104)
	at org.apache.kafka.clients.ClusterConnectionStates$NodeConnectionState.currentAddress(ClusterConnectionStates.java:394)
	at org.apache.kafka.clients.ClusterConnectionStates$NodeConnectionState.access$200(ClusterConnectionStates.java:354)
	at org.apache.kafka.clients.ClusterConnectionStates.currentAddress(ClusterConnectionStates.java:142)
	at org.apache.kafka.clients.NetworkClient.initiateConnect(NetworkClient.java:920)
	at org.apache.kafka.clients.NetworkClient.access$700(NetworkClient.java:67)
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:1092)
	at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:983)
	at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:533)
	at org.apache.kafka.clients.producer.internals.Sender.runOnce(Sender.java:312)
	at org.apache.kafka.clients.producer.internals.Sender.run(Sender.java:235)



这个错误说是找不到cfanlei这个node，我们使用hostname查看kafka服务器，发现cfanlei正是这台机器的机器名，所以我们在本地修改host（C:\Windows\System32\drivers\etc）文件进行映射：

192.168.106.135   cfanlei
    
    
    
    优化
    
    /**
 * kafka生产者
 */
public class ProducerFastStart {
    private static final  String BROKERLIST ="192.168.106.135:9092";
    private static final String TOPIC ="cfanlei";
    public static void main(String[] args) {
        Properties properties =new Properties();

        //设置key初始化器
        //properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,2);

        //设置值初始化器
        //properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        //设置集群地址
        //properties.put("bootstrap.servers",BROKERLIST);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKERLIST);
        KafkaProducer<String,String> producer =new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record =new ProducerRecord<>(TOPIC,"kafka-demo","hello kafka,im cfanlei o!!");
                try{
                    producer.send(record);
                }catch (Exception e){
                    e.printStackTrace();
                }
                producer.close();
    }
}

```

## 3.java创建消息消费者

```java
/**
 * 消息消费者
 */
public class ConsumerFastStart {

    private static final  String BROKERLIST ="192.168.106.135:9092";

    private static final String TOPIC ="cfanlei";

    private static final String GROUPID = "group.demo";
    public static void main(String[] args) {
        Properties properties =new Properties();

        //设置key初始化器
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        //设置值初始化器
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        //设置集群地址
        properties.put("bootstrap.servers",BROKERLIST);
        properties.put("group.id",GROUPID);

        KafkaConsumer<String,String> consumer =new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC));

        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String,String> record :records){
                System.err.println(record.value());
            }
        }

    }
}

优化
    
    
/**
 * 消息消费者
 */
public class ConsumerFastStart {

    private static final  String BROKERLIST ="192.168.106.135:9092";

    private static final String TOPIC ="cfanlei";

    private static final String GROUPID = "group.demo";
    public static void main(String[] args) {
        Properties properties =new Properties();

        //设置key初始化器
        //properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //properties.put

        //设置值初始化器
        //properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        //设置集群地址
        //properties.put("bootstrap.servers",BROKERLIST);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKERLIST);

        //properties.put("group.id",GROUPID);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,GROUPID);

        KafkaConsumer<String,String> consumer =new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC));

        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for(ConsumerRecord<String,String> record :records){
                System.err.println(record.value());
            }
        }

    }
}

```



