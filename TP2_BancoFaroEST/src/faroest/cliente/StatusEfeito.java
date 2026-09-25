package faroest.cliente;

/** Estado em que se acrescenta um efeito especial à porta,
 * normalmente o dinheiro do depositar
 */
public class StatusEfeito extends StatusTransitorio {
	
	private String fxImg;
	
	/** Cria um efeito
	 * 
	 * @param sufixoImg nome da imagem do cliente (sufixo apenas) a usar neste estado
	 * @param saidaImg  nome da imagem a usar para o efeito
	 * @param proximoStatus o próximo estado
	 */
	public StatusEfeito( String sufixoImg, String saidaImg, StatusCliente proximoStatus) {
		super( sufixoImg, proximoStatus );
		this.fxImg = saidaImg;
	}
	
	@Override
	public void ativar( Cliente v ) {
		super.ativar( v );
		getCliente().setImagemSaida( fxImg );
	}
}
