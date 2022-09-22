package com.flink.demo.transform;

import com.flink.demo.entity.WordAndOne;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * @author ldx
 * @date 2022/9/20
 */
public class WordCountFlatMap implements FlatMapFunction<String, WordAndOne> {

    @Override
    public void flatMap(String value, Collector<WordAndOne> out) throws Exception {
        String[] words = value.split(" ");
        for (String word : words) {
            out.collect(new WordAndOne(word.trim(), 1));
        }
    }
}
