package estairways;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Voo {
    private String numero;
    private String codigoAeroportoOrigem;
    private String codigoAeroportoDestino;
    private LocalDateTime diaHoraPartida;
    private long custoBagagemPorao;
    private long custoCadaLugar;

    //os preços a praticar pelas várias categorias de conforto: a primeira para a classe Deluxe, a segunda para a classe Comfort e a terceira para a Standard. Os preços estão na ordem do maior para o menor.
    private HashMap<ClasseConforto, List<Long>> precosCategorias;

    private HashMap<ClasseConforto, Integer> lugaresDisponiveis;

    private HashMap<ClasseConforto, Integer> lugaresOcupados;

    private List<Passageiro> passageiros;

    public Voo(String numero, String codigoAeroportoOrigem, String codigoAeroportoDestino, LocalDateTime diaHoraPartida, long custoBagagemPorao, long custoCadaLugar) {
        this.numero = numero;
        this.codigoAeroportoOrigem = codigoAeroportoOrigem;
        this.codigoAeroportoDestino = codigoAeroportoDestino;
        this.diaHoraPartida = diaHoraPartida;
        this.custoBagagemPorao = custoBagagemPorao;
        this.custoCadaLugar = custoCadaLugar;
        this.precosCategorias = new HashMap<>();
        this.lugaresDisponiveis = new HashMap<>();
        this.lugaresOcupados = new HashMap<>();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoAeroportoOrigem() {
        return codigoAeroportoOrigem;
    }

    public void setCodigoAeroportoOrigem(String codigoAeroportoOrigem) {
        this.codigoAeroportoOrigem = codigoAeroportoOrigem;
    }

    public String getCodigoAeroportoDestino() {
        return codigoAeroportoDestino;
    }

    public void setCodigoAeroportoDestino(String codigoAeroportoDestino) {
        this.codigoAeroportoDestino = codigoAeroportoDestino;
    }

    public LocalDateTime getDiaHoraPartida() {
        return diaHoraPartida;
    }

    public void setDiaHoraPartida(LocalDateTime diaHoraPartida) {
        this.diaHoraPartida = diaHoraPartida;
    }

    public long getCustoBagagemPorao() {
        return custoBagagemPorao;
    }

    public void setCustoBagagemPorao(long custoBagagemPorao) {
        this.custoBagagemPorao = custoBagagemPorao;
    }

    public long getCustoCadaLugar() {
        return custoCadaLugar;
    }

    public void setCustoCadaLugar(long custoCadaLugar) {
        this.custoCadaLugar = custoCadaLugar;
    }

    public boolean getLugaresDisponiveis(ClasseConforto classe, int numPassageiros) {
        return lugaresDisponiveis.getOrDefault(classe, 0) >= numPassageiros;
    }

    public HashMap<ClasseConforto, List<Long>> getPrecosCategorias() {
        return precosCategorias;
    }

    public HashMap<ClasseConforto, Integer> getLugaresDisponiveis() {
        return lugaresDisponiveis;
    }
    
    //Método que calcula o preço total com base na classe de conforto e no número de passageiros
    public long calcularPrecoTotal(ClasseConforto classe, int numPassageiros) {
        List<Long> precos = precosCategorias.get(classe);
        if (precos == null || precos.size() < numPassageiros) {
            throw new IllegalArgumentException("Assentos insuficientes disponíveis para a classe " + classe);
        }
        long total = 0;
        for (int i = 0; i < numPassageiros; i++) {
            total += precos.get(i);
        }
        return total;
    }

    public void setTabelaPrecos(ClasseConforto classeConforto, List<Long> precosClasses) {
        this.precosCategorias.put(classeConforto, precosClasses); 

        //Define a quantidade de lugares totais com base na lista de preços
        this.lugaresDisponiveis.put(classeConforto, precosClasses.size());

        //Define a quantidade de lugares disponíveis com base na lista de preços
        this.lugaresOcupados.put(classeConforto, precosClasses.size());
    }

    //MÉTODO PARA VERIFICAR SE HÁ LUGARES DISPONÍVEIS PARA A CLASSE DE CONFORTO E O NÚMERO DE PASSAGEIROS E O NUMERO DE PASSAGEIROS
    public boolean terLugaresDisponiveis(ClasseConforto classe, int numPassageiros) {
        return lugaresDisponiveis.getOrDefault(classe, 0) >= numPassageiros;
    }

    //método para reservar bilhetes
    public void reservarAssentos(ClasseConforto classe, int numAssentos) {
        if (!terLugaresDisponiveis(classe, numAssentos)) {
            throw new IllegalArgumentException("Não há assentos disponíveis para a classe " + classe);
        }
        lugaresDisponiveis.put(classe, lugaresDisponiveis.get(classe) - numAssentos);
    }

    //Método para verficar se a classe de conforto suporta um determinado tipo de reserva
    public boolean suportaTipoReserva(ClasseConforto classe, String tipoReserva) {
        for (String c : classe.getReservaAssociadas()) {
            if (c.equals(tipoReserva)) {
                return true;
            }
        }
        return false;
    }
}
