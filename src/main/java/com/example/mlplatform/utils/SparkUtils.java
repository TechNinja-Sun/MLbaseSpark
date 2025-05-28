package com.example.mlplatform.utils;

import org.apache.spark.sql.SparkSession;

public class SparkUtils {
    public static SparkSession createSparkSession(String appName) {
        return SparkSession.builder()
                .appName(appName)
                .master("local[*]")
                .getOrCreate();
    }
}
