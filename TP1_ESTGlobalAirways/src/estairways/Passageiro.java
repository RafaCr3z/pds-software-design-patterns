package estairways;

public class Passageiro {
    
    private String nomePassageiro;
    private String lugarPassageiro;
    private int numMalasPorao;

    //Construtor
    public Passageiro(String nomePassageiro, String lugarPassageiro, int numMalasPorao) {
        this.nomePassageiro = nomePassageiro;
        this.lugarPassageiro = lugarPassageiro;
        this.numMalasPorao = numMalasPorao;
    }

    //Definir o nome do passageiro
    public Passageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
        this.lugarPassageiro = "Não definido";
        this.numMalasPorao = 0;
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    public String getLugarPassageiro() {
        return lugarPassageiro;
    }

    public void setLugarPassageiro(String lugarPassageiro) {
        this.lugarPassageiro = lugarPassageiro;
    }

    public int getNumMalasPorao() {
        return numMalasPorao;
    }

    public void setNumMalasPorao(int numMalasPorao) {
        this.numMalasPorao = numMalasPorao;
    }
}
