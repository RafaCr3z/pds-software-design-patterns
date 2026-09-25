package faroest.cliente;

/** Estado em que se espera que uma animação seja completada 
 */
public class StatusTransitorio extends StatusDefault {
	
	/** Cria um estado transitório
	 * @param sufixoImg  imagem da animação de transição que o cliente passa (sufixo apenas)
	 * @param proximoStatus o esatdo a assumir no final da animação
	 */
	public StatusTransitorio(String sufixoImg, StatusCliente proximoStatus) {
		super( sufixoImg, proximoStatus );
	}
	
	@Override
	public void atualizar( ) {
		if( getCliente().getImagem().numCiclosFeitos() > 0 )
			nextStatus();			
	}
}
