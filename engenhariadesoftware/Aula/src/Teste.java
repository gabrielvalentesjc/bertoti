package Aula;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.List;

class CampoFutebolTest {

    @Test
    void test() {
        CampoFutebol brasil = new CampoFutebol();
        brasil.adicionarJogador(new Jogador("Ronaldo", "9"));
        assertEquals(1, brasil.getJogadores().size());
        
        List<Jogador> busca = brasil.buscarJogadorNome("Ronaldo");
        assertEquals(busca.get(0).getNumero(), "9");
        
    }
}
