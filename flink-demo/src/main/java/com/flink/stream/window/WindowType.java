package com.flink.stream.window;

import com.flink.stream.window.function.MyWordCountProcessWindowFunction;
import com.flink.utls.YmlUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author ldx
 * @date 2022/9/23
 */
public class WindowType {
    public static void main(String[] args) throws Exception {
        String socketIp = (String) YmlUtils.getAssignYmlProperties("job-conf", "socket.ip");
        Integer socketPort = (Integer) YmlUtils.getAssignYmlProperties("job-conf", "socket.port");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketSourceStream = env.socketTextStream(socketIp, socketPort);
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordFlatMapStream = socketSourceStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(Tuple2.of(word, 1));
                }
            }
        });
        // keyedWindow
        wordFlatMapStream
                .keyBy(entity -> entity.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .process(new MyWordCountProcessWindowFunction())
                .print();

        // noKeyedWindow
//        wordFlatMapStream
//                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
//                .process(new MyWordCountProcessAllWindowFunction())
//                .print();

        env.execute("window type job");
    }
}
