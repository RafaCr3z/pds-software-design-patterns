package faroest.app;

import faroest.mundo.Mundo;
import prof.jogos2D.util.SKeyboard;

import java.awt.event.KeyEvent;

public class RotationControlStrategy implements GameControlStrategy {
    @Override
    public void execute(Mundo mundo, SKeyboard teclado, GameState gameState) {
        if (teclado.estaPremida(KeyEvent.VK_RIGHT)) {
            mundo.rodarDir();
        } else if (teclado.estaPremida(KeyEvent.VK_LEFT)) {
            mundo.rodarEsq();
        }
    }
}