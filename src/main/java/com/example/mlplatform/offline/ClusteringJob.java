package com.example.mlplatform.offline;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.*;

public class ClusteringJob {
    public static void run() {
        SparkSession spark = SparkSession.builder()
                .appName("Clustering Job")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> dataset = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("data/clustering.csv");

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"x", "y"})
                .setOutputCol("features");

        Dataset<Row> assembled = assembler.transform(dataset);


        KMeans kmeans = new KMeans().setK(2).setSeed(1L).setFeaturesCol("features");
        KMeansModel model = kmeans.fit(assembled);
        // 添加保存结果
        Dataset<Row> predictions = model.transform(assembled);
        predictions.select("x", "y", "prediction")
            .write()
            .option("header", "true")
            .mode(SaveMode.Overwrite)
            .csv("output/clustering_result");

        System.out.println("Cluster Centers:");
        for (org.apache.spark.ml.linalg.Vector center : model.clusterCenters()) {
            System.out.println(center);
        }

        try {
            Process p = Runtime.getRuntime().exec("python visualization/plot_clustering.py");
            p.waitFor();
            System.out.println("✅ 可视化图已生成！");
        } catch (Exception e) {
            System.err.println("❌ 无法执行可视化脚本！");
            e.printStackTrace();
        }

        spark.stop();
    }
}
