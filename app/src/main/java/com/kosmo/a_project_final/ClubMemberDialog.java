package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ClubMemberDialog extends Dialog{
    private Context context;

    public ClubMemberDialog(Context context){
        super(context);
        this.context = context;
    }

    public void callFunction( String m_pic,
                              String m_name,
                              String m_birth,
                              String m_phone,
                              String m_position,
                              String m_sex,
                              String m_foot,
                              String m_date,
                              String m_abil){
        Dialog dig = new Dialog(context);

        //다이얼로그 레이아웃 설정
        dig.setContentView(R.layout.activity_club_member_dialog);



        final ImageView m_picImg=(ImageView)dig.findViewById(R.id.m_picImg);
        final TextView m_nameTxt=(TextView)dig.findViewById(R.id.m_nameTxt);
        final TextView m_dateTxt=(TextView)dig.findViewById(R.id.m_dateTxt);
        final TextView m_birthTxt=(TextView)dig.findViewById(R.id.m_birthTxt);
        final TextView m_sexTxt=(TextView)dig.findViewById(R.id.m_sexTxt);
        final TextView m_phoneTxt=(TextView)dig.findViewById(R.id.m_phoneTxt);
        final TextView m_positionTxt=(TextView)dig.findViewById(R.id.m_positionTxt);
        final TextView m_footTxt=(TextView)dig.findViewById(R.id.m_footTxt);
        final TextView m_abilTxt=(TextView)dig.findViewById(R.id.m_abilTxt);

        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+m_pic;
        Picasso.get().load(img).into(m_picImg);
        m_nameTxt.setText(m_name);
        m_dateTxt.setText(m_date);
        m_birthTxt.setText(m_birth);
        m_sexTxt.setText(m_sex);
        m_phoneTxt.setText(m_phone);
        m_positionTxt.setText(m_position);
        m_footTxt.setText(m_foot);
        m_abilTxt.setText(m_abil);

        dig.show();
    }
}
