package ar.edu.itba.pod.MapReduce.utils;

import java.time.LocalDateTime;

public class Query3ReturnType {
    private final String start;
    private final String end;
    private final LocalDateTime date;
    private final Integer minutes;


    public Query3ReturnType(String start, String end, LocalDateTime date, Integer minutes) {
        this.start = start;
        this.end = end;
        this.date = date;
        this.minutes = minutes;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getMinutes() {
        return minutes;
    }
}
