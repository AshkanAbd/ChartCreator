package ir.ashkanabd;

import java.util.List;
import java.util.Objects;

public class Vahed {
    String name;
    int vahed;
    Info info;

    public static class Info {
        String exam;
        List<Time> times;

        public static class Time {
            String day;
            String time;
            boolean even;
            boolean odd;

            @Override
            public String toString() {
                return "Time{ day='" + day + "\', time='" + time + "\', even=" + even + ", odd=" + odd + " }";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Time time1 = (Time) o;
                return even == time1.even && odd == time1.odd && day.equals(time1.day) && time.equals(time1.time);
            }

            @Override
            public int hashCode() {
                return Objects.hash(day, time, even, odd);
            }
        }

        @Override
        public String toString() {
            return "Info{ exam='" + exam + "\', times=" + times + " }";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Info info = (Info) o;
            return exam.equals(info.exam) && times.equals(info.times);
        }

        @Override
        public int hashCode() {
            return Objects.hash(exam, times);
        }
    }

    @Override
    public String toString() {
        return "Vahed{ name='" + name + "\', vahed=" + vahed + ", info=" + info + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vahed vahed = (Vahed) o;
        return name.equals(vahed.name) && info.equals(vahed.info) && this.vahed == vahed.vahed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, info);
    }
}
