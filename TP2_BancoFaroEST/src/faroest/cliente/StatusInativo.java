package faroest.cliente;

/** Estado em que o cliente nada faz e que não tem estado seguinte,
 * permite que a porta feche e, normalmente, é o último estado
 * antes da porta fechar
 */
public class StatusInativo extends StatusDefault {
	
	/** Cria um StatusInativo
	 */
	public StatusInativo() {
		super(null, null);
	}
	
	@Override
	public boolean podeFechar() {
		return true;
	}
}
