/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

	public void calculaFirst() {

		/* O first de um terminal sempre é ele mesmo */
		for (SimboloGLC simboloTerminal : simbolosTerminais) {
			simboloTerminal.adicionaFirst(simboloTerminal);
		}

		for (SimboloGLC cabecaProd : producoes.keySet()) {
			List<ProducaoGLC> prods = producoes.get(cabecaProd);
			for (ProducaoGLC prod : prods) {
				SimboloGLC simbolo = prod.getSimbolos().get(0);
				if (simbolo.isTerminal()) {
					cabecaProd.adicionaFirst(simbolo);
				}
			}

		} // Adiciona os casos tipo (t)NT (já comeca com terminal)
		/*
		 * Neste caso, tratamos o & como um terminal qualquer. A semântica de
		 * inserção do mesmo é semelhante a de um não-terminal
		 */

		// "Até que nao tenha mudancas"
		// ====================================================================================

		boolean mudanca = true;
		while (mudanca) {
			mudanca = false;
			Set<SimboloGLC> cabecasProd = producoes.keySet();
			if (cabecasProd.size() < 1) {
				System.out.println("[WARNING] Calculou First pra gramatica sem cabeca");
				break;
			}
			for (SimboloGLC cabecaProducaoAnalisada : cabecasProd) {
				List<ProducaoGLC> producoesCabecaAnalisada = producoes.get(cabecaProducaoAnalisada);
				if (producoesCabecaAnalisada.isEmpty()) {
					System.out.println("[WARNING] existe uma cabeca de producao sem producoes");
					continue;
				}
				for (ProducaoGLC producaoCabecaAnalisada : producoesCabecaAnalisada) {
					List<SimboloGLC> simbolosProducaoCabecaAnalisada = producaoCabecaAnalisada.getSimbolos();
					if (simbolosProducaoCabecaAnalisada.isEmpty()) {
						System.out.println("[WARNING]Existe uma produção vazia!11!!");
						continue;
					}
					for (SimboloGLC simboloProducaoCabecaAnalisada : simbolosProducaoCabecaAnalisada) {

						if (simboloProducaoCabecaAnalisada.isTerminal()) {
							if (!cabecaProducaoAnalisada.temNoFirst(simboloProducaoCabecaAnalisada)) {
								mudanca = true;
								cabecaProducaoAnalisada.adicionaFirst(simboloProducaoCabecaAnalisada);
							}

						} else /* n terminal */ {

							if (simboloProducaoCabecaAnalisada.temNoFirst("&")) {
								/* pode gerar &, entao tem que ir ate o final */
								int posSimbolo = 0;
								for (int posicao = 0; posicao < producaoCabecaAnalisada.getSimbolos()
										.size(); posicao++) {
									SimboloGLC simboloProdAtual = producaoCabecaAnalisada.getSimbolos().get(posicao);
									if (simboloProdAtual.getFirst().equals(simboloProducaoCabecaAnalisada.getFirst())) {
										posSimbolo = posicao;

									}
								}
								for (int posicao = posSimbolo; posicao < producaoCabecaAnalisada.getSimbolos()
										.size(); posicao++) {

									System.out.println("[DEBUG] producaoCabecaAnalisada tem "
											+ producaoCabecaAnalisada.getSimbolos().size());
									System.out.println("[DEBUG] posicao é " + posicao);
									SimboloGLC simboloAtual = producaoCabecaAnalisada.getSimbolos().get(posicao);
									if (simboloAtual.obterFirst().isEmpty()) {
										break;
									}
									if (simboloAtual.isTerminal()) {
										if (!cabecaProducaoAnalisada.temNoFirst(simboloAtual.getFirst())) {
											mudanca = true;
											cabecaProducaoAnalisada.adicionaFirst(simboloAtual);
										}
									} else {
										if (simboloAtual.temNoFirst("&")) {
											for (SimboloGLC simboloFirstsimboloAnalisado : simboloAtual.obterFirst()) {
												if (!cabecaProducaoAnalisada.temNoFirst(simboloFirstsimboloAnalisado)) {
													mudanca = true;
													cabecaProducaoAnalisada.adicionaFirst(simboloFirstsimboloAnalisado);
												}
											}
											continue;
										} else {
											for (SimboloGLC simboloFirstsimboloAnalisado : simboloAtual.obterFirst()) {
												if (!cabecaProducaoAnalisada.temNoFirst(simboloFirstsimboloAnalisado)) {
													mudanca = true;
													cabecaProducaoAnalisada.adicionaFirst(simboloFirstsimboloAnalisado);
												}
											}
											break;
										}
									}

								}
							} else/* NT sem & */ {
								for (SimboloGLC simboloFirstSimboloAnalisado : simboloProducaoCabecaAnalisada
										.obterFirst()) {
									if (!cabecaProducaoAnalisada.temNoFirst(simboloFirstSimboloAnalisado)) {
										mudanca = true;
										cabecaProducaoAnalisada.adicionaFirst(simboloFirstSimboloAnalisado);
									}
								}
							}
						}
					}

				}

			}

		} // Enquanto houver mudancas

		/*
		 * Agora, para cada cabeca, verifica se tem uma producao que vai para &
		 * ou uma cadeia de nt que vai para &
		 */

		for (SimboloGLC cabecaProducaoAnalisado : producoes.keySet()) {
			boolean temEpsilon = false;
			for (ProducaoGLC producaoCabecaAnalisada : producoes.get(cabecaProducaoAnalisado)) {

				if (producaoCabecaAnalisada.getSimbolos().size() == 1) {
					if (producaoCabecaAnalisada.getSimbolos().get(0).getFirst().equals("&")) {
						temEpsilon = true;
					}
				}

				if (producaoCabecaAnalisada.soNaoTeminais()) {
					boolean soEpsilon = true;
					for (int posicao = 0; posicao < producaoCabecaAnalisada.getSimbolos().size(); posicao++) {
						boolean simboloTemEpsilon = false;
						SimboloGLC naoTerminalAnalisado = producaoCabecaAnalisada.getSimbolos().get(posicao);

						/* Para cada producao do simbolo da prod */
						for (ProducaoGLC producaoNaoTerminalAnalisdado : producoes.get(naoTerminalAnalisado)) {
							if (producaoNaoTerminalAnalisdado.getSimbolos().get(0).getFirst().equals("&")) {
								simboloTemEpsilon = true;
							}
						}
						if (!simboloTemEpsilon) {
							soEpsilon = false;
						}

					}
					if (soEpsilon) {
						temEpsilon = true;
					}
				}
			}
			if (!temEpsilon) {
				cabecaProducaoAnalisado.removeFirst(new SimboloGLC("&", true));
			}
		}

		/*
		 * } mudanca = false; if (producoes.isEmpty()) { return; } // primeiro
		 * caso: S-> ABC tal que A,B e C geram &; for (SimboloGLC cabecaProd :
		 * producoes.keySet()) { if (producoes.get(cabecaProd).isEmpty()) {
		 * continue; } for (ProducaoGLC producao : producoes.get(cabecaProd)) {
		 * if (!producao.soNaoTeminais()) { continue; } int firstAntigo =
		 * cabecaProd.obterFirst().size();
		 * 
		 * boolean todoEpsilon = true; // Verifica se todos os símbolos na
		 * produção tem & no first for (SimboloGLC simbol :
		 * producao.getSimbolos()) { if (simbol.obterFirst().isEmpty()) { //
		 * Caso não tenha first continue; } boolean temEpsilon = false; // Para
		 * cada simbolo no first, ve se tem & for (SimboloGLC
		 * simboloFirstAnalisado : simbol.obterFirst()) { if
		 * (simboloFirstAnalisado.getFirst().equals("&")) { temEpsilon = true; }
		 * } if (!temEpsilon) { todoEpsilon = false;
		 * 
		 * }
		 * 
		 * } // Aqui, todosEpsilon é true se todos os firsts dos // simbolos da
		 * producao da cabeça tiverem & if (!todoEpsilon) { // Essa produção não
		 * entra no caso analizado continue; } else { // Essa producao entrou no
		 * caso analizado boolean jaTemEspsilon = false; for (SimboloGLC
		 * simboloFirstCabeca : cabecaProd.obterFirst()) { if
		 * (simboloFirstCabeca.getFirst().equals("&")) { jaTemEspsilon = true; }
		 * } if (!jaTemEspsilon) { cabecaProd.adicionaFirst(new SimboloGLC("&",
		 * true)); mudanca = true; } }
		 * 
		 * } } // Para cada cabeça de producao - > fim dos casos ABC com ABC //
		 * gerando &
		 * 
		 * // =================================================
		 * =================================== // PRIMEIRO CASO OK! //
		 * =================================================
		 * ===================================
		 * 
		 * // Inicio do caso A->ABaX, em que A e B geram & for (SimboloGLC
		 * cabecaProd : producoes.keySet()) { if
		 * (producoes.get(cabecaProd).isEmpty()) { continue; } for (ProducaoGLC
		 * producao : producoes.get(cabecaProd)) { if
		 * (producao.getSimbolos().isEmpty()) { continue; } if
		 * (producao.soNaoTeminais()) { break; } for (Iterator<SimboloGLC> iProd
		 * = producao.getSimbolos().iterator(); iProd.hasNext();) { SimboloGLC
		 * simboloProducao = iProd.next(); if (simboloProducao.isTerminal()) {
		 * // Neste caso, o simbolo é terminal e deve ser // inserido no first
		 * da cabeça boolean contemSimboloTerminalAnalizado = false; if
		 * (!cabecaProd.obterFirst().isEmpty()) { for (SimboloGLC
		 * simboloFirstCabeca : cabecaProd.obterFirst()) { if
		 * (simboloFirstCabeca.getFirst().equals( simboloProducao.getFirst())) {
		 * contemSimboloTerminalAnalizado = true; } } if
		 * (!contemSimboloTerminalAnalizado) { mudanca = true;
		 * cabecaProd.adicionaFirst(simboloProducao); }
		 * 
		 * } } else { // Aqui, o simbolo da producao analizado não é um //
		 * terminal
		 * 
		 * if (simboloProducao.obterFirst().isEmpty()) { continue; } boolean
		 * epsilonNoFirst = false; for (SimboloGLC simboloFirstProducao :
		 * simboloProducao.obterFirst()) { if
		 * (simboloFirstProducao.getFirst().equals("&")) { epsilonNoFirst =
		 * true; } } if (!epsilonNoFirst) { // Neste caso, é um nao terminal que
		 * nao tem // & no first. Entao, tira o & do first da // cabeca e une
		 * com o first desse simbolo for (SimboloGLC simboloFirstProducao :
		 * simboloProducao.obterFirst()) { if
		 * (!cabecaProd.obterFirst().contains( simboloFirstProducao)) {
		 * cabecaProd.adicionaFirst(simboloFirstProducao); mudanca = true; } }
		 * SimboloGLC epsilonCabeca = null; for (SimboloGLC simboloFirstCabeca :
		 * cabecaProd.obterFirst()) { if
		 * (simboloFirstCabeca.getFirst().equals("&")) { epsilonCabeca =
		 * simboloFirstCabeca; } } if (epsilonCabeca != null) {
		 * cabecaProd.obterFirst().remove(epsilonCabeca); } } // fim caso NT sem
		 * &
		 * 
		 * } } } } }
		 */

		// O FIRST ta pronto. os casos 2 e 3 tao misturados
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
				if (simbolo.getFirst().equals(simboloInicial)) {
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
					for (int posicao = producaoCabecaAnalisada.size() - 1; posicao > 0; posicao--) {
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
						}
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
								continue;

							} else {
								/*
								 * Caso nao tenha & no first. Entao add o first
								 * e quebra o for
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
