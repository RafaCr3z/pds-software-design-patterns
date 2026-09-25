package faroest.cliente;

/** Representa um estado que faz o cliente perder uma vida
 */
public class StatusTerminal extends StatusDefault {

	private String imgFim; // imagem para usar para indicar a perda de vida 
	
	/** Cria um estado terminal
	 * @param sufixoImg  imagem do cliente (sufixo apenas) a usar neste estado
	 * @param imgFim imagem que indica o motivo da perda de vida
	 * @param proxStatus o estado seguinte
	 */
	public StatusTerminal( String sufixoImg, String imgFim, StatusCliente proxStatus) {
		super( sufixoImg, proxStatus);
		this.imgFim = imgFim;  
	}
	
	@Override
	public void ativar(Cliente v) {
		super.ativar(v);
		v.fezAsneira( imgFim );
	}
}
