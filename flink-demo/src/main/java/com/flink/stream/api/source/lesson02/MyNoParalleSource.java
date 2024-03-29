package com.flink.stream.api.source.lesson02;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 *
 * Long:数据输出的数据类型
 *
 * 代表我们的这个数据源只能支持一个并行度（单并行度）
 */
public class MyNoParalleSource implements SourceFunction<Long> {
    private long number = 1L;
    private boolean isRunning = true;
    @Override
    public void run(SourceContext<Long> sct) throws Exception {
        while (isRunning){
            //往下游发送数据
            sct.collect(number);
            number++;
            //每秒生成一条数据
            Thread.sleep(1000);
        }

    }

    @Override
    public void cancel() {
        isRunning=false;
    }
}