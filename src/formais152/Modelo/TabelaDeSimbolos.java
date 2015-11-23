package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelaDeSimbolos {

	private Map<String, Token> palavrasReservadas;
	private List<Pair<String, Token>> tokens;

	public TabelaDeSimbolos(List<String> palavrasReservadas) {

		this.palavrasReservadas = new HashMap<>();
		adicionaPalavrasReservadas(palavrasReservadas);
		tokens = new ArrayList<>();
	}

	private void adicionaPalavrasReservadas(List<String> palavrasReservadas) {

		for (String pr : palavrasReservadas) {
			this.palavrasReservadas.put(pr, Token.PALAVRA_RESERVADA);
		}

	}

	public void adicionaItem(String lexema, Token token) {

		if (palavrasReservadas.containsKey(lexema)) {
			token = palavrasReservadas.get(lexema);
		}

		tokens.add(new Pair<String, Token>(lexema, token));
	}

	public List<Pair<String, Token>> getTokens() {
		return tokens;
	}

}
