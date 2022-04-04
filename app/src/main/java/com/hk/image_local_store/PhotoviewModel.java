package com.hk.image_local_store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoviewModel extends AndroidViewModel {
    private PhotoRepo photoRepo;
    LiveData<List<Photo>> photos;

    public PhotoviewModel(@NonNull Application application) {
        super(application);
        photoRepo = new PhotoRepo(application);
        photos = photoRepo.getAllPhoto();
    }

    LiveData<List<Photo>> getAllPhoto() {
        return photos;
    }

    public void insertPhoto(final Photo photo) {
        photoRepo.insertPhoto(photo);
    }

    public void deletePhoto(final Photo photo) {
        photoRepo.deletePhoto(photo);
    }
}
