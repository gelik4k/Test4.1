import static org.junit.jupiter.api.Assertions.*;

import entity.Habit;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecases.AuthenticationService;
import usecases.HabitTracker;


public class HabitTrackerTest {
    private HabitTracker habitTracker;
    private AuthenticationService authService;

    @BeforeEach
    public void setup() {
        habitTracker = new HabitTracker();
        authService = new AuthenticationService(habitTracker);
    }

    @Test
    public void testRegisterAndLogin() {
        assertTrue(authService.register("Test User", "test@example.com", "password"));
        User user = authService.login("test@example.com", "password");
        assertNotNull(user);
        assertEquals("Test User", user.getName());
    }

    @Test
    public void testDuplicateEmail() {
        assertTrue(authService.register("Test User", "test@example.com", "password"));
        assertFalse(authService.register("Another User", "test@example.com", "newpassword"));
    }

    @Test
    public void testCreateAndViewHabits() {
        User user = new User("Test User", "test@example.com", "password");
        habitTracker.registerUser(user);
        Habit habit = new Habit("Read", "Read a book", "Daily");
        habitTracker.addHabit(habit);
        assertEquals(1, habitTracker.getHabits(user).size());
    }

    @Test
    public void testEditProfile() {
        User user = new User("Test User", "test@example.com", "password");
        habitTracker.registerUser(user);
        assertTrue(authService.updateUserProfile(user, "new@example.com", "New Name", "newpassword"));
        assertEquals("New Name", user.getName());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    public void testDeleteAccount() {
        User user = new User("Test User", "test@example.com", "password");
        habitTracker.registerUser(user);
        authService.deleteUser(user);
        assertFalse(habitTracker.getUserByEmail("test@example.com").isPresent());
    }
}
