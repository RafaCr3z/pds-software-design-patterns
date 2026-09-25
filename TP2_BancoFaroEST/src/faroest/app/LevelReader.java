package faroest.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import faroest.cliente.*;
import faroest.mundo.Mundo;
import prof.jogos2D.util.ESTProperties;

/** Classe responsável pela leitura dos ficheiros de nível
 */
public class LevelReader {
	
	private static ESTProperties props;    // as propriedades a ler
	
	public static Mundo lerNivel( int level, VersaoInfo versao ) throws FileNotFoundException, IOException{
		String file = "niveis/nivel" + level + ".bfe";

		// ler o ficheiro como uma sequência de propriedades
		props = new ESTProperties( new FileReader( file ) );
		
		// ler imagem do fundo e da porta
		BufferedImage imgFundo = ImageIO.read( new File( "art/" + props.getConfig("fundo") ));
		BufferedImage imgPorta = ImageIO.read( new File( "art/" + props.getConfig("porta") ));
		
		// ler as restantes infos do mundo
		int numPortas = props.getConfigAsInt("numPortas");
		int numVisitantes = props.getConfigAsInt("numVisitantes");
		int pontos = props.getConfigAsInt("pontos"); 
		
		// criar o mundo com as infos lidas
		Mundo mundo = new Mundo( imgFundo, imgPorta, numPortas, pontos );
		
		// ler os clientes possíveis
		for( int i=1; i <= numVisitantes; i++ ) {
			String p = "visitante_" + (i<10? "0": "") + i;
			String info[] = props.getConfig( p ).split(",");
			Cliente v = null;
			try {
				switch( info[0] ) {
				case "depositante": v = criarDepositante(info, versao); break;
				case "assaltante": v = criarAssaltante(info, versao); break;
				case "troca": v = criarTroca(info, versao); break;
				case "aleatorio": v = criarAleatorio(info, versao); break;
				case "insatisfeito": v= criarInsatisfeito(info, versao); break;
				}
			} catch( Exception e ) {
				throw new IOException( e );
			}
			if( v == null )
				throw new IOException();
			mundo.addPossivelCliente(v);
		}
		
		// criar e retornar o mundo
		return mundo;
	}

