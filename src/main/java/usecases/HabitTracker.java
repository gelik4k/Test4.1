package usecases;

import entity.Habit;
import entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabitTracker {
    private List<User> users;
    private List<Habit> habits;

    public HabitTracker() {
        this.users = new ArrayList<>();
        this.habits = new ArrayList<>();
    }

    public boolean registerUser(User user) {
        if (getUserByEmail(user.getEmail()).isPresent()) {
            return false; // Пользователь с таким email уже существует
        }
        users.add(user);
        return true;
    }

    public Optional<User> login(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public void deleteUser(User user) {
        users.remove(user);
        habits.removeIf(habit -> habit.getName().equals(user.getName())); // Удалить все привычки, связанные с пользователем
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public List<Habit> getHabits(User user) {
        return new ArrayList<>(habits);
    }
}
