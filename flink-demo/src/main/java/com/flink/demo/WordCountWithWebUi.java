package com.flink.demo;

import com.flink.demo.entity.WordAndOne;
import com.flink.demo.transform.WordCountFlatMap;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.UUID;

/**
 * @author ldx
 * @date 2022/9/20
 * 通过webUI在本地观察flink任务
 */
public class WordCountWithWebUi {
    public static void main(String[] args) throws Exception {

        // 使用flink官方工具包解析参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        // 设置整体的并行度
        env.setParallelism(2);

        DataStreamSource<String> sourceStream = env.socketTextStream(hostname, port);
        SingleOutputStreamOperator<WordAndOne> result = sourceStream
                .flatMap(new WordCountFlatMap())
                // 可以针对每一个算子设置并行度
//                .setParallelism(12)
                .keyBy(WordAndOne::getWord)
                .sum("count");

        result.print();

        env.execute();

    }
}
