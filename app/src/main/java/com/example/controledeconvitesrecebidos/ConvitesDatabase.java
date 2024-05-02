package com.example.controledeconvitesrecebidos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Convite.class}, version = 1, exportSchema = false)
public abstract class ConvitesDatabase extends RoomDatabase {
    public abstract ConviteDAO conviteDAO();

    private static ConvitesDatabase instancia;

    public static ConvitesDatabase getDatabase(final Context context) {
        if(instancia == null) {
            synchronized (ConvitesDatabase.class) {
                if(instancia == null) {
                    instancia = Room.databaseBuilder(context, ConvitesDatabase.class, "convites.db").allowMainThreadQueries().build();
                }
            }
        }

        return instancia;
    }
}
