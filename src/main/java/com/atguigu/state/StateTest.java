package com.atguigu.state;

import com.atguigu.chapter05.ClickSource;
import com.atguigu.chapter05.Event;
import com.atguigu.chapter05.TransformFlatMapTest;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class StateTest {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        })
                );

        stream.keyBy(data -> data.user)
                .flatMap( new MyFlatMap() )
                .print();

        env.execute();

    }

    //实现自定义FlatMapFunction, 用于Keyed State测试
    public static class MyFlatMap extends RichFlatMapFunction<Event, String>{
        //state
        ValueState<Event> myValueState;

        @Override
        public void open(Configuration parameters) throws Exception{
            myValueState = getRuntimeContext().getState(new ValueStateDescriptor<Event>("my-state", Event.class));

        }


        @Override
        public void flatMap(Event value, Collector<String> out) throws Exception {
            System.out.println(myValueState.value());
            myValueState.update(value);
            System.out.println("my value: " + myValueState.value());

        }

    }
}
