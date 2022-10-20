package com.flink.stream.basic.lesson05;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 单词计数
 *
 * 数据很复杂
 * 有可能会有几个个，上百个字段
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
        /**
         * kafka 地址
         * reids,mysql 地址
         * socket 地址
         */
        //步骤一：初始化程序入口
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
       // StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度，在开发的时候，我们的数据源是kafka,kafka有几个partition，就设置一个并行度就可以了。
        //设置的是整个任务的并行度
      //  env.setParallelism(2);


        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");
        //步骤二：数据的输入

        //socket支持的并行度就是1
        DataStreamSource<String> dataStream = env.socketTextStream(hostname, port).setParallelism(1);
        //步骤三：数据的处理
        SingleOutputStreamOperator<WordAndCount> result = dataStream
                .flatMap(new SplitWordFunction()).setParallelism(1)
                .keyBy("word")
                .sum("count").setParallelism(1);

        //步骤四：数据的输出
        result.print().setParallelism(1);
        //步骤五：启动程序
        env.execute("test word count...");

        /**
         * 大家认为这个任务启动起来以后会有一个task
         *
         */
    }


    /**
     * 分割单词
     */
    public static class SplitWordFunction implements FlatMapFunction<String,WordAndCount>{
        @Override
        public void flatMap(String line, Collector<WordAndCount> out) throws Exception {
            String[] fields = line.split(",");
            for (String word : fields) {
                out.collect(new WordAndCount(word, 1));
            }
        }
    }


    public static class WordAndCount{
        private String word;
        private Integer count;
        public WordAndCount(){

        }

        @Override
        public String toString() {
            return "WordAndCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }

        public WordAndCount(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}
