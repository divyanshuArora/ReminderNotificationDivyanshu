package app.gobusiness.com.remindernotificationdivyanshu;

public class ReminderModel  {


    public ReminderModel( String title, String description, String time, String date) {

        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
    }


    public ReminderModel()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String id, title, description, time, date;


}
