package faroest.mundo;

import faroest.app.PortaState;
import faroest.app.PortaStateType;
import faroest.cliente.Cliente;
import faroest.util.GeradorAleatorio;
import faroest.util.ReguladorVelocidade;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ComponenteVisualLoader;

import java.awt.*;

/**
 * Representa uma porta no jogo.
 */
public class Porta {

    private int minFechada;          // mínimo de tempo que está fechada
    private int maxFechada;          // máximo de tempo que está fechada
    private boolean recebeu = false; // indica se já recebeu dinheiro
    private Cliente cliente = null;  // quem é o cliente que está na porta
    private long proxAbertura;       // tempo programado para a próxima abertura
    private ComponenteMultiAnimado img;  // imagem da porta
    private ComponenteVisual tiro;       // imagem do efeito do tiro
    private Rectangle soleira;       // representa a soleira da porta de modo
    // a centrar as imagens dos clientes
    private Mundo mundo;             // mundo a que a porta está associada


    // Software Design Pattern: State
    private PortaState state;

    /**
     * Construtor da porta
     *
     * @param banco      banco associado
     * @param img        imagem com as várias animações da porta
     * @param soleira    retângulo que contém as dimensões da soleira da porta
     * @param minFechada mínimo de tempo entre aberturas da porta
     * @param maxFechada máximo de tempo entre aberturas da porta
     */
    public Porta(Mundo banco, ComponenteMultiAnimado img, Rectangle soleira, int minFechada, int maxFechada) {
        this.mundo = banco;
        this.img = img;
        img.setPosicao(new Point());
        this.minFechada = minFechada;
        this.maxFechada = maxFechada;
        this.soleira = soleira;
        programarAbertura();

        // Software Design Pattern: State
        state = new FechadaState();
    }

    public void setState(PortaState state) {
        this.state = state;
    }

    public int disparo() {
        return state.disparo(this);
    }

    /**
     * atualiza este elemento
     *
     * @return a pontuação obtida neste ciclo
     */
    public int atualizar() {
        // inicializa pontuação a 0
        int pts = 0;

        if (estaBloqueada())
            return pts;

        return state.atualizar(this);
    }

    /**
     * define onde colocar um tiro na porta
     *
     * @param imgId imagem do tiro
     * @param pos   posição onde colocar o centro da imagem do tiro
     */
    public void setTiro(String imgId, Point pos) {
        tiro = ComponenteVisualLoader.getCompVisual(imgId);
        tiro.setPosicaoCentro(pos);
    }

    /**
     * desenha a porta no ambiente gráfico especificado
     *
     * @param g ambiente gráfico onde desenhar
     */
    public void desenhar(Graphics2D g) {
        // se tiver um cliente, desenhá-lo também
        if (cliente != null)
            cliente.desenhar(g);
        // desenhar a imagem da porta
        img.desenhar(g);

        // se houver imagem de tiro desenhar
        if (tiro != null) {
            tiro.desenhar(g);
            if (tiro.numCiclosFeitos() > 0)
                tiro = null;
        }
    }

    /**
     * define a posiçao da porta
     *
     * @param p posição
     */
    public void setPosicao(Point p) {
        img.setPosicao(p);
        //se tem cliente é preciso também alterar a posição deste
        if (cliente != null) {
            Point pv = (Point) img.getPosicao().clone();
            pv.translate(soleira.x, soleira.y);
            cliente.setPosicao(pv);
        }
    }

    /**
     * retorna o cliente na porta
     *
     * @return o cliente na porta
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * indica se a porta está aberta
     *
     * @return true se a porta está aberta
     */
    public boolean estaAberta() {
        return state.getTipo() != PortaStateType.FECHADA && state.getTipo() != PortaStateType.BLOQUEADA;
    }

    /**
     * indica se a porta está bloqueada
     *
     * @return true se a porta está bloqueada
     */
    public boolean estaBloqueada() {
        return state.getTipo() == PortaStateType.BLOQUEADA;
    }


    /**
     * bloqueia/desbloqueia a porta
     *
     * @param b estado do bloqueio da porta
     */
    public void setBloqueada(boolean b) {

        if (b) {
            state = new BloqueadaState();
        } else {
            state = new FechadaState();
        }
        img.setAnim(PortaStateType.FECHADA.ordinal());
        img.setFrameNum(0);
        cliente = null;
        programarAbertura();
    }

    /**
     * Atribui um cliente à porta
     *
     * @param c novo cliente
     */
    public void setCliente(Cliente c) {
        this.cliente = c;
        if (c == null)
            return;

        //centrar o cliente na soleira da porta
        Point pv = (Point) img.getPosicao().clone();
        pv.translate(soleira.x + (soleira.width - c.getImagem().getComprimento()) / 2, soleira.y + soleira.height - c.getImagem().getAltura());
        cliente.setPosicao(pv);

        // indicar ao cliente em que porta está
        cliente.setPorta(this);
    }

