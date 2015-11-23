/**
 * 
 */
package formais152.Modelo;

/**
 * @author cesar
 *
 */
public class SimboloGLC extends Pair<String, Boolean>{

	
	public SimboloGLC(String simbolos, Boolean isTerminal) {
		super(simbolos, isTerminal);
		
	}
	
	public String getSimbolos(){
		return super.getFirst();
	}
	
	public boolean isTerminal(){
		return super.getSecond();
	}

}
