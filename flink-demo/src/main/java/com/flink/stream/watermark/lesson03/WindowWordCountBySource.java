package com.flink.stream.watermark.lesson03;



import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * 需求：每隔5秒计算最近10秒的单词次数
 */
public class WindowWordCountBySource {
    public static void main(String[] args) throws  Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> dataStream = env.addSource(new TestSource());
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
                .process(new SumProcessFunction()).print().setParallelism(1);


        env.execute("WindowWordCountAndTime");

    }

    public static class TestSource implements
            SourceFunction<String>{
        FastDateFormat dateformat =  FastDateFormat.getInstance("HH:mm:ss");
        @Override
        public void run(SourceContext<String> cxt) throws Exception {

            String currTime = String.valueOf(System.currentTimeMillis());
            //这个操作是我为了保证是 10s的倍数。
            /**
             * 0
             * 10
             * 20
             * 30
             * 40
             * 50
             */
            while(Integer.valueOf(currTime.substring(currTime.length() - 4)) > 100){

                currTime=String.valueOf(System.currentTimeMillis());
                continue;
            }

            System.out.println("当前时间："+dateformat.format(System.currentTimeMillis()));
            TimeUnit.SECONDS.sleep(13);
            cxt.collect("flink");
            cxt.collect("flink");
            TimeUnit.SECONDS.sleep(3);
            cxt.collect("flink");

            TimeUnit.SECONDS.sleep(300000);

        }

        @Override
        public void cancel() {

        }
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
        @Override
        public void process(Tuple tuple, Context context,
                            Iterable<Tuple2<String, Integer>> allElements,
                            Collector<Tuple2<String, Integer>> out) {
//            System.out.println("当前系统时间："+dataformat.format(System.currentTimeMillis()));
//            System.out.println("窗口处理时间："+dataformat.format(context.currentProcessingTime()));
//            System.out.println("窗口开始时间："+dataformat.format(context.window().getStart()));
//            System.out.println("窗口结束时间："+dataformat.format(context.window().getEnd()));
//
//            System.out.println("=====================================================");

            int count=0;
           for (Tuple2<String,Integer> e:allElements){
               count++;
           }
            out.collect(Tuple2.of(tuple.getField(0),count));

        }
    }
}
