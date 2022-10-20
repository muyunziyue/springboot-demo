package com.flink.project.zeye.order;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 * 实时每天的订单数
 */
public class OrderAmount {
    public static void main(String[] args) throws Exception {
        //获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.readTextFile("D:\\soft\\work_space\\nx-flink\\src\\data\\order_test") //读取数据
       // env.readTextFile(Utils.ORDER_PATH)
                .map(new ParseOrder())  //解析数据
                .assignTimestampsAndWatermarks(new ExtractorOrderEventTime()) //执行watermark
                .keyBy( order -> order.f0) //按天进行分组
                .window(TumblingEventTimeWindows.of(Time.days(1),Time.hours(16))) //按天滑动
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(5)))
                .evictor(TimeEvictor.of(Time.seconds(0),true))
                .process(new CountProcess())
                .print();

        env.execute("OrderAmount");
    }


    /**
     * 实现累加的效果
     */
    public static class CountProcess extends ProcessWindowFunction<Tuple3<String,String,Double>,Long, String, TimeWindow>{

        //累计出现的次数
        private ValueState<Long> countState;

        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<Long> descriptor = new ValueStateDescriptor<Long>(
                    "countState",
                    Long.class);
            countState = getRuntimeContext().getState(descriptor);
        }

        @Override
        public void process(String key, Context context,
                            Iterable<Tuple3<String, String, Double>> iterable,
                            Collector<Long> out) throws Exception {
            Long count = countState.value();
            //赋初始值
            if(count == null){
                count = 0L;
            }
            Iterator<Tuple3<String, String, Double>> iterator = iterable.iterator();
            //遍历里面的数据
            while(iterator.hasNext()){
                iterator.next();
                count++;
            }
            //实现累加的效果
            countState.update(count);
            out.collect(count);
        }
    }


    public static class ExtractorOrderEventTime
            implements AssignerWithPeriodicWatermarks<Tuple3<String,String,Double>>{
        public Long currentMaxEventTime = 0L;
        public int maxOutofOrderness = 10000;//最大乱序时间

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOutofOrderness);
        }

        @Override
        public long extractTimestamp(Tuple3<String, String, Double> tuple, long l) {
            //指定时间字段
            SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long date = TIME_FORMAT.parse(tuple.f1).getTime();
                currentMaxEventTime = Math.max(date, currentMaxEventTime);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return 0;


        }
    }


    /**
     * 解析数据
     */
    public static class ParseOrder implements MapFunction<String, Tuple3<String,String,Double>> {

        @Override
        public Tuple3<String, String, Double> map(String line) throws Exception {
             String[] fields = line.split("\t");
             String date = fields[6].trim();
             Double amount =Double.parseDouble( fields[1].trim());
             //获取到当前的时间
             String day = date.split(" ")[0];
            return Tuple3.of(day,date,amount);
        }
    }

}
