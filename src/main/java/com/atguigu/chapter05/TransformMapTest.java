package com.atguigu.chapter05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransformMapTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Nick", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L)
        );

        //1 use self declared function
        SingleOutputStreamOperator<String> result = stream.map(new MyMapper());


        //2 use anoymous class 匿名类
        SingleOutputStreamOperator<String> result1 = stream.map(new MapFunction<Event, String>() {

            @Override
            public String map(Event value) throws Exception {
                return value.user;
            }
        });


        //3, 匿名函数, lambda expression, 匿名函数也称为lambda函数。
        SingleOutputStreamOperator<String> result3 = stream.map(data -> data.user);


        result1.print();
        env.execute();

    }


    //mapper
    public static class MyMapper implements MapFunction<Event, String>{

        @Override
        public String map(Event value) throws Exception {
            return value.user;
        }
    }

}
