package faroest.app;

import faroest.mundo.Porta;

import java.awt.*;

public interface PortaState {
    PortaStateType getTipo();
    int atualizar(Porta porta);
    int disparo(Porta porta);
}