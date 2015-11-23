package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelaDeSimbolos {

	private Map<String, TipoToken> palavrasReservadas;
	private List<Token> tokens;

	public TabelaDeSimbolos(List<String> palavrasReservadas) {

		this.palavrasReservadas = new HashMap<>();
		adicionaPalavrasReservadas(palavrasReservadas);
		tokens = new ArrayList<>();
	}

	private void adicionaPalavrasReservadas(List<String> palavrasReservadas) {

		for (String pr : palavrasReservadas) {
			this.palavrasReservadas.put(pr, TipoToken.PALAVRA_RESERVADA);
		}

	}

	public void adicionaItem(String lexema, TipoToken token) {

		if (palavrasReservadas.containsKey(lexema)) {
			token = palavrasReservadas.get(lexema);
		}

		tokens.add(new Token(lexema, token));
	}

	public List<Token> getTokens() {
		return tokens;
	}

}
