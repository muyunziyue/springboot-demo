package com.flink.table;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * @author ldx
 * @date 2022/10/21
 */
public class CommonTableApiTest {
    public static void main(String[] args) {
        EnvironmentSettings envSettings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .useBlinkPlanner()
                .build();
        TableEnvironment tableEnv = TableEnvironment.create(envSettings);



    }
}
