package com.jianxin.util.common.defer;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 用于注册延迟到某个条件满足时再执行的逻辑
 *
 * @author jianxinliu
 * @date 2022/02/23 11:58
 */
public class Defer<T> {

    private final T type;
    private Predicate<T> predicate;
    private Consumer<T> consumer;

    public Defer(T t) {
        this.type = t;
    }

    /**
     * 注册一个函数等待执行
     *
     * @param predicate
     * @param consumer
     * @return
     */
    public Defer<T> until(Predicate<T> predicate, Consumer<T> consumer) {
        this.predicate = predicate;
        this.consumer = consumer;

        return this;
    }

    /**
     * 立即触发一次已注册条件的判断
     */
    public boolean now() {
        if (this.predicate.test(this.type)) {
            this.consumer.accept(this.type);
            return true;
        }
        return false;
    }

    public T getType() {
        return type;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }
}
