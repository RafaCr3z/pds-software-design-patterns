package faroest.app;

import faroest.cliente.Cliente;

public class LadroesVersaoInfo extends VersaoInfo {

    // Construtor
    public LadroesVersaoInfo(String nome, String highscores, String imagem) {
        super(nome, highscores, imagem);
    }

    @Override
    public Cliente criarDepositante(String nome, int pontos, int extras, int minAberto, int maxAberto) {
        return LevelReader.criarDepositanteLadrao(nome, pontos, extras, minAberto, maxAberto);
    }

    @Override
    public Cliente criarAssaltante(String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar) {
        return LevelReader.criarAssaltanteLadrao(nome, pontos, minSacar, maxSacar, minDisparar, maxDisparar);
    }

    @Override
    public Cliente criarAleatorio(String nome, int pontos, int numExtras, int minEspera, int maxEspera) {
        return LevelReader.criarAleatorioLadrao(nome, pontos, numExtras, minEspera, maxEspera);
    }

    @Override
    public Cliente criarTroca(String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar) {
        return LevelReader.criarTrocaLadrao(nome, nomeBandido, pontos, minTrocar, maxTrocar, minDisparar, maxDisparar);
    }

    @Override
    public Cliente criarInsatisfeito(String nome, int pontos, int numExtras, int minEspera, int maxEspera) {
        return LevelReader.criarInsatisfeitoLadrao(nome, pontos, numExtras, minEspera, maxEspera);
    }
}
