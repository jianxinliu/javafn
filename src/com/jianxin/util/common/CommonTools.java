package com.jianxin.util.common;


import java.util.*;

/**
 * @author jianxinliu
 * @date 2022/02/17 15:49
 */
public class CommonTools {

    public static <T, K, R> T getClassFactory(Map<K, String> keyTypeMap, K key, Class<R> baseClass) {
        T handler;

        String handlerName = keyTypeMap.get(key);
        if (handlerName == null) {
            throw new IllegalArgumentException("暂不支持的类型");
        }
        String handlerPath = getObjectPath(baseClass);
        try {
            handler = (T) Class.forName(handlerPath + handlerName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("暂不支持的类型或 handler 未定义");
        }
        return handler;
    }

    private static <R> String getObjectPath(Class<R> clazz) {
        String path = clazz.getName();
        String simpleName = clazz.getSimpleName();
        return path.replace(simpleName, "") + "impl.";
    }

    /**
     * 将 list 前后为 null 的元素去除
     *
     * @param list list
     * @param <T>  list 内元素类型
     * @return trim 后的 list
     */
    public static <T> List<T> trimList(List<T> list) {
        List<Integer> needRemove = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                needRemove.add(i);
            } else {
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) == null) {
                needRemove.add(i);
            } else {
                break;
            }
        }

        List<T> ret = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!needRemove.contains(i)) {
                ret.add(list.get(i));
            }
        }
        return ret;
    }

}
