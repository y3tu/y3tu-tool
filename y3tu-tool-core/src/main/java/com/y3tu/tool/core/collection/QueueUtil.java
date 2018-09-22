package com.y3tu.tool.core.collection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue工具集.
 * 各种Queue，Dequeue的创建
 *
 * @author y3tu
 */
public class QueueUtil {

    /**
     * 创建ArrayDeque (JDK无ArrayQueue)
     * <p>
     * 需设置初始长度，默认为16，数组满时成倍扩容
     *
     * @param initSize 初始长度
     * @return ArrayDeque对象
     */
    public static <E> ArrayDeque<E> newArrayDeque(int initSize) {
        return new ArrayDeque<E>(initSize);
    }

    /**
     * 创建LinkedDeque (LinkedList实现了Deque接口)
     *
     * @return LinkedList对象
     */
    public static <E> LinkedList<E> newLinkedDeque() {
        return new LinkedList<E>();
    }

    /**
     * 创建无阻塞情况下，性能最优的并发队列
     *
     * @return ConcurrentLinkedQueue对象
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentNonBlockingQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    /**
     * 创建无阻塞情况下，性能最优的并发双端队列
     *
     * @return ConcurrentLinkedDeque对象
     */
    public static <E> Deque<E> newConcurrentNonBlockingDeque() {
        return new java.util.concurrent.ConcurrentLinkedDeque<E>();
    }

    /**
     * 创建并发阻塞情况下，长度不受限的队列.
     * <p>
     * 长度不受限，即生产者不会因为满而阻塞，但消费者会因为空而阻塞.
     *
     * @return LinkedBlockingQueue对象
     */
    public static <E> LinkedBlockingQueue<E> newBlockingUnlimitQueue() {
        return new LinkedBlockingQueue<E>();
    }

    /**
     * 创建并发阻塞情况下，长度不受限的双端队列.
     * <p>
     * 长度不受限，即生产者不会因为满而阻塞，但消费者会因为空而阻塞.
     *
     * @return LinkedBlockingDeque对象
     */
    public static <E> LinkedBlockingDeque<E> newBlockingUnlimitDeque() {
        return new LinkedBlockingDeque<E>();
    }

    /**
     * 创建并发阻塞情况下，长度受限，更节约内存，但共用一把锁的队列（无双端队列实现
     *
     * @return ArrayBlockingQueue对象
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue<E>(capacity);
    }

    /**
     * 创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的队列.
     *
     * @return LinkedBlockingQueue对象
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
        return new LinkedBlockingQueue<E>(capacity);
    }

    /**
     * 创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的双端队列.
     *
     * @param capacity 固定长度
     * @return LinkedBlockingDeque对象
     */
    public static <E> LinkedBlockingDeque<E> newBlockingDeque(int capacity) {
        return new LinkedBlockingDeque<E>(capacity);
    }


}
