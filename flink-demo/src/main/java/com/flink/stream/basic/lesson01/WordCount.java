package com.flink.stream.basic.lesson01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 单词计数
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
        //步骤一：初始化程序入口
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //步骤二：数据的输入 operator
        DataStreamSource<String> dataStream = env.socketTextStream("192.168.123.102", 9999);
        //步骤三：数据的处理 operator state
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line,
                                Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    // out.collect(new Tuple2<>(word,1));
                    out.collect(Tuple2.of(word, 1));
                }
            }
        }).keyBy(0)
                .sum(1);//keyed state
        //步骤四：数据的输出
        result.print();
        //步骤五：启动程序
        env.execute("test word count...");
    }
}
