package faroest.cliente;

import java.awt.Graphics2D;

/** interface que representa o estado de um cliente
 */
public interface StatusCliente extends Cloneable {
	
	/** ativar o estado. efetua ações no início do estado
	 * @param c cliente associado ao estado
	 */
	void ativar(Cliente c);
	
	/** atualiza o estado */
	void atualizar( );
	
	/** chamado quando a porta onde está o cliente é aberta */
	void portaAberta();
	
	/** chamado quando a porta onde está o cliente é fechada
	 * @return a pontuação obtida por fechar a porta 
	 */
	int fecharPorta();
	
	/** indica se a porta onde está o cliente pode ser fechada
	 * @return true se a porta onde está o cliente pode ser fechada
	 */
	boolean podeFechar();
	
	/** chamado quando o cliente é baleado
	 * @return a pontuação obtida pelo tiro
	 */
	int baleado();
	
	/** desenha o cliente na porta
	 * @param g onde desenhar
	 */
	void desenhar(Graphics2D g);
	
	/** qual o próximo estado
	 * @return o próximo estado
	 */
	StatusCliente getProxStatus();

	/** retorna um clone deste estado (usa a prototype)
	 * @return um clone deste estado 
	 */
	StatusCliente clone();
}
