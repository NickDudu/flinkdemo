package com.atguigu.chapter05;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Calendar;
import java.util.Random;

public class ClickSource implements SourceFunction<Event> {
    //biao zhi wei
    private Boolean running = true;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {

        //ramdom generator

        Random random = new Random();
        //define coloumn
        String[] users = {"Mary", "Alice", "Bob", "Cary"};
        String[] urls = {"./home", "./cart", "./fav", "./prod?id=100", "./prod?id=0"};

        while (running){
            String user = users[random.nextInt(users.length)];
            String url = urls[random.nextInt(users.length)];
            Long timestamp = Calendar.getInstance().getTimeInMillis();
            sourceContext.collect(new Event(user, url, timestamp));
            Thread.sleep(1000L);
        }

    }

    @Override
    public void cancel() {
        running = false;
    }
}
