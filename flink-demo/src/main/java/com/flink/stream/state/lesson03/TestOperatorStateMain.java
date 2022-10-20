package com.flink.stream.state.lesson03;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * 需求：每两个元素输出一次
 */
public class TestOperatorStateMain {
    public static void main(String[] args) throws  Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Tuple2<String, Integer>> dataStreamSource =
                env.fromElements(Tuple2.of("Spark", 3), Tuple2.of("Flink", 5), Tuple2.of("Hadoop", 7),
                        Tuple2.of("Spark", 4));


        dataStreamSource
                .addSink(new CustomSink(2)).setParallelism(1);


        dataStreamSource.print();//sink print 一条一条的打印
                                 //sink print 两条两条打印


        env.execute("TestOperatorStateMain...");
    }
}
