package components;
import java.io.Serializable;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

    private String CourseID;
    private String Title;
    private String Section;
    private String Days;
    private String Time;
    private String Room;
    private String Description;
    private String Units;

    public Course(String courseID, String title, String units, String section, String days, String time, String room) {
        super();
        CourseID = courseID;
        Title = title;
        Units = units;
        Section = section;
        Days = days;
        Time = time;
        Room = room;
    }

    
    public String getCourseID() {   return CourseID;    }

    public String getTitle() {        return Title;    }

    public String getUnits() {        return Units;    }

    public String getSection() {        return Section;    }

    public String getDays() {        return Days;    }

    public String getTime() {        return Time;    }

    public String getRoom() {        return Room;    }

    public String getDescription() {        return Description;    }

    
    public void setSection(String section) {        Section = section;    }

    public void setDays(String days) {        Days = days;    }

    public void setTime(String time) {        Time = time;    }

    public void setRoom(String room) {        Room = room;    }

    public void setDescription(String description) {        Description = description;    }

    void getDetails() {
        // dehins pa toh sah
        System.out.println("Course ID: " + CourseID);
        System.out.println("Title: " + Title);
        System.out.println("Units: " + Units);
        System.out.println("Section: " + Section);
        System.out.println("Days: " + Days);
        System.out.println("Time: " + Time);
        System.out.println("Room: " + Room);
    }
}

