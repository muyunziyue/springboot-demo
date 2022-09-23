package com.flink.stream.window.function;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Iterator;

/**
 * @author ldx
 * @date 2022/9/23
 */
public class MyWordCountProcessAllWindowFunction extends ProcessAllWindowFunction<Tuple2<String, Integer>, Long, TimeWindow> {
    @Override
    public void process(Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Long> out) throws Exception {
        Iterator<Tuple2<String, Integer>> iterator = elements.iterator();
        long sum = 0L;
        while (iterator.hasNext()) {
            Tuple2<String, Integer> element = iterator.next();
            sum += element.f1;
        }
        out.collect(sum);
    }
}
