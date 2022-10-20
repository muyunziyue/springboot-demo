package com.flink.batch.lesson02;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class WordCount {
    public static void main(String[] args)throws  Exception {
        //步骤一：获取离线的程序入口
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String inputPath="D:\\soft\\work_space\\nx-flink\\src\\input\\hello.txt";
        //步骤二：获取数据源
        DataSource<String> dataSet = env.readTextFile(inputPath);
        //步骤三：数据处理
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOneDataSet = dataSet.flatMap(new MySplitWordsTask());

        AggregateOperator<Tuple2<String, Integer>> result = wordAndOneDataSet.groupBy(0)
                .sum(1);
        //步骤四：数据结果处理
        result.writeAsText("D:\\soft\\work_space\\nx-flink\\src\\output2").setParallelism(1);
        //步骤五：启动程序
        env.execute("word count");
    }


    public static class MySplitWordsTask implements   FlatMapFunction<String,Tuple2<String,Integer>>{
        @Override
        public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
            String[] fileds = line.split(",");
            for (String word : fileds) {
                collector.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }
}
