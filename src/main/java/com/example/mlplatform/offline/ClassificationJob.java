package com.example.mlplatform.offline;

import com.example.mlplatform.utils.SparkUtils;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import java.util.Map;

public class ClassificationJob {
    public static void run() {
        SparkSession spark = SparkUtils.createSparkSession("Offline Classification Job");
        System.out.println("-----------------------offline--------------------------");
        Dataset<Row> data = spark.read().format("libsvm")
                .load("data/sample_libsvm_data.txt");

        LogisticRegression lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3);

        LogisticRegressionModel model = lr.fit(data);

        Dataset<Row> predictions = model.transform(data);

        predictions.select("features", "label", "prediction").show(10);

        // 计算混淆矩阵
        Dataset<Row> confusion = predictions.groupBy("label", "prediction").count();
        confusion.coalesce(1)  // 合并成一个文件，便于Python读取
         .write()
         .mode(SaveMode.Overwrite)
         .option("header", "true")
         .csv("output/confusion_matrix");

        // 计算准确率
        long correct = predictions.filter(functions.expr("label == prediction")).count();
        long total = predictions.count();
        double accuracy = (double) correct / total;
        System.out.printf("Accuracy: %.2f%%\n", accuracy * 100);

        spark.stop();
    }
}
