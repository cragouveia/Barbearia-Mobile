package br.com.quantati.barbearia.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by carlos on 29/05/17.
 */

public class Agendamento implements Serializable{

    private long id;
    private String fotoAntes;
    private String fotoDepois;
    private String nome;
    private String telefone;
    private Calendar dataHora;
    private Procedimento procedimento;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFotoAntes() {
        return fotoAntes;
    }

    public void setFotoAntes(String fotoAntes) {
        this.fotoAntes = fotoAntes;
    }

    public String getFotoDepois() {
        return fotoDepois;
    }

    public void setFotoDepois(String fotoDepois) {
        this.fotoDepois = fotoDepois;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

}
