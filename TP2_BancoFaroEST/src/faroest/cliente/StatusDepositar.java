package faroest.cliente;

/** Estado que representa um depósitar no banco
 */
public class StatusDepositar extends StatusDefault {
	
	/** Cria um StatusDepositar
	 * @param sufixoImg imagem a usar neste estado (só sufixo)
	 */
	public StatusDepositar(String sufixoImg) {
		super(null, null);
	}
	
	@Override
	public boolean podeFechar() {
		return true;
	}
	
	@Override
	public int fecharPorta() {
		getCliente().getPorta().setRecebeu( true );
		return getCliente().getPontos();
	}
}
