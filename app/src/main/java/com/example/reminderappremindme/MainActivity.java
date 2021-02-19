package com.example.reminderappremindme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.MINUTE;

public class MainActivity extends AppCompatActivity {

    public static final String NC_ID = "10001";

    private final static String default_notif_ID = "default";

    private static final String TAG = "MainActivity";

    private com.example.reminderappremindme.DBhelp dbhelp;

    private ListView listView;

    private FloatingActionButton FAbtn;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);

    // set main page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelp = new com.example.reminderappremindme.DBhelp(this);
        FAbtn = findViewById(R.id.FAbtn);
        listView = findViewById(R.id.listItem);

        produceListView();
        FAbtnClicked();
    }

    // set notification
    private void scheduleNotif(Notification notif, long latencies) {
        Intent notifIntent = new Intent(this, com.example.reminderappremindme.Notifications.class);
        notifIntent.putExtra(com.example.reminderappremindme.Notifications.NOTIF_ID, 1);
        notifIntent.putExtra(com.example.reminderappremindme.Notifications.NOTIF, notif);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getLayoutInflater().getContext().getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.set(AlarmManager.RTC_WAKEUP, latencies, pi);
        }
    }

    private Notification getNotification(String contents) {

        // when notif is clicked go to main activity
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder bobTheBuilder = new NotificationCompat.Builder(getLayoutInflater().getContext(), default_notif_ID);
        bobTheBuilder.setContentTitle(" Reminder ");
        bobTheBuilder.setContentText(contents);
        bobTheBuilder.setContentIntent(pi);
        bobTheBuilder.setAutoCancel(true);
        bobTheBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        bobTheBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        bobTheBuilder.setChannelId(NC_ID);
        bobTheBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        return bobTheBuilder.build();
    }

    // Input data to db
    private void inputDataToDb(String DBtitle, String DBdate, String DBtime) {
        boolean inputData = dbhelp.inputData(DBtitle, DBdate, DBtime);
        if (inputData) {
            produceListView();
            toastMsg("Task added");
        }
    }

    // move all data from db to listview
    private void produceListView() {
        ArrayList<com.example.reminderappremindme.Datas> itemDatas = dbhelp.takeData();
        com.example.reminderappremindme.Items items = new com.example.reminderappremindme.Items(this, itemDatas);
        listView.setAdapter(items);
        items.notifyDataSetChanged();

    }

    private void FAbtnClicked() {
        FAbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                showDialog();
            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder bobBuildsDialog = new AlertDialog.Builder(getLayoutInflater().getContext());
        LayoutInflater lif = this.getLayoutInflater();
        final View dialogView = lif.inflate(R.layout.dialog, null);
        bobBuildsDialog.setView(dialogView);

        final EditText titleED = dialogView.findViewById(R.id.edit_title);
        final TextView dateED = dialogView.findViewById(R.id.date);
        final TextView timeED = dialogView.findViewById(R.id.time);

        final long date = System.currentTimeMillis();
        SimpleDateFormat dateSdf = new SimpleDateFormat("d MMMM");
        String dateString = dateSdf.format(date);
        dateED.setText(dateString);

        SimpleDateFormat timeSdf = new SimpleDateFormat("hh : mm a");
        String timeString = timeSdf.format(date);
        timeED.setText(timeString);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //Set dates
        dateED.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final DatePickerDialog dpd = new DatePickerDialog(getLayoutInflater().getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String newMonth = findMonth(monthOfYear + 1);
                                dateED.setText(dayOfMonth + " " + newMonth);
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                dpd.getDatePicker().setMinDate(date);
            }
        });

        //Set Time
        timeED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(getLayoutInflater().getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time;
                                String minTime = String.format("%02d", minute);
                                if (hourOfDay >= 0 && hourOfDay < 12) {
                                    time = hourOfDay + " : " + minTime + " AM";
                                } else {
                                    if (hourOfDay != 12) {
                                        hourOfDay = hourOfDay - 12;
                                    }
                                    time = hourOfDay + " : " + minTime + " PM";
                                }
                                timeED.setText(time);
                                calendar.set(Calendar.HOUR, hourOfDay);
                                calendar.set(MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                Log.d(TAG, "Time set Successfully");
                            }
                        }, calendar.get(Calendar.HOUR), calendar.get(MINUTE), false);
                tpd.show();
            }
        });

        bobBuildsDialog.setTitle("Create New Task");
        bobBuildsDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = titleED.getText().toString();
                String date = dateED.getText().toString();
                String time = timeED.getText().toString();
                if (title.length() != 0) {
                    inputDataToDb(title, date, time);
                    scheduleNotif(getNotification(title), calendar.getTimeInMillis());
                } else {
                    toastMsg("Task cant be empty");
                }
            }
        });
        bobBuildsDialog.setNegativeButton("Abort", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = bobBuildsDialog.create();
        alertDialog.show();
    }


    private void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }


    private String findMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

}