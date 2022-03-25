package com.jianxin.util;

import com.jianxin.util.functional.FunctionVoid;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 语法糖工具
 *
 * @author jianxinliu
 * @date 2022/02/18 11:42
 */
public class Sugar {

    /**
     * 如果对象不为空则执行相应的逻辑
     * 如：
     * <pre>
     *     ifNotNullThen(student, stu -> {
     *        // do something with student object
     *     })
     * </pre>
     *
     * @param obj      待判断的对象
     * @param consumer 处理的逻辑。消费者，消费 obj 对象
     * @param <T>      对象类型
     * @return Optional<T>
     */
    public static <T> Optional<T> ifNotNullThen(T obj, Consumer<? super T> consumer) {
        Optional<T> obj1 = Optional.ofNullable(obj);
        obj1.ifPresent(consumer);
        return obj1;
    }

    public static <T> Optional<T> ifNotNullThenElse(T obj, Consumer<T> consumer, FunctionVoid elseConsumer) {
        Optional<T> optional = Optional.ofNullable(obj);
        if (optional.isPresent()) {
            consumer.accept(obj);
        } else {
            elseConsumer.apply();
        }
        return optional;
    }

    /**
     * 如果对象不为空，则传递给后面的函数作为参数，对其进行操作
     *
     * @param obj 被判断的对象
     * @param fn  具体操作对象的逻辑，返回另一个值
     * @param <T> 对象类型
     * @param <R> 返回值类型
     * @return 若对象为 null，则返回的 Optional 是 empty
     */
    public static <T, R> Optional<R> ifNotNullThenGet(T obj, Function<T, R> fn) {
        Optional<R> ret = Optional.empty();
        if (obj != null) {
            ret = Optional.ofNullable(fn.apply(obj));
        }
        return ret;
    }

    /**
     * 如果对象不为空，对该对象进行操作得到中间结果，并将中间结果传给最后的 consumer
     * <p>
     * 如：
     * <pre>
     *     Sugar.ifNotNullThenThen(fontSetting, FontSetting::getFontSize, style::setFontSize);
     * </pre>
     *
     * @param obj      待检测对象
     * @param fn       对该对象的操作， 得到中间结果
     * @param consumer 对中间结果的操作
     * @param <T>      对象的类型
     * @param <S>      中间结果的类型
     * @return
     */
    public static <T, S> Optional<T> ifNotNullThenThen(T obj, Function<T, S> fn, Consumer<? super S> consumer) {
        Optional<T> op = Optional.ofNullable(obj);
        if (op.isPresent()) {
            Optional.ofNullable(fn.apply(obj)).ifPresent(consumer);
        }
        return op;
    }

    /**
     * 在对象不为空的前提下获取属性值，否则给默认值
     * 如： getOrElse(student, () -> student.getName(), "jack");
     *
     * @param p   取值的对象
     * @param iff 对象不为空时的取值逻辑
     * @param els 默认值
     * @param <T> 对象的类型
     * @param <R> 值的类型
     * @return 对象字段值
     */
    public static <T, R> R getOrElse(T p, Supplier<R> iff, R els) {
        return p != null ? iff.get() : els;
    }

    /**
     * 如果条件不为空且为 true, 则执行逻辑
     *
     * @return null and true => empty; not null and false => Optional.of(false) 可据此做否则的操作
     */
    public static <Boolean> Optional<Boolean> ifTrueThen(Boolean p, FunctionVoid fn) {
        Optional<Boolean> ret = Optional.empty();
        if (p != null) {
            if (p.equals(true)) {
                fn.apply();
            } else {
                ret = Optional.of(p);
            }
        }
        return ret;
    }

    /**
     * 如果条件不为空且为 true, 则执行逻辑，获取值
     *
     * @return Optional, 如果 p 为空或 false, 则返回 Optional.empty()
     */
    public static <Boolean, R> Optional<R> ifTrueThenGet(Boolean p, Supplier<R> fn) {
        Optional<R> ret = Optional.empty();
        if (p != null) {
            if (p.equals(true)) {
                ret = Optional.ofNullable(fn.get());
            }
        }
        return ret;
    }

    /**
     * 安全的访问数组，而不报 ArrayIndexOutOfRangeException
     * 如： safeArrayAccess(arr, -1);  => null
     *
     * @param array 待访问的数组
     * @param index 下标
     * @param <T>   数组元素的类型
     * @return 访问不到则返回 null
     */
    public static <T> T safeArrayAccess(T[] array, int index) {
        return safeAccess(array, arr -> arr[index]).orElse(null);
    }

    public static <T> T safeListAccess(List<T> list, int index) {
        return safeAccess(list, li -> li.get(index)).orElse(null);
    }

    /**
     * 安全的从 T 类型的对象中获取 R 类型的值
     *
     * @param t
     * @param accessFn 获取方式
     * @param <T>      源对象类型
     * @param <R>      返回值类型
     * @return
     */
    public static <T, R> Optional<R> safeAccess(T t, Function<T, R> accessFn) {
        R r = null;
        try {
            r = accessFn.apply(t);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(r);
    }

    /**
     * 组成观感上更内聚的代码块，无实际作用
     *
     * @param description 该代码块的功能，可代替注释
     * @param fn          代码块实际内容
     */
    public static void codeBlock(String description, FunctionVoid fn) {
        fn.apply();
    }
}
