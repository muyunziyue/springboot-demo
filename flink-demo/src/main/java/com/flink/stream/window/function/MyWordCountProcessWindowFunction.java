package com.flink.stream.window.function;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Iterator;

/**
 * @author ldx
 * @date 2022/9/23
 */
public class MyWordCountProcessWindowFunction extends ProcessWindowFunction<Tuple2<String, Integer>, Integer, String, TimeWindow> {
    private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    @Override
    public void process(String s, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Integer> out) throws Exception {
        String startTime = dateFormat.format(context.window().getStart());
        String endTime = dateFormat.format(context.window().getEnd());
        System.out.println("窗口开始时间=" + startTime + "\n窗口结束时间=" + endTime + "\nkey=" + s);

        Iterator<Tuple2<String, Integer>> iterator = elements.iterator();
        System.out.println("[");
        while (iterator.hasNext()){
            Tuple2<String, Integer> element = iterator.next();
            System.out.println(element);
        }
        System.out.println("]");
    }
}
