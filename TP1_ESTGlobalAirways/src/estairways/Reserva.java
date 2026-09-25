package estairways;

import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private String reservaId;
    private Voo voo;
    private ClasseConforto classeConforto;
    private String tipoReserva;
    private ArrayList<Passageiro> passageiros;

    public Reserva(String reservaId, Voo voo, ClasseConforto classeConforto, String tipoReserva) {
        this.reservaId = reservaId;
        this.voo = voo;
        this.classeConforto = classeConforto;
        this.tipoReserva = tipoReserva;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
    }

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }

    public ClasseConforto getClasseConforto() {
        return classeConforto;
    }

    public void setClasseConforto(ClasseConforto classeConforto) {
        this.classeConforto = classeConforto;
    }

    public String getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public ArrayList<Passageiro> getPassageiros() {
        return passageiros;
    }

    //Método para adicionar um passageiro à reserva
    public void addPassageiro(Passageiro passageiro) {
        if (passageiros.size() < ESTAirways.MAX_PASSAGEIROS_RESERVA) {
            passageiros.add(passageiro);
        } else {
            throw new IllegalStateException("Máximo de passageiros excedido para esta reserva.");
        }
    }

    //Método para remover um passageiro da reserva
    public void removePassageiro(Passageiro passageiro) {
        this.passageiros.remove(passageiro);
    }

    //Método para calcular o custo total da reserva
    public long calcularCustoTotal() {
        long custoTotal = 0;
        for (Passageiro passageiro : passageiros) {
            custoTotal += voo.getCustoCadaLugar();
            custoTotal += voo.getCustoBagagemPorao() * passageiro.getNumMalasPorao();
        }
        return custoTotal;
    }
    
    
}
