package com.flink.project.zeye.network;

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
 * 统计热门页面
 */
public class HotPage {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.readTextFile(Utils.APACHE_LOG_PAH) //读取数据
                .map(new ParseLog()) //解析日志数据
                .assignTimestampsAndWatermarks(new HotPageEventTimeExtractor()) //添加水位
                .keyBy(apacheLog -> apacheLog.url) //按照URL分组
                .timeWindow(Time.minutes(10),Time.seconds(5)) //滑动窗口
                .aggregate(new PageCountAgg(),new PageWindowResult()) //计算每个URL出现的次数
                .keyBy(urlView -> urlView.windowEnd) //按照窗口进行分组
                .process(new TopNHotPage(3)) //求TopN
                .print();

        env.execute("HotPage");

    }

    /**
     * 计算热门热面
     * K, I, O
     */
    public static class TopNHotPage extends KeyedProcessFunction<Long, UrlView,String>{

        private int topN = 0;

        public TopNHotPage(int topN) {
            this.topN = topN;
        }

        public int getTopN() {
            return topN;
        }

        public void setTopN(Integer topN) {
            this.topN = topN;
        }

        //key:url
        //value:count 出现的次数
        public MapState<String,Long> urlState;
        @Override
        public void open(Configuration parameters) throws Exception {
            // 注册状态
            MapStateDescriptor<String,Long> descriptor =
                    new MapStateDescriptor<String, Long>(
                            "average",  // 状态的名字
                            String.class, Long.class); // 状态存储的数据类型
            urlState = getRuntimeContext().getMapState(descriptor);

        }

        @Override
        public void processElement(UrlView urlView,
                                   Context context,
                                   Collector<String> out) throws Exception {

           urlState.put(urlView.getUrl(),urlView.getCount());

           context.timerService().registerEventTimeTimer(urlView.windowEnd + 1);

        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            List<UrlView> urlViewArrayList = new ArrayList<UrlView>();

            List<String> allElementKey = Lists.newArrayList(urlState.keys());
            for(String url:allElementKey){
                urlViewArrayList.add(new UrlView(url, new Timestamp(timestamp - 1).getTime(),urlState.get(url).longValue()));
            }
            Collections.sort(urlViewArrayList);

            List<UrlView> topN = urlViewArrayList.subList(0,this.topN);

            for (UrlView urlView:topN){
                System.out.println(urlView);
            }
            System.out.println("======================");


        }
    }

    /**
     * IN, 输入的数据类型
     * OUT, 输出的数据类型
     * KEY, key
     * W <: Window window的类型
     *
     */
    public static class PageWindowResult
            implements WindowFunction<Long,UrlView, String, TimeWindow> {

        @Override
        public void apply(String key, TimeWindow timeWindow, Iterable<Long> iterable,
                          Collector<UrlView> collector) throws Exception {
            collector.collect(new UrlView(key,
                    timeWindow.getEnd(),
                    iterable.iterator().next()));
        }
    }

    public static class PageCountAgg implements AggregateFunction<ApacheLogEvent,Long,Long>{

        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(ApacheLogEvent apacheLogEvent, Long acc) {
            return acc + 1L;
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
     * 指定事件字段
     */
    public static class HotPageEventTimeExtractor implements AssignerWithPeriodicWatermarks<ApacheLogEvent>{

        private Long  currentMaxEventTime = 0L; //设置当前窗口里面最大的时间
        private Long  maxOufOfOrderness = 10000L;//最大乱序时间 10s

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOufOfOrderness);
        }

        @Override
        public long extractTimestamp(ApacheLogEvent apacheLogEvent, long l) {
            //时间字段
            Long timeStamp = apacheLogEvent.getEventTime();
            currentMaxEventTime = Math.max(timeStamp, currentMaxEventTime);
            return timeStamp;
        }
    }

    /**
     * 把字符串解析成日志
     */
    public static class ParseLog implements MapFunction<String,ApacheLogEvent>{
        @Override
        public ApacheLogEvent map(String line) throws Exception {

            String[] fields = line.split(" ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
            long timeStamp = dateFormat.parse(fields[3].trim()).getTime();
            return new ApacheLogEvent(fields[0].trim(),fields[1].trim(),timeStamp,
                    fields[5].trim(),fields[6].trim());
        }
    }


    /**
     * 系统日志对象
     */
    public static class ApacheLogEvent{
        private String ip;
        private String userId;
        private Long eventTime;
        private String method;
        private String url;

        public ApacheLogEvent(){

        }

        public ApacheLogEvent(String ip, String userId, Long eventTime, String method, String url) {
            this.ip = ip;
            this.userId = userId;
            this.eventTime = eventTime;
            this.method = method;
            this.url = url;
        }

        @Override
        public String toString() {
            return "ApacheLogEvent{" +
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

    /**
     * 结果对象
     */
    public static class UrlView implements Comparable<UrlView>{
        private String url;
        private long windowEnd;
        private long count;


        @Override
        public String toString() {
            return "UrlView{" +
                    "url='" + url + '\'' +
                    ", windowEnd=" + new Timestamp(windowEnd) +
                    ", count=" + count +
                    '}';
        }

        public UrlView(){

        }

        public UrlView(String url, Long windowEnd, Long count) {
            this.url = url;
            this.windowEnd = windowEnd;
            this.count = count;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getWindowEnd() {
            return windowEnd;
        }

        public void setWindowEnd(Long windowEnd) {
            this.windowEnd = windowEnd;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        /**
         * 降序排序
         * @param urlView
         * @return
         */
        @Override
        public int compareTo(UrlView urlView) {
            return (this.count > urlView.count) ? -1 : ((this.count == urlView.count) ? 0 : 1);
        }
    }
}
