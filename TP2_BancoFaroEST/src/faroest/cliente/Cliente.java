package faroest.cliente;

import java.awt.*;

import faroest.mundo.Porta;
import prof.jogos2D.image.*;
import prof.jogos2D.util.ComponenteVisualLoader;

/**
 * Esta classe define o comportamento de um cliente no jogo,
 */
public class Cliente implements Cloneable {
	/** imagem representativa do cliente, num dado momento */
	private ComponenteVisual img;   
	private String nome;   // o nome do cliente (usado nas imagens)
	private int pontos;    // quanto pontos vale
	
	private StatusCliente statusAtual;  // o estado atual (usa a pattern STATE)
	
	private ComponenteVisual extraSai;  // imagem de um extra a sair
	private ComponenteAnimado imgSaida; // imagem de saida do visitante 
	// A imagem de saída é um "efeito especial",
	// que pode ser o dinheiro ou outra (num futuro)
	
	private ComponenteVisual extras[]; // imagens dos extras
	private int nExtras;               // número de extras que ainda tem
	
	private Porta porta;  // a porta onde está       
	
	/** Cria um cliente
	 * @param nome nome (a usar nas imagens)
	 * @param pontos quantos pontos vale
	 * @param nExtras o número de extras que tem
	 * @param statusInicial o estado inicial do cliente
	 */
	public Cliente( String nome, int pontos, int nExtras, StatusCliente statusInicial ){
		this.nome = nome;
		this.pontos = pontos;
		this.nExtras = nExtras;
		extras = new ComponenteVisual[ this.nExtras ];
		for( int i = 0; i < this.nExtras; i++){
			extras[i] = ComponenteVisualLoader.getCompVisual( nome + "_extra" + i );
		}
		statusAtual = statusInicial;
		statusInicial.ativar( this );
	}

	/** atualiza o cliente
	 */
	public void atualizar() {
		statusAtual.atualizar( );
	}
	
	/** método chamado quando a porta onde está é aberta
	 */
	public void portaAberta() {
		statusAtual.portaAberta();
	}

	/** indica se a porta onde está pode fechar
	 * @return true se a porta onde está pode fechar, false caso contrário
	 */
	public boolean podeFechar() {
		return statusAtual.podeFechar();
	}

	/** método chamado quando a porta onde está fecha
	 * @return a pontuação obtida no fecho da porta
	 */
	public int fecharPorta() {
		return statusAtual.fecharPorta();
	}

	/** método chamado quando o cliente é baleado
	 * @return a pontuação obtida pelo tiro
	 */
	public int baleado() {
		return statusAtual.baleado();
	}


	/** desenha no ambiente gráfico
	 * @param g o ambiente gráfico onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		img.desenhar(g);
		if( imgSaida != null )
			imgSaida.desenhar( g );
		for( int i = 0; i < nExtras; i++){
			extras[i].desenhar( g );
		}
		if( extraSai != null ){
			extraSai.desenhar( g );
			if( extraSai.numCiclosFeitos() > 0 )
				extraSai = null;
		}
		statusAtual.desenhar(g );
	}
	
	/** devolve o nome do cliente
	 * @return o nome do cliente
	 */
	public String getNome() {
		return nome;
	}
	
	/** altera o nome do cliente
	 * @param nome novo nome 
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/** retorna o status atual
	 * @return  o status atual
	 */
	public StatusCliente getStatusAtual( ) {
		return statusAtual;
	}
	
	/** muda o status do visitante
	 * @param status o novo status
	 */
	public void setStatusAtual(StatusCliente statusAtual) {
		this.statusAtual = statusAtual;
		statusAtual.ativar( this );
	}
	
	/** retorna o número de extras que ainda possui
	 * @return o número de extras que ainda possui
	 */
	public int getnExtras() {
		return nExtras;
	}
	
	/** remove um extra
	 */
	public void reduzExtra(){
		nExtras--;
		extraSai = ComponenteVisualLoader.getCompVisual( getNome() + "_extra"+nExtras+"_sai"); 
		extraSai.setPosicao( (Point)getImagem().getPosicao().clone() );
	}

	/** Indica se ainda tem extras
	 * @return true, se ainda tem extras
	 */
	public boolean temExtras(){
		return nExtras > 0;
	}
	
	/** devolve o extra que o cliente tem 
	 * @param idx índice do extra a retornar
	 * @return retorna o extra com o índice idx
	 */
	public ComponenteVisual getExtra( int idx ) {
		return extras[idx];
	}
	
	/** define qual a imagem de saída. A imagem de saída é um "efeito especial",
	 * que pode ser o dinheiro ou outra (num futuro) 
	 * @param nomeImg o nome da imagem de saida
	 */
	public void setImagemSaida( String nomeImg ){
		imgSaida = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nomeImg );
		Rectangle r = getImagem().getBounds();
		imgSaida.setPosicao( new Point( r.x+(r.width - imgSaida.getComprimento())/2 , r.y) );
	}
	
	/** retorna a imagem de saída
	 * @return  a imagem de saída
	 */
	protected ComponenteAnimado getImagemSaida(){
		return imgSaida;
	}

	/** Alguem fez asneira (matou o cliente) ou deixou o cliente fazer asneira
	 * @param nomeImg imagem do tipo de asneira
	 */
	public void fezAsneira( String nomeImg ){
		getPorta().getMundo().perdeNivel( nomeImg );	
	}

	/** define a porta onde o cliente está
	 * @param p a porta onde o cliente aparece
	 */
	public void setPorta(Porta p) {
		porta = p;
	}
	
	/** retorna a porta onde o cliente está
	 * @return a porta onde o cliente está
	 */
	public Porta getPorta() {
		return porta;
	}
	
	/** retorna a pontuação associada ao cliente
	 * @return a pontuação associada ao cliente
	 */
	public int getPontos() {
		return pontos;
	}

	/** retorna a imagem do cliente
	 * @return a imagem do cliente
	 */
	public ComponenteVisual getImagem() {
		return img;
	}

	/** define a imagem do cliente
	 * @param nome identificador da imagem a usar
	 */
	public void setImagem(String nome) {
		Point p = img != null? img.getPosicao() : null;
		img = ComponenteVisualLoader.getCompVisual( nome );
		img.setPosicao( p );
	}

	/** define a posição do cliente
	 * @param posicao a nova posição
	 */
	public void setPosicao(Point posicao) {
		img.setPosicao( (Point)posicao.clone() );
		if( imgSaida != null )
			imgSaida.setPosicao( (Point)posicao.clone() );
		for( int i = 0; i < nExtras; i++){
			extras[i].setPosicao( (Point)posicao.clone() );
		}
	}
	
	/** retorna um clone do cliente
	 */
	public Cliente clone() {
		try {
			Cliente v = (Cliente) super.clone(); // uso da PROTOTYPE nos estados
			if( img != null )
				v.img = img.clone();
			v.extras = new ComponenteVisual[ extras.length ];
			for( int i=0; i < extras.length; i++ ){
				v.extras[i] = extras[i].clone();
			}
			v.statusAtual = statusAtual.clone();
			v.statusAtual.ativar(v);
			return v;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
