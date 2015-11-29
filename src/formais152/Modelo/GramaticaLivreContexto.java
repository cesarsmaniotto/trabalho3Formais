/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import formais152.Modelo.Excecoes.ProducaoMalFormadaException;

/**
 * @author cesar, decker
 *
 */
public class GramaticaLivreContexto {

	private HashMap<SimboloGLC, List<ProducaoGLC>> producoes;
	private HashSet<SimboloGLC> simbolosTerminais;
	private SimboloGLC simboloInicial;

	public GramaticaLivreContexto() {
		producoes = new HashMap<>();
		simbolosTerminais = new HashSet<>();

	}

	public HashMap<SimboloGLC, List<ProducaoGLC>> getProducoes() {
		return producoes;
	}

	public Set<SimboloGLC> getNaoTerminais() {
		return producoes.keySet();
	}

	private boolean copyFirst(SimboloGLC dest, SimboloGLC b){
		List<SimboloGLC> firstB = b.obterFirst();
		
		boolean changed=false;
		for(int i=0; i<firstB.size(); i++){
			SimboloGLC g = firstB.get(i);
			if(!dest.temNoFirst(g)){
				changed=true;
				dest.adicionaFirst(g);
			}
		}
		return changed;
	}
	
	public void calcularFirst(){
		//producoes.size();
		boolean changed=false;
		HashMap<SimboloGLC,Boolean> hasNull= new HashMap<SimboloGLC, Boolean>();

		
		
		/**Zera o hasNull por causadessa frecura do java.
		 *  não aguento mais essa porra de linguagem
		 */
		for (Map.Entry<SimboloGLC, List<ProducaoGLC>> entry : producoes.entrySet()) {		
			SimboloGLC vn = entry.getKey();
			hasNull.put(vn, false);
		}
		/**     */
		
		
		//repetir até ninguem mais ter follow novo
		do{
			changed = false;
			// fazer pra cada linha da gramatica
			for (Map.Entry<SimboloGLC, List<ProducaoGLC>> entry : producoes.entrySet()) {
				
				SimboloGLC vn = entry.getKey();
				List<ProducaoGLC> vtList = entry.getValue();
				
				//repetir pra cada producao da gramatica
				for(int i=0; i<vtList.size() ; i++){
					
					List<SimboloGLC> vt = vtList.get(i).getSimbolos();
					
					
					if(vt.size()==0){
						System.out.println("Producao sem simbolos");
						continue;
					}
					//repetir pra cada simbolo da producao
					for(int j=0; j<vt.size(); j++){
						SimboloGLC prod= vt.get(j);
						
						if(prod.isTerminal()){
							if(!vn.temNoFirst(prod)){
								vn.adicionaFirst(prod);
								//marca essa produçao contem &
								if(prod.getFirst().equals("&")){
									hasNull.put(vn,true );
								}
								
								changed=true;
							}
							break;
						}else{
							
							//
							changed= changed || copyFirst(vn , prod) ;				
				
							if(hasNull.get(prod)){
								hasNull.put(vn, true);
							}else{
								break;
								//se Simbolo naoterminao não tem & então paramos de pegar first dos seguintes Simbolos
							}
					
						}
						
					}
				
				}
			}
	
		}while(changed);
		
		
	}

	private void followRec(SimboloGLC simbolo) {
		// n sei se vou usar isso
	}

