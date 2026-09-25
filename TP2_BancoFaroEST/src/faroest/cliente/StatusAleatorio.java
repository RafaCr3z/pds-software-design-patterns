package faroest.cliente;

import faroest.util.GeradorAleatorio;

/** Representa um estado aleatório. Neste estado é selecionado
 * aleatóriamente o estado a seguir entre dois estados possíveis
 * (por exemplo, num estado rebenta noutro deposita). 
 * Neste estado, o cliente pode ter 3 situaçõs possíveis:
 * se passar o tempo sem atingir o ponto de escolha, segue
 * para o próximo estado. Se for atingido escolhe o caminho
 * dado pela parte aleatória. (Exemplo, se deixar passar o tempo
 * sem remover os extras do cliente, segue o caminho normal
 * mas se remover todos os extras e disparar novamente,
 * segue o caminho aleatório)
 */
public class StatusAleatorio extends StatusTemporal {

	private boolean escolha;           // qual a escolha aleatória           
	private String sufixoImgTrue;	   // a imagem se escolha true
	private String sufixoImgFalse;     // a aimgem se escolha false
	private StatusCliente statusTrue;  // o esatdo a seguir se escolha true 
	private StatusCliente statusFalse; // o estado a seguir se escolha false
	
	/** Cria um estado Aleatorio
	 * @param sufixoImgTrue  sufixo da imagem, caso o aleatório seja true
	 * @param sufixoImgFalse sufixo da imagem, caso o aleatório seja false
	 * @param proximoStatus estado seguinte, caso não se escolha nenhum dos aleatórios
	 * @param minTempo mínimo de tempo de espera
	 * @param maxTempo máximo tempo de espera
	 * @param statusTrue  estado seguinte se o aleatório for true e for baleado
	 * @param statusFalse estado seguinte se o aleatório for false e for baleado
	 */
	public StatusAleatorio(String sufixoImgTrue, String sufixoImgFalse, StatusCliente proximoStatus, int minTempo, int maxTempo,
			StatusCliente statusTrue, StatusCliente statusFalse) {
		super(sufixoImgTrue, proximoStatus, minTempo, maxTempo, statusTrue);
		this.sufixoImgTrue = sufixoImgTrue;
		this.sufixoImgFalse = sufixoImgFalse;
		this.statusTrue = statusTrue;
		this.statusFalse = statusFalse;
	}

	@Override
	public void ativar(Cliente v) {
		super.ativar(v);
		escolha = GeradorAleatorio.getRandom().nextBoolean();
		if( escolha )
			getCliente().setImagem( getCliente().getNome() + sufixoImgTrue );
		else
			getCliente().setImagem( getCliente().getNome() + sufixoImgFalse );
	}

	@Override
	public int baleado() {
		if( getCliente().temExtras() ){
			return tiroNoExtra();
		}
		
		tiroNoCliente();
		getCliente().setStatusAtual( escolha? statusTrue: statusFalse );
		return 0;
	}
	
	@Override
	public StatusCliente clone() {
		StatusAleatorio va = (StatusAleatorio)super.clone();
		va.statusFalse = statusFalse.clone();
		va.statusTrue = statusTrue.clone();
		return va;		
	}

	/** retorna a escolha feita
	 * @return a escolha feita
	 */
	public boolean qualEscolheu() {
		return escolha;
	}
	
	/** retorna o status se escolha for true
	 * @return o status se escolha for true
	 */
	public StatusCliente getStatusTrue() {
		return statusTrue;
	}
	
	/** retorna o status se escolha for false
	 * @return o status se escolha for false
	 */
	public StatusCliente getStatusFalse() {
		return statusFalse;
	}
}
