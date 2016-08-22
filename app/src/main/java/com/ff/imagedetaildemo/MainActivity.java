package com.ff.imagedetaildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ff.imagezoomdrag.ImageDetailActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(R.mipmap.a+"");
        strings.add(R.mipmap.b+"");
        strings.add(R.mipmap.c+"");
        startActivity(ImageDetailActivity.getMyStartIntent(this,strings,0, ImageDetailActivity.local_file_path));
    }
}
