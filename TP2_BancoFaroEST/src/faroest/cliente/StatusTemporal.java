package faroest.cliente;

import faroest.app.BancoFaroEst;
import faroest.util.GeradorAleatorio;
import faroest.util.ReguladorVelocidade;

/** Estado que dura um determinado tempo (aleatório). Ao fim desse tempo passa
 * para o estado seguinte. Se for baleado dentro do tempo, segue para outro estado.
 */
public class StatusTemporal extends StatusDefault {
	
	private StatusCliente statusBaleado; // estado a usar se for baleado 
	private int minTempo, maxTempo;      // tempos mínimo e máximo do intervalo ativo deste estado
	private long fimTempo;               // tempo calculado para o fim do estado
	
	/** Cria um estado temporal
	 * @param sufixoImg imagem( sufixo apenas) do cliente a usar neste estado
	 * @param proximoStatus o estado a seguir no final do tempo (se não for baleado)
	 * @param minTempo mínimo de tempo (milisegudos) até terminar este estado
	 * @param maxTempo máximo de tempo (milisegudos) até terminar este estado
	 * @param baleado estado a seguir se for baleado
	 */
	public StatusTemporal(String sufixoImg, StatusCliente proximoStatus, int minTempo, int maxTempo, StatusCliente baleado ) {
		super( sufixoImg, proximoStatus );
		this.minTempo = minTempo;
		this.maxTempo = maxTempo;
		statusBaleado = baleado;
	}
	
	@Override
	public void ativar( Cliente v ) {
		super.ativar( v );
		fimTempo = GeradorAleatorio.proxTempo( minTempo, maxTempo);
	}

	@Override
	public void atualizar( ) {
		if( fimEspera() )
			nextStatus();
	}
	
	@Override
	public int baleado() {
		if( getCliente().temExtras() ){
			return tiroNoExtra();
		}
		
		tiroNoCliente();
		getCliente().setStatusAtual(statusBaleado);
		return 0;
	}
	
	/** retorna o estado que assume, se for baleado
	 * @return o estado que assume, se for baleado
	 */
	public StatusCliente getStatusBaleado() {
		return statusBaleado;
	}
	
	/** retorna o tempo em que vai terminar este estado
	 * @return o tempo em que vai terminar este estado 
	 */
	public long getFimTempo() {
		return fimTempo;
	}

	/** indica se já passou o tempo limite deste estado
	 * @return true, se já passou o tempo limite deste estado
	 */
	protected boolean fimEspera(){
		ReguladorVelocidade reguladorVelocidade = ReguladorVelocidade.getInstance();
		return reguladorVelocidade.getTempoRelativo() >= fimTempo;
	}
	
	@Override
	public StatusCliente clone() {
		StatusTemporal st = (StatusTemporal)super.clone();
		st.statusBaleado = statusBaleado.clone();
		return st;		
	}
}
