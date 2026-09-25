package faroest.app;

import faroest.mundo.Mundo;
import faroest.mundo.Porta;
import faroest.util.ReguladorVelocidade;
import prof.jogos2D.util.SKeyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Esta classe representa o jogo em si.
 */
@SuppressWarnings("serial")
public class BancoFaroEst extends JFrame {

    // constantes a usar no jogo
    private static final int COMPRIMENTO = 999;
    private static final int ALTURA = 600;
    private static final int ULTIMO_NIVEl = 10;

    // janela de splash para apresentar o jogo
    private static JWindow splash;

    // variáveis para os vários elementos visuais do jogo
    private JPanel jContentPane = null;
    private JPanel zonaJogo = null;

    // imagem usada para melhorar as animações
    private Image ecran;

    // As cores e as fontes a usar
    private Color pontColor1 = new Color(0, 0, 100, 255);
    private Color pontColor2 = new Color(50, 50, 200, 255);
    private Font pontFont;
    private Font textFont;
    private Font scoreFont;

    // para processar as teclas
    private SKeyboard teclado;

    // Para lidar com as pontuações máximas
    private ImageIcon scoreImg;
    private HighScoreHandler score;

    // Informações sobre o jogo
    /*
    private int nivel;             // número do nível em que se está a jogar
    private int vidas;             // quantas vidas faltam
    private boolean terminouRound; // se acabou este nível
    private int pontuacao;         // a pontuação atual
    */

    private Mundo mundo;           // as informaçoes sobre o nível atual
    private VersaoInfo versao;     // informações sobre o pack selecionado

    private JanelaStatus janelaStatus; // a janela de status

    // Guarda o estado do jogo num objeto
    private GameState gameState;

    // Software Design Pattern: Singleton
    private final ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();

    /**
     * Método chamado sempre que é necessário atualizar qualquer coisa na aplicação.
     * Atenção! Este método NÃO desenha nada. Usar o método desenharJogo para isso.
     */
    private void atualizarJogo() {

        // Update game state
        gameState.addPontuacao(mundo.atualizar());

        // Initialize the control context and strategies
        GameControlContext controlContext = new GameControlContext();
        controlContext.addStrategy(new RotationControlStrategy());
        controlContext.addStrategy(new ShootingControlStrategy());

        // Execute the strategies
        controlContext.executeStrategies(mundo, teclado, gameState);

        // Check round completion
        gameState.setTerminouRound(passouNivel() || mundo.terminouRound());
        if (mundo.terminouRound()) {
            gameState.decrementVidas();
        }
    }

