package com.atguigu.chapter05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class SinkToKafka {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "hadoop102:9092");

        DataStreamSource<String> kafkaStream = env.addSource(new FlinkKafkaConsumer<String>("clicks", new SimpleStringSchema(), properties));

        //anonymous class
        //return string
        SingleOutputStreamOperator<String> result = kafkaStream.map(new MapFunction<String, String>() {

            @Override
            public String map(String value) throws Exception {
                String[] fields = value.split(",");
                return new Event(fields[0].trim(), fields[1].trim(), Long.valueOf(fields[2].trim())).toString();
            }
        });

        result.addSink(new FlinkKafkaProducer<String>("hadoop102:9092", "events", new SimpleStringSchema()));

    }
}
