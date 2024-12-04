package NCafeManagement;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Validation {
    
    // Method to check for duplicate usernames
    public static boolean isUsernameUnique(String username, List<Admin> adminList) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                return false; // Username already exists
            }
        }
        return true; // Username is unique
    }
    
    public static boolean isPcIdUnique(int id, List<PC> pcs) {
        for (PC pc : pcs) {
            if(pc.getId()==id)
            {
                return false; //id is not unique
            }
        }
        return true; //id is unique
    }
    
    public static int readInt(Scanner scanner) {
        int intValue;
        while (true) {
            try {
                intValue = Integer.parseInt(scanner.nextLine());
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return intValue;
    }
    
    public static double readDouble(Scanner scanner) {
        double doubleValue;
        while (true) {
            try {
                doubleValue = Double.parseDouble(scanner.nextLine());
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double.");
            }
        }
        return doubleValue;
    }
    
    public static long readLong(Scanner scanner) {
        long longValue;
        while (true) {
            try {
                longValue = Long.parseLong(scanner.nextLine());
                break; // Exit the loop if input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid long.");
            }
        }
        return longValue;
    }
} 