    private VersaoInfo escolherVersao() {


        // Define the available factories
        VersaoFabrica[] factories = {
                new BaseFabrica(),
                new ZombieFabrica(),
                new LadroesFabrica()
        };

        // Define version names for user selection
        String[] opcoes = {"Sede", "ZombieLand", "Gamapolis"};

        // perguntar ao utilizador que versão quer jogar
        int res = JOptionPane.showOptionDialog(this, "Escolha a versão que deseja jogar", "Escolha a versão que deseja jogar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (res == JOptionPane.CLOSED_OPTION)
            System.exit(0);

        // Use the selected factory to create the version
        return factories[res].createVersao();
    }

    /**
     * Construtor da aplicação
     */
    public BancoFaroEst() {
        super();
        showSplash();
        initialize();
        teclado = new SKeyboard();
        // ler as imagens das animações e efeitos
        try {
            ImagensReader.lerImagens("config/imagens.bfe");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ficheiro niveis/imagens.bfe não encontrado");
            System.exit(1);
        }
    }

    /**
     * Método para arrancar com o jogo
     */
    public void comecar() {
        closeSplash();
        versao = escolherVersao();
        try {
            score = new HighScoreHandler(versao.scoreFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ficheiro " + versao.scoreFile + " não encontrado");
            System.exit(1);
        }
        scoreImg = new ImageIcon(versao.scoreImage);
        janelaStatus = new JanelaStatus(this);

        int nivel = 1;      // alterar este valor se quiserem testar algum nível em particular
        int vidas = 3;      // alterar este valor para testes (nada de batotas!)

        // Inicializar o estado do jogo
        gameState = new GameState(nivel, vidas);
        jogarNivel();
    }

    /**
     * Ler e começar a jogar o nível atual
     */
    private void jogarNivel() {

        int nivel = gameState.getNivel();

        try {
            // mundo = LevelReader.lerNivel(nivel, versao.pack);
            mundo = LevelReader.lerNivel(nivel, versao);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro na leitura do nível " + nivel);
            e.printStackTrace();
            System.exit(0);
        }
        mundo.desbloquearPortasVisiveis();

        // Atualizar o estado do jogo
        gameState.setTerminouRound(false);

        Atualizador actualiza = new Atualizador();
        janelaStatus.comecarNivel(mundo);
        actualiza.start();
    }

    /**
     * Voltar a jogar o mesmo nível
     */
    private void resetNivel() {
        mundo.bloquearPortasVisiveis();

        // Atualizar o estado do jogo
        gameState.setTerminouRound(false);

        mundo.reset();
        mundo.desbloquearPortasVisiveis();
    }

    /**
     * Método chamado quando se pretende redesenhar o jogo
     * QUALQUER DESENHO DEVE SER FEITO AQUI
     *
     * @param g elemento onde se vai desenhar
     */
    private void desenharJogo(Graphics2D g) {

        // Obter as informações do estado do jogo
        int nivel = gameState.getNivel();
        int vidas = gameState.getVidas();
        int pontuacao = gameState.getPontuacao();

        // Passar para graphics2D pois este é mais avançado
        Graphics2D ge = (Graphics2D) ecran.getGraphics();

        AffineTransform tf = ge.getTransform();
        mundo.desenhar(ge);
        ge.setTransform(tf);

        // desenhar as quantidades
        ge.setColor(pontColor1);
        ge.setFont(textFont);
        ge.drawString("Nivel ", 20, 580);
        ge.drawString("Vidas ", 850, 580);

        ge.setColor(pontColor2);
        ge.drawString("" + nivel, 120, 580);
        ge.drawString("" + vidas, 950, 580);

        // fazer a sombra para a pontuação e portas
        ge.setFont(pontFont);
        Porta portas[] = mundo.getPortas();
        ge.setColor(Color.black);
        for (int i = 0; i < portas.length; i++) {
            ge.drawString("" + (i + 1), 232 + 40 * i, 582);
        }
        ge.drawString("" + pontuacao, 622, 582);

        // desenhar a pontuação e info das portas
        for (int i = 0; i < portas.length; i++) {
            ge.setColor(portas[i].jaRecebeu() ? Color.green : Color.RED);
            ge.drawString("" + (i + 1), 230 + 40 * i, 580);
        }

        ge.setColor(Color.YELLOW);
        ge.drawString("" + pontuacao, 620, 580);

        // Agora que está tudo desenhado na imagem auxiliar, desenhar no ecrá
        g.drawImage(ecran, 0, 0, null);

        janelaStatus.redesenhar();
    }

    /**
     * Método chamado quando se termina um nível
     */
    private void terminouNivel() {

        if (perdeuTodasVidas()) {
            verPontuacao();
            String escolhas[] = {"Voltar a jogar", "Terminar Jogo"};
            int resposta = JOptionPane.showOptionDialog(null, "Foi despedido pelo banco! Que deseja fazer?", "DERROTA", JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, escolhas, escolhas[0]);
            switch (resposta) {
                case 0:
                    comecar();
                    break;
                case 1:
                    System.exit(0);
            }
        } else if (passouNivel()) {

            // Atualizar o estado do jogo
            gameState.addPontuacao(mundo.getPontos());

            // Verificar se passou o nível
            int nivel = gameState.getNivel();

            if (nivel < ULTIMO_NIVEl) {
                JOptionPane.showMessageDialog(BancoFaroEst.this, "Nível " + nivel + " completo. Recompensa: " + mundo.getPontos(), "Nível Completo", JOptionPane.PLAIN_MESSAGE);

                // Atualizar o estado do jogo
                gameState.addNivel();

                jogarNivel();

            } else {
                verPontuacao();
                // se ganhou vai mostrar a mensagem de vitória e perguntar o que deseja fazer
                String escolhas[] = {"Voltar a Jogar", "Terminar Jogo"};
                int resposta = JOptionPane.showOptionDialog(null, "Parabéns, completou o Jogo", "Jogo Completo", JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, escolhas, escolhas[0]);
                switch (resposta) {
                    case 0:
                        comecar();
                        break;
                    case 1:
                        System.exit(0);
                }
            }
        }
    }

    /**
     * Verifica se obteve uma pontuação digna de figurar na tabela de records.
     * Pede o nome do jogador, caso atinja esse objetivo.
     */
    private void verPontuacao() {

        // Obter a pontuação do jogador
        int pontuacao = gameState.getPontuacao();

        int pos = score.classificarScore(pontuacao);
        if (pos != -1) {
            String nome = null;
            do {
                try {
                    nome = JOptionPane.showInputDialog((pos + 1) + "º Lugar! Parabéns!\nIntroduza o seu nome.");
                    score.addScore(nome, pontuacao);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Nome tem de ter 20 caracteres no máximo");
                    nome = null;
                }
            } while (nome == null);
        }
        showScores(pos);
        try {
            score.saveScores();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro a gravar pontuações");
            System.exit(1);
        }
    }

    /**
     * Apresenta as pontuações máximas, destacando a pontuação
     * obtida pelo jogador, se for caso disso
     *
     * @param destaqueIdx o índice da pontuação a destacar (0 a 19),
     *                    ou -1 se não houver pontuação a destacar
     */
    private void showScores(int destaqueIdx) {
        JPanel scorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                scoreImg.paintIcon(null, g, 0, 0);
                java.util.List<HighScoreHandler.Score> pontuacoes = score.getScores();
                g.setFont(scoreFont);
                for (int i = 0; i < pontuacoes.size(); i++) {
                    g.setColor(i == destaqueIdx ? Color.RED : Color.BLACK);
                    HighScoreHandler.Score s = pontuacoes.get(i);
                    int y = 145 + i * 28;
                    g.drawString(s.getNome(), 30, y);
                    String pontStr = "" + s.getPontuacao();
                    int comp = g.getFontMetrics().stringWidth(pontStr);
                    g.drawString(pontStr, 570 - comp, y);
                }
            }
        };
        scorePanel.setPreferredSize(new Dimension(scoreImg.getIconWidth(), scoreImg.getIconHeight()));
        JOptionPane.showMessageDialog(this, scorePanel, "HighScores", JOptionPane.PLAIN_MESSAGE, null);
    }

