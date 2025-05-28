package com.example.mlplatform;

import com.example.mlplatform.offline.ClassificationJob;
import com.example.mlplatform.offline.ClusteringJob;
import com.example.mlplatform.offline.RecommendationJob;
import com.example.mlplatform.offline.RegressionJob;
import com.example.mlplatform.streaming.RealtimeProcessor;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("用法: java -jar app.jar [offline-classification|offline-clustering|offline-recommendation|offline-regression|streaming]");
            return;
        }

        switch (args[0]) {
            case "offline-classification":
                ClassificationJob.run();
                break;
            case "offline-clustering":
                ClusteringJob.run();
                break;
            case "offline-recommendation":
                RecommendationJob.run();
                break;
            case "offline-regression":
                RegressionJob.run();
                break;
            case "streaming":
                RealtimeProcessor.run();
                break;
            default:
                System.out.println("未知参数: " + args[0]);
        }
    }
}
