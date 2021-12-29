package com.myjava.multithread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @Author lidexiu
 * @Date 2021/11/18
 * @Description
 */
@RestController
@RequestMapping("/concurrent")
@Slf4j
public class ConcurrentHashMapUsage {
    //线程个数
    private static int THREAD_COUNT = 10;
    // 总元素数量
    private static int ITEM_COUNT=1000;

    private ConcurrentHashMap<String, Long> getData(int count){
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new))
                ;
    }
    @GetMapping("/wrong")
    public String concurrentHashMapWrongUsage() throws InterruptedException {
        ConcurrentHashMap<String, Long> data = getData(ITEM_COUNT - 100);
        log.info("init size:{}", data.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            int gap = ITEM_COUNT - data.size();
            log.info("gap size:{}", gap);
            //补充元素
            data.putAll(getData(gap));
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        log.info("finish size:{}", data.size());

        return "OK";
    }
}
