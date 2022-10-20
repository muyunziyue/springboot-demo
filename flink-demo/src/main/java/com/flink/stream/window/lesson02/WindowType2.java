package com.flink.stream.window.lesson02;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Non Keyed Window 和 Keyed Window
 */
public class WindowType2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Tuple2<String, Integer>> stream = dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    collector.collect(Tuple2.of(word, 1));
                }
            }
        });

        //Non keyed Stream
//        AllWindowedStream<Tuple2<String, Integer>, TimeWindow> nonkeyedStream = stream.timeWindowAll(Time.seconds(3));
//        nonkeyedStream.sum(1)
//                .print();

        //Keyed Stream


        /**
         *
         * 滚动窗口  2s
         *
         *  .timeWindow(Time.seconds(2))  == .window(TumblingProcessingTimeWindows.of(Time.seconds(2)))
         *
         *
         */
        stream.keyBy("0")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(2)))
                //.countWindow(100)
               // .countWindow(2,1)
                //.timeWindow(Time.seconds(3),Time.seconds(1))
                .sum(1)
                .print();


        stream.keyBy("0")
               .timeWindow(Time.seconds(10))
                .sum(1)
                .print();





        stream.keyBy(0)
                .timeWindow(Time.seconds(10))
                .sum(1)
                .print();

        stream.keyBy(0)
                .timeWindow(Time.seconds(10),Time.seconds(2))
                .sum(1)
                .print();


        stream.keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .sum(1)
                .print();


        stream.keyBy(0)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(10),Time.seconds(2)))
                .sum(1)
                .print();




        env.execute("word count");


    }
}

