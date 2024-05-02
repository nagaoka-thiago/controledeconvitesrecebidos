package com.example.controledeconvitesrecebidos;


import androidx.room.TypeConverter;

public class HomensMulheresConversor {
    @TypeConverter
    public int fromHomensMulheres(HomensMulheres valor) {
        return valor.ordinal();
    }
    @TypeConverter
    public HomensMulheres toHomensMulheres(int valor) {
        return HomensMulheres.values()[valor];
    }
}
