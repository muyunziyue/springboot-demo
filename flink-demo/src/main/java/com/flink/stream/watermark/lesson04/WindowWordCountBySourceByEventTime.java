package com.flink.stream.watermark.lesson04;



import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * 需求：每隔5秒计算最近10秒的单词次数
 * 乱序
 */
public class WindowWordCountBySourceByEventTime {
    public static void main(String[] args) throws  Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        //步骤一：设置时间为EventTime
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> dataStream = env.addSource(new TestSource());
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String,Long>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                String[] fields = s.split(",");
                collector.collect(Tuple2.of(fields[0],Long.valueOf(fields[1])));
            }
        }).assignTimestampsAndWatermarks(new EventTimeExtractor()) //步骤二：指定哪个字段为事件时间。
                .keyBy(0)
                .timeWindow(Time.seconds(10),Time.seconds(5))
                .process(new SumProcessFunction()).print().setParallelism(1);


        env.execute("WindowWordCountAndTime");

    }


    public static class EventTimeExtractor
            implements AssignerWithPeriodicWatermarks<Tuple2<String,Long>>{

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(System.currentTimeMillis());
        }

        /**
         * 在这个方法里面指定事件事件的字段
         * @param element
         * @param previousElementTimestamp
         * @return
         */
        @Override
        public long extractTimestamp(Tuple2<String, Long> element,
                                     long previousElementTimestamp) {
            return element.f1; //事件得精确到毫秒
        }
    }

    public static class TestSource implements
            SourceFunction<String>{
        FastDateFormat dateformat =  FastDateFormat.getInstance("HH:mm:ss");
        @Override
        public void run(SourceContext<String> cxt) throws Exception {
            String currTime = String.valueOf(System.currentTimeMillis());
            while(Integer.valueOf(currTime.substring(currTime.length() - 4)) > 100){
                currTime=String.valueOf(System.currentTimeMillis());
                continue;
            }
            System.out.println("当前时间："+dateformat.format(System.currentTimeMillis()));
            TimeUnit.SECONDS.sleep(13);
            //13
            String event="hadoop,"+System.currentTimeMillis();
            String event1=event;

            cxt.collect(event);

            TimeUnit.SECONDS.sleep(3);
            cxt.collect("hadoop,"+System.currentTimeMillis());

            TimeUnit.SECONDS.sleep(3);
            //19
            cxt.collect(event1);
            TimeUnit.SECONDS.sleep(3000);


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
            extends ProcessWindowFunction<Tuple2<String,Long>,Tuple2<String,Integer>,Tuple,TimeWindow>{

        FastDateFormat dataformat=FastDateFormat.getInstance("HH:mm:ss");
        @Override
        public void process(Tuple tuple, Context context,
                            Iterable<Tuple2<String, Long>> allElements,
                            Collector<Tuple2<String, Integer>> out) {
//            System.out.println("当前系统时间："+dataformat.format(System.currentTimeMillis()));
//            System.out.println("窗口处理时间："+dataformat.format(context.currentProcessingTime()));
//            System.out.println("窗口开始时间："+dataformat.format(context.window().getStart()));
//            System.out.println("窗口结束时间："+dataformat.format(context.window().getEnd()));
//
//            System.out.println("=====================================================");

            int count=0;
           for (Tuple2<String,Long> e:allElements){
               count++;
           }
            out.collect(Tuple2.of(tuple.getField(0),count));

        }
    }
}
