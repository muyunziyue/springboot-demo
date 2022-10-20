package com.flink.stream.state.lesson04;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 * state:
 *  keyed
 *  operator  -> checkpoint
 *
 *
 */
public class WordCount {
    public static void main(String[] args) throws  Exception{
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

      //  env.setStateBackend(new FsStateBackend("hdfs://"));
      //  env.setStateBackend(new RocksDBStateBackend("hdfs://"));




        //10s 15s
        //如果数据量比较大，建议5分钟左右checkpoint的一次。
        //阿里他们使用的时候 也是这样建议的。
        //默认情况是没有启用checkpoint的
        //但是我们生产里面的代码，都需要启用checkpoint
        env.enableCheckpointing(10000);//10s 15s state
        env.setStateBackend(new FsStateBackend("xxx"));

//        FsStateBackend fsStateBackend = new FsStateBackend("hdfs://192.168.167.251:8020/flink/checkpoint");

//        MemoryStateBackend memoryStateBackend = new MemoryStateBackend();
//
//        env.setStateBackend(fsStateBackend);
      //  env.setStateBackend(new RocksDBStateBackend("hdfs://10.0.0.9:8020/flink/checkpoint"));


        //设置checkpoint支持的语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //两次checkpoint的时间间隔
        //10s
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        //checkpoint超时的时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        //cancel程序的时候保存checkpoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        /**
         * 策略一
         * 默认使用的是这种策略，每隔10秒启动一次
         * 重试的次数是Integer.MAXVALUE
         * 3-5
         */
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                3, // 尝试重启的次数
                Time.of(10, TimeUnit.SECONDS) // 间隔
        ));
        /**
         * 策略二
         */
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, // 一个时间段内的最大失败次数
                Time.of(5, TimeUnit.MINUTES), // 衡量失败次数的是时间段
                Time.of(10, TimeUnit.SECONDS) // 间隔
        ));
        /**
         * 策略三
         * 不推荐
         */
        env.setRestartStrategy(RestartStrategies.noRestart());


        DataStreamSource<String> dataStream = env.socketTextStream(hostname, port);

        SingleOutputStreamOperator<Tuple2<String, Integer>> result = dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    out.collect(Tuple2.of(word, 1));
                }
            }
        }).keyBy(0)
                .sum(1);

        result.print();

        env.execute("WordCount check point....");
    }
}
