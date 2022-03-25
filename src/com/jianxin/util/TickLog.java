package com.jianxin.util;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianxinliu
 * @date 2022/03/08 20:35
 */
public class TickLog {
    private String groupName = "";
    private final List<Record> steps = new ArrayList<>();

    public TickLog() {
        steps.add(new Record());
    }

    public TickLog(String name) {
        this.groupName = name;
        steps.add(new Record());
    }

    private static class Record {
        String msg;
        Long time = System.currentTimeMillis();
        Double cost = 0.0;

        public Record() {}

        public Record(String msg, Long time) {
            this.msg = msg;
            this.time = time;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return String.format("[%s cost: %f]", msg, cost);
        }
    }

    public void tick(String msg) {
        Record last = Sugar.safeAccess(steps, list -> list.get(list.size() - 1)).orElse(new Record());
        long now = System.currentTimeMillis();
        double cost = toSecond(now - last.time);
        Record record = new Record(msg, now);
        record.setCost(cost);
        steps.add(record);
        String out = groupName + "-" + msg + " cost: " + cost + "s.";
        System.out.println(out);
//        LogUtil.info(out);
    }

    private double toSecond(Long mils) {
        return (mils / 1000.0);
    }

    public void state() {
        String collect = steps.stream().map(Record::toString).collect(Collectors.joining("; ", "【", "】"));
        List<Long> collect1 = steps.stream().map(Record::getTime).sorted().collect(Collectors.toList());
        Long min = Sugar.safeAccess(collect1, list -> list.get(0)).orElse(0L);
        Long max = Sugar.safeAccess(collect1, list -> list.get(list.size() - 1)).orElse(0L);
        String out = String.format(groupName + " total cost: %fs; \n %s", toSecond(max - min), collect);
        System.out.println(out);
//        LogUtil.info(out);
    }
}
