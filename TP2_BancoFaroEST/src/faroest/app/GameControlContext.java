package faroest.app;

import faroest.mundo.Mundo;
import prof.jogos2D.util.SKeyboard;

import java.util.ArrayList;
import java.util.List;

public class GameControlContext {

    private final List<GameControlStrategy> strategies = new ArrayList<>();

    public void addStrategy(GameControlStrategy strategy) {
        strategies.add(strategy);
    }

    public void executeStrategies(Mundo mundo, SKeyboard teclado, GameState gameState) {
        for (GameControlStrategy strategy : strategies) {
            strategy.execute(mundo, teclado, gameState);
        }
    }

}