	/**
	 * Cria um depositante
	 * @param info as informações sobre o depositante
	 * @param versao a versão para a qual deve criar o depositante
	 * @return o depositante criado
	 */
	private static Cliente criarDepositante( String info[], VersaoInfo versao ) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int extras = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );

		return versao.criarDepositante(nome, pontos, extras, minAberto, maxAberto);
	}
	
	public static Cliente criarDepositanteBase( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", new StatusDepositar( "_adeus" ) );
		StatusTerminal morto = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera, maxEspera, morto );
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera ); 
		
		Cliente vs = new Cliente(nome, pontos, numExtras, ola );
		return vs;
	}
	
	public static Cliente criarDepositanteZombie( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusTerminal morder = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVez = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacar  = new StatusReativo( "_zombie", morder, 1000, 2000, morreOutraVez );		
		StatusTransitorio riseFromTheDead = new StatusTransitorio( "_rise", atacar );

		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", new StatusDepositar( "_adeus" ) );
		StatusTransitorio morto = new StatusTransitorio( "_morte", riseFromTheDead );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera, maxEspera, morto );
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera ); 
		
		Cliente vs = new Cliente(nome, pontos, numExtras, ola );
		return vs;
	}

	public static Cliente criarDepositanteLadrao( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusTransitorio sairDepositando = new StatusTransitorio( "_sair", new StatusDepositar( "_adeus" ) );
		StatusTransitorio sairRoubando = new StatusTransitorio( "_adeus", new StatusRoubar("_adeus") );
		StatusTemporal esperaRoubar = new StatusTemporal( "_espera", sairRoubando, minEspera, maxEspera, sairDepositando );
		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", esperaRoubar );
		StatusTerminal morto = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera, maxEspera, morto );
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera ); 
		
		Cliente vs = new Cliente(nome, pontos, numExtras, ola );
		return vs;
	}

	
	 /** Cria um assaltante
	 * @param info as informações sobre o assaltante
	 * @param versao a versão para a qual deve criar o assaltante
	 * @return o assaltante criado
	 */
	private static Cliente criarAssaltante( String info[], VersaoInfo versao ) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int minSacar = Integer.parseInt( info[3] );
		int maxSacar = Integer.parseInt( info[4] );
		int minDisparar = Integer.parseInt( info[5] );
		int maxDisparar = Integer.parseInt( info[6] );

		return versao.criarAssaltante(nome, pontos, minSacar, maxSacar, minDisparar, maxDisparar);
	}

	public static Cliente criarAssaltanteBase( String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar ) {
		StatusTransitorio morteSacada = new StatusTransitorio( "_morte2", new StatusInativo( ) );
		StatusTerminal mortePorSacar = new StatusTerminal( "_morte1", "oops", new StatusInativo( ) );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );
		StatusTransitorio sacar = new StatusTransitorio( "_saca", sacada ); 
		StatusTemporal espera   = new StatusTemporal( "_espera", sacar, minSacar, maxSacar, mortePorSacar );
		return new Cliente(nome, pontos, 0, (maxSacar > 0? espera: sacada) );
	}
	
	public static Cliente criarAssaltanteZombie( String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar ) {
		StatusTerminal morder = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVez = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacar  = new StatusReativo( "_zombie", morder, minDisparar, maxDisparar, morreOutraVez );		
		StatusTransitorio riseFromTheDead = new StatusTransitorio( "_rise", atacar );

		StatusTerminal morderPorSacar = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVezPorSacar = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacarPorSacar  = new StatusReativo( "_zombie", morderPorSacar, minDisparar, maxDisparar, morreOutraVezPorSacar );		
		StatusTransitorio riseFromTheDeadPorSacar = new StatusTransitorio( "_rise", atacarPorSacar );

		StatusTransitorio morteSacada = new StatusTransitorio( "_morte2", riseFromTheDead );
		StatusTransitorio mortePorSacar = new StatusTransitorio( "_morte1", riseFromTheDeadPorSacar );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );
		StatusTransitorio sacar = new StatusTransitorio( "_saca", sacada ); 
		StatusTemporal espera   = new StatusTemporal( "_espera", sacar, minSacar, maxSacar, mortePorSacar );
		return new Cliente(nome, pontos, 0, (maxSacar > 0? espera: sacada) );
	}

	public static Cliente criarAssaltanteLadrao( String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar ) {
		StatusTransitorio morteSacada = new StatusTransitorio( "_morte2", new StatusInativo( ) );
		StatusTerminal mortePorSacar = new StatusTerminal( "_morte1", "oops", new StatusInativo( ) );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );
		StatusTransitorio sacar = new StatusTransitorio( "_saca", sacada ); 
		StatusTemporal espera   = new StatusTemporal( "_espera", sacar, minSacar, maxSacar, mortePorSacar );
		return new Cliente(nome, pontos, 0, (maxSacar > 0? espera: sacada) );
	}
	
	
	
	 /** Cria um aleatório
	 * @param info as informações sobre o aleatório
	 * @param versao a versão para a qual deve criar o aleatório
	 * @return o aleatório criado
	 */
	private static Cliente criarAleatorio( String info[], VersaoInfo versao ) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int nExtras = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );

		return versao.criarAleatorio(nome, pontos, nExtras, minAberto, maxAberto);
	}
	
	public static Cliente criarAleatorioBase( String nome, int pontos, int numExtras, int minEspera, int maxEspera  ) {
		StatusTerminal morte = new StatusTerminal( "_mata", "boom", new StatusInativo( ) );
		StatusEfeito deposita = new StatusEfeito( "_deposita", "dinheiro", new StatusDepositar( "_deposita" ) );
		StatusAleatorio espera   = new StatusAleatorio( "_deposita", "_mata", new StatusInativo( ), minEspera, maxEspera, deposita, morte );
		StatusTransitorio ola = new StatusTransitorio( "_deposita", espera );
		return new Cliente(nome, pontos, numExtras, ola );
	}
	
	public static Cliente criarAleatorioZombie( String nome, int pontos, int numExtras, int minEspera, int maxEspera  ) {
		StatusTerminal morte = new StatusTerminal( "_mata", "boom", new StatusInativo( ) );
		StatusEfeito deposita = new StatusEfeito( "_deposita", "dinheiro", new StatusDepositar( "_deposita" ) );
		StatusAleatorio espera   = new StatusAleatorio( "_deposita", "_mata", new StatusInativo( ), minEspera, maxEspera, deposita, morte );
		StatusTransitorio ola = new StatusTransitorio( "_deposita", espera );
		return new Cliente(nome, pontos, numExtras, ola );
	}

	public static Cliente criarAleatorioLadrao( String nome, int pontos, int numExtras, int minEspera, int maxEspera  ) {
		StatusTransitorio sairRoubando = new StatusTransitorio( "_deposita", new StatusRoubar("_deposita") );
		StatusTerminal morte = new StatusTerminal( "_mata", "boom", new StatusInativo( ) );
		StatusEfeito deposita = new StatusEfeito( "_deposita", "dinheiro", new StatusDepositar( "_deposita" ) );
		StatusAleatorioRoubar espera   = new StatusAleatorioRoubar( "_deposita", "_mata", sairRoubando, new StatusInativo(),
				                                                    minEspera, maxEspera, deposita, morte );
		StatusTransitorio ola = new StatusTransitorio( "_deposita", espera );
		return new Cliente(nome, pontos, numExtras, ola );
	}
	
	
	
	 /** Cria um Troca
	 * @param info as informações sobre o troca
	 * @param versao a versão para a qual deve criar o troca
	 * @return o troca criado
	 */
	private static Cliente criarTroca( String info[], VersaoInfo versao ) {
		String nome = info[1];
		String nomeBandido = info[2];
		int pontos = Integer.parseInt( info[3] );
		int minTrocar = Integer.parseInt( info[4] );
		int maxTrocar = Integer.parseInt( info[5] );
		int minDisparar = Integer.parseInt( info[6] );
		int maxDisparar = Integer.parseInt( info[7] );
		
		return versao.criarTroca(nome, nomeBandido, pontos, minTrocar, maxTrocar, minDisparar, maxDisparar);
	}
	
	public static Cliente criarTrocaBase( String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar ) {
		StatusTerminal morteAntesTrocar = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTransitorio morteSacada = new StatusTransitorio( "_morte", new StatusInativo( ) );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );		
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );	
		StatusTrocando trocar = new StatusTrocando( "_troca", nomeBandido, sacada ); 		
		StatusTemporal espera   = new StatusTemporal( "_espera", trocar, minTrocar, maxTrocar, morteAntesTrocar );		
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera );
		return new Cliente(nome, pontos, 0, ola );
	}

	public static Cliente criarTrocaZombie( String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar ) {
		StatusTerminal morder = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVez = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacar  = new StatusReativo( "_zombie", morder, minDisparar, maxDisparar, morreOutraVez );
		StatusTransitorio riseFromTheDead = new StatusTransitorio( "_rise", atacar );

		StatusTerminal morderAntes = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVezAntes = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacarAntes  = new StatusReativo( "_zombie", morderAntes, minDisparar, maxDisparar, morreOutraVezAntes );
		StatusTransitorio riseFromTheDeadAntes = new StatusTransitorio( "_rise", atacarAntes );
		
		StatusTransitorio morteAntesTrocar = new StatusTransitorio( "_morte", riseFromTheDeadAntes );
		StatusTransitorio morteSacada = new StatusTransitorio( "_morte", riseFromTheDead );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );		
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );	
		StatusTrocando trocar = new StatusTrocando( "_troca", nomeBandido, sacada ); 		
		StatusTemporal espera   = new StatusTemporal( "_espera", trocar, minTrocar, maxTrocar, morteAntesTrocar );		
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera );
		return new Cliente(nome, pontos, 0, ola );
	}

	public static Cliente criarTrocaLadrao( String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar ) {
		StatusTerminal morteAntesTrocar = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTransitorio morteSacada = new StatusTransitorio( "_morte", new StatusInativo( ) );
		StatusTerminal disparar = new StatusTerminal( "_sacada", "bang", new StatusInativo( ) );		
		StatusReativo sacada   = new StatusReativo( "_sacada", disparar, minDisparar, maxDisparar, morteSacada );	
		StatusTrocando trocar = new StatusTrocando( "_troca", nomeBandido, sacada ); 		
		StatusTemporal espera   = new StatusTemporal( "_espera", trocar, minTrocar, maxTrocar, morteAntesTrocar );		
		StatusTransitorio ola = new StatusTransitorio( "_ola", espera );
		return new Cliente(nome, pontos, 0, ola );
	}

	 /** Cria um insatisfeito
	 * @param info as informações sobre o insatisfeito
	 * @param versao a versão para a qual deve criar o insatisfeito
	 * @return o insatisfeito criado
	 */
	private static Cliente criarInsatisfeito( String info[], VersaoInfo versao ) {
		String nome = info[1];
		int pontos = Integer.parseInt( info[2] );
		int nExtras = Integer.parseInt( info[3] );
		int minAberto = Integer.parseInt( info[4] );
		int maxAberto = Integer.parseInt( info[5] );

		return versao.criarInsatisfeito(nome, pontos, nExtras, minAberto, maxAberto);
	}
	
	public static Cliente criarInsatisfeitoBase( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", new StatusDepositar( "_adeus" ) );
		StatusTerminal morto = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera/2, maxEspera/2, morto ); 		
		StatusTransitorio pacifica = new StatusTransitorio( "_pacificando", espera );
		StatusTerminal explode = new StatusTerminal( "_zangado", "boom", new StatusInativo( ) );
		StatusTemporal zangado = new StatusTemporal( "_zangado", explode, minEspera, maxEspera, pacifica ); 		
		return new Cliente(nome, pontos, numExtras, zangado );
	}

	public static Cliente criarInsatisfeitoZombie( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusTerminal morder = new StatusTerminal( "_zombie", "nham", new StatusInativo( ) );
		StatusTransitorio morreOutraVez = new StatusTransitorio( "_remorre", new StatusInativo() );
		StatusReativo atacar  = new StatusReativo( "_zombie", morder, minEspera, maxEspera, morreOutraVez );
		StatusTransitorio riseFromTheDead = new StatusTransitorio( "_rise", atacar );

		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", new StatusDepositar( "_adeus" ) );
		StatusTransitorio morto = new StatusTransitorio( "_morte", riseFromTheDead );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera/2, maxEspera/2, morto ); 		
		StatusTransitorio pacifica = new StatusTransitorio( "_pacificando", espera );
		StatusTerminal explode = new StatusTerminal( "_zangado", "boom", new StatusInativo( ) );
		StatusTemporal zangado = new StatusTemporal( "_zangado", explode, minEspera, maxEspera, pacifica ); 		
		return new Cliente(nome, pontos, numExtras, zangado );
	}

	public static Cliente criarInsatisfeitoLadrao( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
		StatusTransitorio sairDepositando = new StatusTransitorio( "_sair", new StatusDepositar( "_adeus" ) );
		StatusTransitorio sairRoubando = new StatusTransitorio( "_adeus", new StatusRoubar("_adeus") );
		StatusTemporal esperaRoubar = new StatusTemporal( "_espera", sairRoubando, minEspera, maxEspera, sairDepositando );
		StatusEfeito saida = new StatusEfeito( "_adeus", "dinheiro", esperaRoubar );
		StatusTerminal morto = new StatusTerminal( "_morte", "oops", new StatusInativo( ) );
		StatusTemporal espera = new StatusTemporal( "_espera", saida, minEspera/2, maxEspera/2, morto ); 		
		StatusTransitorio pacifica = new StatusTransitorio( "_pacificando", espera );
		StatusTerminal explode = new StatusTerminal( "_zangado", "boom", new StatusInativo( ) );
		StatusTemporal zangado = new StatusTemporal( "_zangado", explode, minEspera, maxEspera, pacifica ); 		
		return new Cliente(nome, pontos, numExtras, zangado );
	}

}


