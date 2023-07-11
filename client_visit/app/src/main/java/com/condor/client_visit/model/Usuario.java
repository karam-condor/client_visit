package com.condor.client_visit.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("USUARIO")
    private String usaurio;
    @SerializedName("SENHA")
    private String senha;
    @SerializedName("TOKEN")
    private String token;

    @SerializedName("CODUSUR")
    private String codusur;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Usuario(String usaurio, String senha) {
        this.usaurio = usaurio;
        this.senha = senha;
    }


    public String getUsaurio() {
        return usaurio;
    }

    public void setUsaurio(String usaurio) {
        this.usaurio = usaurio;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCodusur() {
        return codusur;
    }

    public void setCodusur(String codusur) {
        this.codusur = codusur;
    }
}