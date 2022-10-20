package com.flink.table;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;

/**
 * @Author ldx
 * @Date 2022/10/20
 * @Description table/sql的公共api
 **/
public class CommonApiTest {
    public static void main(String[] args) {
        // 1.1 基于blink版本进行流处理（流批一体）
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode() //.inBatchMode() 流/批执行模式
                .useBlinkPlanner() //.useOldPlanner() blink或旧的执行计划器
                .build();

        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 1.2 基于blink版本planner进行批处理
//        ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
//        BatchTableEnvironment batchTableEnv = BatchTableEnvironment.create(batchEnv);

        // 表在环境中有一个唯一的ID,由三部分构成：目录（catalog)、数据库（database）、表名（table）
        // 表的创建方式有两种：通过连接器（connector）、虚拟表（virtual table)两种

        // 指定使用的catalog和database
        tableEnv.useCatalog("default_catalog");
        tableEnv.useDatabase("default_database");

        //2. 创建连接器表
        String createSourceDDL = "CREATE TABLE clickTable (" +
                "`user` STRING," +
                "url STRING," +
                "ts BIGINT" +
                ") WITH(" +
                "'connector' = 'filesystem'," +
                "'path' = 'flink-demo/input'," +
                "'format' = 'csv'" +
                ")";

        // executeSql没有返回结果
        tableEnv.executeSql(createSourceDDL);

        // 创建一张用于输出的表
        String createSinkDDL = "CREATE TABLE outTable(" +
                "`user` STRING," +
                "ts BIGINT" +
                ") WITH(" +
                "'connector' = 'filesystem'," +
                "'path' = 'flink-demo/output'," +
                "'format' = 'csv'" +
                ")";

        tableEnv.executeSql(createSinkDDL);

        Table aliceVisitTable = tableEnv.sqlQuery(
                "SELECT `user`, ts FROM clickTable Where `user` = 'Alice'"
        );
        // 3. 创建临时表
        tableEnv.createTemporaryView("aliceVisitTable", aliceVisitTable);

        // 4.1 通过executeInsert将结果输出到outTable
        aliceVisitTable.executeInsert("outTable", false);

        // 4.2 通过SQL语句将结果输出到outTable中
        tableEnv.executeSql(
                "INSERT INTO outTable" +
                        "SELECT `user`, url FROM clickTable WHERE `user` = 'Alice'"
        );

    }

}
