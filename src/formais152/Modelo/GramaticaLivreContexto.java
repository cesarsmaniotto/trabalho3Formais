/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import formais152.Modelo.Excecoes.ProducaoMalFormadaException;

/**
 * @author cesar, decker
 *
 */
public class GramaticaLivreContexto {

	private HashMap<SimboloGLC, List<ProducaoGLC>> producoes;
	private HashSet<SimboloGLC> simbolosTerminais;
	private String simboloInicial;

	public GramaticaLivreContexto() {
		producoes = new HashMap<>();
		simbolosTerminais = new HashSet<>();

	}

	private void calculaFirst() {

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
						for (SimboloGLC simboloFirstAnalisado : simbol.obterFirst()) {
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
						for (SimboloGLC simboloFirstCabeca : cabecaProd.obterFirst()) {
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
					for (Iterator<SimboloGLC> iProd = producao.getSimbolos().iterator(); iProd.hasNext();) {
						SimboloGLC simboloProducao = iProd.next();
						if (simboloProducao.isTerminal()) {
							// Neste caso, o simbolo é terminal e deve ser
							// inserido no first da cabeça
							boolean contemSimboloTerminalAnalizado = false;
							if (!cabecaProd.obterFirst().isEmpty()) {
								for (SimboloGLC simboloFirstCabeca : cabecaProd.obterFirst()) {
									if (simboloFirstCabeca.getFirst().equals(simboloProducao.getFirst())) {
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
							for (SimboloGLC simboloFirstProducao : simboloProducao.obterFirst()) {
								if (simboloFirstProducao.getFirst().equals("&")) {
									epsilonNoFirst = true;
								}
							}
							if (!epsilonNoFirst) {
								// Neste caso, é um nao terminal que nao tem
								// & no first. Entao, tira o & do first da
								// cabeca e une com o first desse simbolo
								for (SimboloGLC simboloFirstProducao : simboloProducao.obterFirst()) {
									if (!cabecaProd.obterFirst().contains(simboloFirstProducao)) {
										cabecaProd.adicionaFirst(simboloFirstProducao);
										mudanca = true;
									}
								}
								SimboloGLC epsilonCabeca = null;
								for (SimboloGLC simboloFirstCabeca : cabecaProd.obterFirst()) {
									if (simboloFirstCabeca.getFirst().equals("&")) {
										epsilonCabeca = simboloFirstCabeca;
									}
								}
								if (epsilonCabeca != null) {
									cabecaProd.obterFirst().remove(epsilonCabeca);
								}
							} // fim caso NT sem &

						}
					}
				}
			}

		} // Enquanto houver mudancas
	}

	public String getSimboloInicial() {
		return simboloInicial;
	}

	public void setSimboloInicial(String cabecaProducao) {
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

		if (producoes.containsKey(cabeca)) {
			producoes.get(cabeca).add(corpo);
		} else {

		}

	}

}
