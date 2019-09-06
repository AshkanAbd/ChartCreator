package ir.ashkanabd;

import java.util.*;

public class Chart implements Cloneable {
    Day sh;
    Day sh1;
    Day sh2;
    Day sh3;
    Day sh4;
    HashMap<String, String> exams;
    HashSet<String> vahedList;
    int sum = 0;

    Chart() {
        sh = new Day();
        sh1 = new Day();
        sh2 = new Day();
        sh3 = new Day();
        sh4 = new Day();
        exams = new HashMap<>();
        vahedList = new HashSet<>();
    }

    private Day getDay(String day) {
        if (day.equalsIgnoreCase("sh")) {
            return sh;
        }
        if (day.contains("1")) {
            return sh1;
        }
        if (day.contains("2")) {
            return sh2;
        }
        if (day.contains("3")) {
            return sh3;
        }
        if (day.contains("4")) {
            return sh4;
        }
        return null;
    }

    boolean canAdd(Vahed.Info.Time time, String exam, String name) {
        Day d = getDay(time.day);
        if (d == null) return false;
        Day.Time t = d.getTime(time.time);
        if (t == null) return false;
        if (time.odd) {
            if (t.odd != null) return false;
        }
        if (time.even) {
            if (t.even != null) return false;
        }
        if (this.exams.values().contains(exam)) {
            return this.vahedList.contains(name);
        }
        return true;
    }

    void addToDay(Vahed.Info.Time time, String exam, String name) {
        Day d = getDay(time.day);
        if (d == null) return;
        Day.Time t = d.getTime(time.time);
        if (t == null) return;
        if (time.even)
            t.even = name;
        if (time.odd)
            t.odd = name;
        this.exams.put(name, exam);
        this.vahedList.add(name);
    }

    private String examsToSting() {
        StringBuilder builder = new StringBuilder("[ ");
        for (Map.Entry<String, String> e : exams.entrySet()) {
            builder.append(e.getKey()).append(": ").append(e.getValue()).append(" , ");
        }
        return builder.delete(builder.length() - 3, builder.length()).append(" ]").toString();
    }

    private String vahedListToString() {
        StringBuilder builder = new StringBuilder("[ ");
        for (String e : vahedList) {
            builder.append(e).append(" , ");
        }
        return builder.delete(builder.length() - 3, builder.length()).append(" ]").toString();
    }

    @Override
    public String toString() {
        return "Chart{" + "\nsh => " + sh + ", \nsh1 => " + sh1 + ", \nsh2 => " + sh2 + ", \nsh3 => " + sh3
                + ", \nsh4 => " + sh4 + "\nTotal: " + sum + "\nExams: " + examsToSting() + "\nVahed: "
                + vahedListToString() + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chart chart = (Chart) o;
        return sum == chart.sum && sh.equals(chart.sh) && sh1.equals(chart.sh1) && sh2.equals(chart.sh2)
                && sh3.equals(chart.sh3) && sh4.equals(chart.sh4) && exams.equals(chart.exams) &&
                vahedList.equals(chart.vahedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sh, sh1, sh2, sh3, sh4, exams, vahedList, sum);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Chart chart = (Chart) super.clone();
        chart.sh = (Day) sh.clone();
        chart.sh1 = (Day) sh1.clone();
        chart.sh2 = (Day) sh2.clone();
        chart.sh3 = (Day) sh3.clone();
        chart.sh4 = (Day) sh4.clone();
        chart.vahedList = (HashSet<String>) vahedList.clone();
        chart.exams = (HashMap<String, String>) exams.clone();
        chart.sum = sum;
        return chart;
    }

    public static class Day implements Cloneable {

        Time t8;
        Time t10;
        Time t2;
        Time t4;

        Day() {
            t8 = new Time();
            t10 = new Time();
            t2 = new Time();
            t4 = new Time();
        }

        Time getTime(String time) {
            if (time.contains("8")) {
                return t8;
            }
            if (time.contains("10")) {
                return t10;
            }
            if (time.contains("2")) {
                return t2;
            }
            if (time.contains("4")) {
                return t4;
            }
            return null;
        }

        @Override
        public String toString() {
            return "{ t8 = " + t8 + " , t10 = " + t10 + " , t2 = " + t2 + " , t4 = " + t4 + " }";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Day day = (Day) o;
            return t8.equals(day.t8) && t10.equals(day.t10) && t2.equals(day.t2) && t4.equals(day.t4);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t8, t10, t2, t4);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Day day = (Day) super.clone();
            day.t8 = (Time) t8.clone();
            day.t10 = (Time) t10.clone();
            day.t2 = (Time) t2.clone();
            day.t4 = (Time) t4.clone();
            return day;
        }

        public static class Time implements Cloneable {
            String odd = null;
            String even = null;

            @Override
            public String toString() {
                if (odd != null && even != null) {
                    if (odd.equals(even)) {
                        return "< " + odd + " >";
                    } else {
                        return "< " + odd + " / " + even + " >";
                    }
                }
                if (odd != null && even == null) {
                    return "< " + odd + " / Free >";
                }
                if (odd == null && even != null) {
                    return "< Free / " + even + " >";
                } else {
                    return "< Free >";
                }
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Time time = (Time) o;
                return Objects.equals(odd, time.odd) && Objects.equals(even, time.even);
            }

            @Override
            public int hashCode() {
                return Objects.hash(odd, even);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {
                Time time = (Time) super.clone();
                time.odd = odd;
                time.even = even;
                return time;
            }
        }
    }
}
