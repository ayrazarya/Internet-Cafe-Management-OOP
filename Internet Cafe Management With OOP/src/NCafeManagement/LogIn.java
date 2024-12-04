package NCafeManagement;

import java.io.*;

public class LogIn {
    private static boolean loggedIn = false;
    
    public static void logAdmin(Admin logAdmin, AdminManager amanage) 
    {
        for (Admin admin : amanage.getAdminList()) 
        {
          if(admin.getUsername().equals(logAdmin.getUsername())&&admin.getPassword().equals(logAdmin.getPassword()))
          {
             System.out.println("Succesfully logged in.");
             LogIn.loggedIn = true;
          }
        }
        if(!loggedIn)
        {
            System.out.println("That admin is not found.");
        }
    }
    
    public static void setLogIn(boolean newValue) 
    {
        LogIn.loggedIn = newValue;
    }
    
    public static boolean getLogIn() 
    {
        return loggedIn;
    }
    
}
