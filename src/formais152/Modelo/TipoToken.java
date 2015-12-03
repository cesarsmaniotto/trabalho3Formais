/**
 * 
 */
package formais152.Modelo;

/**
 * @author cesar
 *
 */
public enum TipoToken {

	CONSTANTE_NUMERICA("CTENUM"), STRING("STRING"), PALAVRA_RESERVADA("PR"), OPERADOR("OP"), IDENTIFICADOR(
			"ID"), SEPARADOR("SEP"), MARGEM("MG"), ESPACO_EM_BRANCO("BRANCO"), ERRO("ERRO"), FIM_DE_PILHA(
					"FIM_DE_PILHA"), TIPO("TIPO"), FOR("FOR"), WHILE("WHILE"), IF("IF"), ELSE("ELSE"), STARTB(
							"{"), ENDB("}"), STARTP("("), ENDP(")"), RETURN("RETURN");

	private String token;

	private TipoToken(String token) {
		this.token = token;
	}

	public String getTipo() {
		return token;
	}

}
