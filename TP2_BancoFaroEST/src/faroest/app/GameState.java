package faroest.app;

public class GameState {

    private int nivel;             // número do nível em que se está a jogar
    private int vidas;             // quantas vidas faltam
    private int pontuacao;         // a pontuação atual
    private boolean terminouRound; // se acabou este nível

    private boolean estaDisparar; // se está a disparar

    public GameState(int nivel, int vidas) {
        this.nivel = nivel;
        this.vidas = vidas;
        this.pontuacao = 0;
        this.terminouRound = false;
        this.estaDisparar = false;

    }

    public int getNivel() {
        return nivel;
    }

    public void addNivel() {
        nivel++;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void addPontuacao(int pontos) {
        pontuacao += pontos;
    }

    public int getVidas() {
        return vidas;
    }

    public void decrementVidas() {
        vidas--;
    }

    public boolean isTerminouRound() {
        return terminouRound;
    }

    public void setTerminouRound(boolean terminouRound) {
        this.terminouRound = terminouRound;
    }

    public boolean isEstaDisparar() {
        return estaDisparar;
    }

    public void setEstaDisparar(boolean estaDisparar) {
        this.estaDisparar = estaDisparar;
    }
}
