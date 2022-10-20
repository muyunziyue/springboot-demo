package com.flink.table;

import com.flink.datasource.EventDataSource;
import com.flink.entity.Event;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author ldx
 * @date 2022/10/20
 */
public class TableSimpleDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置全局并行度
        env.setParallelism(1);

        //设置state的保存方式
        env.setStateBackend(new FsStateBackend("file:///home/data/flink/checkpoint"));

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
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 设置重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.minutes(5L)));

        DataStreamSource<Event> eventDataSource = env.addSource(new EventDataSource());
        // 设置watermark
        SingleOutputStreamOperator<Event> watermarkStream = eventDataSource.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(1L))
                        .withTimestampAssigner(
                                (SerializableTimestampAssigner<Event>) (event, timestamp) -> event.getTimestamp())
                        .withIdleness(Duration.ofSeconds(2L)));

        SingleOutputStreamOperator<Tuple2<String, Long>> processStream = watermarkStream.keyBy(Event::getUser)
                .window(TumblingEventTimeWindows.of(org.apache.flink.streaming.api.windowing.time.Time.seconds(5L)))
                .process(new ProcessWindowFunction<Event, Tuple2<String, Long>, String, TimeWindow>() {
                    private Long count;
                    private FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

                    @Override
                    public void process(String s, Context context, Iterable<Event> elements, Collector<Tuple2<String, Long>> out) throws Exception {
                        System.out.println("窗口开始时间：" + fastDateFormat.format(context.window().getStart()));
                        System.out.println("当前系统时间：" + fastDateFormat.format(System.currentTimeMillis()));
                        count = 0L;
                        elements.forEach(event -> ++count);
                        out.collect(Tuple2.of(s, count));
                        System.out.println("窗口结束时间：" + fastDateFormat.format(context.window().getEnd()));

                        count = 0L;
                    }
                });

        processStream.print();

        env.execute("Simple Demo");
    }
}
