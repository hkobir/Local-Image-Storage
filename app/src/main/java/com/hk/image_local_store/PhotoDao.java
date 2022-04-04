package com.hk.image_local_store;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("select * From photo")
    LiveData<List<Photo>> getAllPhoto();

    @Insert
    void insertPhoto(Photo photo);

    @Delete
    void deletePhoto(Photo photo);
}
