package faroest.app;

public class ZombieFabrica implements VersaoFabrica {
    @Override
    public VersaoInfo createVersao() {
        return new ZombieVersaoInfo("Filial da ZombieLand", "config/highscores_zombies.bfe", "art/scores_zombies.png");
    }
}
