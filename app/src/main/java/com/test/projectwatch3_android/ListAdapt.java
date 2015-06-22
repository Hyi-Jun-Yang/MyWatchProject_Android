package com.test.projectwatch3_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by stu on 2015-03-10.
 */
public class ListAdapt extends BaseAdapter{

    private Context c;
    private ArrayList<String> list;

    LayoutInflater inflater;
    Bitmap[] imgArray;
    ListAdapt(Context c,ArrayList<String> list,Bitmap[] imgArray){
        this.c=c;
        this.list=list;
        this.imgArray=imgArray;
        inflater =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return imgArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imgArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

           convertView =inflater.inflate(R.layout.list_row,null);
           TextView con_name = (TextView)convertView.findViewById(R.id.con_name);
           TextView con_writer = (TextView)convertView.findViewById(R.id.con_writer);
           TextView scount = (TextView)convertView.findViewById(R.id.scount);
           TextView con_regdate = (TextView)convertView.findViewById(R.id.con_regdate);
           final ImageView i= (ImageView)convertView.findViewById(R.id.imageView);


            String[] result = list.get(position).split(":");
            con_name.setText(result[0]);
            con_writer.setText(result[1]);
            scount.setText(result[2]);
            Date date = new Date(Long.parseLong(result[3]));

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today =  df.format(date);

            con_regdate.setText(today);
            i.setImageBitmap(imgArray[position]);



        return convertView;
    }
}
