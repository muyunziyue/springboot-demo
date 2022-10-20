package com.flink.stream.window.lesson07;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 求每隔窗口中的数据的平均值
 *
 * 思路：
 *  累加值/总的个数=平均值
 */
public class aggregateWindowTest {
    public static void main(String[] args)  throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStream = env.socketTextStream("localhost", 8888);

        SingleOutputStreamOperator<Integer> numberStream = dataStream.map(line -> Integer.valueOf(line));
        AllWindowedStream<Integer, TimeWindow> windowStream = numberStream.timeWindowAll(Time.seconds(5));

        windowStream.aggregate(new MyAggregate())
                .print();

        env.execute("aggregateWindowTest");
    }

    /**
     * IN, 输入的数据类型
     * ACC,自定义的中间状态
     *      Tuple2<Integer,Integer>:
     *          key: 计算数据的个数
     *          value:计算总值
     * OUT，输出的数据类型
     */
    private static class MyAggregate
            implements AggregateFunction<Integer,Tuple2<Integer,Integer>,Double>{
        /**
         * 初始化 累加器，辅助变量
         * @return
         */
        @Override
        public Tuple2<Integer, Integer> createAccumulator() {
            //key 累积有多少个数
            //value 累积总的值
            return new Tuple2<>(0,0);
        }

        /**
         * 针对每个数据的操作
         * @return
         */
        @Override
        public Tuple2<Integer, Integer> add(Integer element,
                                            Tuple2<Integer, Integer> accumulator) {
            //个数+1
            //总的值累计
            return new Tuple2<>(accumulator.f0+1,accumulator.f1+element);
        }

        @Override
        public Tuple2<Integer, Integer> merge(Tuple2<Integer, Integer> a1,
                                              Tuple2<Integer, Integer> b1) {
            return Tuple2.of(a1.f0+b1.f0,a1.f1+b1.f1);
        }



        @Override
        public Double getResult(Tuple2<Integer, Integer> accumulator) {

            return (double)accumulator.f1/accumulator.f0;
        }


    }
}
