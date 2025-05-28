package com.example.mlplatform.offline;

import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.sql.*;

public class RegressionJob {
    public static void run() {
        SparkSession spark = SparkSession.builder()
                .appName("Regression Job")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> dataset = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("data/regression.csv");

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"x"})
                .setOutputCol("features");

        Dataset<Row> trainingData = assembler.transform(dataset);

        LinearRegression lr = new LinearRegression()
                .setFeaturesCol("features")
                .setLabelCol("y");

        LinearRegressionModel model = lr.fit(trainingData);
        Dataset<Row> predictions = model.transform(trainingData);
        predictions.select("x", "y", "prediction")
        .write()
        .option("header", "true")
        .mode(SaveMode.Overwrite)
        .csv("output/regression_result");


        System.out.println("Coefficients: " + model.coefficients());
        System.out.println("Intercept: " + model.intercept());

        try {
            Process p = Runtime.getRuntime().exec("python visualization/plot_regression.py");
            p.waitFor();
            System.out.println("✅ 可视化图已生成！");
        } catch (Exception e) {
            System.err.println("❌ 无法执行可视化脚本！");
            e.printStackTrace();
        }

        spark.stop();
    }
}
