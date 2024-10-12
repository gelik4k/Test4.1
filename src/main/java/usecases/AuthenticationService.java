package usecases;

import entity.User;

public class AuthenticationService {
    private HabitTracker habitTracker;

    public AuthenticationService(HabitTracker habitTracker) {
        this.habitTracker = habitTracker;
    }

    public boolean register(String name, String email, String password) {
        return habitTracker.registerUser(new User(name, email, password));
    }

    public User login(String email, String password) {
        return habitTracker.login(email, password).orElse(null);
    }

    public boolean updateUserProfile(User user, String newEmail, String newName, String newPassword) {
        if (habitTracker.getUserByEmail(newEmail).isPresent() && !newEmail.equals(user.getEmail())) {
            return false;
        }
        user.setEmail(newEmail);
        user.setName(newName);
        user.setPassword(newPassword);
        return true;
    }

    public void deleteUser(User user) {
        habitTracker.deleteUser(user);
    }
}
