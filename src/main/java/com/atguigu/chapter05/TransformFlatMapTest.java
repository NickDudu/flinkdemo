package com.atguigu.chapter05;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class TransformFlatMapTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Nick", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L)
        );
        //1, implement interface
        stream.flatMap(new MyFlatMap()).print("1");


        //2, lambda expression, collector used for output
        stream.flatMap((Event value, Collector<String> out) -> {
            if (value.user.equals("Mary"))
                out.collect(value.url);

            else if (value.user.equals("Bob")){
                out.collect(value.user);
                out.collect(value.url);
                out.collect(value.timestamp.toString());
            }

        }).returns(new TypeHint<String>() {})
                .print("2");
        env.execute();
    }

    //实现一个自定义
    public static class MyFlatMap implements FlatMapFunction<Event, String>{

        @Override
        public void flatMap(Event event, Collector<String> out) throws Exception {
            out.collect(event.user);
            out.collect(event.url);
            out.collect(event.timestamp.toString());
        }
    }
}
