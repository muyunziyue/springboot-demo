package com.flink.batch.lesson04;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class DistinctDemo {
    public static void main(String[] args) throws  Exception{
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<String> data = new ArrayList<>();
        data.add("you,jump");
        data.add("i,jump");
        DataSource<String> dataSet = env.fromCollection(data);
        FlatMapOperator<String, String> wordDataSet = dataSet.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] fields = s.split(",");
                for (String word : fields) {
                    collector.collect(word);
                }
            }
        });

        wordDataSet.distinct().print();


    }
}
