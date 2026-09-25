package faroest.app;

import faroest.cliente.Cliente;

public class VersaoInfo {
    String nome;       // nome da versão
    String scoreFile;  // nome do ficheiro com as pontuações máximas
    String scoreImage; // nome da imagem a usar como fundo das pontuações máximas

    // Remover este atributo
    // int pack; // indicação de quais os clientes a criar

    public VersaoInfo(String nome, String scoreFile, String scoreImage) {
        this.nome = nome;
        this.scoreFile = scoreFile;
        this.scoreImage = scoreImage;
        // this.pack = pack;
    }

    public Cliente criarDepositante(String nome, int pontos, int extras, int minAberto, int maxAberto) {
        return LevelReader.criarDepositanteBase(nome, pontos, extras, minAberto, maxAberto);
    }

    public Cliente criarAssaltante(String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar ) {
        return LevelReader.criarAssaltanteBase(nome, pontos, minSacar, maxSacar, minDisparar, maxDisparar);
    }

    public Cliente criarAleatorio( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
        return LevelReader.criarAleatorioBase(nome, pontos, numExtras, minEspera, maxEspera);
    }

    public Cliente criarTroca( String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar ) {
        return LevelReader.criarTrocaBase(nome, nomeBandido, pontos, minTrocar, maxTrocar, minDisparar, maxDisparar);
    }

    public Cliente criarInsatisfeito( String nome, int pontos, int numExtras, int minEspera, int maxEspera ) {
        return LevelReader.criarInsatisfeitoBase(nome, pontos, numExtras, minEspera, maxEspera);
    }

    @Override
    public String toString() {
        return nome;
    }
}