package faroest.app;

import faroest.cliente.*;
import faroest.mundo.Mundo;
import faroest.mundo.Porta;
import faroest.util.ReguladorVelocidade;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.DefaultView;
import org.graphstream.ui.swing_viewer.SwingViewer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Janela de Status do jogo. Permite que o jogador/testador
 * veja várias informações sobre os clientes além de poder
 * mudar a velocidade do jogo
 */
public class JanelaStatus {

    private JDialog dialog;    // janela onde aparece o status do jogo
    private BancoFaroEst game; // o jogo

    // as fontes de texto a usar
    private static final Font fontStatus = new Font("Arial", Font.BOLD, 20);
    private static final Font fontInfo = new Font("Arial", Font.PLAIN, 16);

    // Lista de renderers
    private Map<Class<? extends StatusCliente>, StatusRenderer> renderers = new HashMap<>();

    /**
     * Cria uma janela de testes para o jogo respetivo
     *
     * @param owner o jogo que vai ser analisado
     */
    public JanelaStatus(BancoFaroEst owner) {
        game = owner;
        dialog = setupDialog(owner, "BancoFaroEST - app de teste");
        dialog.setVisible(true);

        // Adicionar renderers (instanciar todos)
        renderers.put(StatusAleatorioRoubar.class, new StatusAleatorioRoubarRenderer());
        renderers.put(StatusAleatorio.class, new StatusAleatorioRenderer());
        renderers.put(StatusDepositar.class, new StatusDepositarRenderer());
        renderers.put(StatusEfeito.class, new StatusEfeitoRenderer());
        renderers.put(StatusInativo.class, new StatusInativoRenderer());
        renderers.put(StatusReativo.class, new StatusReativoRenderer());
        renderers.put(StatusRoubar.class, new StatusRoubarRenderer());
        renderers.put(StatusTemporal.class, new StatusTemporalRenderer());
        renderers.put(StatusTerminal.class, new StatusTerminalRenderer());
        renderers.put(StatusTransitorio.class, new StatusTransitorioRenderer());
        renderers.put(StatusTrocando.class, new StatusTrocandoRenderer());
    }

    /**
     * redesenha a janela de testes
     */
    public void redesenhar() {
        dialog.repaint();
    }

    /**
     * começa um nível novo
     *
     * @param m o mundo do novo nível
     */
    public void comecarNivel(Mundo m) {
        dialog.repaint();
    }

    /**
     * desenha o painel de estados (há sempre 3 paineis)
     *
     * @param g         onde desenhar
     * @param numPainel qual o painel a ser desenhado
     */
    private void desenharPainelEstado(Graphics g, int numPainel) {
        if (game.getMundo() == null)
            return;
        Porta p = game.getMundo().getPortasVisiveis()[numPainel];
        Cliente c = p.getCliente();
        if (c == null) {
            g.setFont(fontStatus);
            g.drawString("Sem cliente", 10, 20);
        } else {
            StatusCliente stat = c.getStatusAtual();

            // Obter o renderer do estado
            StatusRenderer renderer = renderers.get(stat.getClass());

            if (renderer != null) {
                renderer.render(g, stat);
            }
        }
    }

    private String getNomeStatus(StatusCliente stat) {
        return renderers.get(stat.getClass()).getName();
    }

