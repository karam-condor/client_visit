package com.condor.client_visit.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Photo{
    @SerializedName("ID")
    private String id;

    @SerializedName("ID_VISITA")
    private String id_visita;

    @SerializedName("URL")
    private String url;

    @SerializedName("SEQ")
    private String seq;

    @SerializedName("DESCRICAO")
    private String descricao;

    public Photo(String id_visita, String url, String descricao, String base64, String codusur) {
        this.id_visita = id_visita;
        this.url = url;
        this.descricao = descricao;
        this.base64 = base64;
        this.codusur = codusur;
    }

    @SerializedName("BASE64")
    private String base64;

    @SerializedName("CODUSUR")
    private String codusur;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_visita() {
        return id_visita;
    }

    public void setId_visita(String id_visita) {
        this.id_visita = id_visita;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
