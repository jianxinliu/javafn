package com.jianxin.util.functional;

/**
 * @author jianxinliu
 * @date 2022/02/21 11:29
 */
@FunctionalInterface
public interface Function3<One, Two, Three, Ret> {

    Ret apply(One one, Two two, Three three);
}
