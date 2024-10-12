import entity.Habit;
import entity.User;
import usecases.AuthenticationService;
import usecases.HabitTracker;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static HabitTracker habitTracker = new HabitTracker();
    private static AuthenticationService authService = new AuthenticationService(habitTracker);
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        switch (choice) {
            case 1 -> register();
            case 2 -> login();
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private static void register() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authService.register(name, email, password)) {
            System.out.println("Registration successful.");
        } else {
            System.out.println("Email already in use.");
        }
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = authService.login(email, password);
        if (currentUser == null) {
            System.out.println("Invalid credentials.");
        } else {
            System.out.println("Login successful.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n1. Create Habit");
        System.out.println("2. View Habits");
        System.out.println("3. Edit Habit");
        System.out.println("4. Delete Habit");
        System.out.println("5. Edit Profile");
        System.out.println("6. Delete Account");
        System.out.println("7. Logout");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        switch (choice) {
            case 1 -> createHabit();
            case 2 -> viewHabits();
            case 3 -> editHabit();
            case 4 -> deleteHabit();
            case 5 -> editProfile();
            case 6 -> deleteAccount();
            case 7 -> currentUser = null;
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private static void createHabit() {
        System.out.print("Enter habit name: ");
        String name = scanner.nextLine();
        System.out.print("Enter habit description: ");
        String description = scanner.nextLine();
        System.out.print("Enter frequency: ");
        String frequency = scanner.nextLine();

        Habit habit = new Habit(name, description, frequency);
        habitTracker.addHabit(habit);
        System.out.println("Habit created.");
    }

    private static void viewHabits() {
        List<Habit> habits = habitTracker.getHabits(currentUser);
        if (habits.isEmpty()) {
            System.out.println("No habits found.");
        } else {
            System.out.println("Filter by: 1. Date Created 2. Completion Status 0. No Filter");
            int filterChoice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера
            switch (filterChoice) {
                case 1 -> habits.sort(Comparator.comparing(Habit::getName));
                case 2 -> habits.sort(Comparator.comparing(Habit::getDescription));
                case 0 -> {}
                default -> System.out.println("Invalid option. Showing unfiltered habits.");
            }
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName() + " - " + habits.get(i).getDescription());
            }
        }
    }

    private static void editHabit() {
        viewHabits();
        System.out.print("Choose habit number to edit: ");
        int habitNumber = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        Habit habit = habitTracker.getHabits(currentUser).get(habitNumber - 1);

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new frequency: ");
        String newFrequency = scanner.nextLine();

        habit.editHabit(newName, newDescription, newFrequency);
        System.out.println("Habit updated.");
    }

    private static void deleteHabit() {
        viewHabits();
        System.out.print("Choose habit number to delete: ");
        int habitNumber = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        habitTracker.getHabits(currentUser).remove(habitNumber - 1);
        System.out.println("Habit deleted.");
    }

    private static void editProfile() {
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        if (authService.updateUserProfile(currentUser, newEmail, newName, newPassword)) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Email already in use.");
        }
    }

    private static void deleteAccount() {
        System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirmation = scanner.nextLine();
        if ("yes".equalsIgnoreCase(confirmation)) {
            authService.deleteUser(currentUser);
            currentUser = null;
            System.out.println("Account deleted.");
        }
    }
}

