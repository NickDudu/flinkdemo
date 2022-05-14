package com.atguigu.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


public class WordCount {
    public static void main(String[] args) throws Exception{
        // chunjinahuajinag, just like spark context = executionEnvrionment
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // read file

        String inputPath = "/Users/huxiaod/Documents/github/flinkdemo/src/main/resources/hello.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);

        //for data
        DataSet<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap( new MyFlatMapper() )
                .groupBy(0)
                .sum(1);
        resultSet.print();

    }

    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>>{

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split(" ");
            for( String word: words ){
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}