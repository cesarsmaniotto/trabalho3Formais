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
	private boolean belongsto(String word, String[] list){
		
		for(int i=0; i<list.length;i++){
			if(word.equals(list[i]))return true;
		}
		return false;
	}
	private List<Token> updateTokens(List<Token> list){
		String[] tipo={"int","double","char","float","void"};
	
		
		List<Token> nova = new ArrayList<>();	
		for(Token t: list){
			if(t.getTipoToken()==TipoToken.MARGEM){
				String s= t.getLexema();
				if(s.equals("(")){
					nova.add(new Token(s,TipoToken.STARTP));
					continue;
				}
				if(s.equals(")")){
					nova.add(new Token(s,TipoToken.ENDP));
					continue;
				}
				if(s.equals("{")){
					nova.add(new Token(s,TipoToken.STARTB));
					continue;
				}
				if(s.equals("}")){
					nova.add(new Token(s,TipoToken.ENDB));
					continue;
				}
				System.out.println("Margem não identificada");
				continue;
			}
			
			if( !(t.getTipoToken()==TipoToken.PALAVRA_RESERVADA) ){
		
				nova.add(t);
			
			}else{
				
				String s= t.getLexema();
				if(belongsto(s,tipo)){
					nova.add(new Token(s,TipoToken.TIPO));
					continue;
				}
				if(s.equals("for")){
					nova.add(new Token(s,TipoToken.FOR));
					continue;
				}
				if(s.equals("while")){
					nova.add(new Token(s,TipoToken.WHILE));
					continue;
				}
				if(s.equals("if")){
					nova.add(new Token(s,TipoToken.IF));
					continue;
				}
				if(s.equals("else")){
					nova.add(new Token(s,TipoToken.ELSE));
					continue;
				}
				nova.add(t);
				System.out.println("Palavra reservada nào identificada");
			}
		}
		return nova;
	}

	public boolean reconhecerPrograma(List<Token> tokens) {

		tokens= updateTokens(tokens);
		
		Token fimDePilha = new Token("$", TipoToken.FIM_DE_PILHA);

		Stack<String> pilha = new Stack<>();

		tokens.add(fimDePilha);
		pilha.push(fimDePilha.getTipoToken().getTipo());
		// TODO colocar simbolo inicial no topo da pilha

		for (int i = 0; i < tokens.size(); i++) {

			String ultimoPilha = pilha.peek().toLowerCase();
			String simbLookAhead = tokens.get(i).getTipoToken().getTipo().toLowerCase();

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
