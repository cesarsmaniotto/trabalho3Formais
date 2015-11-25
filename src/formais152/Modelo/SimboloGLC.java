/**
 * 
 */
package formais152.Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cesar
 *
 */
public class SimboloGLC extends Pair<String, Boolean> {

	private List<SimboloGLC> first, follow;

	public SimboloGLC(String simbolos, Boolean isTerminal) {
		super(simbolos, isTerminal);
		first = new ArrayList<>();
		follow = new ArrayList<>();
	}

	public void adicionaFirst(SimboloGLC simbolo) {
		this.first.add(simbolo);
	}

	public List<SimboloGLC> obterFirst() {
		return this.first;
	}

	public String getSimbolos() {
		return super.getFirst();
	}

	public boolean isTerminal() {
		return super.getSecond();
	}

}
