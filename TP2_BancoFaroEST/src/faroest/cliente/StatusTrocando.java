package faroest.cliente;

import java.awt.Graphics2D;

import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.ComponenteVisualLoader;

/** Esatdo em que o cliente é substituido por outro
 */
public class StatusTrocando extends StatusDefault {
	
	private String nomePersonagem;           // nome da nova personagem
	private ComponenteAnimado imgPersonagem; // imagem do personagem que troca
	
	/** Cria um estado de troca
	 * @param sufixoImg imagem do cliente atual durante a troca
	 * @param nomeTroca nome do novo personagem 
	 * @param proximoStatus estado assumido após a troca ter sido efetuada
	 */
	public StatusTrocando(String sufixoImg, String nomeTroca, StatusCliente proximoStatus) {
		super( sufixoImg, proximoStatus );
		this.nomePersonagem = nomeTroca;
	}

	@Override
	public void ativar(Cliente v) {
		super.ativar(v);
		imgPersonagem = (ComponenteAnimado)ComponenteVisualLoader.getCompVisual( nomePersonagem + "_troca" );
		imgPersonagem.setPosicao( getCliente().getImagem().getPosicao() ); 
	}
	
	@Override
	public void desenhar(Graphics2D g) {
		super.desenhar(g);
		imgPersonagem.desenhar(g);
	}
	
	@Override
	public void atualizar( ) {
		if( getCliente().getImagem().numCiclosFeitos() > 0 ) {
			getCliente().setNome(nomePersonagem);
			imgPersonagem = null;
			nextStatus();			
		}
	}
	
	@Override
	public StatusCliente clone() {
		StatusTrocando st = (StatusTrocando)super.clone();
		if( imgPersonagem != null )
			st.imgPersonagem = imgPersonagem.clone();
				
		return st;		
	}
}
