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

	public void calculaFirst() {

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
			if (producoes.isEmpty()) {
				return;
			}
			// primeiro caso: S-> ABC tal que A,B e C geram &;
			for (SimboloGLC cabecaProd : producoes.keySet()) {
				if (producoes.get(cabecaProd).isEmpty()) {
					continue;
				}
				for (ProducaoGLC producao : producoes.get(cabecaProd)) {
					if (!producao.soNaoTeminais()) {
						continue;
					}
					int firstAntigo = cabecaProd.obterFirst().size();

					boolean todoEpsilon = true;
					// Verifica se todos os símbolos na produção tem & no first
					for (SimboloGLC simbol : producao.getSimbolos()) {
						if (simbol.obterFirst().isEmpty()) {
							// Caso não tenha first
							continue;
						}
						boolean temEpsilon = false;
						// Para cada simbolo no first, ve se tem &
						for (SimboloGLC simboloFirstAnalisado : simbol
								.obterFirst()) {
							if (simboloFirstAnalisado.getFirst().equals("&")) {
								temEpsilon = true;
							}
						}
						if (!temEpsilon) {
							todoEpsilon = false;

						}

					} // Aqui, todosEpsilon é true se todos os firsts dos
						// simbolos da producao da cabeça tiverem &
					if (!todoEpsilon) {
						// Essa produção não entra no caso analizado
						continue;
					} else {
						// Essa producao entrou no caso analizado
						boolean jaTemEspsilon = false;
						for (SimboloGLC simboloFirstCabeca : cabecaProd
								.obterFirst()) {
							if (simboloFirstCabeca.getFirst().equals("&")) {
								jaTemEspsilon = true;
							}
						}
						if (!jaTemEspsilon) {
							cabecaProd.adicionaFirst(new SimboloGLC("&", true));
							mudanca = true;
						}
					}

				}
			} // Para cada cabeça de producao - > fim dos casos ABC com ABC
				// gerando &

			// ====================================================================================
			// PRIMEIRO CASO OK!
			// ====================================================================================

			// Inicio do caso A->ABaX, em que A e B geram &
			for (SimboloGLC cabecaProd : producoes.keySet()) {
				if (producoes.get(cabecaProd).isEmpty()) {
					continue;
				}
				for (ProducaoGLC producao : producoes.get(cabecaProd)) {
					if (producao.getSimbolos().isEmpty()) {
						continue;
					}
					if (producao.soNaoTeminais()) {
						break;
					}
					for (Iterator<SimboloGLC> iProd = producao.getSimbolos()
							.iterator(); iProd.hasNext();) {
						SimboloGLC simboloProducao = iProd.next();
						if (simboloProducao.isTerminal()) {
							// Neste caso, o simbolo é terminal e deve ser
							// inserido no first da cabeça
							boolean contemSimboloTerminalAnalizado = false;
							if (!cabecaProd.obterFirst().isEmpty()) {
								for (SimboloGLC simboloFirstCabeca : cabecaProd
										.obterFirst()) {
									if (simboloFirstCabeca.getFirst().equals(
											simboloProducao.getFirst())) {
										contemSimboloTerminalAnalizado = true;
									}
								}
								if (!contemSimboloTerminalAnalizado) {
									mudanca = true;
									cabecaProd.adicionaFirst(simboloProducao);
								}

							}
						} else {
							// Aqui, o simbolo da producao analizado não é um
							// terminal

							if (simboloProducao.obterFirst().isEmpty()) {
								continue;
							}
							boolean epsilonNoFirst = false;
							for (SimboloGLC simboloFirstProducao : simboloProducao
									.obterFirst()) {
								if (simboloFirstProducao.getFirst().equals("&")) {
									epsilonNoFirst = true;
								}
							}
							if (!epsilonNoFirst) {
								// Neste caso, é um nao terminal que nao tem
								// & no first. Entao, tira o & do first da
								// cabeca e une com o first desse simbolo
								for (SimboloGLC simboloFirstProducao : simboloProducao
										.obterFirst()) {
									if (!cabecaProd.obterFirst().contains(
											simboloFirstProducao)) {
										cabecaProd
												.adicionaFirst(simboloFirstProducao);
										mudanca = true;
									}
								}
								SimboloGLC epsilonCabeca = null;
								for (SimboloGLC simboloFirstCabeca : cabecaProd
										.obterFirst()) {
									if (simboloFirstCabeca.getFirst().equals(
											"&")) {
										epsilonCabeca = simboloFirstCabeca;
									}
								}
								if (epsilonCabeca != null) {
									cabecaProd.obterFirst().remove(
											epsilonCabeca);
								}
							} // fim caso NT sem &

						}
					}
				}
			}

		} // Enquanto houver mudancas
			// O FIRST ta pronto. os casos 2 e 3 tao misturados
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
					for (ProducaoGLC producaoAnalisando : producoes
							.get(cabecaOrigem)) {
						if (!producaoAnalisando.contemSimbolo(simboloAnalisando
								.getFirst())) {
							continue;
						}
						for (int posAtual = 0; producaoAnalisando.getSimbolos()
								.size() > posAtual; posAtual += posAtual) {
							if (producaoAnalisando.getSimbolos().get(posAtual)
									.getFirst()
									.equals(simboloAnalisando.getFirst())) {
								/*
								 * posAtual é a posicao do simbolo sendo
								 * analizado dentro da producao
								 */

								/*
								 * Primeiro caso: está no fim da produção e
								 * recebe o follow da cabeça
								 */
								if (posAtual + 1 == producaoAnalisando
										.getSimbolos().size()) {
									for (SimboloGLC simboloFollowCabeca : cabecaOrigem
											.obterFollow()) {
										if (!simboloAnalisando
												.temNoFollow(simboloFollowCabeca
														.getFirst())) {
											mudanca = true;
											simboloAnalisando
													.adicionaFollow(simboloFollowCabeca);
										}
									}
								} // Simbolo no fim da prod
								/*
								 * Segundo caso Está no meio da producao, e o
								 * próximo simbolo é um NT que nao vira &
								 */
								else if (!producaoAnalisando.getSimbolos()
										.get(posAtual + 1).isTerminal()
										&& (posAtual + 1 != producaoAnalisando
												.getSimbolos().size() && !producaoAnalisando
												.getSimbolos()
												.get(posAtual + 1)
												.temNoFirst("&"))) {
									if (producaoAnalisando.getSimbolos()
											.get(posAtual + 1).obterFollow()
											.isEmpty()) {
										/*
										 * O proximo simbolo ainda nao tem
										 * follow, entao que se foda
										 */
										continue;
									}
									for (SimboloGLC simboloFollowSeguinte : producaoAnalisando
											.getSimbolos().get(posAtual + 1)
											.obterFollow()) {
										if (!simboloAnalisando
												.temNoFollow(simboloFollowSeguinte)) {
											mudanca = true;
											simboloAnalisando
													.adicionaFollow(simboloFollowSeguinte);
										}
									}
								} // fim do caso de proximo NT sem &
								/*
								 * Caso 3 proximo simbolo é um terminal
								 */
								else if (producaoAnalisando.getSimbolos()
										.get(posAtual + 1).isTerminal()) {
									if (!simboloAnalisando
											.temNoFollow(producaoAnalisando
													.getSimbolos().get(
															posAtual + 1))) {
										mudanca = true;
										simboloAnalisando
												.adicionaFollow(producaoAnalisando
														.getSimbolos().get(
																posAtual + 1));
									}
								} // fim do caso do proximo ser terminal

								/*
								 * Caso 4 o proximo é um NT que tem & no first
								 */
								else if (!producaoAnalisando.getSimbolos()
										.get(posAtual + 1).isTerminal()
										&& producaoAnalisando.getSimbolos()
												.get(posAtual + 1)
												.temNoFirst("&")) {
									int posAtualProd = posAtual + 1;
									while (producaoAnalisando.getSimbolos()
											.get(posAtualProd).temNoFirst("&")
											&& posAtualProd < producaoAnalisando
													.getSimbolos().size()) {
										for (SimboloGLC simboloFirstAnalisado : producaoAnalisando
												.getSimbolos()
												.get(posAtualProd).obterFirst()) {
											if (!simboloAnalisando
													.temNoFirst(simboloFirstAnalisado)) {
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
			throw new ProducaoMalFormadaException(
					"Símbolo terminal definido como cabeça da produção");
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