	public void calculaFollow() {
		if (simboloInicial != null) {

			for (SimboloGLC simbolo : this.producoes.keySet()) {
				if (simbolo.getFirst().equals(simboloInicial)) {
					simbolo.adicionaFirst(new SimboloGLC("&", true));
				}
			}
		}
		/* Simbolo de final de pilha inserido no símbolo inicial da gramática */
		boolean mudanca = true;
		while (mudanca) {
			mudanca = false;
			/*
			 * primeiro para todos os NT, depois pra todos os T (Pq eu quero,
			 * não gostou me processa)
			 */
			for (SimboloGLC simboloAnalisando : producoes.keySet()) {
				/* Verifica cada producao de cada terminal */
				for (SimboloGLC cabecaOrigem : producoes.keySet()) {
					/* Para cada producao de cabecaOrigem */
					if (producoes.get(cabecaOrigem).isEmpty()) {
						continue;
					}
					for (ProducaoGLC producaoAnalisando : producoes.get(cabecaOrigem)) {
						if (!producaoAnalisando.contemSimbolo(simboloAnalisando.getFirst())) {
							continue;
						}
						for (int posAtual = 0; producaoAnalisando.getSimbolos()
								.size() > posAtual; posAtual += posAtual) {
							if (producaoAnalisando.getSimbolos().get(posAtual).getFirst()
									.equals(simboloAnalisando.getFirst())) {
								/*
								 * posAtual é a posicao do simbolo sendo
								 * analizado dentro da producao
								 */

								/*
								 * Primeiro caso: está no fim da produção e
								 * recebe o follow da cabeça
								 */
								if (posAtual + 1 == producaoAnalisando.getSimbolos().size()) {
									for (SimboloGLC simboloFollowCabeca : cabecaOrigem.obterFollow()) {
										if (!simboloAnalisando.temNoFollow(simboloFollowCabeca.getFirst())) {
											mudanca = true;
											simboloAnalisando.adicionaFollow(simboloFollowCabeca);
										}
									}
								} // Simbolo no fim da prod
								/*
								 * Segundo caso Está no meio da producao, e o
								 * próximo simbolo é um NT que nao vira &
								 */
								else if (!producaoAnalisando.getSimbolos().get(posAtual + 1).isTerminal() && (posAtual
										+ 1 != producaoAnalisando.getSimbolos().size()
										&& !producaoAnalisando.getSimbolos().get(posAtual + 1).temNoFirst("&"))) {
									if (producaoAnalisando.getSimbolos().get(posAtual + 1).obterFollow().isEmpty()) {
										/*
										 * O proximo simbolo ainda nao tem
										 * follow, entao que se foda
										 */
										continue;
									}
									for (SimboloGLC simboloFollowSeguinte : producaoAnalisando.getSimbolos()
											.get(posAtual + 1).obterFollow()) {
										if (!simboloAnalisando.temNoFollow(simboloFollowSeguinte)) {
											mudanca = true;
											simboloAnalisando.adicionaFollow(simboloFollowSeguinte);
										}
									}
								} // fim do caso de proximo NT sem &
								/*
								 * Caso 3 proximo simbolo é um terminal
								 */
								else if (producaoAnalisando.getSimbolos().get(posAtual + 1).isTerminal()) {
									if (!simboloAnalisando
											.temNoFollow(producaoAnalisando.getSimbolos().get(posAtual + 1))) {
										mudanca = true;
										simboloAnalisando
												.adicionaFollow(producaoAnalisando.getSimbolos().get(posAtual + 1));
									}
								} // fim do caso do proximo ser terminal

								/*
								 * Caso 4 o proximo é um NT que tem & no first
								 */
								else if (!producaoAnalisando.getSimbolos().get(posAtual + 1).isTerminal()
										&& producaoAnalisando.getSimbolos().get(posAtual + 1).temNoFirst("&")) {
									int posAtualProd = posAtual + 1;
									while (producaoAnalisando.getSimbolos().get(posAtualProd).temNoFirst("&")
											&& posAtualProd < producaoAnalisando.getSimbolos().size()) {
										for (SimboloGLC simboloFirstAnalisado : producaoAnalisando.getSimbolos()
												.get(posAtualProd).obterFirst()) {
											if (!simboloAnalisando.temNoFirst(simboloFirstAnalisado)) {
												mudanca = true;
											}
										}

									}
								}
							} else
								continue;
						}
					}
				}
			}
		}

	}

	public SimboloGLC getSimboloInicial() {
		return simboloInicial;
	}

	public void setSimboloInicial(SimboloGLC cabecaProducao) {
		simboloInicial = cabecaProducao;
	}

	public void adicionaProducao(SimboloGLC cabeca, ProducaoGLC corpo) {

		if (simbolosTerminais.contains(cabeca)) {
			throw new ProducaoMalFormadaException("Símbolo terminal definido como cabeça da produção");
		}

		for (SimboloGLC simbolo : corpo.getSimbolos()) {
			if (simbolo.isTerminal()) {
				simbolosTerminais.add(simbolo);
			}
		}

		if (!producoes.containsKey(cabeca)) {
			producoes.put(cabeca, new ArrayList<ProducaoGLC>());
		}
		producoes.get(cabeca).add(corpo);
	}

}
