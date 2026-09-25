package faroest.app;

import faroest.cliente.Cliente;

public class ZombieVersaoInfo extends VersaoInfo {

    // Construtor
    public ZombieVersaoInfo(String nome, String highscores, String imagem) {
        super(nome, highscores, imagem);
    }

    @Override
    public Cliente criarDepositante(String nome, int pontos, int extras, int minAberto, int maxAberto) {
        return LevelReader.criarDepositanteZombie(nome, pontos, extras, minAberto, maxAberto);
    }

    @Override
    public Cliente criarAssaltante(String nome, int pontos, int minSacar, int maxSacar, int minDisparar, int maxDisparar) {
        return LevelReader.criarAssaltanteZombie(nome, pontos, minSacar, maxSacar, minDisparar, maxDisparar);
    }

    @Override
    public Cliente criarAleatorio(String nome, int pontos, int numExtras, int minEspera, int maxEspera) {
        return LevelReader.criarAleatorioZombie(nome, pontos, numExtras, minEspera, maxEspera);
    }

    @Override
    public Cliente criarTroca(String nome, String nomeBandido, int pontos, int minTrocar, int maxTrocar, int minDisparar, int maxDisparar) {
        return LevelReader.criarTrocaZombie(nome, nomeBandido, pontos, minTrocar, maxTrocar, minDisparar, maxDisparar);
    }

    @Override
    public Cliente criarInsatisfeito(String nome, int pontos, int numExtras, int minEspera, int maxEspera) {
        return LevelReader.criarInsatisfeitoZombie(nome, pontos, numExtras, minEspera, maxEspera);
    }
}
