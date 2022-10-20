package com.flink.project.zeye.lesson01;

import com.flink.project.zeye.utils.Utils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 实时统计热门的页面
 */
public class TopNPage {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置成checkpoints等等之类的参数
        env.readTextFile(Utils.APACHE_LOG_PAH)//读取数据
        .map(new ParseZeyeLog()) //解析日志数据
        .assignTimestampsAndWatermarks(new ZeyeHotPageEventTimeExtractor()) //指定watermark
        .keyBy( logEvent -> logEvent.url) //指定分组字段
        .timeWindow(Time.minutes(10),Time.seconds(5)) //实现滑动窗口
        .aggregate(new ZeyePageCountAgg(),new ZeyePageWindowResult()) //实现一个类似单词计数效果，只不过我们需要打印出来更丰富的信息。
        .keyBy( urlView -> urlView.windowEnd) //按照窗口进行分组
        .process(new ZeyeTopNPage(3)) //求TopN
        .print();

        env.execute("TopNPage");
    }


    /**
     * K, I, O
     */
    public static class ZeyeTopNPage
            extends KeyedProcessFunction<Long,ZeyeUrlView,String>{
        //state

        private int topN =0;

        public ZeyeTopNPage(int topN) {
            this.topN = topN;
        }

        public int getTopN() {
            return topN;
        }

        public void setTopN(int topN) {
            this.topN = topN;
        }

        /**
         * key: url
         * value: count
         */
        public MapState<String,Long> urlState;

        /**
         * 注册state
         * @param parameters
         * @throws Exception
         */
        @Override
        public void open(Configuration parameters) throws Exception {
            MapStateDescriptor<String, Long> descriptor = new MapStateDescriptor<>("urlstate",
                    String.class, Long.class);

            urlState = getRuntimeContext().getMapState(descriptor);
        }

        @Override
        public void processElement(ZeyeUrlView urlView,
                                   Context context,
                                   Collector<String> collector) throws Exception {

            urlState.put(urlView.getUrl(),urlView.count);
            //注册一个定时器
            context.timerService().registerEventTimeTimer(urlView.windowEnd+1);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            List<ZeyeUrlView> urlViewArrayList = new ArrayList<>();
            //state -> urlViewArrayList
            ArrayList<String> keys = Lists.newArrayList(urlState.keys());
            for(String url:keys){
                Long count = urlState.get(url);
                urlViewArrayList.add(new ZeyeUrlView(url,
                        new Timestamp(timestamp - 1).getTime(),count));
            }

            //可以进行排序，降序的效果，方法有很多，大家
            //怎么实现都可以。
            Collections.sort(urlViewArrayList);
            List<ZeyeUrlView> topN = urlViewArrayList.subList(0, this.topN);
            for(ZeyeUrlView view:topN){
                System.out.println(view);
            }

            System.out.println("==============================");
        }
    }

    /**
     * IN,输入的数据类型
     *    aggregate里面的第一个函数的输出类型，就是第二个函数的输出类型
     * OUT, 输出的数据类型
     * KEY,指定Key
     * W extends Window 指定窗口的类型
     */
    public static class ZeyePageWindowResult
            implements WindowFunction<Long,ZeyeUrlView,String, TimeWindow>{

        @Override
        public void apply(String key,
                          TimeWindow timeWindow,
                          Iterable<Long> iterable,
                          Collector<ZeyeUrlView> out) throws Exception {
            out.collect(new ZeyeUrlView(key,timeWindow.getEnd(),iterable.iterator().next()));

        }
    }


    /**
     * IN,输入的数据类型
     * ACC, 指定累加的辅助变量的数据类型
     * OUT 指定输出的数据类型
     *
     * 其实就是实现了sum的效果。
     */
    public static class ZeyePageCountAgg
            implements AggregateFunction<ZeyeApacheLogEvent,Long,Long>{

        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(ZeyeApacheLogEvent zeyeApacheLogEvent, Long acc) {
            return acc + 1;
        }

        @Override
        public Long getResult(Long acc) {
            return acc;
        }

        @Override
        public Long merge(Long acc1, Long acc2) {
            return acc1 + acc2;
        }
    }

    /**
     * 指定时间时间的字段
     */
    public static class ZeyeHotPageEventTimeExtractor
            implements AssignerWithPeriodicWatermarks<ZeyeApacheLogEvent>{
        private long currentMaxEventTime = 0L;
        private long maxOutOfOrderness = 10000L;//可以延迟10s的意思
        /**
         * 指定watermark的时间
         * @return
         */
        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOutOfOrderness);
        }

        /**
         * 指定事件时间的字段
         * @param zeyeApacheLogEvent
         * @param l
         * @return
         */
        @Override
        public long extractTimestamp(ZeyeApacheLogEvent zeyeApacheLogEvent,
                                     long l) {
            //日志里面的事件的字段
            Long eventTime = zeyeApacheLogEvent.getEventTime();
            currentMaxEventTime = Math.max(eventTime,currentMaxEventTime);
            return eventTime;
        }
    }

    /**
     * 读取字符串，把解析成日志对象
     */
    public static class ParseZeyeLog
            implements MapFunction<String,ZeyeApacheLogEvent>{

        @Override
        public ZeyeApacheLogEvent map(String line) throws Exception {
            String[] fields = line.split(" ");
            SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
            long timeStamp = dataFormat.parse(fields[3].trim()).getTime();

            return new ZeyeApacheLogEvent(fields[0].trim(),fields[1].trim(),
                    timeStamp,fields[5].trim(),fields[6].trim());

        }
    }





    public static class ZeyeUrlView implements Comparable<ZeyeUrlView>{
        private String url;
        private long windowEnd;
        private long count;

        public ZeyeUrlView(){

        }

        public ZeyeUrlView(String url, long windowEnd, long count) {
            this.url = url;
            this.windowEnd = windowEnd;
            this.count = count;
        }

        @Override
        public String toString() {
            return "ZeyeUrlView{" +
                    "url='" + url + '\'' +
                    ", windowEnd=" + windowEnd +
                    ", count=" + count +
                    '}';
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getWindowEnd() {
            return windowEnd;
        }

        public void setWindowEnd(long windowEnd) {
            this.windowEnd = windowEnd;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        /**
         * 按照页面点击的次数降序排序
         * @param urlView
         * @return
         */
        @Override
        public int compareTo(ZeyeUrlView urlView) {
            return (this.count > urlView.count) ? -1 :((this.count == urlView.count) ? 0 :1);
        }
    }

    /**
     * 系统日志类
     */
    public static class ZeyeApacheLogEvent{
        private String ip;
        private String userId;
        private Long eventTime;
        private String method;
        private String url;

        public ZeyeApacheLogEvent(){

        }

        public ZeyeApacheLogEvent(String ip, String userId, Long eventTime, String method, String url) {
            this.ip = ip;
            this.userId = userId;
            this.eventTime = eventTime;
            this.method = method;
            this.url = url;
        }

        @Override
        public String toString() {
            return "ZeyeApacheLogEvent{" +
                    "ip='" + ip + '\'' +
                    ", userId='" + userId + '\'' +
                    ", eventTime=" + eventTime +
                    ", method='" + method + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getEventTime() {
            return eventTime;
        }

        public void setEventTime(Long eventTime) {
            this.eventTime = eventTime;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
