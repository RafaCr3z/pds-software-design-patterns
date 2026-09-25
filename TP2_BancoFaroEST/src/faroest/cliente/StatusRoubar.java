package faroest.cliente;

/** Estado em que o cliente rouba o banco, isto é,
 * coloca a porta como não tendo recebido dinheiro
 * Não tem estado seguinte e permite fechar a porta.
 */
public class StatusRoubar extends StatusDefault {
	
	/** cria o estado roubar
	 * @param sufixoImg a imagema usar neste estado
	 */
	public StatusRoubar(String sufixoImg) {
		super(null, null);
	}
	
	@Override
	public boolean podeFechar() {
		return true;
	}
	
	@Override
	public int fecharPorta() {
		getCliente().getPorta().setRecebeu( false );
		return -getCliente().getPontos();
	}
}
