package com.justin.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.justin.customview.touch.Touch1Activity;

public class ScrollViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);

        initView();
    }

    private void initView() {
        findViewById(R.id.scrollview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scrollview:
                startActivity(new Intent(this, Touch1Activity.class));
                break;


            default:
                break;
        }
    }
}