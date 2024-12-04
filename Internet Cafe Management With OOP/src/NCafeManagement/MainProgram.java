package NCafeManagement;

import java.util.*;

public class MainProgram {
    public static void main(String[] args)
    {   //initialize classes used in the program
        Admin admin = null;
        AdminManager admins = new AdminManager();
        PCManager pcs = new PCManager();
        TransactionHistory transaction = new TransactionHistory();
        Scanner in = new Scanner(System.in);
        admins.loadValidAdmin();//check if file exists, read valid admins and commit to object list. If file does not exist, create it.
        
        while (!LogIn.getLogIn()){
            String username;
            String password;
            System.out.println("======================\n");
            System.out.print("Username    :");
            username = in.nextLine();
            System.out.print("Password    :");
            password = in.nextLine();
            admin = admins.getThatAdmin(username, password); //make inputted admin credentials to existing admin object.
            if (admin != null){
                LogIn.logAdmin(admin, admins);//log admin in if successful.
            }//if admin is still null, back to the top.
        }
        System.out.println("\n======================");
        System.out.println("Welcome, "+admin.getUsername()+"\n");
        pcs.loadPCs(); //same as validadmins for pcs, done after log in.
        transaction.readTransactionHistory();
        boolean exit = false;
        while(!exit){//repeat menu until exit option is selected
            System.out.println("Select your action: \n1. PC Management\n2. Admin Management\n3. Exit");
            int input;
            input = Validation.readInt(in);
            switch (input){
                case 1:
                    PCManagementMenu(in, pcs, transaction);
                    break;
                case 2:
                    if(admins.isInstanceOfRegularAdmin(admin)){     //admin references currently logged in admin.
                        adminManagementMenu(in, admins, admin);
                    }
                    else if (admins.isInstanceOfSuperAdmin(admin)){
                        superAdminManagementMenu(in, admins, admin);
                    }
                    break;
                case 3:     //write objects to file and exit program.
                    pcs.writePCsToFile();
                    admins.writeValidAdmins();
                    exit = true;
                    break;
                }
        }
        in.close();
    }
    
    static void PCManagementMenu(Scanner in, PCManager pcs, TransactionHistory transaction)
    {
        boolean exit = false;
        long paidDuration = 0;
        while(!exit){
            System.out.println("\n======================\nPC MANAGEMENT MENU\nPlease select an option:");
            if(paidDuration != 0){
                System.out.println("Customer has paid for "+paidDuration/60000+" minutes");
            }
            System.out.println("1. View managed PCs\n2. Activate a PC\n3. Add a new PC to the system\n4. Payment\n5. Back");
            int input = Validation.readInt(in);
            switch (input){
                case 1:
                    pcs.displayPCStatus();
                    break;
                case 2:
                    int id;
                    long duration;
                    System.out.print("Enter the PC's ID: ");
                    id = Validation.readInt(in);
                    PC pc = pcs.findPC(id);
                    if(pc!=null){
                        if(0<paidDuration){
                            pcs.occupyPC(pc, paidDuration);
                            paidDuration = 0;
                        }
                        else{
                            System.out.print("Enter the customer's duration (in minutes): ");
                            duration = Validation.readLong(in)*60000;
                            pcs.occupyPC(pc, duration);
                        }
                    }
                    break;
                case 3:
                    System.out.print("Assign ID to the new PC: ");
                    id = Validation.readInt(in);
                    pcs.listPC(id);
                    break;
                case 4:
                    System.out.println("======================\n1. Cash\n2. QRIS\n3. Back");
                    int paymentInput = Validation.readInt(in);
                    double amount;
                    switch(paymentInput){
                        case 1:
                            System.out.println("Enter the amount");
                            amount = Validation.readDouble(in);
                            transaction.addCashTransaction(amount, new Date());
                            paidDuration = transaction.paymentToDuration(amount);
                            break;
                        case 2:
                            System.out.println("Enter the amount");
                            amount = Validation.readDouble(in);
                            transaction.addQrisTransaction(amount, new Date());
                            paidDuration = transaction.paymentToDuration(amount);
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                    break;
                case 5:
                    exit = true;
                    break;
                
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    
    static void adminManagementMenu(Scanner in, AdminManager admins, Admin admin)
    {
        int input;
        System.out.println("\n======================\nADMIN MANAGEMENT MENU\nPlease select an option:");
        System.out.println("1. View registered admins\n2. Change current admin's password\n3. Back");
        input = Validation.readInt(in);
        switch (input)
        {
            case 1:
                admins.displayAdmins();
                break;
            case 2:
                boolean exitProcess = false;
                while(!exitProcess){
                    System.out.print("Enter your username: ");
                    String username = in.nextLine();
                    if(username.equals(admin.getUsername())) {
                        System.out.println("Enter your old password: ");
                        String password = in.nextLine();
                        if(password.equals(admin.getPassword())){
                            System.out.println("Confirm your old password: ");
                            String confirmation = in.nextLine();
                            if(confirmation.equals(password)){
                                System.out.println("Enter your new password: ");
                                String newPassword = in.nextLine();
                                admin.setPassword(newPassword);
                                exitProcess = true;
                            }
                            else{
                                System.out.println("Password does not match!");
                            }
                        }
                        else {
                            System.out.println("Wrong password!");
                        }
                    } 
                    else {
                        System.out.println("Wrong username!");
                    }
                }
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid option");
                adminManagementMenu(in, admins, admin);
        }
    }
    
    static void superAdminManagementMenu(Scanner in, AdminManager admins, Admin admin)
    {
        int input;
        System.out.println("\n======================\nADMIN MANAGEMENT MENU\nPlease select an option:");
        System.out.println("1. View registered admins\n2. Change current admin's password\n3. Change other admins password\n4. Remove an admin\n5. Back");
        input = Validation.readInt(in);
        switch (input){
            case 1:
                admins.displayAdmins();
                break;
            case 2:
                boolean exitProcess = false;
                while(!exitProcess){
                    System.out.print("Enter your username: ");
                    String username = in.nextLine();
                    if(username.equals(admin.getUsername())) {
                        System.out.println("Enter your old password: ");
                        String password = in.nextLine();
                        if(password.equals(admin.getPassword())){
                            System.out.println("Confirm your old password: ");
                            String confirmation = in.nextLine();
                            if(confirmation.equals(password)){
                                System.out.println("Enter your new password: ");
                                String newPassword = in.nextLine();
                                admin.setPassword(newPassword);
                                exitProcess = true;
                            }
                            else{
                                System.out.println("Password does not match!");
                            }
                        }
                        else {
                            System.out.println("Wrong password!");
                        }
                    } 
                    else {
                        System.out.println("Wrong username!");
                    }
                }
                break;
            case 3:
                System.out.println("Enter your password: ");
                String password = in.nextLine();
                System.out.println("Confirm your password: ");
                String confirmPass = in.nextLine();
                if(password.equals(admin.getPassword()) && password.equals(confirmPass)){
                    System.out.println("Enter the admin's username: ");
                    String username = in.nextLine();
                    System.out.println("Enter new password: ");
                    String newPass = in.nextLine();
                    admins.changePassword(username, newPass);
                }
                else{
                    System.out.println("You have entered the wrong password.");
                }
                break;
            case 4:
                System.out.print("Enter the admin's username: ");
                String username = in.next();
                admins.removeAdmin(username);
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid option");
                superAdminManagementMenu(in, admins, admin);
                break;
        }
    }
    
}
