package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class GameMember_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String,Object>> list;
    private int a;

    public GameMember_Adapter(Context context, int a, List<Map<String, Object>> list) {
        this.context = context;
        this.a = a;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

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
            convertView = inflater.inflate(R.layout.activity_game_member__adapter, null);
        }
        final String m_img = list.get(position).get("m_pic").toString();
        final TextView gameMember_name = (TextView)convertView.findViewById(R.id.gameMember_name);
        final TextView gameMember_phone = (TextView)convertView.findViewById(R.id.gameMember_phone);
        final ImageView gameMember_img = (ImageView)convertView.findViewById(R.id.gameMember_img);

        Log.i(TAG,"BaseAdapter 에 들어온 값 : "+list);

        gameMember_name.setText(list.get(position).get("m_name").toString());
        gameMember_phone.setText(list.get(position).get("m_phone").toString());
        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+m_img;
        Picasso.get().load(img).into(gameMember_img);



        return convertView;
    }
}
