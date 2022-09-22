package com.flink.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * @author ldx
 * @date 2022/9/20
 */
public class WordCountWithKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        String topic="test";
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers","192.168.123.102:9092");
        consumerProperties.setProperty("group.id","testSlot_consumer");

        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), consumerProperties);

        // 3 task
        DataStreamSource<String> kafkaStreamSource = env.addSource(kafkaConsumer).setParallelism(3);
        // 2 task
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordOneStream = kafkaStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {

            @Override
            public void flatMap(String line,
                                Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    out.collect(Tuple2.of(word, 1));
                }
            }
        }).setParallelism(2);

        // 2 task
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = wordOneStream
                .keyBy(value -> value.f0)
                .sum(1)
                .setParallelism(2);

        result.map( tuple -> tuple.toString()).setParallelism(2)
                // 1 task
                .print().setParallelism(1);

        env.execute("WordCount2");

    }
}
