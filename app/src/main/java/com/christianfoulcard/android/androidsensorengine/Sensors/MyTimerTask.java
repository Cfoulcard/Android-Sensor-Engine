package com.christianfoulcard.android.androidsensorengine.Sensors;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


abstract class MyTimerTask extends TimerTask {
    TimerTask task = new TimerTask() {
        public void run() {
            Timer timer = new Timer("Timer");
            long delay = 5000;
            timer.schedule(task, delay);
            System.out.println("Task performed on: " + new Date() + "n" +
                    "Thread's name: " + Thread.currentThread().getName());
        }
    };
}


