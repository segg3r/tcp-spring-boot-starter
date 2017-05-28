package com.segg3r.spring;

import com.segg3r.spring.tcp.TcpConnection;
import com.segg3r.spring.tcp.TcpController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@TcpController
public class TestTcpController {

    private final Lock lock = new ReentrantLock();
    private final Condition full = lock.newCondition();
    private final Condition empty = lock.newCondition();

    private Object object;

    public void receiveData(TcpConnection connection, Object object) throws InterruptedException {
        lock.lock();
        try {
            if (this.object != null)
                empty.await(10, TimeUnit.SECONDS);

            this.object = object;
            full.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object waitForObject() throws InterruptedException {
        lock.lock();
        try {
            if (this.object == null)
                full.await(10, TimeUnit.SECONDS);

            Object result = this.object;
            this.object = null;
            empty.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        this.object = null;
    }

}