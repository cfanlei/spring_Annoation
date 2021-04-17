package com.cfanlei.mykafka.producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

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
