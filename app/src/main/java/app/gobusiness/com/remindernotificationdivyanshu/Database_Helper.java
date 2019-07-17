package app.gobusiness.com.remindernotificationdivyanshu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Database_Helper extends SQLiteOpenHelper {



private static final String DB_NAME = "reminder_notification";
private static final String  TABLE_NAME = "reminder";
private static final String ID = "id";
private static final String TITLE = "title";
private static final String DESCRIPTION = "description";
private static final String TIME = "time";
private static final String DATE ="date";


public  Database_Helper database_helper;
public  SQLiteDatabase sqLiteDatabase;


        public Database_Helper(Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        String CREATE_TABLE_REMINDER = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + TITLE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + TIME + " TEXT,"
                + DATE + " TEXT"
                + ")";

                sqLiteDatabase.execSQL(CREATE_TABLE_REMINDER);


        Log.d("Create Table:", " "+ CREATE_TABLE_REMINDER);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addReminder(ReminderModel reminderModel)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title",reminderModel.getTitle());
        contentValues.put("description",reminderModel.getDescription());
        contentValues.put("time",reminderModel.getTime());
        contentValues.put("date",reminderModel.getDate());

        sqLiteDatabase.insert(TABLE_NAME, null,contentValues);
        sqLiteDatabase.close();

        return true;
    }




    public List<ReminderModel> getAllReminder() {
        String[] column = {ID, TITLE, DESCRIPTION, TIME, DATE};

        String sortOrder = DATE;

        List<ReminderModel> reminderModels = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, column, null, null, null, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {

                ReminderModel reminderModel = new ReminderModel();

                reminderModel.setId(cursor.getString(cursor.getColumnIndex(ID)));
                reminderModel.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                reminderModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                reminderModel.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                reminderModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));

                reminderModels.add(reminderModel);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return reminderModels;
    }
    }
