package com.atguigu.chapter05;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Random;

public class SourceCustomTest {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

       /* DataStreamSource<Event> customStream = env.addSource(new ClickSource());*/

        DataStreamSource<Integer> customStream = env.addSource(new ParallelCustomSource()).setParallelism(2);

        customStream.print();
        env.execute();

    }

    public static class ParallelCustomSource implements ParallelSourceFunction<Integer>{
        private Boolean running = true;
        private Random ranndom = new Random();


        public void run(SourceContext<Integer> sourceContext) throws Exception {
            while (running)
                    sourceContext.collect(ranndom.nextInt());

        }

        @Override
        public void cancel() {
            running = false;
        }
    }
}
