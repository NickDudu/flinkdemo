package com.atguigu.chapter05;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.ArrayList;
import java.util.Properties;

public class SourceTest {
    public SourceTest() {
    }

    public static void main(String[] args) throws Exception {
        // create env

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //1. read data from diff source
        DataStreamSource<String> stream1 = env.readTextFile("input/clicks.txt");

        //2. Collection In the code, normally for testing
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(5);

        DataStreamSource<Integer> numStream = env.fromCollection(nums);

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Mary", "./home", 1000L));
        events.add(new Event("bob", "./cart", 1000L));
        DataStreamSource<Event> stream2 = env.fromCollection(events);

        //3. from element
        DataStreamSource<Event> stream3 = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Nick", "./home", 2000L)
        );


        //4, from socket
        DataStreamSource<String> stream4 = env.socketTextStream("localhost", 7777);



        /*stream1.print("1");
        numStream.print("num");
        stream2.print("2");
        stream3.print("3");
        stream4.print("4");*/


        // 5, from Kafka
        Properties properties = new Properties();
        properties.setProperty("bootstrap servers", "");

        DataStreamSource<String> KafkaStream = env.addSource(new FlinkKafkaConsumer<String>("clicks", new SimpleStringSchema(), properties));

       /* KafkaStream.print();*/

        //6. custom




        env.execute();

    }
}
