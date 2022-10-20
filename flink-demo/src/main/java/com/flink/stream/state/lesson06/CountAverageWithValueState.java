package com.flink.stream.state.lesson06;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * IN, OUT
 */
public class CountAverageWithValueState
        extends RichFlatMapFunction<Tuple2<Long,Long>,Tuple2<Long,Double>> {
    /**
     * ValueState 里面只能存一个数据
     * ListState 里面就可以存很多条数据
     * long: key出现的次数
     * long: key对应value的总的和
     *
     *
     */
    private ValueState<Tuple2<Long,Long>> countAndSum;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Tuple2<Long, Long>> descriptor = new ValueStateDescriptor<>(
                "average",
                Types.TUPLE(Types.LONG, Types.LONG)
        );
        countAndSum = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void flatMap(Tuple2<Long, Long> element,
                        Collector<Tuple2<Long, Double>> out) throws Exception {

        Tuple2<Long, Long> currentState = countAndSum.value();
        if(currentState == null){
            currentState = Tuple2.of(0L,0L);
        }
        //更新元素出现的次数
        currentState.f0 += 1;
        //更新value的总和
        currentState.f1 += element.f1;
        //更新state
        countAndSum.update(currentState);
        if(currentState.f0 == 3){
            double avg=(double) currentState.f1 / currentState.f0;
            out.collect(Tuple2.of(element.f0,avg));
            //清空当前的state的数据
            countAndSum.clear();
        }

    }
}
