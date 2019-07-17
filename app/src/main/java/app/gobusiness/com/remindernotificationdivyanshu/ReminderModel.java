package app.gobusiness.com.remindernotificationdivyanshu;

public class ReminderModel  {


    public ReminderModel( String title, String description, String time, String date, String reminder_id) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.reminder_id = reminder_id;

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

    String id;
    String title;
    String description;
    String time;
    String date;
    String reminder_id;

    public String getUniqueReminderId() {
        return uniqueReminderId;
    }

    public void setUniqueReminderId(String uniqueReminderId) {
        this.uniqueReminderId = uniqueReminderId;
    }

    String uniqueReminderId;

    public String getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(String reminder_id) {
        this.reminder_id = reminder_id;
    }




}
