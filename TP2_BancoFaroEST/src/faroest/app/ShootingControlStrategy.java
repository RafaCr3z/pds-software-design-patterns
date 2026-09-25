package faroest.app;

import faroest.mundo.Mundo;
import prof.jogos2D.util.SKeyboard;

import java.awt.event.KeyEvent;

public class ShootingControlStrategy implements GameControlStrategy {

    @Override
    public void execute(Mundo mundo, SKeyboard teclado, GameState gameState) {
        if( teclado.estaPremida( KeyEvent.VK_1 ) && !gameState.isEstaDisparar() ){
            gameState.addPontuacao(mundo.getPortasVisiveis()[0].disparo());
            gameState.setEstaDisparar(true);
        }

        if( teclado.estaPremida( KeyEvent.VK_2 ) && !gameState.isEstaDisparar() ){
            gameState.addPontuacao(mundo.getPortasVisiveis()[1].disparo());
            gameState.setEstaDisparar(true);
        }

        if( teclado.estaPremida( KeyEvent.VK_3 ) && !gameState.isEstaDisparar() ){
            gameState.addPontuacao(mundo.getPortasVisiveis()[2].disparo());
            gameState.setEstaDisparar(true);
        }

        if( !teclado.estaPremida( KeyEvent.VK_1 ) && !teclado.estaPremida( KeyEvent.VK_2 ) && !teclado.estaPremida( KeyEvent.VK_3 ) )
            gameState.setEstaDisparar(false);
    }
}