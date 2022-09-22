package com.flink.stream.api.source;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

/**
 * @author ldx
 * @date 2022/9/21
 */
public class MyParallelSource implements ParallelSourceFunction<Long> {
    private Long number = 1L;
    private boolean isRunning = true;
    @Override
    public void run(SourceContext ctx) throws Exception {
        while (isRunning) {
            ctx.collect(number);
            number++;
            Thread.sleep(100);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
