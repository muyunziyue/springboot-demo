package com.flink.stream.state.lesson05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestCheckpoint {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置 state保存位置
        env.setStateBackend(new FsStateBackend("hdfs://192.168.123.102:9000/flink/state"));
        //设置checkpoint //建议不要太短
        env.enableCheckpointing(5000);
        //设置checkpoint支持的语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //两次checkpoint的时间间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(5000);
        // 允许两个连续的 checkpoint 错误
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(2);
        //最多一个checkpoints同时进行
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //checkpoint超时的时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        //cancel程序的时候保存checkpoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);



        DataStreamSource<String> dataStream = env.socketTextStream(hostname, port);
        SingleOutputStreamOperator<String> result = dataStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String line) throws Exception {
                return "nx_" + line;
            }
        }).uid("split-map");
        result.print().uid("print-operator");

        env.execute("test ");
    }
}
