package com.atguigu.chapter05;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformSimpleAggTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L),
                new Event("Bob", "./prod?id=1", 3300L),
                new Event("Bob", "./home", 3500L),
                new Event("Alice", "./prod?id=200", 3200L),
                new Event("Bob", "./prod?id=2", 3800L),
                new Event("Bob", "./prod?id=3", 4200L)

        );

        //use key to partition then agg
        stream.keyBy(new KeySelector<Event, String>() {


            @Override
            public String getKey(Event value) throws Exception {
                return value.user;
            }
        }).max("timestamp")
                .print("max: ");

        //use lambda
        stream.keyBy(data -> data.user)
                .maxBy("timestamp")
                .print("maxBy: ");

        env.execute();
    }
}
