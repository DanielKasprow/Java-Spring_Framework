package com.example.demo.model.event;

import com.example.demo.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
