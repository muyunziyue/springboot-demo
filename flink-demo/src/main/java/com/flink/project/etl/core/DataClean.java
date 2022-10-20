package com.flink.project.etl.core;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.flink.project.etl.source.NxRedisSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Properties;

/**
 * 数据清洗
 * 码表
 */
public class DataClean {
    public static void main(String[] args) throws Exception {

        /**
         * 步骤一：获取执行环境
         *
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //我们是从Kafka里面读取数据，所以这儿就是topic有多少个partition，那么就设置几个并行度。
        env.setParallelism(25);
         //设置checkpoint的参数
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(10000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        String topic="data";
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers","10.0.0.9:9092");
        consumerProperties.put("group.id","allTopic_consumer");
        consumerProperties.put("enable.auto.commit", "false");
        consumerProperties.put("auto.offset.reset","earliest");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic,
                new SimpleStringSchema(), consumerProperties);
        /**
         * 步骤二：获取数据源
         * 1. kafka数据源
         * 2. Redis数据源
         */
        DataStreamSource<String> allData = env.addSource(consumer);


        //大家使用的时候要注意，这个数据建议不要太多
        //因为广播的数据是在内存里面的。
        DataStream<HashMap<String, String>> mapData = env.addSource(new NxRedisSource()).broadcast();


        /**
         * 步骤三，ETL处理
         */
        SingleOutputStreamOperator<String> etlData = allData.connect(mapData).flatMap(new ETLProcessFunction());


        String outputTopic="allDataClean";
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers","192.168.16.254:9092");
        FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(outputTopic,
                new KeyedSerializationSchemaWrapper<String>(new SimpleStringSchema()),
                producerProperties);

        //搞一个Kafka的生产者
        etlData.addSink(producer);

        env.execute("DataClean");

    }



    public static class ETLProcessFunction implements CoFlatMapFunction<String, HashMap<String, String>, String>{
        //用来存广播变量里面的数据
        HashMap<String, String> allMap = new HashMap<String, String>();

        //里面处理的是kafka的数据
        @Override
        public void flatMap1(String line, Collector<String> out) throws Exception {

            JSONObject jsonObject = JSONObject.parseObject(line);
            String dt = jsonObject.getString("dt");
            String countryCode = jsonObject.getString("countryCode");
            //可以根据countryCode获取大区的名字
            String area = allMap.get(countryCode);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject dataObject = data.getJSONObject(i);
                System.out.println("大区："+area);
                dataObject.put("dt", dt);
                dataObject.put("area", area);
                //下游获取到数据的时候，也就是一个json格式的数据
                out.collect(dataObject.toJSONString());
            }


        }

        //里面处理的是redis里面的数据
        //allMap里面给一些初始数据
        @Override
        public void flatMap2(HashMap<String, String> map,
                             Collector<String> collector) throws Exception {
            System.out.println(map.toString());
            allMap = map;
        }
    }

}
