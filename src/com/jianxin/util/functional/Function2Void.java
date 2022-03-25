package com.jianxin.util.functional;

/**
 * @author jianxinliu
 * @date 2022/03/01 15:10
 */
@FunctionalInterface
public interface Function2Void<R, V> {
    void accept(R r, V v);
}
