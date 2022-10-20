package com.flink.project.zeye.user;

import com.flink.project.zeye.utils.Utils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 统计每小时的UV
 *
 * 如果在不是很严谨的情况下，我们用这种方式也是可以。
 */
public class UVCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        env.readTextFile(Utils.USER_BEHAVIOR_PATH) //读取数据
                .map(new ParseUserLog()) //解析数据
                .assignTimestampsAndWatermarks(new EventTimeExtractor()) //执行watermark
                .filter( behavior -> behavior.behavior.equalsIgnoreCase("P")) //过滤用户行为
                .timeWindowAll(Time.hours(1))//滚动窗口
                .apply(new UvCountByWindow()) //UV统计
                .print();

        env.execute("UVCount");

    }

    public static class UvInfo{
        private String windowEnd;
        private Long uvCount;

        public UvInfo(){

        }

        @Override
        public String toString() {
            return "UvInfo{" +
                    "windowEnd='" + windowEnd + '\'' +
                    ", uvCount=" + uvCount +
                    '}';
        }

        public UvInfo(String windowEnd, Long uvCount) {
            this.windowEnd = windowEnd;
            this.uvCount = uvCount;
        }

        public String getWindowEnd() {
            return windowEnd;
        }

        public void setWindowEnd(String windowEnd) {
            this.windowEnd = windowEnd;
        }

        public Long getUvCount() {
            return uvCount;
        }

        public void setUvCount(Long uvCount) {
            this.uvCount = uvCount;
        }
    }

    /**
     * 统计UV
     * 这个方式其实是能计算出来结果的
     * 但是不适合数量特别大的情况。
     */
    public static class UvCountByWindow
            implements AllWindowFunction<UserBehavior,UvInfo, TimeWindow> {

        @Override
        public void apply(TimeWindow timeWindow,
                          Iterable<UserBehavior> iterable,
                          Collector<UvInfo> out) throws Exception {
            //Set集合，自动去重
            //数据在内存里面，其实不安全。
            Set<Long> userIds = new HashSet<Long>();
            Iterator<UserBehavior> iterator = iterable.iterator();
            while(iterator.hasNext()){
                //把当前窗口里面所有的userID都存到集合里面
               userIds.add(iterator.next().userId);
            }

            //集合的大小就是当前窗口的UV
            int count = userIds.size();
            //输出结果。
            out.collect(new UvInfo(new Timestamp(timeWindow.getEnd())+"",
                    Long.parseLong(count+"")));

        }
    }

    public static class EventTimeExtractor
            implements AssignerWithPeriodicWatermarks<UserBehavior>{
        private long currentMaxEventTime = 0L;
        private long  maxOutOfOrderness = 10;//10 000

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOutOfOrderness);
        }

        @Override
        public long extractTimestamp(UserBehavior userBehavior, long l) {
            long timeStamp = userBehavior.timeStamp* 1000;
            currentMaxEventTime = Math.max(timeStamp,currentMaxEventTime);
            return timeStamp;
        }
    }


    /**
     * 解析用户行为数据
     */
    public static class ParseUserLog implements MapFunction<String,UserBehavior>{
        @Override
        public UserBehavior map(String line) throws Exception {
            String[] fields = line.split(",");
            return new UserBehavior(Long.parseLong(fields[0].trim()),
                    Long.parseLong(fields[1].trim()),
                    Long.parseLong(fields[2].trim()),
                    fields[3].trim(),
                    Long.parseLong(fields[4].trim()),
                    fields[5].trim()
            );
        }
    }

    /**
     * 用户行为类
     */
    public static class UserBehavior{
        private Long userId;
        private Long productId;
        private Long categoryId;
        private String behavior;
        private Long timeStamp;
        private String sessionId;

        public UserBehavior(){

        }

        public UserBehavior(Long userId, Long productId,
                            Long categoryId,
                            String behavior,
                            Long timeStamp,
                            String sessionId) {
            this.userId = userId;
            this.productId = productId;
            this.categoryId = categoryId;
            this.behavior = behavior;
            this.timeStamp = timeStamp;
            this.sessionId = sessionId;
        }

        @Override
        public String toString() {
            return "UserBehavior{" +
                    "userId=" + userId +
                    ", productId=" + productId +
                    ", categoryId=" + categoryId +
                    ", behavior='" + behavior + '\'' +
                    ", timeStamp=" + timeStamp +
                    ", sessionId='" + sessionId + '\'' +
                    '}';
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public String getBehavior() {
            return behavior;
        }

        public void setBehavior(String behavior) {
            this.behavior = behavior;
        }

        public Long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(Long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }

}
