package com.hk.image_local_store;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Photo.class}, version = 1, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {
    public abstract PhotoDao photoDao();

    private static volatile PhotoDatabase INSTANCE;   //access variable from any thread respect to any change
    private static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    public static PhotoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PhotoDatabase.class, "photo_database")
                            .build();


                }
            }
        }
        return INSTANCE;
    }
}
