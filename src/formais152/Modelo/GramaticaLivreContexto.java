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
		simboloInicial = null;

	}

	public HashMap<SimboloGLC, List<ProducaoGLC>> getProducoes() {
		return producoes;
	}

	public Set<SimboloGLC> getNaoTerminais() {
		return producoes.keySet();
	}

	private boolean copyFirst(SimboloGLC dest, SimboloGLC b) {
		List<SimboloGLC> firstB = b.obterFirst();

		boolean changed = false;
		for (int i = 0; i < firstB.size(); i++) {
			SimboloGLC g = firstB.get(i);
			if (!dest.temNoFirst(g)) {
				changed = true;
				dest.adicionaFirst(g);
			}
		}
		return changed;
	}

	public void calcularFirst() {
		// producoes.size();
		boolean changed = false;
		HashMap<SimboloGLC, Boolean> hasNull = new HashMap<SimboloGLC, Boolean>();

		/**
		 * Zera o hasNull por causadessa frecura do java. não aguento mais essa
		 * porra de linguagem
		 */
		for (Map.Entry<SimboloGLC, List<ProducaoGLC>> entry : producoes.entrySet()) {
			SimboloGLC vn = entry.getKey();
			hasNull.put(vn, false);
		}
		for(SimboloGLC g: simbolosTerminais){
			g.adicionaFirst(g);
			
		}
		/**     */

		// repetir até ninguem mais ter follow novo
		do {
			changed = false;
			// fazer pra cada linha da gramatica
			for (Map.Entry<SimboloGLC, List<ProducaoGLC>> entry : producoes.entrySet()) {

				SimboloGLC vn = entry.getKey();
				List<ProducaoGLC> vtList = entry.getValue();

				// repetir pra cada producao da gramatica
				for (int i = 0; i < vtList.size(); i++) {

					List<SimboloGLC> vt = vtList.get(i).getSimbolos();

					if (vt.size() == 0) {
						System.out.println("Producao sem simbolos");
						continue;
					}
					// repetir pra cada simbolo da producao
					for (int j = 0; j < vt.size(); j++) {
						SimboloGLC prod = vt.get(j);

						if (prod.isTerminal()) {
							if (!vn.temNoFirst(prod)) {
								vn.adicionaFirst(prod);
								// marca essa produçao contem &
								if (prod.getFirst().equals("&")) {
									hasNull.put(vn, true);
								}

								changed = true;
							}
							break;
						} else {

							//
							changed = changed || copyFirst(vn, prod);

							if (hasNull.get(prod)) {
								hasNull.put(vn, true);
							} else {
								break;
								// se Simbolo naoterminao não tem & então
								// paramos de pegar first dos seguintes Simbolos
							}

						}

					}

				}
			}

		} while (changed);

	}

	private void followRec(SimboloGLC simbolo) {
		// n sei se vou usar isso
	}

	private boolean existemProducoes() {
		return !producoes.isEmpty();
	}

	private List<ProducaoGLC> obterProducoesCabeca(SimboloGLC cabecaProducao) {
		return producoes.get(cabecaProducao);
	}

	private Set<SimboloGLC> obterCabecas() {
		return producoes.keySet();
	}

	private boolean cabecaSemProducao(SimboloGLC cabecaProducao) {
		return (producoes.get(cabecaProducao).isEmpty());
	}

	public void calculaFollow() {
		if (simboloInicial != null) {

			for (SimboloGLC simbolo : this.producoes.keySet()) {
				if (simbolo.getFirst().equals(simboloInicial.getFirst())) {
					simbolo.adicionaFollow(new SimboloGLC("$", true));
				}
			}
		} else {
			System.out.println("A gramática nao tem simbolo inicial, fodeu tudo.");
			return;
		}
		/* Simbolo de final de pilha inserido no símbolo inicial da gramática */
		boolean mudanca = true;
		while (mudanca) {
			mudanca = false;
			if (!existemProducoes()) {
				System.out.println("nao existem producoes");
				return;
			}
			for (SimboloGLC cabecaProducao : obterCabecas()) {
				if (cabecaSemProducao(cabecaProducao)) {
					System.out.println("Existe uma cabeca sem producao ->" + cabecaProducao);
					return;
				}
				for (ProducaoGLC producaoCabecaAnalisada : obterProducoesCabeca(cabecaProducao)) {
					if (producaoCabecaAnalisada.vazia()) {
						System.out.println("Existe uma producao vazia em " + cabecaProducao);
					}
					for (int posicao = producaoCabecaAnalisada.size() - 1; posicao >= 0; posicao--) {
						SimboloGLC simboloAnalisando = producaoCabecaAnalisada.obterSimbolo(posicao);

						/*
						 * Verifica se é a ultima posição, aí adiciona o follow
						 * do simbolo da cabeça.
						 */
						if (posicao == producaoCabecaAnalisada.size() - 1) {
							if (cabecaProducao.followVazio()) {
								continue;
							} else {
								for (SimboloGLC simboloNoFollowDaCabeca : cabecaProducao.obterFollow()) {
									if (!simboloAnalisando.temNoFollow(simboloNoFollowDaCabeca)) {
										mudanca = true;
										simboloAnalisando.adicionaFollow(simboloNoFollowDaCabeca);
									}
								}
							}
						} else {
							/* Caso nao seja a ultima producao */
							for (int posicaoProximos = posicao + 1; posicaoProximos < producaoCabecaAnalisada
									.size(); posicaoProximos++) {
								SimboloGLC vizinho = producaoCabecaAnalisada.obterSimbolo(posicaoProximos);
								if (vizinho.temNoFirst("&")) {
									/*
									 * Neste caso, o vizinho pode virar um & e
									 * xablau
									 */

									/* Follow (analisado) += first (vizinho) */
									for (SimboloGLC simboloFirstVizinho : vizinho.obterFirst()) {
										if (!simboloAnalisando.temNoFollow(simboloFirstVizinho)
												&& !simboloFirstVizinho.getFirst().equals("&")) {
											mudanca = true;
											simboloAnalisando.adicionaFollow(simboloFirstVizinho);
										}
									}
									if(posicaoProximos + 1 == producaoCabecaAnalisada.size()){
										for(SimboloGLC simboloNaCabeca : cabecaProducao.obterFollow()){
											if(!simboloAnalisando.temNoFollow(simboloNaCabeca)){
												mudanca = true; 
												simboloAnalisando.adicionaFollow(simboloNaCabeca);
											}
											
										}
									}
									continue;

								} else {
									/*
									 * Caso nao tenha & no first. Entao add o
									 * first e quebra o for
									 */
									/* Follow (analisado) += first (vizinho) */
									for (SimboloGLC simboloFirstVizinho : vizinho.obterFirst()) {
										if (!simboloAnalisando.temNoFollow(simboloFirstVizinho)
												&& !simboloFirstVizinho.getFirst().equals("&")) {
											mudanca = true;
											simboloAnalisando.adicionaFollow(simboloFirstVizinho);
										}
									}
									break;
								}

							}
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
