package app.gobusiness.com.remindernotificationdivyanshu;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addReminder;
    Database_Helper database_helper;
    TimePickerDialog timePickerDialog;
    EditText title, desc, times, date;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;

    int uniqueReminderId  = (int)System.currentTimeMillis();

    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DATE);
    int month = calendar.get(Calendar.MONTH);
    int mYear = calendar.get(Calendar.YEAR);


    List<ReminderModel> reminderModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recycle);


        loadReminder();


        calendar = Calendar.getInstance();
        addReminder = findViewById(R.id.addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReminderPOP();
            }
        });

    }

    public void loadReminder() {
        database_helper = new Database_Helper(MainActivity.this);

        reminderModelList = database_helper.getAllReminder();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

        reminderAdapter = new ReminderAdapter(MainActivity.this, reminderModelList);
        recyclerView.setAdapter(reminderAdapter);


    }

    private void addReminderPOP() {

        final Dialog dialog = new Dialog(this);
        View view = this.getLayoutInflater().inflate(R.layout.reminder_add_dialog, null);
        dialog.setContentView(view);


        Button add;
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        times = view.findViewById(R.id.time);
        date = view.findViewById(R.id.date);
        add = view.findViewById(R.id.ADD);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //datePop();
                displayDate();
            }
        });

///////////////////time picker
        times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("time", "Click");

                final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                        times.setText(hourOfDay+":"+minutes);

                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database_helper = new Database_Helper(MainActivity.this);


                String title1 = title.getText().toString();
                String desc1 = desc.getText().toString();
                String times1 = times.getText().toString();
                String date2 = date.getText().toString();

                String storeReminderId  = String.valueOf(uniqueReminderId);



                Boolean result = database_helper.addReminder(new ReminderModel(title1, desc1, times1, date2,storeReminderId));

                Log.d("#", "query: " + result.toString());

                try {

                    final Date tmpdate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(date2 + " " + times1);
                    Log.e(" ", "" + tmpdate.getTime());

                   setAlarm(tmpdate.getTime(), title1, desc1, times1, date2);

                  //  setAlarm(tmpdate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

            }


        });

        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        dialog.show();

    }

    private void setAlarm(long time, String ttl, String des, String times1, String date2) {

        Intent intent = new Intent(MainActivity.this, MyAlarm.class);
        intent.putExtra("title", ttl);
        intent.putExtra("desc", des);
        intent.putExtra("time", times1);
        intent.putExtra("date", date2);

        Log.d("MainActivity", "title: " + ttl);
        Log.d("MainActivity", "desc: " + des);
        Log.d("MainActivity", "time: " + times1);
        Log.d("MainActivity", "date: " + date2);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueReminderId, intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        loadReminder();
    }



    public void displayDate() {
        //opening date dialog
        new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        month = monthOfYear+1 ;
                        day = dayOfMonth;

                        String specDate = day + "/" + month + "/" + mYear;
                        Log.d("MainActivity", "Specified date: " + specDate);
                        date.setText(specDate);


                    }
                }, mYear, month, day).show();
    }


}