    // Software Design Pattern: Strategy
    private class StatusAleatorioRoubarRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Aleatório Roubar";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusAleatorioRoubar sar = (StatusAleatorioRoubar) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Aleatório Roubar");

            // Desenhar o próximo estado
            desenharProximoEstado(g, sar.qualEscolheu() ? "Depositar" : "Rebentar");

            // Desenhar o tempo que falta para mudar de estado
            desenharTempoMudancaEstado(g, sar.getFimTempo());
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusAleatorioRoubar sar = (StatusAleatorioRoubar) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Aleatório Roubar");

            String saidaTrue = criarNo(sar.getStatusTrue());
            String saidaFalse = criarNo(sar.getStatusFalse());
            String saidaT = criarNo(sar.getProxTrue());
            String saidaF = criarNo(sar.getProxFalse());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saidaTrue, nomeNo, saidaTrue, true).setAttribute("ui.label", "Disparo (true)");
            graph.addEdge(nomeNo + "-" + saidaFalse, nomeNo, saidaFalse, true).setAttribute("ui.label", "Disparo (false)");
            graph.addEdge(nomeNo + "-" + saidaT, nomeNo, saidaT, true).setAttribute("ui.label", "fim tempo (true)");
            graph.addEdge(nomeNo + "-" + saidaF, nomeNo, saidaF, true).setAttribute("ui.label", "fim tempo (false)");
        }
    }

    private class StatusAleatorioRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Aleatório";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusAleatorio sa = (StatusAleatorio) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Aleatório");

            // Desenhar o próximo estado
            desenharProximoEstado(g, sa.qualEscolheu() ? "Depositar" : "Rebentar");

            // Desenhar o tempo que falta para mudar de estado
            desenharTempoMudancaEstado(g, sa.getFimTempo());
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusAleatorio sa = (StatusAleatorio) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Aleatório");

            String saidaTrue = criarNo(sa.getStatusTrue());
            String saidaFalse = criarNo(sa.getStatusFalse());
            String saida = criarNo(sa.getProxStatus());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saidaTrue, nomeNo, saidaTrue, true).setAttribute("ui.label", "Disparo (true)");
            graph.addEdge(nomeNo + "-" + saidaFalse, nomeNo, saidaFalse, true).setAttribute("ui.label", "Disparo (false)");
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim tempo");
        }
    }

    private class StatusDepositarRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Depositar";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusDepositar sd = (StatusDepositar) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Depositar");

            // Desenhar o próximo estado
            desenharProximoEstado(g, "Dinheiro em caixa");
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusDepositar sd = (StatusDepositar) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Depositar");
        }
    }

    private class StatusEfeitoRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Efeito";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusEfeito se = (StatusEfeito) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Efeito");

            // Desenhar o próximo estado
            desenharProximoEstado(g, getNomeStatus(se.getProxStatus()));
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusEfeito se = (StatusEfeito) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Efeito");

            String saida = criarNo(se.getProxStatus());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim animação");
        }
    }

    private class StatusInativoRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Inativo";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            // StatusInativo si = (StatusInativo) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Inativo");

            // Desenhar o próximo estado
            desenharProximoEstado(g, "Fechar a porta");
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusInativo si = (StatusInativo) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Inativo");
        }
    }

    private class StatusReativoRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Reativo";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusReativo sr = (StatusReativo) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Reativo");

            // Desenhar o próximo estado
            desenharProximoEstado(g, getNomeStatus(sr.getProxStatus()));

            // Desenhar o tempo que falta para mudar de estado
            desenharTempoMudancaEstado(g, sr.getFimTempo());
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusReativo sr = (StatusReativo) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Reativo");

            String saida = criarNo(sr.getProxStatus());
            String baleado = criarNo(sr.getStatusBaleado());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim tempo");
            graph.addEdge(nomeNo + "-" + baleado, nomeNo, baleado, true).setAttribute("ui.label", "se baleado");
        }
    }

    private class StatusRoubarRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Roubar";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            // StatusRoubar sr = (StatusRoubar) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Roubar");

            // Desenhar o próximo estado
            desenharProximoEstado(g, "ROUBADO!!!");
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusRoubar sr = (StatusRoubar) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Roubar");
        }
    }

    private class StatusTemporalRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Temporal";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusTemporal st = (StatusTemporal) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Temporal");

            // Desenhar o próximo estado
            desenharProximoEstado(g, getNomeStatus(st.getProxStatus()));

            // Desenhar o tempo que falta para mudar de estado
            desenharTempoMudancaEstado(g, st.getFimTempo());
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusTemporal st = (StatusTemporal) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Temporal");

            String saida = criarNo(st.getProxStatus());
            String baleado = criarNo(st.getStatusBaleado());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim tempo");
            graph.addEdge(nomeNo + "-" + baleado, nomeNo, baleado, true).setAttribute("ui.label", "se baleado");
        }
    }

    private class StatusTerminalRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Terminal";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            // StatusTerminal st = (StatusTerminal) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Terminal");

            // Desenhar o próximo estado
            desenharProximoEstado(g, "Já foste");
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusTerminal st = (StatusTerminal) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Terminal");

            String saida = criarNo(st.getProxStatus());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim animação final");
        }
    }

    private class StatusTransitorioRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Transitório";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusTransitorio st = (StatusTransitorio) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Transitório");

            // Desenhar o próximo estado
            desenharProximoEstado(g, getNomeStatus(st.getProxStatus()));
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusTransitorio st = (StatusTransitorio) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Transitório");

            String saida = criarNo(st.getProxStatus());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim animação");
        }
    }

    private class StatusTrocandoRenderer implements StatusRenderer {

        @Override
        public String getName() {
            return "Trocando";
        }

        @Override
        public void render(Graphics g, StatusCliente sc) {

            // Obter o estado atual do cliente
            StatusTrocando st = (StatusTrocando) sc;

            // Desenhar o nome do estado
            desenharNomeEstado(g, "Trocando");

            // Desenhar o próximo estado
            desenharProximoEstado(g, getNomeStatus(st.getProxStatus()));
        }

        @Override
        public void renderGraph(Graph graph, StatusCliente sc, String nomeNo) {

            // Obter o estado atual do cliente
            StatusTrocando st = (StatusTrocando) sc;

            // Adicionar estados
            graph.addNode(nomeNo).setAttribute("ui.label", "Trocando");

            String saida = criarNo(st.getProxStatus());

            // Adicionar transições
            graph.addEdge(nomeNo + "-" + saida, nomeNo, saida, true).setAttribute("ui.label", "fim animação troca");
        }
    }

    /**
     * desenha o nome do estado no painel
     *
     * @param g    onde desenhar
     * @param nome o nome a escrever
     */
    private void desenharNomeEstado(Graphics g, String nome) {
        g.setFont(fontStatus);
        g.drawString(nome, 10, 20);
    }

    /**
     * desenha o próximo estado no painel
     *
     * @param g    onde desenhar
     * @param prox o próximo estado a escrever
     */
    private void desenharProximoEstado(Graphics g, String prox) {
        g.setFont(fontInfo);
        g.drawString("Próximo: " + prox, 10, 40);
    }

    /**
     * desenha o tempo que falta para mudar de estado
     *
     * @param g        onde desenhar
     * @param fimTempo o tempo em que muda de estado
     */
    private void desenharTempoMudancaEstado(Graphics g, long fimTempo) {

        // Obter regulador de velocidade
        ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();

        // Calcular o tempo que falta para mudar de estado
        long falta = fimTempo - regVelocidade.getTempoRelativo();

        g.drawString("Mudar em: " + falta, 10, 60);
    }


    /**
     * configura os botões de velocidade
     *
     * @return o painel com os botões de velocidade
     */
    private JPanel setupBotoesVelocidade() {

        // Obter regulador de velocidade
        ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();

        JPanel panelBt = new JPanel(new GridLayout(0, 1));
        panelBt.setBorder(new TitledBorder("Velocidade"));
        ButtonGroup grp = new ButtonGroup();
        JToggleButton normalBt = new JToggleButton("100%", true);
        normalBt.addActionListener(e -> regVelocidade.setVelocidadePercentagem(100));
        grp.add(normalBt);
        JToggleButton metadeBt = new JToggleButton(" 50%", false);
        metadeBt.addActionListener(e -> regVelocidade.setVelocidadePercentagem(50));
        grp.add(metadeBt);
        JToggleButton quartoBt = new JToggleButton(" 25%", false);
        quartoBt.addActionListener(e -> regVelocidade.setVelocidadePercentagem(25));
        grp.add(quartoBt);
        JToggleButton stopBt = new JToggleButton("Stop", false);
        stopBt.addActionListener(e -> regVelocidade.setVelocidadePercentagem(0));
        grp.add(stopBt);

        panelBt.add(normalBt);
        panelBt.add(metadeBt);
        panelBt.add(quartoBt);
        panelBt.add(stopBt);
        return panelBt;
    }

    /**
     * método chamado quando o utilizador pressiona o botão de ver estados
     *
     * @param numPainel qual o painel associado ao botão
     */
    private void verMapaEstados(int numPainel) {
        Porta p = game.getMundo().getPortasVisiveis()[numPainel];
        Cliente c = p.getCliente();
        if (c == null)
            return;

        // Obter regulador de velocidade
        ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();

        long oldSpeed = regVelocidade.getIntervaloEntreAtualizacoes();
        regVelocidade.setIntervaloEntreAtualizacoes(0);

        // preparar e mostrar o grafo de estados
        Graph graph = prepararGrafo();
        construirGrafo(graph, c.getStatusAtual());
        mostrarGraph(graph);

        regVelocidade.setIntervaloEntreAtualizacoes(oldSpeed);
    }

    // variáveis que dizem respeito à criação do grafo
    private char letra;
    private Graph graph;
    private String nomeNo;

    /**
     * constroi o grafo com os estados do cliente
     *
     * @param graph o grafo que vai ter os estados
     * @param c     o estado atual do cliente
     */
    public void construirGrafo(Graph graph, StatusCliente c) {
        letra = 'A';  // o primeiro nó é o A  (isto pode ter uma limitação de 26 estados no máximo, o que para este caso deve ser suficiente)
        this.graph = graph;
        String no = criarNo(c);
        // este nó tem atributos especiais porque é o primeiro e início do grafo
        Node n = graph.getNode(no);
        n.setAttribute("ui.style", "fill-color: rgb(0,200,200);");
    }

    private String criarNo(StatusCliente sc) {

        String esteNo = (letra++) + "";
        nomeNo = esteNo;

        if (sc == null) {
            return esteNo;
        }

        // Obter o renderer do estado
        StatusRenderer renderer = renderers.get(sc.getClass());

        if (renderer != null) {
            renderer.renderGraph(graph, sc, nomeNo);
        }

        return esteNo;
    }

    /**
     * cria e prepara o grafo que irá conter os vários estados do cliente
     *
     * @return o grafo corretamento criado
     */
    private Graph prepararGrafo() {
        Graph graph = new SingleGraph("State Machine");
        // Configurar estilo
        System.setProperty("org.graphstream.ui", "swing");

        String styleSheet =
                "node { shape: circle; size: 100px, 50px;" +
                        "text-size: 25px; text-color: black;" +
                        "fill-mode: plain; fill-color:white;" +
                        "stroke-mode: plain; stroke-color: black;" +
                        "}" +
                        "edge {" +
                        "   text-size: 20px;" +  // Tamanho da fonte dos rótulos das arestas
                        "   text-color: red;" +
                        "}";
        graph.removeAttribute("ui.stylesheet");

        graph.setAttribute("ui.stylesheet", "graph { fill-color: red; }");
        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        return graph;
    }

    /**
     * desenha o grafo numa janela dedicada ao efeito
     *
     * @param graph o grafo a ser desenhado
     */
    private void mostrarGraph(Graph graph) {
        // criar um visualizador de grafos para mostrar o nosso grafo
        SwingViewer viewer = new SwingViewer(graph, SwingViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewer.addDefaultView(false);
        DefaultView view = (DefaultView) viewer.getDefaultView();
        view.setPreferredSize(new Dimension(800, 600));
        JOptionPane.showMessageDialog(dialog, view);
    }

    /**
     * configura o painel que terá os painéis de cada porta
     *
     * @return o painel que terá os painéis de cada porta
     */
    private JPanel setupPainelPortas() {
        JPanel panel = new JPanel(new GridLayout(1, 0));
        panel.add(new PainelEstado(0));
        panel.add(new PainelEstado(1));
        panel.add(new PainelEstado(2));
        return panel;
    }

    /**
     * classe privada que representa um painel onde será
     * apresentada info sobre uma porta
     */
    @SuppressWarnings("serial")
    private class PainelEstado extends JPanel {
        private int numPainel; // o número do painel

        public PainelEstado(int numPainel) {
            setLayout(new BorderLayout());
            this.numPainel = numPainel;
            JButton mapBt = new JButton("Ver mapa");
            add(mapBt, BorderLayout.SOUTH);
            mapBt.addActionListener(e -> verMapaEstados(numPainel));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharPainelEstado(g, numPainel);
        }
    }

    /**
     * configura a janela de status
     *
     * @param owner  janela principal do jogo
     * @param titulo titulo da janela de satus
     * @return a janela configurada
     */
    private JDialog setupDialog(JFrame owner, String titulo) {
        Rectangle r = owner.getBounds();
        JDialog diag = new JDialog(owner, titulo);
        diag.setBounds(r.x, r.y + r.height, r.width, 150);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelBt = setupBotoesVelocidade();
        JPanel panelPortas = setupPainelPortas();
        panel.add(panelBt, BorderLayout.WEST);
        panel.add(panelPortas, BorderLayout.CENTER);
        diag.setContentPane(panel);
        return diag;
    }


}
