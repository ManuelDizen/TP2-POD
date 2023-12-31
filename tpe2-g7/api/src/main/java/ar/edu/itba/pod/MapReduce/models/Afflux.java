package ar.edu.itba.pod.MapReduce.models;

import java.util.Objects;

public class Afflux {
    private Integer positive;
    private Integer negative;
    private Integer neutral;

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

    public void setNeutral(Integer neutral) {
        this.neutral = neutral;
    }

    public void incrementPositive(){this.positive += 1;}
    public void incrementNeutral(){this.neutral += 1;}
    public void incrementNegative(){this.negative += 1;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Afflux afflux = (Afflux) o;
        return Objects.equals(positive, afflux.positive) && Objects.equals(negative, afflux.negative) && Objects.equals(neutral, afflux.neutral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positive, negative, neutral);
    }
    public Integer getTotal(){
        return this.positive + this.negative + this.neutral;
    }
}