    /**
     * Testa se passou o nível, isto é, se já tem todas as portas
     * com dinheiro recebido e fechadas
     *
     * @return true se passou o nível
     */
    private boolean passouNivel() {
        for (Porta p : mundo.getPortas())
            if (!p.jaRecebeu() || p.estaAberta())
                return false;
        return true;
    }

    /**
     * Testa se perdeu o jogo, isto é, já não tem vidas
     *
     * @return true, se perdeu o jogo
     */
    private boolean perdeuTodasVidas() {
        return gameState.getVidas() <= 0;
    }

    /**
     * devolve o mundo onde se está a jogar
     *
     * @return o mundo onde se está a jogar
     */
    public Mundo getMundo() {
        return mundo;
    }

    /**
     * Classe responsável pela criação da thread que vai atualizar o mundo de x em x tempo
     *
     * @author F. Sergio Barbosa
     */
    class Atualizador extends Thread {
        public void run() {
            long mili = System.currentTimeMillis();
            long target = mili + regVelocidade.getIntervaloEntreAtualizacoes();
            do {
                do {
                    if (regVelocidade.getIntervaloEntreAtualizacoes() == 0)
                        continue;
                    atualizarJogo();
                    zonaJogo.repaint();
                    while (mili < target)
                        mili = System.currentTimeMillis();
                    target = mili + regVelocidade.getIntervaloEntreAtualizacoes();
                } while (!gameState.isTerminouRound());
                resetNivel();
            } while (!passouNivel() && !perdeuTodasVidas());
            terminouNivel();
        }
    }

    ;

    /**
     * Fecha o splash screen do jogo
     */
    private static void closeSplash() {
        splash.setVisible(false);
    }

    /**
     * apresenta o splash screen do jogo
     */
    private static void showSplash() {
        splash = new JWindow();
        ImageIcon icon = new ImageIcon("art/faroest.png");
        splash.getContentPane().add(new JLabel("", icon, SwingConstants.CENTER));
        splash.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
    }

    /**
     * Vai inicializar a aplicação
     */
    private void initialize() {
        // características da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(getJContentPane());
        setTitle("Banco no FaroEST");
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            // criar as fontes para os textos e pontuações e registá-las no sistema
            File fontFile = new File("font/Saddlebag.ttf");
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font novaFonte = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            ge.registerFont(novaFonte);

            // definir que se quer esta nova fonte, mas com o tamanho 44
            pontFont = novaFonte.deriveFont(44.0f);

            fontFile = new File("font/RioGrande.ttf");
            novaFonte = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            ge.registerFont(novaFonte);
            textFont = novaFonte.deriveFont(33.0f);
            // definir que se quer esta nova fonte, mas com o tamanho 35
            scoreFont = novaFonte.deriveFont(35.0f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        // criar a imagem para melhorar as animações e configurá-la para isso mesmo
        ecran = new BufferedImage(COMPRIMENTO, ALTURA, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D ge = (Graphics2D) ecran.getGraphics();
        ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    /**
     * Método auxiliar para configurar a janela
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getZonaJogo(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * Este método inicializa a zonaJogo, AQUI NÃO DEVEM ALTERAR NADA
     */
    private JPanel getZonaJogo() {
        if (zonaJogo == null) {
            zonaJogo = new JPanel() {
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    desenharJogo((Graphics2D) g);
                }
            };
            Dimension d = new Dimension(COMPRIMENTO, ALTURA);
            zonaJogo.setPreferredSize(d);
            zonaJogo.setSize(d);
            zonaJogo.setMinimumSize(d);
            zonaJogo.setBackground(Color.pink);
        }
        return zonaJogo;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BancoFaroEst jogo = new BancoFaroEst();
        jogo.comecar();
        jogo.setVisible(true);
    }

}
