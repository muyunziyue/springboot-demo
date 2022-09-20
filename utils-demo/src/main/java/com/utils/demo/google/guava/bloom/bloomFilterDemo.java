package com.utils.demo.google.guava.bloom;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ldx
 * @date 2022/8/26
 */
public class bloomFilterDemo {
    private static final Long SIZE = 100000L;
    private static BloomFilter<Long> bloomFilter = BloomFilter.create(Funnels.longFunnel(), SIZE, 0.0001);
    private static StopWatch stopWatch = StopWatch.create();

    private static List<Long> storeList = new ArrayList<>(SIZE.intValue());

    public static void main(String[] args) {

        putBloomFilter(bloomFilter);
        putList(storeList);

        stopWatch.start();
        notContainBloomFilter(bloomFilter);
        stopWatch.stop();
        System.out.println("布隆过滤器判定不存在耗时统计：" + stopWatch.getTime() + "ms");

        stopWatch.reset();
        stopWatch.start();
        notContainStoreList(storeList);
        stopWatch.stop();
        System.out.println("列表存储中判定不存在耗时统计：" + stopWatch.getTime() + "ms");

        containsBloomFilter(bloomFilter);

    }

    private static void putBloomFilter(BloomFilter<Long> bloomFilter){
        for (long i = 0; i < SIZE; i++) {
            bloomFilter.put(i);
        }
        System.out.println("write over");
    }
    private static void putList(List<Long> storeList){
        for (long i = 0; i < SIZE; i++) {
            storeList.add(i);
        }
        System.out.println("storeList write over");
    }
    private static void notContainStoreList(List<Long> storeList){
        for (long i = 0; i < SIZE; i++) {
            if(!storeList.contains(i)){
                System.out.println("list里这个元素不存在" + i);
            }
        }
    }



    private static void notContainBloomFilter(BloomFilter<Long> bloomFilter){
        for (long i = 0; i < SIZE; i++) {
            if (!bloomFilter.mightContain(i)) {
                System.out.println("这个元素不存在：" + i);
            }
        }
    }

    private static void containsBloomFilter(BloomFilter<Long> bloomFilter){
        List<Long> list = new ArrayList<>();
        for (long i = SIZE + 1; i < SIZE + 20000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("误伤数" + list.size());
    }
}
