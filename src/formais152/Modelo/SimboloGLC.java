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

	public void removeFirst(SimboloGLC simbolo) {
		SimboloGLC simboloRemocao = null;
		for (SimboloGLC simboloFirst : first) {
			if (simboloFirst.getFirst().equals(simbolo.getFirst())) {
				simboloRemocao = simboloFirst;
			}

		}
		if (simboloRemocao != null) {
			first.remove(simboloRemocao);
		}
	}

	public void adicionaFirst(SimboloGLC simbolo) {
		this.first.add(simbolo);
	}

	public void adicionaFollow(SimboloGLC simbolo) {
		this.follow.add(simbolo);
	}

	public List<SimboloGLC> obterFollow() {
		return this.follow;
	}

	public boolean temNoFirst(String simbolo) {
		if (first.isEmpty())
			return false;
		for (SimboloGLC simboloFollow : first) {
			if (simboloFollow.getFirst().equals(simbolo)) {
				return true;
			}
		}
		return false;
	}

	public boolean temNoFirst(SimboloGLC simbolo) {
		if (first.isEmpty())
			return false;
		for (SimboloGLC simboloFollow : first) {
			if (simboloFollow.getFirst().equals(simbolo.getFirst())) {
				return true;
			}
		}
		return false;
	}

	public boolean temNoFollow(String simbolo) {
		if (follow.isEmpty())
			return false;
		for (SimboloGLC simboloFollow : follow) {
			if (simboloFollow.getFirst().equals(simbolo)) {
				return true;
			}
		}
		return false;
	}

	public boolean temNoFollow(SimboloGLC simbolo) {
		if (follow.isEmpty())
			return false;
		for (SimboloGLC simboloFollow : follow) {
			if (simboloFollow.getFirst().equals(simbolo.getFirst())) {
				return true;
			}
		}
		return false;
	}

	public List<SimboloGLC> obterFirst() {
		return this.first;
	}

	public String getSimbolo() {
		return super.getFirst();
	}

	public boolean isTerminal() {
		return super.getSecond();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see formais152.Modelo.Pair#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getSimbolo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see formais152.Modelo.Pair#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {

		if ((other instanceof SimboloGLC) && ((SimboloGLC) other).getSimbolo().equals(this.getSimbolo())

				&& ((SimboloGLC) other).isTerminal() == this.isTerminal()) {
			return true;
		}

		return false;

	}

}
