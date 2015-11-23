/**
 * 
 */
package formais152.Modelo;

/**
 * @author cesar
 *
 */
public enum Token {

	CONSTANTE_NUMERICA("CTENUM"), STRING("STRING"), PALAVRA_RESERVADA("PR"), OPERADOR("OP"), IDENTIFICADOR(
			"ID"), SEPARADOR("SEP"), MARGEM("MG"), ESPACO_EM_BRANCO("BRANCO"), ERRO("ERRO");

	private String token;

	private Token(String token) {
		this.token = token;
	}

}
