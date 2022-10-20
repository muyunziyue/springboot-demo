package com.flink.stream.watermark.lesson02;


import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * 需求：每隔5秒计算最近10秒的单词次数
 *
 *
 * 窗口开始时间：20:17:03
 * 窗口结束时间：20:17:13
 *
 *窗口开始时间：20:17:08
 *窗口结束时间：20:17:18
 *
 */
public class WindowWordCountAndTime {
    public static void main(String[] args) throws  Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> dataStream = env.socketTextStream("192.168.123.102", 9999);
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String line,
                                Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word:fields){
                    out.collect(Tuple2.of(word,1));
                }
            }
        })
                .keyBy(0)
                .timeWindow(Time.seconds(10),Time.seconds(5))
                .process(new SumProcessFunction())
                .print().setParallelism(1);

        env.execute("WindowWordCountAndTime");

    }

    /**
     * IN
     * OUT
     * KEY
     * W extends Window
     *
     */
    public static class  SumProcessFunction
            extends ProcessWindowFunction<Tuple2<String,Integer>,Tuple2<String,Integer>,Tuple,TimeWindow>{

        FastDateFormat dataformat=FastDateFormat.getInstance("HH:mm:ss");

        /**
         * 在里面实现一个单词计数的功能，sum的功能。
         * @param tuple key
         * @param context
         * @param allElements
         * @param out
         */
        @Override
        public void process(Tuple tuple, Context context,
                            Iterable<Tuple2<String, Integer>> allElements,
                            Collector<Tuple2<String, Integer>> out) {
            System.out.println("当前系统时间："+dataformat.format(System.currentTimeMillis()));
            System.out.println("窗口处理时间："+dataformat.format(context.currentProcessingTime()));

            System.out.println("窗口开始时间："+dataformat.format(context.window().getStart()));
            System.out.println("窗口结束时间："+dataformat.format(context.window().getEnd()));

            System.out.println("=====================================================");

            int count=0;
           for (Tuple2<String,Integer> e:allElements){
               count++;
           }
            out.collect(Tuple2.of(tuple.getField(0),count));

        }
    }
}
