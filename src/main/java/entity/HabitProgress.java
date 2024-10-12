package entity;

import java.time.LocalDate;

public class HabitProgress {
    private LocalDate date;
    private boolean completed;

    public HabitProgress(LocalDate date, boolean completed) {
        this.date = date;
        this.completed = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }
}

