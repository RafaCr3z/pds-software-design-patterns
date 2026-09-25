package faroest.app;

import faroest.mundo.Mundo;
import prof.jogos2D.util.SKeyboard;

public interface GameControlStrategy {
    void execute(Mundo mundo, SKeyboard teclado, GameState gameState);
}
