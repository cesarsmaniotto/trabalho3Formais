/**
 * 
 */
package formais152.Modelo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import formais152.Modelo.Excecoes.ProducaoMalFormadaException;

/**
 * @author cesar
 *
 */
public class GramaticaLivreContexto {
	
	private HashMap<String, List<SimboloGLC>> producoes;
	private HashSet<String> simbolosTerminais;
	private HashSet<String> simbolosNaoTerminais;
	private String simboloInicial;

	public GramaticaLivreContexto() {
		producoes = new HashMap<>();
		simbolosNaoTerminais = new HashSet<>();
		simbolosTerminais = new HashSet<>();
	}

	public String getSimboloInicial() {
		return simboloInicial;
	}

	public void setSimboloInicial(String cabecaProducao) {
		simboloInicial = cabecaProducao;
	}
	
	public void adicionaProducao(String cabeca, List<SimboloGLC> corpo){
		
		if(simbolosTerminais.contains(cabeca)){
			throw new ProducaoMalFormadaException("Símbolo terminal definido como cabeça da produção");
		}
		
		for(SimboloGLC simbolo : corpo){
			if(simbolo.isTerminal()){
				simbolosTerminais.add(simbolo.getSimbolos());
			}else{
				simbolosNaoTerminais.add(simbolo.getSimbolos());
			}
		}
		
		simbolosNaoTerminais.add(cabeca);
		producoes.put(cabeca, corpo);
		
	}

}
