/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
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

	public Map<String, Map<String, ProducaoGLC>> getTabela() {
		return tabela;
	}

	public void constroiTabelaReconhecimentoSintaticoPreditivo() {
		for (SimboloGLC ladoEsquerdo : gramatica.getNaoTerminais()) {
			for (ProducaoGLC producao : gramatica.getProducoes().get(ladoEsquerdo)) {
				
				if (producao.getSimbolos().size() == 1 && producao.getSimbolos().get(0).getSimbolo().equals("&")) {

					for (SimboloGLC followLadoEsquerdo : ladoEsquerdo.obterFollow()) {
						addItemATabela(ladoEsquerdo.getSimbolo(), followLadoEsquerdo.getSimbolo(), producao);
					}
				} else {

					for (SimboloGLC alfa : producao.getSimbolos()) {
						if (alfa.isTerminal()) {
							addItemATabela(ladoEsquerdo.getSimbolo(), alfa.getSimbolo(), producao);
							break;
						} else {

							for (SimboloGLC firstAlfa : alfa.obterFirst()) {

								if (!firstAlfa.getSimbolo().equals("&")) {
								addItemATabela(ladoEsquerdo.getSimbolo(), firstAlfa.getSimbolo(), producao);
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

	private void addItemATabela(String naoTerminal, String terminal, ProducaoGLC producao) {

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

	

	private boolean belongsTo(String word, String[] list) {

		for (int i = 0; i < list.length; i++) {
			if (word.equals(list[i]))
				return true;
		}
		return false;
	}

	private List<Token> updateTokens(List<Token> list) {

		String[] tipo = { "int", "double", "char", "float", "void", "bool","string" };

		List<Token> nova = new ArrayList<>();
		for (Token t : list) {
			if (t.getTipoToken() == TipoToken.MARGEM) {
				String s = t.getLexema();
				if (s.equals("(")) {
					nova.add(new Token(s, TipoToken.STARTP));
					continue;
				}
				if (s.equals(")")) {
					nova.add(new Token(s, TipoToken.ENDP));
					continue;
				}
				if (s.equals("{")) {
					nova.add(new Token(s, TipoToken.STARTB));
					continue;
				}
				if (s.equals("}")) {
					nova.add(new Token(s, TipoToken.ENDB));
					continue;
				}
				System.out.println("Margem não identificada");
				continue;
			}
			if(t.getTipoToken() == TipoToken.ERRO){
				String s = t.getLexema();
				if(s.length()==0)continue;
				if(s.equals(" "))continue;
				if(s.equals("\n"))continue;
				
			}

			if (!(t.getTipoToken() == TipoToken.PALAVRA_RESERVADA)) {

				nova.add(t);

			} else {

				String s = t.getLexema();
				if (belongsTo(s, tipo)) {
					nova.add(new Token(s, TipoToken.TIPO));
					continue;
				}
				if (s.equals("for")) {
					nova.add(new Token(s, TipoToken.FOR));
					continue;
				}
				if (s.equals("while")) {
					nova.add(new Token(s, TipoToken.WHILE));
					continue;
				}
				if (s.equals("if")) {
					nova.add(new Token(s, TipoToken.IF));
					continue;
				}
				if (s.equals("else")) {
					nova.add(new Token(s, TipoToken.ELSE));
					continue;
				}
				if(s.equals("return")){
					nova.add(new Token(s, TipoToken.RETURN));
					continue;
				}
				nova.add(t);
				System.out.println("Palavra reservada nào identificada");
			}
		}
		return nova;
	}

	public boolean reconhecerPrograma(List<Token> tokens) {

		
		tokens = updateTokens(tokens);
		
		this.constroiTabelaReconhecimentoSintaticoPreditivo();
		Token fimDePilha = new Token("$", TipoToken.FIM_DE_PILHA);
		
		Stack<String> pilha = new Stack<>();

		tokens.add(fimDePilha);
		pilha.push(fimDePilha.getTipoToken().getTipo());
		
		
		
		
		SimboloGLC inicial = gramatica.getSimboloInicial();
		String alce= inicial.getFirst();
		if(tabela.keySet().contains(alce)){
		//	System.out.println("Achou o inicial " + inicial);
		}else{
			System.out.println("nao achou o inicial " + inicial + " tem só \n\n" + tabela.keySet());
			
			
			return false;
		}
		pilha.push(inicial.getFirst());
		
		
	
		for (int i = 0; i < tokens.size(); ) {
			
			String ultimoPilha = pilha.peek();
			String simbLookAhead = tokens.get(i).getTipoToken().getTipo().toLowerCase();
			if (i==18) {
				int donothing=5;
				int b =donothing;
			}
			if (ultimoPilha.equals("&")) {
				pilha.pop();
				continue;
			}
			
			
			if (ultimoPilha.equals(TipoToken.FIM_DE_PILHA.toString())) {
				return true;
			}

			else if (isTerminal(ultimoPilha)) {
				if (ultimoPilha.equals(simbLookAhead.toLowerCase())) {
				//	System.out.println("achou "+ultimoPilha);
					
					pilha.pop();
					i++;
				} else {
					throw new ErroSintaticoException(
							"Erro sintático: esperava-se " + simbLookAhead + " e tinha " + ultimoPilha);
				}
			} else {
				if (!existeItemTabela(ultimoPilha, simbLookAhead)) {
					if(gramatica.getSimbolo(ultimoPilha).hasEpsilon){
						//se simbolo não tem valor na tabela mas pode gerar episilon enTao ele vai vazer isso
						pilha.pop();
						continue;
					}
		
					throw new ErroSintaticoException("Erro sintático");
				} else {
					pilha.pop();
					ProducaoGLC prodTabela = getItemTabela(ultimoPilha, simbLookAhead);

					for (SimboloGLC simbolo : prodTabela.obterInverso()) {
						pilha.push(simbolo.getSimbolo());
					}
				}
			}

		}

		return false;
	}

}