    /**
     * indica em que posição está a porta
     *
     * @return a posição da porta
     */
    public Point getPosicao() {
        return img.getPosicao();
    }

    /**
     * indica se a porta já recebeu dinheiro
     *
     * @return true se a porta já recebeu dinheiro
     */
    public boolean jaRecebeu() {
        return recebeu;
    }

    /**
     * define se a prta já recebeu dinheiro
     *
     * @param b true se já recebeu, false caso contrário
     */
    public void setRecebeu(boolean b) {
        recebeu = b;
    }

    /**
     * devolve o mundo associado à porta
     *
     * @return o mundo associado à porta
     */
    public Mundo getMundo() {
        return mundo;
    }

    /**
     * calcula quando vai ser a próxima abertura
     */
    private void programarAbertura() {
    	ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();
        proxAbertura = regVelocidade.getTempoRelativo() + GeradorAleatorio.nextInt(minFechada, maxFechada);
    }

    /**
     * devolve o tempo programado para a próxima abertura
     *
     * @return o tempo programado para a próxima abertura
     */
    public long getProxAbertura() {
        return proxAbertura;
    }

    /**
     * devolve a imagem da porta
     *
     * @return a imagem da porta
     */
    public ComponenteMultiAnimado getImg() {
        return img;
    }

    // Software Design Pattern: State

    private class FechadaState implements PortaState {

        @Override
        public PortaStateType getTipo() {
            return PortaStateType.FECHADA;
        }

        @Override
        public int atualizar(Porta porta) {
        	ReguladorVelocidade regVelocidade = ReguladorVelocidade.getInstance();
            if ( regVelocidade.getTempoRelativo() > porta.getProxAbertura()) {

                // Obtém o próximo estado
                PortaState newState = new AbrindoState();

                porta.setState(newState);
                porta.getImg().setAnim(newState.getTipo().ordinal());
                porta.getImg().setFrameNum(0);
                porta.getMundo().portaAbrindo(porta);
            }
            return 0;
        }

        @Override
        public int disparo(Porta porta) {
            return 0;
        }
    }

    private class AbrindoState implements PortaState {

        @Override
        public PortaStateType getTipo() {
            return PortaStateType.ABRINDO;
        }

        @Override
        public int atualizar(Porta porta) {
            if (porta.getImg().numCiclosFeitos() > 0) {

                // Obtém o próximo estado
                PortaState newState = new AbertaState(); // Instanciar quando se muda de estado

                porta.setState(newState);
                porta.getImg().setAnim(newState.getTipo().ordinal());
                porta.getImg().setFrameNum(0);
                porta.getCliente().portaAberta();
            }
            return 0;
        }

        @Override
        public int disparo(Porta porta) {
            return 0;
        }
    }

    private class AbertaState implements PortaState {

        @Override
        public PortaStateType getTipo() {
            return PortaStateType.ABERTA;
        }

        @Override
        public int atualizar(Porta porta) {
            int pts = 0;
            porta.getCliente().atualizar();
            if (porta.getCliente().podeFechar()) {

                // Obtém o próximo estado
                PortaState newState = new FechandoState();

                porta.setState(newState);
                porta.getImg().setAnim(newState.getTipo().ordinal());
                porta.getImg().setFrameNum(0);
                pts += porta.getCliente().fecharPorta();
            }
            return pts;
        }

        @Override
        public int disparo(Porta porta) {
            if (porta.getCliente() != null) {
                return porta.getCliente().baleado();
            }
            return 0;
        }
    }

    private class FechandoState implements PortaState {

        @Override
        public PortaStateType getTipo() {
            return PortaStateType.FECHANDO;
        }

        @Override
        public int atualizar(Porta porta) {
            if (porta.getImg().numCiclosFeitos() > 0) {

                // Obtém o próximo estado
                PortaState newState = new FechadaState();

                porta.setState(newState);
                porta.getImg().setAnim(newState.getTipo().ordinal());
                porta.getImg().setFrameNum(0);
                porta.setCliente(null);
                porta.programarAbertura();
            }
            return 0;
        }

        @Override
        public int disparo(Porta porta) {
            return 0;
        }
    }

    private class BloqueadaState implements PortaState {

        @Override
        public PortaStateType getTipo() {
            return PortaStateType.BLOQUEADA;
        }

        @Override
        public int atualizar(Porta porta) {
            return 0;
        }

        @Override
        public int disparo(Porta porta) {
            return 0;
        }
    }

}
