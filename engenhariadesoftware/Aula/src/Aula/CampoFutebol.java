package Aula;
import java.util.List;
import java.util.LinkedList;

public class CampoFutebol{
	
	private List<Jogador> jogadores = new LinkedList<Jogador>();
	
	public void adicionarJogador(Jogador jogador) {
		jogadores.add(jogador);
	}
	
	public List<Jogador> getJogadores(){
		return jogadores;
	}
	
	
	public List<Jogador> buscarJogadorNome(String nome){
		List<Jogador> encontrados = new LinkedList<Jogador>();
		for(Jogador jogador:jogadores) {
			if(jogador.getNome().equals(nome))
				encontrados.add(jogador);
		}
		return encontrados;
	}
}