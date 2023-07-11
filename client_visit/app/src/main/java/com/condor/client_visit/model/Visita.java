package com.condor.client_visit.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Visita {
    @SerializedName("ID")
    private String id;

    @SerializedName("CODUSUR")
    private String codusur;
    @SerializedName("CNPJ")
    private String cnpj;
    @SerializedName("CODCLI")
    private String codcli;
    @SerializedName("EMAIL")
    private String email;
    @SerializedName("TELEFONE")
    private String telefone;
    @SerializedName("REPRESENTANTE")
    private String representante;
    @SerializedName("OBS")
    private String obs;
    @SerializedName("LATITUDE")
    private float latitude;
    @SerializedName("LONGITUDE")
    private float longitude;
    public Visita() {

    }
    @SerializedName("FOTOS")
    private ArrayList<Photo> fotos;

    public Visita(String codusur, String codcli, String cnpj) {
        this.codusur = codusur;
        this.cnpj = cnpj;
        this.codcli = codcli;
    }

    public Visita(String codusur, String cnpj, String codcli, float latitude, float longitude) {
        this.codusur = codusur;
        this.cnpj = cnpj;
        this.codcli = codcli;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public Visita(String id, String codusur, String email, String telefone, String representante, String obs) {
        this.id = id;
        this.codusur = codusur;
        this.email = email;
        this.telefone = telefone;
        this.representante = representante;
        this.obs = obs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodusur() {
        return codusur;
    }

    public void setCodusur(String codusur) {
        this.codusur = codusur;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Photo> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<Photo> fotos) {
        this.fotos = fotos;
    }
}
