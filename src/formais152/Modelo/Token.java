/**
 * 
 */
package formais152.Modelo;

/**
 * @author cesar
 *
 */
public class Token extends Pair<String, TipoToken>{
	
	/**
	 * 
	 */
	public Token(String lexema, TipoToken tipoToken) {
		super(lexema, tipoToken);
	}
	
	public String getLexema(){
		return getFirst();
	}
	
	public TipoToken getTipoToken(){
		return getSecond();
	}

}
