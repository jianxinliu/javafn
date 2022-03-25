package com.jianxin.util.common.defer;


import java.util.*;

/**
 * @author jianxinliu
 * @date 2022/02/23 16:45
 */
public class DeferManager {
    private Map<Object, List<Defer<Object>>> taskMap = new HashMap<>();

    public void register(Defer<Object> defer) {
        boolean exist = taskMap.containsKey(defer.getType());
        if (exist) {
            List<Defer<Object>> defers = taskMap.get(defer.getType());
            defers.add(defer);
            taskMap.put(defer.getType(), defers);
        } else {
            taskMap.put(defer.getType(), Arrays.asList(defer));
        }
    }

    /**
     * @return 返回是否还需要再次调用
     */
    public boolean checkAll() {
        Map<Object, List<Defer<Object>>> tempMap = new HashMap<>(taskMap.size());
        taskMap.forEach((type, defers) -> {
            List<Defer<Object>> tempList = new ArrayList<>(defers.size());
            defers.forEach(defer -> {
                boolean done = defer.now();
                if (!done) {
                    tempList.add(defer);
                }
            });
            if (!tempList.isEmpty()) {
                tempMap.put(type, tempList);
            }
        });
        taskMap = tempMap;
        return !taskMap.isEmpty();
    }

    // TODO:
    public void rolling() {
        checkAll();
    }
}
