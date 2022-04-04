package com.hk.image_local_store;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoRepo {
    public PhotoDao photoDao;
    LiveData<List<Photo>> photos;

    public PhotoRepo(Application application) {
        PhotoDatabase db = PhotoDatabase.getDatabase(application);
        photoDao = db.photoDao();
        photos = photoDao.getAllPhoto();
    }

    LiveData<List<Photo>> getAllPhoto() {
        return photos;
    }

    public void insertPhoto(final Photo photo) {
        PhotoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDao.insertPhoto(photo);
            }
        });
    }
    public void deletePhoto(final Photo photo) {
        PhotoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDao.deletePhoto(photo);
            }
        });
    }
}
