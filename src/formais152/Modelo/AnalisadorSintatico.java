/**
 * 
 */
package formais152.Modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cesar
 *
 */
public class AnalisadorSintatico {
	
	private GramaticaLivreContexto gramatica;
	private Map<String, Map<String,ProducaoGLC>> tabela = new HashMap<>();
	
	public AnalisadorSintatico(GramaticaLivreContexto gramatica) {
		this.gramatica = gramatica;
		constroiTabelaReconhecimentoSintaticoPreditivo();
	}
	
	
	public void constroiTabelaReconhecimentoSintaticoPreditivo(){
		
		
		for(SimboloGLC ladoEsquerdo : gramatica.getNaoTerminais()){
			
			for(ProducaoGLC producao : gramatica.getProducoes().get(ladoEsquerdo)){
				
				
				if(producao.getSimbolos().size() == 1 && producao.getSimbolos().get(0).equals("&")){
					
					for(SimboloGLC followLadoEsquerdo : ladoEsquerdo.obterFollow()){
						addItemATabela(ladoEsquerdo.getSimbolo(), followLadoEsquerdo.getSimbolo(), producao);
					}
				}else{
				
					for(SimboloGLC alfa : producao.getSimbolos()){
						if(alfa.isTerminal()){
							addItemATabela(ladoEsquerdo.getSimbolo(), alfa.getSimbolo(), producao);
							break;
						}else{
							
							for(SimboloGLC firstAlfa : alfa.obterFirst()){
								
								if(!firstAlfa.getSimbolo().equals("&")){
									addItemATabela(ladoEsquerdo.getSimbolo(), firstAlfa.getSimbolo(), producao);
								}
								
							}
							
							if(!alfa.temNoFirst("&")){
								break;
							}
						}
						
						
					}					
				}			
				
			}			
		}
	}
	
	private void addItemATabela(String naoTerminal, String terminal, ProducaoGLC producao){
		
		if(!tabela.containsKey(naoTerminal)){
			tabela.put(naoTerminal, new HashMap<String,ProducaoGLC>());
		}
		
		if(!tabela.get(naoTerminal).containsKey(terminal)){
			tabela.get(naoTerminal).put(terminal, producao);
		}
		
	}

}
