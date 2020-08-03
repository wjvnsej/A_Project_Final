package com.kosmo.a_project_final;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ClubList extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String, Object>> list;
    private int a;

    public ClubList(Context context, int a, List<Map<String, Object>> list) {
        this.context = context;
        this.a = a;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_club_list, null);
        }

        final TextView clubName = (TextView)convertView.findViewById(R.id.club_name);
        final ImageView clubEmb = (ImageView)convertView.findViewById(R.id.club_emb);
        final ListView club_list = (ListView)convertView.findViewById(R.id.club_list);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);
        Log.i(TAG,"BaseAdapter 에서 출력할 값 : "+list.get(position).get("c_name").toString());

        clubName.setText(list.get(position).get("c_name").toString());

        //이미지 보내기
        String img = "http://192.168.219.130:8282/project_final/resources/uploadsFile/"+list.get(position).get("c_emb").toString();
        Picasso.get().load(img).into(clubEmb);

//        club_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(context.getApplicationContext(),ClubView.class);
//
//                int list_postion = position;
//
//                int c_idx = (int)list.get(position).get("c_idx");
//                String c_name = list.get(position).get("c_name").toString();
//                int c_cash = (int)list.get(position).get("c_cash");
//                String c_emb = list.get(position).get("c_emb").toString();
//                String c_area = list.get(position).get("c_area").toString();
//                String c_type = list.get(position).get("c_type").toString();
//                Date c_date = (Date) list.get(position).get("c_date");
//                String c_memo = list.get(position).get("c_memo").toString();
//                String c_color = list.get(position).get("c_color").toString();
//                String c_ability = list.get(position).get("c_ability").toString();
//                String c_gender = list.get(position).get("c_gender").toString();
//                int c_memlimit = (int)list.get(position).get("c_memlimit");
//                String c_age = list.get(position).get("c_age").toString();
//                int start = (int)list.get(position).get("start");
//                int end = (int)list.get(position).get("end");
//
//                intent.putExtra("list_postion",list_postion);
//                intent.putExtra("c_idx",c_idx);
//                intent.putExtra("c_name",c_name);
//                intent.putExtra("c_cash",c_cash);
//                intent.putExtra("c_emb",c_emb);
//                intent.putExtra("c_area",c_area);
//                intent.putExtra("c_type",c_type);
//                intent.putExtra("c_date",c_date);
//                intent.putExtra("c_memo",c_memo);
//                intent.putExtra("c_color",c_color);
//                intent.putExtra("c_ability",c_ability);
//                intent.putExtra("c_gender",c_gender);
//                intent.putExtra("c_memlimit",c_memlimit);
//                intent.putExtra("c_age",c_age);
//                intent.putExtra("start",start);
//                intent.putExtra("end",end);
//
//
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }
}
