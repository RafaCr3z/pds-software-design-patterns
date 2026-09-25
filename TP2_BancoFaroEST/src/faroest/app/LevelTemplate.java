package faroest.app;

import faroest.mundo.Mundo;

import java.io.IOException;

public abstract class LevelTemplate {

    protected Mundo mundo;

    public void playLevel(int nivel, int pack) throws IOException {
        loadLevel(nivel, pack);
        setupLevel();
        startLevel();
    }

    protected abstract void loadLevel(int nivel, int pack) throws IOException;

    protected void setupLevel() {
        mundo.desbloquearPortasVisiveis();
    }

    protected void startLevel() {
        // Level start logic
    }
}
