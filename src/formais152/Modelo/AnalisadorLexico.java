/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cesar
 *
 */
public class AnalisadorLexico {

	private Automato automato;
	private TabelaDeSimbolos tabela;

	public AnalisadorLexico(TabelaDeSimbolos tabela) {
		this.tabela = tabela;
		this.automato = FabricaDeAutomatos.montaAutomato();

	}

	public void montaTabelaDeSimbolos(String programa) {

		int apontadorInicioLexema = 0, apontadorProximo = 0, limiteLexema = 0;
		String simboloAtual = "";
		boolean voltaAoEstadoInicial = false, fimDoPrograma = false;
		Estado estadoAtual = automato.getEstadoInicial();

		while (!fimDoPrograma) {

			if (voltaAoEstadoInicial) {
				estadoAtual = automato.getEstadoInicial();
			}

			if (programa.length() == apontadorProximo) {
				fimDoPrograma = true;
				limiteLexema = apontadorProximo;
			} else {
				apontadorProximo += 1;
				simboloAtual = programa.substring(apontadorProximo - 1,
						apontadorProximo);
				limiteLexema = apontadorProximo - 1;
			}

			if (StringUtil.ehNuloOuEspacoEmBranco(simboloAtual)
					|| fimDoPrograma) {

				String lexema = programa.substring(apontadorInicioLexema,
						limiteLexema);

				if (estadoAtual.isTerminal()) {
					tabela.adicionaItem(lexema, estadoAtual.getTipoToken());
				} else {
					tabela.adicionaItem(lexema, Token.ERRO);
				}

				voltaAoEstadoInicial = true;
				apontadorInicioLexema = apontadorProximo;
			} else {
				voltaAoEstadoInicial = false;
				estadoAtual = estadoAtual.getTransicao(simboloAtual);
			}

		}
	}

}
