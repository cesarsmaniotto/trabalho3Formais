/**
 * 
 */
package formais152.Modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import formais152.Modelo.Excecoes.ErroSintaticoException;

/**
 * @author cesar
 *
 */
public class AnalisadorSintatico {

	private GramaticaLivreContexto gramatica;
	private Map<String, Map<String, ProducaoGLC>> tabela = new HashMap<>();

	public AnalisadorSintatico(GramaticaLivreContexto gramatica) {
		this.gramatica = gramatica;
		constroiTabelaReconhecimentoSintaticoPreditivo();
	}

	public void constroiTabelaReconhecimentoSintaticoPreditivo() {

		for (SimboloGLC ladoEsquerdo : gramatica.getNaoTerminais()) {

			for (ProducaoGLC producao : gramatica.getProducoes().get(
					ladoEsquerdo)) {

				if (producao.getSimbolos().size() == 1
						&& producao.getSimbolos().get(0).equals("&")) {

					for (SimboloGLC followLadoEsquerdo : ladoEsquerdo
							.obterFollow()) {
						addItemATabela(ladoEsquerdo.getSimbolo(),
								followLadoEsquerdo.getSimbolo(), producao);
					}
				} else {

					for (SimboloGLC alfa : producao.getSimbolos()) {
						if (alfa.isTerminal()) {
							addItemATabela(ladoEsquerdo.getSimbolo(),
									alfa.getSimbolo(), producao);
							break;
						} else {

							for (SimboloGLC firstAlfa : alfa.obterFirst()) {

								if (!firstAlfa.getSimbolo().equals("&")) {
									addItemATabela(ladoEsquerdo.getSimbolo(),
											firstAlfa.getSimbolo(), producao);
								}

							}

							if (!alfa.temNoFirst("&")) {
								break;
							}
						}

					}
				}

			}
		}
	}

	private void addItemATabela(String naoTerminal, String terminal,
			ProducaoGLC producao) {

		if (!tabela.containsKey(naoTerminal)) {
			tabela.put(naoTerminal, new HashMap<String, ProducaoGLC>());
		}

		if (!tabela.get(naoTerminal).containsKey(terminal)) {
			tabela.get(naoTerminal).put(terminal, producao);
		}

	}

	private boolean isTerminal(String simbolo) {
		return !tabela.containsKey(simbolo);
	}

	private ProducaoGLC getItemTabela(String naoTerminal, String terminal) {

		return tabela.get(naoTerminal).get(terminal);

	}

	private boolean existeItemTabela(String naoTerminal, String terminal) {

		return tabela.get(naoTerminal).containsKey(terminal);

	}
	private boolean compararToken(Token pilha, Token look){
		String pilhaText = pilha.getLexema();
		String lookText = look.getLexema().toLowerCase();
		
		String compare[]={"for","while","if","else"};
		boolean alce=false;
		
		for(int i=0; i<compare.length;i++){
			if(lookText.equals(compare[i])){
				alce=true;
			}
		}
		if(alce==true){
			return pilhaText.equals(lookText);
		}
		
		pilhaText = pilha.getTipoToken().getTipo().toLowerCase();
		lookText = look.getTipoToken().getTipo().toLowerCase();
		return pilhaText.equals(lookText);

	}
	private List<Token> updateTokens(List<Token> list){
		List<Token> nova;
		
		for(Token t: list){
			
		}
	}

	public boolean reconhecerPrograma(List<Token> tokens) {

		Token fimDePilha = new Token("$", TipoToken.FIM_DE_PILHA);

		Stack<String> pilha = new Stack<>();

		tokens.add(fimDePilha);
		pilha.push(fimDePilha.getTipoToken().getTipo());
		// TODO colocar simbolo inicial no topo da pilha

		for (int i = 0; i < tokens.size(); i++) {

			String ultimoPilha = pilha.peek();
			String simbLookAhead = tokens.get(i).getTipoToken().getTipo();

			if (ultimoPilha.equals(TipoToken.FIM_DE_PILHA)) {
				return true;
			}

			else if (isTerminal(ultimoPilha)) {
				if (ultimoPilha.equals(simbLookAhead)) {
					pilha.pop();
				} else {
					throw new ErroSintaticoException("Erro sintático");
				}
			} else {
				if(!existeItemTabela(ultimoPilha, simbLookAhead)){
					throw new ErroSintaticoException("Erro sintático");
				}else{
					pilha.pop();
					ProducaoGLC prodTabela = getItemTabela(ultimoPilha, simbLookAhead);
					
					for(SimboloGLC simbolo : prodTabela.obterInverso()){
						pilha.push(simbolo.getSimbolo());
					}
				}
			}

		}

		return false;
	}

}
