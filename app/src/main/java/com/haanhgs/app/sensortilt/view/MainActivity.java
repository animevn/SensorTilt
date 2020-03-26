package com.haanhgs.app.sensortilt.view;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.haanhgs.app.sensortilt.R;
import com.haanhgs.app.sensortilt.viewmodel.ViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvAzimuth)
    TextView tvAzimuth;
    @BindView(R.id.tvPitch)
    TextView tvPitch;
    @BindView(R.id.tvRoll)
    TextView tvRoll;
    @BindView(R.id.ivTop)
    ImageView ivTop;
    @BindView(R.id.ivBottom)
    ImageView ivBottom;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;

    private ViewModel viewModel;

    private void setFullScreen() {
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void updateImageView(Float pitch, Float roll){
        ivTop.setAlpha(0f);
        ivBottom.setAlpha(0f);
        ivLeft.setAlpha(0f);
        ivRight.setAlpha(0f);

        if (pitch != null && roll != null){
            if (Math.abs(pitch) < 0.1f) pitch = 0f;
            if (Math.abs(roll) < 0.1f) roll = 0f;

            if (pitch > 0) ivTop.setAlpha(pitch/(float)(Math.PI/2));
            if (pitch < 0) ivBottom.setAlpha(Math.abs(pitch)/(float)(Math.PI/2));
            if (roll > 0) ivRight.setAlpha(roll/(float)(Math.PI/2));
            if (roll < 0) ivLeft.setAlpha(Math.abs(roll)/(float)(Math.PI/2));
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        viewModel.getSensors().observe(this, sensors -> {
            tvAzimuth.setText(getResources().getString(R.string.value, sensors.getAzimuth()));
            tvPitch.setText(getResources().getString(R.string.value, sensors.getPitch()));
            tvRoll.setText(getResources().getString(R.string.value, sensors.getRoll()));
            updateImageView(sensors.getPitch(), sensors.getRoll());
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setFullScreen();
        initViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.stop();
    }
}
