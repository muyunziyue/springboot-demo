package com.flink.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ldx
 * @date 2023/1/31
 */
public class Test {
    public static void main(String[] args) throws Exception {
        MySqlSource<String> mysqlSource = MySqlSource.<String>builder()
                .hostname("192.168.5.96")
                .port(3306)
                .databaseList("mydb")
                .tableList("mydb.*")
                .username("flinkcdc")
                .password("flinkcdc")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(3000);
        env.fromSource(mysqlSource, WatermarkStrategy.noWatermarks(), "Mysql Source")
                .setParallelism(4)
                .print().setParallelism(1);

        env.execute("Print Mysql Snapshot + Binlog");
    }
}
