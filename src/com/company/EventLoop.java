package com.company;

import java.util.LinkedList;
import java.util.List;

public class EventLoop {

    boolean active = true;
    List<Event> loop;

    public EventLoop() {
        loop = new LinkedList<>();
    }

    void run(){
        while(active && loop.size() > 0){
            loop.get(0).run();
            loop.remove(0);
            //run();
        }
    }

    void queueEvent(Event e){
        loop.add(e);
    }

}
