package br.com.quantati.barbearia.model;

/**
 * Created by carlos on 29/05/17.
 */

public enum Procedimento {

    BA("Barba"),
    CA("Cabelo"),
    BI("Bigode"),
    TO("Todos");

    private String procedimento;

    Procedimento(String procedimento){
        this.procedimento = procedimento;
    }

    @Override
    public String toString() {
        return procedimento;
    }
}
