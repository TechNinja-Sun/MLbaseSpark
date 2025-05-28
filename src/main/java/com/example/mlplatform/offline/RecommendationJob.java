package com.example.mlplatform.offline;

import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.*;

public class RecommendationJob {
    public static void run() {
        SparkSession spark = SparkSession.builder()
                .appName("Recommendation Job")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> ratings = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("data/recommendation.csv");

        ALS als = new ALS()
                .setMaxIter(5)
                .setRegParam(0.01)
                .setUserCol("userId")
                .setItemCol("itemId")
                .setRatingCol("rating");

        ALSModel model = als.fit(ratings);

        Dataset<Row> userRecs = model.recommendForAllUsers(3);
        userRecs.write()
        .option("header", "true")
        .mode(SaveMode.Overwrite)
        .json("output/recommendation_result");

        userRecs.show(true);

        try {
            Process p = Runtime.getRuntime().exec("python visualization/plot_recommendation.py");
            p.waitFor();
            System.out.println("✅ 可视化图已生成！");
        } catch (Exception e) {
            System.err.println("❌ 无法执行可视化脚本！");
            e.printStackTrace();
        }

        spark.stop();
    }
}
