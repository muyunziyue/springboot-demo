package com.flink.demo;

import com.flink.demo.entity.Entity;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ldx
 * @date 2022/9/20
 */
public class WorldCount {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> datasource = env.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<Entity> result = datasource.flatMap((line, out) -> {
            String[] words = line.split(",");
            for (String word : words) {
                out.collect(new Entity(word.trim(), 1));
            }
        }, Types.POJO(Entity.class))
                .keyBy(Entity::getKey)
                .sum("count");

        result.print();

        env.execute();
    }
}
