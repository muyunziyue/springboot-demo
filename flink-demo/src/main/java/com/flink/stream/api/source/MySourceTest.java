package com.flink.stream.api.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ldx
 * @date 2022/9/21
 */
public class MySourceTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Long> source = mySourceTest(env);
        source.print();
        env.execute("MyParallelSource Job Test");
    }

    public static DataStreamSource<Long>  mySourceTest(StreamExecutionEnvironment env){
//        return env.addSource(new MyNoParallelSource());
        return env.addSource(new MyParallelSource());
    }
}
