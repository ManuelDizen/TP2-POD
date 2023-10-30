package ar.edu.itba.pod.MapReduce.models;

public class Afflux {
    private final Integer positive;
    private final Integer negative;
    private final Integer neutral;

    public Afflux(Integer positive, Integer negative, Integer neutral) {
        this.positive = positive;
        this.negative = negative;
        this.neutral = neutral;
    }

    public Integer getPositive() {
        return positive;
    }

    public Integer getNegative() {
        return negative;
    }

    public Integer getNeutral() {
        return neutral;
    }
}
