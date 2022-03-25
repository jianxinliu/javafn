package com.jianxin.util.functional;

/**
 * 对特定逻辑只执行一次
 * 两种使用方式：
 * 第一种使用方式
 * <pre>
 *     DoOnce doOnce = new DoOnce();
 *     doOnce.once(() -> {})
 *             .otherwise(() -> {});
 * </pre>
 * <p>
 * 第二种使用方式
 * <pre>
 *     DoOnce doOnce = new DoOnce();
 *     boolean onceDone = doOnce.doIt(() -> {});
 *     if (!onceDone) {
 *         // otherwise
 *     }
 * </pre>
 *
 * @author jianxinliu
 * @date 2022/02/21 15:57
 */
public class DoOnce {

    private Boolean did = false;

    public DoOnce once(FunctionVoid consumer) {
        if (!this.did) {
            consumer.apply();
        }
        return this;
    }

    public void otherwise(FunctionVoid other) {
        if (this.did) {
            other.apply();
        }
        this.did = true;
    }


    private Boolean aDid = false;

    /**
     * @param consumer
     * @return 本次未做
     */
    public boolean doIt(FunctionVoid consumer) {
        boolean did = false;
        if (!this.aDid) {
            consumer.apply();
            this.aDid = true;
            did = true;
        }
        return !did;
    }
}
