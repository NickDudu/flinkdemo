package com.atguigu.wc;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
public class WordCountStreaming {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //String inputPath = "/Users/huxiaod/Documents/github/flinkdemo/src/main/resources/hello.txt";
        //DataStreamSource<String> inputDataStream = env.readTextFile(inputPath);

        // 从socket文本流读取数据

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");
        System.out.println(port);
        DataStream<String> inputDataStream = env.socketTextStream(host, port);


        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new WordCount.MyFlatMapper())
                .keyBy(0)
                .sum(1).setParallelism(2);

        resultStream.print().setParallelism(1);

        env.execute();
    }
}


