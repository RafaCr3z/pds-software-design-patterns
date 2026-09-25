package faroest.cliente;

import java.awt.Graphics2D;
import java.awt.Point;

import prof.jogos2D.image.ComponenteHotPoint;

/** Classe que fornece uma implementação base para os estados 
 */
public abstract class StatusDefault implements StatusCliente {
	private Cliente cliente;        // qual o cliente 
	private StatusCliente proximo;  // qual o próximo estado
	private String sufixoImagem;    // qual a imagem (identificação do sufixo) a usar neste estado
	
	/** construtor do estado
	 * @param sufixoImg  o sufixo da imagem a usar neste estado
	 * @param proxStatus o estado a seguir a este
	 */
	public StatusDefault( String sufixoImg, StatusCliente proxStatus) {
		this.proximo = proxStatus;
		sufixoImagem = sufixoImg;
	}

	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
	@Override
	public void ativar( Cliente c ) {
		cliente = c;
		if( sufixoImagem != null )
			cliente.setImagem( cliente.getNome() + sufixoImagem );
	}
	
	@Override
	public void atualizar( ) {		
	}
	
	@Override
	public void portaAberta() {
	}
	
	@Override
	public int fecharPorta() {
		return 0;
	}
	
	@Override
	public boolean podeFechar() {
		return false;
	}
	
	@Override
	public int baleado() {
		return 0;
	}
	
	@Override
	public void desenhar(Graphics2D g) {
	}
	
	/** muda para o próximo estado
	 */
	protected void nextStatus( ) {
		cliente.setStatusAtual(proximo);
	}
	
	/** retorna o cliente associado ao estado
	 * @return o cliente associado ao estado
	 */
	protected Cliente getCliente( ) {
		return cliente;
	}
	
	@Override
	public StatusCliente getProxStatus() {
		return proximo;
	}
	
	@Override
	public StatusCliente clone() {
		try {
			StatusDefault v = (StatusDefault) super.clone();
			if( proximo != null )
				v.proximo = proximo.clone();
			return v;
		} catch (CloneNotSupportedException e) {
			return null;
		}	
	}
	
	/** desenha o "tiro" na porta
	 * @param posicao onde desenhar o tiro
	 */
	protected void mostrarTiro( Point posicao ) {
		getCliente().getPorta().setTiro("tiro", posicao );
	}
	
	/** atingiu um dos extras do cliente
	 * @return a pontuação obtida por atingir o extra
	 */
	protected int tiroNoExtra() {
		ComponenteHotPoint cp = (ComponenteHotPoint) getCliente().getExtra( getCliente().getnExtras()-1);
		mostrarTiro( cp.getHotPoint() );
		getCliente().reduzExtra();			
		return getCliente().getPontos();
	}
	
	/** atingiu o cliente
	 */
	protected void tiroNoCliente() {
		ComponenteHotPoint cp = (ComponenteHotPoint) getCliente().getImagem();
		mostrarTiro( cp.getHotPoint() );
	}
}
