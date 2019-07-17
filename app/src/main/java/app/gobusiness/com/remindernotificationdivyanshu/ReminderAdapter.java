package app.gobusiness.com.remindernotificationdivyanshu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ItemViewHolder> {


    private static final String TAG = "TAG";
    List<ReminderModel> reminderModelList = new ArrayList<>();
    Context context;
    Database_Helper database_helper;
    long dateSet;

    Calendar calendar = Calendar.getInstance();
    TimePickerDialog timePickerDialog;

    int day = calendar.get(Calendar.DATE);
    int month = calendar.get(Calendar.MONTH);
    int mYear = calendar.get(Calendar.YEAR);



    public ReminderAdapter(Context context,List<ReminderModel> reminderModelList) {
        this.reminderModelList = reminderModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public ReminderAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.reminder_show_item,viewGroup,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReminderAdapter.ItemViewHolder itemViewHolder,final int i) {

        itemViewHolder.title_show.setText("Title: "+reminderModelList.get(i).getTitle());
        itemViewHolder.desc_show.setText("Description: "+reminderModelList.get(i).getDescription());
        itemViewHolder.date_show.setText("Date: "+reminderModelList.get(i).getDate());
        itemViewHolder.time_show.setText("Time: "+reminderModelList.get(i).getTime());

        Log.d(TAG, "time: "+reminderModelList.get(i).getTime().trim());
        Log.d(TAG, "date: "+reminderModelList.get(i).getDate().trim());



        itemViewHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String title3 = reminderModelList.get(i).getTitle();
               // final String id3 = reminderModelList.get(i).getTitle();
                final String desc3 = reminderModelList.get(i).getDescription();
                final String time3 = reminderModelList.get(i).getTime();
                final String date3 = reminderModelList.get(i).getDate();
                showPop(reminderModelList.get(i).getId(),title3,desc3,time3,date3);
                return false;
            }
        }); }

    private void showPop(final String id, final String title,final String descr,final String times, final String date )
    {
        final AlertDialog.Builder Builder = new AlertDialog.Builder(context);
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.update_delete_dialog,null);
        Builder.setView(view);

        final Button update,delete;
        final Dialog dialog = Builder.create();
        update = view.findViewById(R.id.updateItem);
        delete =view.findViewById(R.id.deleteItem);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database_helper = new Database_Helper(context);

                SQLiteDatabase sqLiteDatabase = database_helper.getWritableDatabase();

                String deleteItem = "DELETE FROM reminder WHERE ID="+id;

                Log.d(TAG, "delete item: "+deleteItem);

                sqLiteDatabase.execSQL(deleteItem);

                ((MainActivity)context).loadReminder();
                dialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            updatePop(id,title,descr,times,date);
            dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void updatePop(final String id, final String title,final String descr,final String times, final String date )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.reminder_add_dialog,null);
        builder.setView(view1);
        final Button updateContent;

        final Dialog dialog1 = builder.create();

     final  EditText titleEdit, desEdit, timeEdit ,dateEdit;

        titleEdit = view1.findViewById(R.id.title);
        desEdit = view1.findViewById(R.id.desc);
        timeEdit = view1.findViewById(R.id.time);
        dateEdit = view1.findViewById(R.id.date);


        String idUpdate= id;
        titleEdit.setText(title);
        desEdit.setText(descr);
        timeEdit.setText(times);
        dateEdit.setText(date);

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //opening date dialog
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear=year;
                                month=monthOfYear+1;
                                day=dayOfMonth;

                                String specDate=day + "/" + month + "/" + mYear;
                                Log.d("MainActivity","Specified Time: "+specDate);
                                dateEdit.setText(specDate);


                            }
                        }, mYear, month, day).show();
            }
        });



        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("time","Click");

                final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String am_pm;

                        if (hourOfDay >= 12)
                        {
                            am_pm = "PM";
                        }
                        else
                        {
                            am_pm = "AM";
                        }
                        timeEdit.setText(String.format("%02d:%02d",hourOfDay,minutes)+" "+am_pm);

                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();

            }
        });





        updateContent = view1.findViewById(R.id.ADD);
        updateContent.setText("UPDATE");

        updateContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title1,time1,date1,desc1;

                title1 = titleEdit.getText().toString();
                time1 = timeEdit.getText().toString();
                date1 = dateEdit.getText().toString();
                desc1 = desEdit.getText().toString();


                database_helper = new Database_Helper(context);
                SQLiteDatabase sqLiteDatabase = database_helper.getWritableDatabase();


                try {
                    String rowUpdate = "Update reminder set title ='" + title1 + "', time='" + time1 + "', date='" + date1 + "', description='" + desc1 + "' where id=" + id;
                    Log.d(TAG, "update Query:" + rowUpdate);
                    sqLiteDatabase.execSQL(rowUpdate);
                    ((MainActivity)context).loadReminder();

                    try
                    {

                        final  Date tmpdate = new SimpleDateFormat("dd/MM/yyyy hh:mm a").parse(date1+" "+time1);
                        Log.e("DateTime",""+tmpdate.getTime());

                        dateSet = tmpdate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                  //  setAlarm(dateSet,title1,desc1,time1,date1);
                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Not Updated", Toast.LENGTH_SHORT).show();
                    Log.d("update Query", "exception"+e);
                    ((MainActivity)context).loadReminder();
                    dialog1.dismiss();
                }



            }
        });



        dialog1.setCanceledOnTouchOutside(false);
        Window window = dialog1.getWindow();
        window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        dialog1.show();

    }

    private void setAlarm(long dateSet, String title1, String desc1, String time1, String date1)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,MyAlarm.class);
        intent.putExtra("title",title1);
        intent.putExtra("desc",desc1);
        intent.putExtra("time",time1);
        intent.putExtra("date",date1);

        context.startService(intent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dateSet,AlarmManager.INTERVAL_DAY,pendingIntent);
        ((MainActivity)context).loadReminder();


    }


    @Override
    public int getItemCount() {
        return reminderModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView title_show, time_show,date_show, desc_show;
        LinearLayout item;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title_show = itemView.findViewById(R.id.titleItem);
            time_show = itemView.findViewById(R.id.timeItem);
            date_show = itemView.findViewById(R.id.dateItem);
            desc_show = itemView.findViewById(R.id.descItem);
            item = itemView.findViewById(R.id.item);

        }
    }
}
