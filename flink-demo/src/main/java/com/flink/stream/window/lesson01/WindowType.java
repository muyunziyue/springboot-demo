package com.flink.stream.window.lesson01;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Non Keyed Window 和 Keyed Window
 */
public class WindowType {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(5);

        DataStreamSource<String> dataStream = env.socketTextStream("192.168.123.102", 8888);

        SingleOutputStreamOperator<Tuple2<String, Integer>> stream = dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    collector.collect(Tuple2.of(word, 1));
                }
            }
        });

        stream.timeWindowAll(Time.seconds(5),Time.seconds(2))
                .aggregate(new AggregateFunction<Tuple2<String, Integer>, Long
                        , Long>() {
                    @Override
                    public Long createAccumulator() {
                        return 0L;
                    }

                    @Override
                    public Long add(Tuple2<String, Integer> stringIntegerTuple2, Long aLong) {
                        return aLong + 1L;
                    }

                    @Override
                    public Long getResult(Long aLong) {
                        return aLong;
                    }

                    @Override
                    public Long merge(Long a1, Long a2) {
                        return a1 + a2;
                    }
                })
                .print();



        /**
         * State:
         *     keyed state
         *     Non Keyed state
         *
         * stream:
         *     keyed stream
         *     non keyed stream
         *
         * window:
         *     keyed window
         *     non keyed window
         *
         *
         *
         */
        //Non keyed Stream
//        AllWindowedStream<Tuple2<String, Integer>, TimeWindow> nonkeyedStream = stream.timeWindowAll(Time.seconds(3));
//        nonkeyedStream.sum(1)
//                .print();

        //Keyed Stream
//        stream.keyBy(0)
//                .timeWindow(Time.seconds(3))
//                .sum(1)
//                .print();



        env.execute("word count");


    }
}

