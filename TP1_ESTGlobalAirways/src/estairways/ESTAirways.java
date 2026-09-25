package estairways;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.ArrayList;

/** Classe que tem as constantes para os tipos de reserva, bem como
 * algumas outras constantes (podiam estar num ficheiro, mas já há ficheiros
 * que cheguem :-)   
 */
public class ESTAirways {
	public static final String ECONOMICA = "Económica";
	public static final String BASIC = "Basic";
	public static final String BUSINESS = "Business";
	public static final String EXECUTIVE = "Executive";
	
	public static final int MAX_PASSAGEIROS_RESERVA = 8;
	

	//Criar uma lista de reservas
	private List<Reserva> reservas;
	//Criar uma lista de voos
	private List<Voo> voos;
	//Criar uma lista de aeroportos
	private List<Aeroporto> aeroportos;

	//Construtor
	public ESTAirways() {
		this.reservas = new ArrayList<>();
		this.voos = new ArrayList<>();
		this.aeroportos = new ArrayList<>();
	}

	//Método para adicionar um voo
	public void addVoo(Voo voo) {
		voos.add(voo);
	}

	//Método para adicionar um aeroporto
	public void addAeroporto(Aeroporto aeroporto) {
		aeroportos.add(aeroporto);
	}

	//Lista de reservas
	public List<Reserva> getReservas() {
		return reservas;
	}

	public List<Voo> getVoos() {
		return voos;
	}

	public List<Aeroporto> getAeroportos() {
		return aeroportos;
	}

	
	/** método que gera o id da reserva 
	 * @return um código de 6 caracteres para a reserva
	 */
	public static String gerarReservaId() {
		return GeradorCodigos.gerarCodigo( 6 );
	}

	public String getCodigoAeroporto(String nomeDestino) {
		for (Aeroporto aeroporto : aeroportos) {
			if (aeroporto.getNomeAeroporto().equals(nomeDestino)) {
				return aeroporto.getCodigoAeroporto();
			}
		}
		return null;
	}

	public List<Voo> getVoos(String codigoOrigem, String codigoDestino, LocalDate dataPartida, int numPassageiros, ClasseConforto classe) {
        System.out.println("getVoos chamado com os parâmetros:");
        System.out.println("codigoOrigem: " + codigoOrigem);
        System.out.println("codigoDestino: " + codigoDestino);
        System.out.println("dataPartida: " + dataPartida);
        System.out.println("numPassageiros: " + numPassageiros);
        System.out.println("classe: " + classe);

        List<Voo> voosFiltrados = voos.stream()
            .filter(voo -> {
                boolean origemMatch = (codigoOrigem == null || voo.getCodigoAeroportoOrigem().equals(codigoOrigem));
                boolean destinoMatch = (codigoDestino == null || voo.getCodigoAeroportoDestino().equals(codigoDestino));
                boolean dataMatch = (dataPartida == null || voo.getDiaHoraPartida().toLocalDate().equals(dataPartida));
                boolean lugaresDisponiveis = voo.terLugaresDisponiveis(classe, numPassageiros);

                System.out.println("Voo: " + voo.getNumero() + " origemMatch: " + origemMatch + " destinoMatch: " + destinoMatch + " dataMatch: " + dataMatch + " lugaresDisponiveis: " + lugaresDisponiveis);

                return origemMatch && destinoMatch && dataMatch && lugaresDisponiveis;
            })
            .collect(Collectors.toList());

        System.out.println("Número de voos filtrados: " + voosFiltrados.size());
        return voosFiltrados;
    }

	//Método para obter um voo pelo número
	public Voo getVooPeloNumero(String numero) {
		for (Voo voo : voos) {
			if (voo.getNumero().equals(numero)) {
				return voo;
			}
		}
		return null;
	}

	//Método para obter uma reserva com base no ID
	public Reserva getReservaPeloId(String reservaId) {
		for (Reserva reserva : reservas) {
			if (reserva.getReservaId().equals(reservaId)) {
				return reserva;
			}
		}
		return null;
	}

	//Método a lista de IDs de reservas
	public List<String> getListaReservas() {
		List<String> listaReservas = new ArrayList<>();
		for (Reserva reserva : reservas) {
			listaReservas.add(reserva.getReservaId());
		}
		return listaReservas;
	}

	//Método que confirma uma reserva e gera um ID único
	public String confirmaReserva(ClasseConforto classe, String numeroVoo, String tipoReserva, List<Passageiro> passageiros) {
	int numPassageiros = passageiros.size();

	Voo vooEncontrado = getVooPeloNumero(numeroVoo);

	if (vooEncontrado == null || !vooEncontrado.getLugaresDisponiveis(classe, numPassageiros)) {
		return null;
	}

	// Reservar os assentos e gerar o ID de reserva
	vooEncontrado.reservarAssentos(classe, numPassageiros);
	String idReserva = gerarReservaId();

	// Armazenar a reserva numa lista de reservas
	Reserva reserva = criarReserva(tipoReserva, idReserva, vooEncontrado, classe);

	for (Passageiro passageiro : passageiros) {
		reserva.addPassageiro(passageiro);
	}

	reservas.add(reserva);

	return idReserva;
}

	public Reserva criarReserva(String tipo, String idReserva, Voo voo, ClasseConforto classeConforto) {
		switch (tipo) {
			case ECONOMICA:
				return new ReservaEconomica(idReserva, voo, classeConforto, ECONOMICA);
			case BASIC:
				return new ReservaBasic(idReserva, voo, classeConforto, BASIC);
			case BUSINESS:
				return new ReservaBusiness(idReserva, voo, classeConforto, BUSINESS);
			case EXECUTIVE:
				return new ReservaExecutive(idReserva, voo, classeConforto, EXECUTIVE);
			default:
				throw new IllegalArgumentException("Tipo de reserva inválido");
		}
	}
}
	
