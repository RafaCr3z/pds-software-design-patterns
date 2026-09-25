package faroest.cliente;

/** Estado aleatório com 4 estados seguintes possíveis.
 * Se o cliente não tiver sido atingido e a escolha for true
 * segue por um estado, se for false segue por outro.
 * Se for atingido e escolha for true segue para um terceiro estado
 * se for false segue para o quarto estado-
 */
public class StatusAleatorioRoubar extends StatusAleatorio {
	
	private StatusCliente proxTrue;  // estado seguinte se não for atingido e escolha for true
	private StatusCliente proxFalse; // estado seguinte se não for atingido e escolha for false

	/**
	 * 
	 * 	Cria um estado AleatorioReoubar
	 * @param sufixoImgTrue  sufixo da imagem, caso o aleatório seja true
	 * @param sufixoImgFalse sufixo da imagem, caso o aleatório seja false
	 * @param proxTrue  estado seguinte, caso não se escolha nenhum dos aleatórios e seja true
	 * @param proxFalse estado seguinte, caso não se escolha nenhum dos aleatórios e seja flase
	 * @param minTempo mínimo de tempo de espera
	 * @param maxTempo máximo tempo de espera
	 * @param statusTrue  estado seguinte se o aleatório for true e for baleado
	 * @param statusFalse estado seguinte se o aleatório for false e for baleado
	 */
	public StatusAleatorioRoubar(String sufixoImgTrue, String sufixoImgFalse, StatusCliente proxTrue, StatusCliente proxFalse, int minTempo, int maxTempo,
			StatusCliente statusTrue, StatusCliente statusFalse) {
		super(sufixoImgTrue, sufixoImgFalse, proxTrue, minTempo, maxTempo, statusTrue, statusFalse);
		this.proxTrue = proxTrue;
		this.proxFalse = proxFalse;
	}

	@Override
	public void atualizar() {
		if( fimEspera() ) {
			if( qualEscolheu() || getCliente().temExtras() )
				getCliente().setStatusAtual( proxTrue );
			else
				getCliente().setStatusAtual( proxFalse );
		}
	}
	
	/** retorna o estado a seguir se não for atingido e escolha for true
	 * @return o estado a seguir se não for atingido e escolha for true
	 */
	public StatusCliente getProxTrue() {
		return proxTrue;
	}
	
	/** retorna o estado a seguir se não for atingido e escolha for false
	 * @return o estado a seguir se não for atingido e escolha for false
	 */
	public StatusCliente getProxFalse() {
		return proxFalse;
	}
}
