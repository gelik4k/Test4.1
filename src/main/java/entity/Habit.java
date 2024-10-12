package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Habit {
    private String name;
    private String description;
    private String frequency;
    private List<HabitProgress> progressHistory;

    public Habit(String name, String description, String frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.progressHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void markAsCompleted(LocalDate date) {
        progressHistory.add(new HabitProgress(date, true));
    }

    public List<HabitProgress> getProgressForPeriod(LocalDate start, LocalDate end) {
        return progressHistory.stream()
                .filter(progress -> !progress.getDate().isBefore(start) && !progress.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public int calculateStreak() {
        int streak = 0;
        LocalDate previousDate = null;
        for (HabitProgress progress : progressHistory) {
            if (progress.isCompleted()) {
                if (previousDate == null || previousDate.plusDays(1).equals(progress.getDate())) {
                    streak++;
                } else {
                    streak = 1;
                }
                previousDate = progress.getDate();
            }
        }
        return streak;
    }

    public double calculateCompletionRate(LocalDate start, LocalDate end) {
        long completedDays = getProgressForPeriod(start, end).stream().filter(HabitProgress::isCompleted).count();
        long totalDays = start.until(end).getDays() + 1;
        return (double) completedDays / totalDays * 100;
    }

    public void editHabit(String name, String description, String frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }
}

