package NCafeManagement;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class PC {
    private boolean available = true;
    private int Id;
    private Date occupiedStartTime;
    private long occupiedDuration;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public PC(int id) //constructor
    {
        this.Id = id;
    }
    public PC(int id, Date start, long duration) //overloaded constructor
    {
        this.Id = id;
        this.occupiedStartTime = start;
        this.occupiedDuration = duration;
    }
    
    public void setId(int id)       //encapsulating getter setters
    {
        this.Id=id;
    }
    public int getId()
    {
        return Id;
    }
    public void setAvailability(boolean newbool)
    {
        this.available = newbool;
    }
    public boolean getAvailability()
    {
        return available;
    }
    
    public Date getStartTime()
    {
        return occupiedStartTime;
    }
    
    public void setStartTime(Date date)
    {
        this.occupiedStartTime = date;
    }
    
    public long getDuration()
    {
        return occupiedDuration;
    }
    
    public void setDuration(long duration)
    {
        occupiedDuration = duration;
    }
}
