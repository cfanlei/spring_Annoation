package com.cfanlei.mykafka.consumer;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

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
