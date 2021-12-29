package com.myjava.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author lidexiu
 * @Date 2021/12/7
 * @Description
 */
public class Test {
    public static void main(String[] args) {

            Properties props = new Properties();
            props.put("bootstrap.servers", "192.168.3.170:9092");
            props.put("group.id", "l_test2");//消费者的组id
            props.put("enable.auto.commit", "true");
            props.put("auto.offset.reset", "earliest");
            props.put("auto.commit.interval.ms", "1000");
            props.put("session.timeout.ms", "30000");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
            //订阅主题列表topic
            consumer.subscribe(Arrays.asList("billInfo"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records){
                    System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
                }
            }
        }
}
