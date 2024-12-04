package NCafeManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminManager {
    private final List<Admin> adminList;
    
    public AdminManager()
    {adminList = new ArrayList<>();}
    
    static class RegularAdmin extends Admin
    {
       public RegularAdmin(String username, String password){
           super(username, password);
       }
    }
    
    static class SuperAdmin extends Admin
    {
        public SuperAdmin(String username, String password){
            super(username, password);
        }
    }

    void addRegularAdmin(String username, String password) {
        if(Validation.isUsernameUnique(username, adminList)){
            RegularAdmin admin = new RegularAdmin(username, password);
            adminList.add(admin);
        }
        else{
            System.out.println(username+" has already exist");
        }
    }
    
    void addSuperAdmin(String username, String password) {
        if(Validation.isUsernameUnique(username, adminList)){
            SuperAdmin admin = new SuperAdmin(username, password);
            adminList.add(admin);
        }
        else{
            System.out.println(username+" has already exist");
        }
    }
    
    boolean isInstanceOfRegularAdmin(Admin admin){
        if(admin instanceof RegularAdmin){
            return true;
        }
        else{
            return false;
        }
    }
    
    boolean isInstanceOfSuperAdmin(Admin admin){
        if(admin instanceof SuperAdmin){
            return true;
        }
        else{
            return false;
        }
    }


    void writeValidAdmins() //this is moved.
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("List_Admin.txt"));
            List<String> writtenUsername = new ArrayList<>();
            for (Admin admin : adminList) {
                if(!writtenUsername.contains(admin.getUsername())){
                    if(admin instanceof RegularAdmin){
                        writer.write(admin.getUsername() + ";" + admin.getPassword() + "\n");
                    }
                    else if (admin instanceof SuperAdmin){
                        writer.write(admin.getUsername()+";"+admin.getPassword()+";"+"overadminprivilege"+"\n");
                    }
                }
            }
            writer.close();
            System.out.println("Admin data is saved to file");
        } catch (IOException e) {
            System.err.println("Error in saving admin data");
        }
    }

    void loadValidAdmin() {
        try {
            String fileName = "List_Admin.txt";
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println(fileName + " not found, creating new file...");
                file.createNewFile();
            } else {
                BufferedReader fileRead;
                fileRead = new BufferedReader(new FileReader(fileName));
                String crntLine;
                while ((crntLine = fileRead.readLine()) != null) {
                    String[] adminData = crntLine.split(";");
                    String username = adminData[0];
                    String password = adminData[1];
                    if(adminData.length > 2 && adminData[2].equals("overadminprivilege")){
                        addSuperAdmin(username, password);
                        System.out.print(username+"is super admin\n");
                    }
                    else{
                        addRegularAdmin(username, password);
                        System.out.print(username+"is regular admin\n");
                    }
                }
                fileRead.close();
            }
        } catch (IOException e) {
            System.err.println("Failure to read file.");
        }
    }
    
    public Admin getThatAdmin (String username, String password) //to use an existing object in the list.
    {
        if(Validation.isUsernameUnique(username, adminList)){
            System.out.println("That username does not exist.");
        }
        else{
            for(Admin admin : adminList)
            {
              if (admin.getUsername().equals(username)&&admin.getPassword().equals(password))
                {
                    return admin;
                }
            }
        }
        return null;
    }
    
    void changePassword(String username, String newPassword) //change other admins password functionalities for super admins.
    {
        if(Validation.isUsernameUnique(username, adminList)){
            System.out.println("That username does not exist.");
        }
        else{
            for(Admin admin:adminList){
                if(admin.getUsername().equals(username)){
                    admin.setPassword(newPassword);
                }
            }
        }
    }
    
    void removeAdmin(String username) //remove admin by a super admin.
    {
        if(Validation.isUsernameUnique(username, adminList)){
            System.out.println("That username does not exist.");
        }
        else{
            for(Admin admin:adminList){
                if(admin.getUsername().equals(username)){
                    if(admin instanceof RegularAdmin){
                        System.out.println("Admin found, deleting admin "+admin.getUsername());
                        adminList.remove(admin);
                    }
                    else if (admin instanceof SuperAdmin) {
                        System.out.println("Can't delete a privileged admin");
                    }
                }
            }
        }
    }
    
    void displayAdmins() //display all admins in adminlist + their type
    {
        int iteration = 1;
        for(Admin admin : adminList)
        {
            if(admin instanceof RegularAdmin){
                System.out.println(iteration+". "+admin.getUsername()+" - Regular admin");
            }
            else if(admin instanceof SuperAdmin){
                System.out.println(iteration+". "+admin.getUsername()+" - Super admin");
            }
            iteration++;
        }
    }
    
    public List<Admin> getAdminList()
    {
        return adminList;
    }
}
