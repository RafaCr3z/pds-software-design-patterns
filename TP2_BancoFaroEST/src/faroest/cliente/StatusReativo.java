package faroest.cliente;

import faroest.app.BancoFaroEst;
import faroest.util.ReguladorVelocidade;

/** Estado que mede a reação do jogador desde que o estado começou
 * até que o cliente seja baleado
 */
public class StatusReativo extends StatusTemporal {

	private long tempoSaque; // quando deve comar a contar o tempo
	
	/** Cria um estado reativo
	 * @param sufixoImg  imagem do cliente (sufixo apenas) a usar neste estado
	 * @param proximoStatus o próximo estado, se não for baleado
	 * @param minTempo tempo mínimo até termianr o esatdo
	 * @param maxTempo tempo máximo até terminar o estado
	 * @param baleado estado para onde mudar se for baleado 
	 */
	public StatusReativo(String sufixoImg, StatusCliente proximoStatus, int minTempo, int maxTempo, StatusCliente baleado ) {
		super( sufixoImg, proximoStatus, minTempo, maxTempo, baleado );
	}
	
	@Override
	public void ativar(Cliente v) {
		super.ativar(v);
		ReguladorVelocidade reguladorVelocidade = ReguladorVelocidade.getInstance();
		tempoSaque = reguladorVelocidade.getTempoRelativo();
	}

	@Override
	public int baleado() {
		if( getCliente().temExtras() ){
			return tiroNoExtra();
		}
		
		long tempo = (ReguladorVelocidade.getInstance().getTempoRelativo() - tempoSaque);
		int pontos = getCliente().getPontos();
		float pontuacao;
		if( tempo < 100 )
			pontuacao = pontos;
		else if( tempo < 200 )
			pontuacao = pontos * 0.95f;
		else if( tempo < 400 )
			pontuacao = pontos * 0.8f;
		else if( tempo < 600 )
			pontuacao = pontos * 0.7f;
		else if( tempo < 800 )
			pontuacao = pontos * 0.6f;
		else if( tempo < 1200 )
			pontuacao = pontos * 0.5f;
		else if( tempo < 1500 )
			pontuacao = pontos * 0.4f;
		else
			pontuacao = pontos * 0.2f;
		
		super.baleado();
		// retorna a pontuação arredondada
		return (int)(pontuacao+0.5f);
	}
}
