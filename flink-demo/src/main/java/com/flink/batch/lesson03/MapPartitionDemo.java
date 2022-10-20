package com.flink.batch.lesson03;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapPartitionOperator;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;

public class MapPartitionDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<String> data = new ArrayList<>();
        data.add("you,jump");
        data.add("i,jump");
        DataSource<String> dataSet = env.fromCollection(data);
        /**
         * flink:
         *    stream:没有分区
         *    batch：分区
         * Spark：
         *   mapPartition
         *   foreachParttion
         */
        MapPartitionOperator<String, String> wordDataSet = dataSet.mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<String> collector) throws Exception {
                Iterator<String> it = iterable.iterator();
                while (it.hasNext()) {
                    String line = it.next();
                    String[] fields = line.split(",");
                    for (String word : fields) {
                        collector.collect(word);
                    }
                }
            }
        });

        wordDataSet.print();
    }
}
