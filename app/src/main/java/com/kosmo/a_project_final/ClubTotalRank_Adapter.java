package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ClubTotalRank_Adapter extends BaseAdapter {

    String TAG = "iKOSMO";
    private Context context;
    private List<Map<String,Object>> list;
    private int a;

    public ClubTotalRank_Adapter(Context context, List<Map<String, Object>> list, int a) {
        this.context = context;
        this.list = list;
        this.a = a;
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
            convertView = inflater.inflate(R.layout.activity_club_total_rank__adapter, null);
        }

        final TextView rnumTxt = (TextView)convertView.findViewById(R.id.rnumTxt);
        final TextView c_nameTxt = (TextView)convertView.findViewById(R.id.c_nameTxt);
        final TextView c_areaTxt = (TextView)convertView.findViewById(R.id.c_areaTxt);
        final TextView c_matchTxt = (TextView)convertView.findViewById(R.id.c_matchTxt);
        final TextView winRateTxt = (TextView)convertView.findViewById(R.id.winRateTxt);
        final TextView winTxt = (TextView)convertView.findViewById(R.id.winTxt);
        final TextView drawsTxt = (TextView)convertView.findViewById(R.id.drawsTxt);
        final TextView pointsTxt = (TextView)convertView.findViewById(R.id.pointsTxt);

        ImageView c_emb = (ImageView)convertView.findViewById(R.id.c_picImg);
        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+list.get(position).get("c_emb").toString();
        Picasso.get().load(img).into(c_emb);

//        rnumTxt.setText(list.get(position).get("rnum").toString());
//        c_nameTxt.setText(list.get(position).get("c_name").toString());
//        c_matchTxt.setText(list.get(position).get("matches").toString());
//        winRateTxt.setText(list.get(position).get("winRate").toString());
//        winTxt.setText(list.get(position).get("wins").toString());
//        drawsTxt.setText(list.get(position).get("draws").toString());
//        pointsTxt.setText(list.get(position).get("points").toString());


        String rum1 = list.get(position).get("rnum").toString();
        String matches1 = list.get(position).get("matches").toString();
        String winRate1 = list.get(position).get("winRate").toString();
        String wins1 = list.get(position).get("wins").toString();
        String draws1 = list.get(position).get("draws").toString();
        String points1 = list.get(position).get("points").toString();

        String[] rum = rum1.split("\\.");
        String[] matches = matches1.split("\\.");
        String[] winRate = winRate1.split("\\.");
        String[] wins = wins1.split("\\.");
        String[] draws = draws1.split("\\.");
        String[] points = points1.split("\\.");

        rnumTxt.setText(rum[0]);
        c_nameTxt.setText(list.get(position).get("c_name").toString());
        c_areaTxt.setText(list.get(position).get("c_area").toString());
        c_matchTxt.setText(matches[0]);
        winRateTxt.setText(winRate[0]);
        winTxt.setText(wins[0]);
        drawsTxt.setText(draws[0]);
        pointsTxt.setText(points[0]);

        return convertView;
    }
}
