package faroest.util;

/**
 * Classe responsável por contabilizar o tempo de jogo
 * Tem por base o tempo real, mas como o jogo pode ser
 * acelerado/retardado, esta classe faz a correspondência
 * entre tempo real e tempo de jogo
 */
public class ReguladorVelocidade {

    // Software Design Pattern: Singleton

    /**
     * instância única do regulador de velocidade
     */
	private static ReguladorVelocidade instance;

    /**
     * o intervalo standard entre frames, que dá umas 30 frames por segundo.
     * Este é o valor de referência para calcular a correspondência entre relógio real e simulado
     */
    private static long intervaloStandard = 33;

    /**
     * intervalo atual entre atualizações. É inicializado ao intervalo standard
     */
    private volatile long intervaloEntreAtualizacoes = intervaloStandard;

    private long ultimoReal;      // último valor real lido
    private long ultimoRelativo;  // último valor relativo lido

	/**
	 * Retorna a instância única do regulador de velocidade
	 * @return a instância única do regulador de velocidade
	 */
	public static ReguladorVelocidade getInstance() {
		if (instance == null) {
			instance = new ReguladorVelocidade();
		}
		return instance;
	}

    /**
     * Cria o regulador de velocidade (privado, pois é um singleton)
     */
	private ReguladorVelocidade() {
		ultimoReal = System.currentTimeMillis();
		ultimoRelativo = ultimoReal;
	}

    /**
     * Retorna o tempo relativo de jogo, em milisegundos.
     * @return o tempo relativo de jogo, em milisegundos.
     */
    public long getTempoRelativo() {
        if (intervaloEntreAtualizacoes == 0)
            return ultimoRelativo;
        return ultimoRelativo + (System.currentTimeMillis() - ultimoReal) * intervaloStandard / intervaloEntreAtualizacoes;
    }


    /**
     * define a velocidade do jogo, em percentagem da velocidade normal (100 = 100%)
     *
     * @param perc percentagem da velocidade normal
     */
    public void setVelocidadePercentagem(int perc) {
        if (perc < 0)
            throw new IllegalArgumentException();
        ultimoRelativo = getTempoRelativo();
        ultimoReal = System.currentTimeMillis();
        if (perc == 0)
            intervaloEntreAtualizacoes = 0;
        else
            intervaloEntreAtualizacoes = 100 * intervaloStandard / perc;
    }

    /**
     * retorna o intervalo, em milisegundos, entre atualizações do jogo
     *
     * @return o intervalo, em milisegundos, entre atualizações do jogo
     */
    public long getIntervaloEntreAtualizacoes() {
        return intervaloEntreAtualizacoes;
    }

    /**
     * define o intervalo, em milisegundos, entre atualizações do jogo
     *
     * @param sp o novo intervalo
     */
    public void setIntervaloEntreAtualizacoes(long sp) {
        if (sp < 0)
            throw new IllegalArgumentException();
        ultimoRelativo = getTempoRelativo();
        ultimoReal = System.currentTimeMillis();
        intervaloEntreAtualizacoes = sp;
    }

    /**
     * alterar o valor standard, caso se queiram jogos com taxas maiores/menores de atualizações
     */
    public static void setIntervaloStandard(long is) {
        if (is < 0)
            throw new IllegalArgumentException("is tem de ser positivo: " + is);
        intervaloStandard = is;
    }

    /**
     * ver qual o valor standard das atualizaçoes
     *
     * @return o valor standard das atualizações
     */
    public static long getIntervaloStandard() {
        return intervaloStandard;
    }
}
