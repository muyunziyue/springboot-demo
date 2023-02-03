package com.flink.cep;

import com.flink.entity.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author ldx
 * @date 2022/10/28
 */
public class LoginFailDetectExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 1. 获取登录数据流
        SingleOutputStreamOperator<LoginEvent> loginEventStream = env.fromElements(
                new LoginEvent("user_1", "192.168.0.1", "fail", 2000L),
                new LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 4000L),
                new LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 9000L),
                new LoginEvent("user_2", "192.168.1.29", "success", 6000L)
        )
                .assignTimestampsAndWatermarks(WatermarkStrategy.<LoginEvent>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner(new SerializableTimestampAssigner<LoginEvent>() {
                            @Override
                            public long extractTimestamp(LoginEvent loginEvent, long l) {
                                return loginEvent.getTimestamp();
                            }
                        })

                );

        // 2. 定义模式，连续三次登陆失败
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("first") // 第一次登录失败
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return "fail".equals(loginEvent.getEventType());
                    }
                })
                .next("second") //紧跟着第二次登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return "fail".equals(loginEvent.getEventType());
                    }
                })
                .next("third") //紧跟着第三次登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return "fail".equals(loginEvent.getEventType());
                    }
                });

        // 3. 将模式应用到数据流上，检测复杂事件
        PatternStream<LoginEvent> patternStream = CEP.pattern(loginEventStream.keyBy(LoginEvent::getUserId), pattern);

        // 4. 将检测到的复杂事件提取出来，进行处理得到报警信息输出
        SingleOutputStreamOperator<String> warningStream = patternStream.select(new PatternSelectFunction<LoginEvent, String>() {
            @Override
            public String select(Map<String, List<LoginEvent>> map) throws Exception { // map的key是上面定义的简单事件的名称，list是因为一个事件允许重复发生
                // 提取复杂事件中的三次登陆失败事件
                LoginEvent firstFailEvent = map.get("first").get(0);
                LoginEvent secondFailEvent = map.get("second").get(0);
                LoginEvent thirdFailEvent = map.get("third").get(0);
                return firstFailEvent.getUserId() + " 连续三次登陆失败！登陆时间："
                        + firstFailEvent.getTimestamp() + "，"
                        + secondFailEvent.getTimestamp() + "，"
                        + thirdFailEvent.getTimestamp();
            }
        });

        warningStream.print();

        env.execute("Login Fail Detect");


    }
}
