package com.haanhgs.app.sensortilt.viewmodel;

import android.app.Application;
import com.haanhgs.app.sensortilt.model.Repo;
import com.haanhgs.app.sensortilt.model.Sensors;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModel extends AndroidViewModel {

    private final Repo repo;

    public ViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application.getApplicationContext());
    }

    public LiveData<Sensors> getSensors(){
        return repo.getLiveSensors();
    }

    public void start(){
        repo.start();
    }

    public void stop(){
        repo.stop();
    }
}
