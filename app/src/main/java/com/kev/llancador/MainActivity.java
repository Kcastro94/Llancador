package com.kev.llancador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static com.kev.llancador.R.id.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    Button[] btn;
    ArrayList<Integer> btnId = new ArrayList<Integer>();

    class BtnInfo {
        int idx;
        String text;
        String url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnId.add(0, R.id.btnNaveg);
        btnId.add(1, R.id.btnCam);
        btnId.add(2, R.id.btnTlf);

        setContentView(R.layout.activity_main);
        btn = new Button[btnId.size()];
        for(int i = 0; i <btnId.size(); i++) {
            btn[i] = (Button) findViewById(btnId.get(i));
            btn[i].setOnClickListener(this);
            btn[i].setOnLongClickListener(this);
        }
    }

    @Override public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String undef = getResources().getString(R.string.undefined);
        for (int i = 0; i < btn.length; i++) {
            String prefText = String.format(Locale.getDefault(), "text%02d", i + 1);
            String text = prefs.getString(prefText, undef);
            if (text.isEmpty()) text = undef;
            String prefUrl = String.format(Locale.getDefault(), "url%02d", i + 1);
            String url = prefs.getString(prefUrl, "");
            BtnInfo info = new BtnInfo();
            info.idx = i + 1;
            info.text = text;
            info.url = url;
            btn[i].setText(text);
            btn[i].setTag(info);
        }
    }

    @Override
    public void onClick(View arg0) {
        BtnInfo info = (BtnInfo) arg0.getTag();
        String url = info.url;
        if (!url.isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }

    @Override
    public boolean onLongClick(View arg0) {
        BtnInfo info = (BtnInfo) arg0.getTag();
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra("idx", info.idx);
        startActivity(i);
        return true;
    }
}
