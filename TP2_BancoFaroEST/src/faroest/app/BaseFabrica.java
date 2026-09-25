package faroest.app;


public class BaseFabrica implements VersaoFabrica {
    @Override
    public VersaoInfo createVersao() {
        return new VersaoInfo("Sede", "config/highscores.bfe", "art/scores.png");
    }
}