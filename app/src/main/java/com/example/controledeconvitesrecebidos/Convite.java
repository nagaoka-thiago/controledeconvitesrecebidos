package com.example.controledeconvitesrecebidos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;

@Entity
public class Convite implements Serializable {
    private static final long serialVersionUID = 7100179587555243994L;
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imgConvite;

    private String titulo;
    private String local;
    private String data;
    private float preco;

    @TypeConverters(HomensMulheresConversor.class)
    private HomensMulheres homensMulheres;
    private boolean bebidaLiberada;
    private String estado;

    public Convite(byte[] imgConvite, String titulo, String local, String data, float preco, HomensMulheres homensMulheres, boolean bebidaLiberada, String estado) {
        this.imgConvite = imgConvite;
        this.titulo = titulo;
        this.local = local;
        this.data = data;
        this.preco = preco;
        this.homensMulheres = homensMulheres;
        this.bebidaLiberada = bebidaLiberada;
        this.estado = estado;
    }

    public long getId() { return id; }

    public byte[] getImgConvite() {
        return imgConvite;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getLocal() {
        return local;
    }

    public String getData() {
        return data;
    }

    public float getPreco() {
        return preco;
    }

    public HomensMulheres getHomensMulheres() {
        return homensMulheres;
    }

    public boolean isBebidaLiberada() {
        return bebidaLiberada;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(long id) { this.id = id; }

    public void setImgConvite(byte[] imgConvite) {
        this.imgConvite =  imgConvite;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setHomensMulheres(HomensMulheres homensMulheres) {
        this.homensMulheres = homensMulheres;
    }

    public void setIsBebidaLiberada(boolean isBebidaLiberada) {
        this.bebidaLiberada = isBebidaLiberada;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
