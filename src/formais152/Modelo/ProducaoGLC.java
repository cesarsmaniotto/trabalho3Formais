package formais152.Modelo;

import formais152.Modelo.SimboloGLC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ProducaoGLC {

	private List<SimboloGLC> simbolos;

	public ProducaoGLC() {
		simbolos = new ArrayList<>();
	}

	public void adicionaSimbolo(SimboloGLC simbolo) {
		simbolos.add(simbolo);
	}

	public List<SimboloGLC> getSimbolos() {
		return simbolos;
	}

	public String toString() {
		// TODO Auto-generated method stub
		String saida = "";
		for (Iterator<SimboloGLC> i = simbolos.iterator(); i.hasNext();) {
			saida += i.next().getFirst();
		}
		return saida;
	}
	
	

	public boolean contemSimbolo(String simbolo) {
		for (SimboloGLC simb : simbolos) {
			if (simb.getFirst().equals(simbolo)) {
				return true;
			}
		}
		return false;
	}

	public boolean soNaoTeminais() {
		for (SimboloGLC simbolo : this.simbolos) {
			if (simbolo.isTerminal()) {
				return false;
			}
		}
		return true;
	}

	public List<SimboloGLC> obterInverso() {
		List<SimboloGLC> retorno = new ArrayList<>();
		if (simbolos.isEmpty()) {
			System.out.println("[WARNING] INVERSO DE PRODUCAO VAZIA!");
			return new ArrayList<>();
		}
		for (int i = simbolos.size() - 1; i >= 0; i++) {
			retorno.add(simbolos.get(i));
		}
		return retorno;
	}

}
