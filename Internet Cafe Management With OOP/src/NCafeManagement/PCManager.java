package NCafeManagement;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class PCManager {
    private final List<PC> pcs;
    private Map<PC, Timer> pcTimers = new HashMap<>();

    public PCManager() 
    {
        pcs = new ArrayList<>();
    }
    
    void listPC(int id)
    {
        if(Validation.isPcIdUnique(id, pcs)){
            PC pc = new PC(id);
            pcs.add(pc);
        }
        else{
            System.out.println("A PC with that ID has already exist.");
        }
    }
    
    void listPC(int id, Date start, long duration)
    {
        if(Validation.isPcIdUnique(id, pcs)){
            PC pc = new PC(id, start, duration);
            pc.setAvailability(false);
            pcs.add(pc);
        }
        else{
            System.out.println("A PC with that ID has already exist.");
        }
    }
    
    void loadPCs()
    {
        try{
            String fileName = "List_PC.txt";
            File pc = new File(fileName);
            if(!pc.exists()){
                System.out.println(fileName+" not found, creating new file...");
                pc.createNewFile();
            }
            else{
                BufferedReader fileRead;
                fileRead = new BufferedReader(new FileReader(fileName));
                String crntLine;
                while((crntLine = fileRead.readLine()) != null){
                    String adminData[] = crntLine.split(";");
                    int id = Integer.parseInt(adminData[0]);
                    String available = adminData[1];
                    if (available.equalsIgnoreCase("available")){
                        listPC(id);
                    }
                    else if (available.equalsIgnoreCase("occupied")){
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date start = sdf.parse(adminData[2]);
                            long duration = Long.parseLong(adminData[3]);
                            listPC(id, start, duration);
                        }
                        catch (ParseException e){
                            e.printStackTrace();
                            System.out.println("Error parsing PC list.");
                        }
                    }
                }
                fileRead.close();
            }
        }
        catch(IOException e){
            System.err.println("Failure to read file.");
        }
    }
    
    public void writePCsToFile() 
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("List_PC.txt"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (PC pc : pcs) {
                if(pc.getAvailability())
                {
                    writer.write(pc.getId()+";"+"AVAILABLE"+"\n");
                }
                else
                {
                    writer.write(pc.getId()+";"+"OCCUPIED"+";"+sdf.format(pc.getStartTime())+";"+pc.getDuration()+"\n");
                }
            }
            writer.close();
            System.out.println("Succesfully saved PCs.");
        } catch (IOException e) {
            System.out.println("Failed to save PCs.");
        }
    }

    public void displayPCStatus() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("======================\n");
        System.out.println("PC Status:");
        int iteration = 1;
        for (PC pc : pcs) {
            if(pc.getAvailability()){
                System.out.println(iteration+". PC "+pc.getId()+" is available.");
            }
            else{
                System.out.println(iteration+". PC "+pc.getId()+" is occupied since: "+sdf.format(pc.getStartTime())+" for "+pc.getDuration()/60000+ "minutes");
            }
        }
        System.out.println("\n======================\n");
    }
    
    public PC findPC(int id)
    {
        boolean found = false;
        for(PC pc : pcs){
            if(pc.getId()==id)
            {
                return pc;
            }
        }
        if(!found){
            System.out.println("A PC with that ID is not found.");
        }
        return null;
    }

    public void occupyPC(PC pc, long duration) {
        if (!pc.getAvailability()) {
            System.out.println("PC " + pc.getId() + " is already occupied.");
            return;
        }

        pc.setAvailability(false);
        pc.setStartTime(new Date());
        pc.setDuration(duration);
        System.out.println("PC " + pc.getId() + " is now occupied.");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logoutPC(pc);
                timer.cancel();
            }
        }, duration);

        pcTimers.put(pc, timer);
    }
    
    public void logoutPC(PC pc) {
        System.out.println("Customer logged out from PC " + pc.getId());
        pc.setAvailability(true);

        Timer timer = pcTimers.get(pc);
        if (timer != null) {
            timer.cancel();
        }

        // Set PC available after a delay
        Timer availableTimer = new Timer();
        availableTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                pc.setAvailability(true);
                System.out.println("PC " + pc.getId() + " is now available.");
                availableTimer.cancel();
            }
        }, 5000); // Set to available after 5 seconds
    }
}
