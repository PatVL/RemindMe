package com.example.reminderappremindme;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Items extends BaseAdapter {

    private Context c;
    private ArrayList<Datas> al;

    public Items(Context c, ArrayList<Datas> al) {
        super();
        this.c = c;
        this.al = al;
    }

    @Override
    public int getCount() {
        return this.al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater lif = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert lif != null;
        convertView = lif.inflate(R.layout.add_todo_list, null);
        TextView titleTV = convertView.findViewById(R.id.titleID);
        TextView dateTV = convertView.findViewById(R.id.dateID);
        TextView timeTV = convertView.findViewById(R.id.timeID);
        final ImageView delIV = convertView.findViewById(R.id.delID);
        delIV.setTag(position);


        delIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = (int) v.getTag();
                delete(position);
            }
        });

        Datas datas = al.get(position);
        titleTV.setText(datas.findTitle());
        dateTV.setText(datas.findDate());
        timeTV.setText(datas.findTime());
        return convertView;
    }

    // deleting task
    private void delete(int position) {
        deleteFromDb(al.get(position).findId());
        al.remove(position);
        notifyDataSetChanged();
    }

    // deleting task from db
    private void deleteFromDb(int ID) {
        DBhelp dBhelp = new DBhelp(c);
        dBhelp.delData(ID);
        toastMsg("Task Deleted");
    }

    private void toastMsg(String message) {
        Toast toast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}