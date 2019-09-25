package com.fct.neec.oficial.ClipRequests.entities;

public class StudenPropinas {

    private String nome;
    private String entidade;
    private String montante;
    private String referencia;
    private String data;
    private String URL;

    public StudenPropinas(String nome){
        this.nome = nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setURL(String url){
        this.URL = url;
    }

    public String getURL() {
        return URL;
    }

    public String getData() {
        return data;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getMontante() {
        return montante;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMontante(String montante) {
        this.montante = montante;
    }

    public String getNome() {
        return nome;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }
}
