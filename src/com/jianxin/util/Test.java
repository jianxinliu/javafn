package com.jianxin.util;

import com.jianxin.util.functional.DoOnce;
import com.jianxin.util.functional.FunctionVoid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 使用反射调用测试方法
 * 用例方法必须是 static void 和 无参数的
 * 使用 test 方法划分测试用例
 *
 * @author jianxinliu
 * @date 2022/02/16 10:56
 */
public class Test {

    private static void doTest(String any) {
        List<Method> methods = new ArrayList<>();
        List<Method> allMethods = Arrays.stream(Test.class.getDeclaredMethods())
                .filter(m -> !m.getName().startsWith("lambda") && m.getParameterCount() == 0)
                .collect(Collectors.toList());
        for (String method : METHODS_RUNS) {
            allMethods.stream()
                    .filter(m -> m.getName().contains(method))
                    .findFirst().ifPresent(methods::add);
        }
        // METHODS_RUNS 为空时，运行全部
        if (METHODS_RUNS.length == 0) {
            methods.addAll(allMethods);
        }
        methods.forEach(method -> {
            method.setAccessible(true);
            try {
                System.out.println("【" + method.getName() + "】: ");
                method.invoke(Test.class);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    private static void test(String msg, FunctionVoid fn) {
        System.out.println("vvvvvvvvvvvvvvvv " + msg + " vvvvvvvvvvvvvvvv");
        fn.apply();
        System.out.println("^^^^^^^^^^^^^^^^ " + msg + " ^^^^^^^^^^^^^^^^");
        System.out.println();
    }


    public static void testOnce() {
        test("test once", () -> {
            DoOnce doOnce = new DoOnce();
            for (int i = 0; i < 3; i++) {
                boolean done = doOnce.doIt(() -> {
                    System.out.println("1----AA");
                });
                if (done) {
                    System.out.println("1----BB");
                }
            }

            DoOnce doOnce1 = new DoOnce();
            for (int i = 0; i < 3; i++) {
                doOnce1.once(() -> System.out.println("2----AA"))
                        .otherwise(() -> System.out.println("2----BB"));
            }
        });
    }

    public static void testDoubleCompare() {
        test("test double compare", () -> {
            List<Double> doubles = Arrays.asList(0.0, 2.0, 4.2, 1.0, 2.0, 50.3, null, 2.0);
            Optional<Double> max = doubles.stream().max(Double::compare);
            System.out.println(max.orElse(-1.3));
        });
    }

    public static void testStreamDistinctOrder() {
        test("stream distinct order", () -> {
            Stream.of("b", "a", "a", "b").distinct().forEach(System.out::println);
        });
    }

    public static void testNullListAccess() {
        test("test null list access", () -> {
            List<String> list = Arrays.asList("A", "B");
            String aa = Sugar.safeAccess(list, li -> li.get(4)).orElse("default");
            System.out.println(aa);
        });

        test("test null array access", () -> {
            String[] arr = new String[]{"A", "B"};
            String aDefault = Sugar.safeAccess(arr, array -> array[3]).orElse("default");
            System.out.println(aDefault);
        });

        test("test null array access", () -> {
            String[] arr = new String[]{"A", "B"};
            String aDefault = Sugar.safeArrayAccess(arr, 3);
            System.out.println(aDefault);
        });

    }

    public static void testReg() {
        Pattern pattern = Pattern.compile("y1|y2|both");
        Matcher s = pattern.matcher("y1");
        boolean b = s.find();
        System.out.println(b);

        Number number = 2;

        System.out.println(number.doubleValue());
    }

    public static void testTick() {
        TickLog tickLog = new TickLog("test");

        try {
            Thread.sleep(1000);
            tickLog.tick("a");
            Thread.sleep(1000);
            tickLog.tick("b");
            Thread.sleep(3300);
            tickLog.tick("c");
            tickLog.state();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testReg2() {
        final Pattern valuePattern = Pattern.compile("^[eE,.0-9]*$");
        System.out.println(valuePattern.matcher("2345.345").matches());
        System.out.println(valuePattern.matcher("0.2345345").matches());
        System.out.println(valuePattern.matcher("2,2345.345").matches());
        System.out.println(valuePattern.matcher("2345e10").matches());
        System.out.println(valuePattern.matcher("2345E10").matches());
        System.out.println(valuePattern.matcher("object[").matches());

        double v = 2.3123443;
        System.out.println((int) v);

        System.out.println(Math.sqrt(2) * Math.cos((45 * Math.PI) / 180));


        Object dou = new Object();
        System.out.println(dou.toString());
    }

    // 这些方法会被按顺序调用
    public static final String[] METHODS_RUNS = new String[]{
            "testReg2"
    };

    public static void main(String[] args) {
        doTest("");
    }

}
