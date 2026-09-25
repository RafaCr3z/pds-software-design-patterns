package estairways;

/**
 * Enumeração com as várias classes de conforto que a companhia
 * suporta (também podia estar configurada num ficheiro, mas 
 * assim simplifica mais o sistema).
 * Cada classe de conforto tem associadas as reserva que suporta
 */
public enum ClasseConforto {
	STANDARD(ESTAirways.ECONOMICA, ESTAirways.BASIC),
	COMFORT(ESTAirways.BUSINESS) , DELUXE( ESTAirways.EXECUTIVE);
	
	
	private String[] tipoReservas;
	
	private ClasseConforto( String ...r ){
		tipoReservas = r;
	}
	
	public String[] getReservaAssociadas() {
		return tipoReservas;
	}

	public static ClasseConforto getClasseConforto(String tipoReserva) {
		for (ClasseConforto c : ClasseConforto.values()) {
			for (String r : c.tipoReservas) {
				if (r.equals(tipoReserva)) {
					return c;
				}
			}
		}
		return null;
	}



}
