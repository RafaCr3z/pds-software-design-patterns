package faroest.app;

import faroest.cliente.StatusCliente;
import org.graphstream.graph.Graph;

import java.awt.*;

public interface StatusRenderer {
    String getName();
    void render(Graphics g, StatusCliente cliente);
    void renderGraph(Graph graph, StatusCliente status, String nomeNo);

}
