package com.flink.stream.api.transform;


import com.flink.datasource.EventDataSource;
import com.flink.entity.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

/**
 *  根据规则把一个数据流切分为多个流
 应用场景：
 * 可能在实际工作中，源数据流中混合了多种类似的数据，多种类型的数据处理规则不一样，所以就可以在根据一定的规则，
 * 把一个数据流切分成多个数据流，这样每个数据流就可以使用不用的处理逻辑了
 */
public class SplitDemo {
    public static void main(String[] args) throws  Exception {
        //获取Flink的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        EventDataSource eventDataSource = new EventDataSource();
        //获取数据源
        SingleOutputStreamOperator<Event> eventStream = env.addSource(eventDataSource)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(1L))
                        .withTimestampAssigner(((SerializableTimestampAssigner<Event>) (event, timestamp) -> event.timestamp))
                        .withIdleness(Duration.ofSeconds(10L))
                );

        // 定义标签输出流
        OutputTag<Event> bobTag = new OutputTag<Event>("Bob", TypeInformation.of(Event.class ));
        //切分流
        SingleOutputStreamOperator<Event> splitStream = eventStream.process(new ProcessFunction<Event, Event>() {
            @Override
            public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                if ("Bob".equals(value.getUser())) {
                    ctx.output(bobTag, value);
                } else {
                    out.collect(value);
                }
            }
        });
        // 获取侧输出流
        splitStream.getSideOutput(bobTag).print("Bob");
        // 获取主流
        splitStream.print("other");

        String jobName = SplitDemo.class.getSimpleName();
        env.execute(jobName);

    }
}