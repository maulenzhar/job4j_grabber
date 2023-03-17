package ru.job4j.grabber.utils;

import org.quartz.SchedulerException;

public interface Grab {
    void init() throws SchedulerException;
}