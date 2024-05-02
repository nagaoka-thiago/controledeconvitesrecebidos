package com.example.controledeconvitesrecebidos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ConviteDAO {
    @Insert
    public long inserir(Convite convite);

    @Delete
    public void deletar(Convite convite);

    @Update
    public void atualizar(Convite convite);

    @Query("SELECT * FROM convite WHERE id = :id")
    public Convite pegarPorId(long id);

    @Query("SELECT * FROM convite ORDER BY id")
    public List<Convite> pegarTodos();
}
