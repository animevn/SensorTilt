package com.haanhgs.app.sensortilt.view;

import android.os.Bundle;
import android.view.WindowManager;
import com.haanhgs.app.sensortilt.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private void setFullScreen(){
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    private void updateImageView(){
//        if (Math.abs(pitch) < 0.1f) pitch = 0f;
//        if (Math.abs(roll) < 0.1f) roll = 0f;
//
//        ivTop.setAlpha(0f);
//        ivBottom.setAlpha(0f);
//        ivLeft.setAlpha(0f);
//        ivRight.setAlpha(0f);
//
//        if (pitch > 0) ivTop.setAlpha(pitch/(float)(Math.PI/2));
//        if (pitch < 0) ivBottom.setAlpha(Math.abs(pitch)/(float)(Math.PI/2));
//        if (roll > 0) ivRight.setAlpha(roll/(float)(Math.PI/2));
//        if (roll < 0) ivLeft.setAlpha(Math.abs(roll)/(float)(Math.PI/2));
//    }

}
