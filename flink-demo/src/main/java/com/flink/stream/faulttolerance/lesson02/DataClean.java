package com.flink.stream.faulttolerance.lesson02;


import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * 实时ETL
 */
public class DataClean {
    public static void main(String[] args) throws Exception{
        /**
         *  步骤一：获取执行环境
         */

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         *    步骤二：设置参数
         */
        env.setParallelism(3);//假设Kafka的主题是3个分区
        //设置checkpoint
        env.enableCheckpointing(60000);
        env.setStateBackend(new RocksDBStateBackend("hdfs://xxx"));

        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(10000);
        //flink停止的时候要不要清空checkpoint的数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        /**
         *   步骤三：获取Kafka数据源
         */
        String topic="data";
        Properties properties = new Properties();
        properties.put("bootstrap.servers","10.0.0.9:9092");
        properties.put("group.id","dataclean_consumer");
        properties.put("enable.auto.commit","false");
        properties.put("auto.offset.reset","earliest");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                topic,
                new SimpleStringSchema(),
                properties
        );
        DataStreamSource<String> allData = env.addSource(consumer);

        /**
         * 步骤四：数据的处理
         */
        SingleOutputStreamOperator<String> etlDataStream = allData.map(new MapFunction<String, String>() {
            @Override
            public String map(String line) throws Exception {
                return "test_" + line;
            }
        });

        /**
         * 步骤五：数据存储
         */
        String etltopic="result";
        Properties sinkProperties = new Properties();
        sinkProperties.put("bootstrap.servers","10.0.0.9:9092");
        FlinkKafkaProducer<String> kafkaSink = new FlinkKafkaProducer<>(etltopic,
                new SimpleStringSchema(),
                sinkProperties);


        etlDataStream.addSink(kafkaSink);

        /**
         * 步骤六：启动程序
         */
        env.execute("data clean");

    }
}
