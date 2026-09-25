package menu;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import estairways.Aeroporto;
import estairways.ClasseConforto;
import estairways.ESTAirways;
import estairways.Voo;

public class Main {

	/**Arranca com a aplicação
	 */
	public static void main(String[] args) {
		ESTAirways albi = new ESTAirways();
		readAeroportos(albi, "C:\\Users\\Lenovo\\OneDrive\\Documentos\\PDS_TP1_ESTGlobalAirways\\ESTGlobalAirways\\data\\aeroportos.dat");
		readVoos(albi, "C:\\Users\\Lenovo\\OneDrive\\Documentos\\PDS_TP1_ESTGlobalAirways\\ESTGlobalAirways\\data\\voos.dat");

		// ver o tamanho do écran onde criar as janelas
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// criar as janelas
		JanelaEscolha je = new JanelaEscolha(albi);
		JanelaReservas jr = new JanelaReservas(albi);
		JanelaVoos jv = new JanelaVoos(albi);

		// posicioná-las
		Rectangle r1 = je.getBounds();
		Rectangle r2 = jr.getBounds();
		Rectangle r3 = jv.getBounds();
		int posx1 = (screenSize.width - r1.width - Math.max(r2.width, r3.width) - 10) / 2;
		int posx2 = posx1 + r1.width + 10;
		int posy = (screenSize.height - Math.max(r1.height, r2.height + 10 + r3.height)) / 2;
		int posy2 = posy + r2.height + 10;

		je.setLocation(posx1, posy);
		je.setVisible(true);

		jr.setLocation(posx2, posy);
		jr.setVisible(true);

		jv.setLocation(posx2, posy2);
		jv.setVisible(true);
	}

	/** método para ler o ficheiro com a informação dos aeroportos
	 * @param estWays a companhia
	 * @param aeroportosFile o nome do ficheiro com a informação
	 */
	private static void readAeroportos(ESTAirways estWays, String aeroportosFile) {
		try (BufferedReader fin = new BufferedReader(new FileReader(aeroportosFile))) {
			String line;
			while ((line = fin.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length == 4) {
					String codigo = parts[0];
					String nome = parts[1];
					long taxaAeroportuaria = Long.parseLong(parts[2]);
					long taxaAlteracoes = Long.parseLong(parts[3]);

					Aeroporto aeroporto = new Aeroporto(codigo, nome, taxaAeroportuaria, taxaAlteracoes);
					estWays.addAeroporto(aeroporto);
				} else {
					System.out.println("Linha mal formatada: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Não tenho o ficheiro " + aeroportosFile);
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Erro na leitura do ficheiro " + aeroportosFile);
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**método para ler o ficheiro com a informação dos voos
	 * @param estWays a companhia
	 * @param file o nome do ficheiro com a informação
	 */
	private static void readVoos( ESTAirways estWays, String file ){
		// formatter para processar a hora
		final DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("H:m");
		try ( BufferedReader fin = new BufferedReader( new FileReader( file ) )) {

			String line;
			while ((line = fin.readLine()) != null) {

				if (line.trim().equals("<-- VOO -->")) {

					line = fin.readLine();

					String[] vooInfo = line.split("\t");

					String numero = vooInfo[0];
					String codigoOrigem = vooInfo[1];
					String codigoDestino = vooInfo[2];


					line = fin.readLine();
					String[] dataHoraInfo = line.split("\t");

					int dia = Integer.parseInt(dataHoraInfo[0]);
					String hora = dataHoraInfo[1];

					LocalDateTime dataHoraPartida = LocalDateTime.now().plusDays(dia)
							.with(LocalTime.parse(hora, formatterHora));

					long custoBagagem = Long.parseLong(fin.readLine());
					long custoLugar = Long.parseLong(fin.readLine());

					// Leitura dos preços para as classes Deluxe, Comfort e Standard
					List<Long> precosDeluxe = parsePrices(fin.readLine());
					List<Long> precosComfort = parsePrices(fin.readLine());
					List<Long> precosStandard = parsePrices(fin.readLine());

					Voo voo = new Voo(numero, codigoOrigem, codigoDestino, dataHoraPartida, custoBagagem, custoLugar);

					voo.setTabelaPrecos(ClasseConforto.DELUXE, precosDeluxe);
					voo.setTabelaPrecos(ClasseConforto.COMFORT, precosComfort);
					voo.setTabelaPrecos(ClasseConforto.STANDARD, precosStandard);

					estWays.addVoo(voo);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Não tenho o ficheiro " + file );
			System.exit( 0 );
		}
		catch (Exception e) {
			System.out.println("Erro na leitura do ficheiro " + file );
			e.printStackTrace();
			System.exit( 0 );
		}
	}

	/**Método para processar a linha com os preços
	 * @param line a linha com os preços
	 * @return a lista com os preços
	 */
	private static List<Long> parsePrices(String line) {
		return Arrays.stream(line.trim().split(","))
				.map(Long::parseLong)
				.collect(Collectors.toList());
	}
}
