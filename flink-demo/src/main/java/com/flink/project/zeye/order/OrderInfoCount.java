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

public class OrderInfoCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.readTextFile("D:\\soft\\work_space\\nx-flink\\src\\data\\order_test")
                // env.readTextFile(Utils.ORDER_PATH)
                .map(new ParseOrder())
                .assignTimestampsAndWatermarks(new OrderAmount.ExtractorOrderEventTime())
                .keyBy( order -> order.f0)
                .window(TumblingEventTimeWindows.of(Time.days(1),Time.hours(16)))
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(10)))
                .evictor(TimeEvictor.of(Time.seconds(0),true))
                .process(new CountAmountProcess())
                .print();

        env.execute("OrderAmount");
    }


    public static class CountAmountProcess extends ProcessWindowFunction<Tuple3<String,String,Double>,Double, String, TimeWindow> {

        private ValueState<Double> amountState;

        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<Double> descriptor = new ValueStateDescriptor<Double>(
                    "amountState",
                    Double.class);
            amountState = getRuntimeContext().getState(descriptor);
        }

        @Override
        public void process(String key, Context context,
                            Iterable<Tuple3<String, String, Double>> iterable,
                            Collector<Double> out) throws Exception {
            Double count = amountState.value();
            if(count == null){
                count = 0.0;
            }
            Iterator<Tuple3<String, String, Double>> iterator = iterable.iterator();
            while(iterator.hasNext()){
                //对金额进行累加
                count += iterator.next().f2;

            }
            amountState.update(count);
            out.collect(count);
        }
    }


    public static class ExtractorOrderEventTime
            implements AssignerWithPeriodicWatermarks<Tuple3<String,String,Double>> {
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



    public static class ParseOrder implements MapFunction<String, Tuple3<String,String,Double>> {

        @Override
        public Tuple3<String, String, Double> map(String line) throws Exception {
            String[] fields = line.split("\t");
            String date = fields[6].trim();
            Double amount =Double.parseDouble( fields[1].trim());
            String day = date.split(" ")[0];
            return Tuple3.of(day,date,amount);
        }
    }

}
