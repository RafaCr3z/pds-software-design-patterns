package estairways;

import java.util.HashMap;

public class Aeroporto {

    private String codigoAeroporto;
    private String nomeAeroporto;
    private long taxaAeroportuaria;
    private long tavaAlteracoes;

    //Construtor
    public Aeroporto(String codigoAeroporto,String nomeAeroporto, long taxaAeroportuaria, long tavaAlteracoes) {
        this.codigoAeroporto = codigoAeroporto;
        this.nomeAeroporto = nomeAeroporto;
        this.taxaAeroportuaria = taxaAeroportuaria;
        this.tavaAlteracoes = tavaAlteracoes;
    }

    public String getNomeAeroporto() {
        return nomeAeroporto;
    }

    public void setNomeAeroporto(String nomeAeroporto) {
        this.nomeAeroporto = nomeAeroporto;
    }

    public String getCodigoAeroporto() {
        return codigoAeroporto;
    }

    public void setCodigoAeroporto(String codigoAeroporto) {
        this.codigoAeroporto = codigoAeroporto;
    }

    public long getTaxaAeroportuaria() {
        return taxaAeroportuaria;
    }

    public void setTaxaAeroportuaria(long taxaAeroportuaria) {
        this.taxaAeroportuaria = taxaAeroportuaria;
    }

    public long getTavaAlteracoes() {
        return tavaAlteracoes;
    }

    public void setTavaAlteracoes(long tavaAlteracoes) {
        this.tavaAlteracoes = tavaAlteracoes;
    }
    
}
