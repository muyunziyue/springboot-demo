package com.flink.stream.window.lesson08;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class WindowJoin {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Tuple2<String, String>> data1 = dataStream.map(new MapFunction<String, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> map(String word) throws Exception {
                return Tuple2.of(word, word);
            }
        });

        DataStreamSource<String> dataStream2 = env.socketTextStream("localhost", 9999);

        SingleOutputStreamOperator<Tuple2<String, String>> data2 = dataStream2.map(new MapFunction<String, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> map(String word) throws Exception {
                return Tuple2.of(word, word);
            }
        });

        data1.join(data2)
                .where( tuple -> tuple.f0)
                .equalTo(tuple -> tuple.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new JoinFunction<Tuple2<String, String>, Tuple2<String, String>, String>() {
                    @Override
                    public String join(Tuple2<String, String> t1,
                                       Tuple2<String, String> t2) throws Exception {
                        System.out.println("t1 " + t1.toString() + "t2 "+t2.toString());

                        return t1.toString() + t2.toString();
                    }
                }).print();


        env.execute("test");


    }
}
