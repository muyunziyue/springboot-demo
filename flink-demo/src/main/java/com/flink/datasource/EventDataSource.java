package com.flink.datasource;

import com.flink.entity.Event;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author ldx
 * @date 2022/10/20
 */
public class EventDataSource implements ParallelSourceFunction<Event> {
    private Long orderNo = 1L;
    private boolean isRunning = true;
    private final List<String> userList = Arrays.asList("Alice", "Bob", "Mary", "Xian", "Li");
    private final List<String> urlList = Arrays.asList("/a", "/b", "/c", "/d/hello_world", "/e");
    private final Random random = new Random();

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        while (isRunning) {
            sourceContext.collect(getNextEvent());
            Thread.sleep(1000L);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

    private Event getNextEvent() {
        String id = Thread.currentThread().getId() + "-" + orderNo++;
        String user = userList.get(random.nextInt(userList.size()));
        String url = urlList.get(random.nextInt(urlList.size()));
        long timestamp = System.currentTimeMillis();

        return new Event(id, user, url, timestamp);
    }
}
