package faroest.app;

public class LadroesFabrica implements VersaoFabrica {
    @Override
    public VersaoInfo createVersao() {
        return new LadroesVersaoInfo("Filial da Gamapolis", "config/highscores_ladroes.bfe", "art/scores_ladroes.png");
    }
}
