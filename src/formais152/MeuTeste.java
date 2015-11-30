/**
 * 
 */
package formais152;

import java.util.ArrayList;

import formais152.Modelo.GramaticaLivreContexto;
import formais152.Modelo.ProducaoGLC;
import formais152.Modelo.SimboloGLC;

/**
 * @author cesar
 *
 */
public class MeuTeste {

	public static void main(String[] args) {

		GramaticaLivreContexto gramatica = new GramaticaLivreContexto();

		SimboloGLC S = new SimboloGLC("S", false);
		SimboloGLC A = new SimboloGLC("A", false);
		SimboloGLC B = new SimboloGLC("B", false);
		SimboloGLC C = new SimboloGLC("C", false);
		SimboloGLC D = new SimboloGLC("D", false);
		SimboloGLC E = new SimboloGLC("E", false);

		
		gramatica.setSimboloInicial(S);
		SimboloGLC a = new SimboloGLC("a", true);
		SimboloGLC b = new SimboloGLC("b", true);
		SimboloGLC c = new SimboloGLC("c", true);
		SimboloGLC d = new SimboloGLC("d", true);
		SimboloGLC e = new SimboloGLC("e", true);
		SimboloGLC epsilon = new SimboloGLC("&", true);

		
		
		ProducaoGLC primeira = new ProducaoGLC();
		primeira.adicionarSimbolo(A);
		primeira.adicionarSimbolo(B);
		gramatica.adicionaProducao(S, primeira);
		
		ProducaoGLC segunda = new ProducaoGLC();
		segunda.adicionarSimbolo(A);
		segunda.adicionarSimbolo(b);
		gramatica.adicionaProducao(S, segunda);
		
		ProducaoGLC terceira = new ProducaoGLC();
		terceira.adicionarSimbolo(a);
		terceira.adicionarSimbolo(B);
		gramatica.adicionaProducao(S, terceira);
		
		primeira  = new ProducaoGLC();
		primeira.adicionarSimbolo(a);
		primeira.adicionarSimbolo(A);
		gramatica.adicionaProducao(A, primeira);
		
		segunda = new ProducaoGLC();
		segunda.adicionarSimbolo(epsilon);
		gramatica.adicionaProducao(A, segunda);
		
		primeira = new ProducaoGLC();
		primeira.adicionarSimbolo(b);
		primeira.adicionarSimbolo(B);
		gramatica.adicionaProducao(B, primeira);
		
		segunda = new ProducaoGLC();
		segunda.adicionarSimbolo(epsilon);
		gramatica.adicionaProducao(B, segunda);
		
		/*
		 * S - > AbA A -> a | & Fi A = a , & Fi S = a ,b
		 */

	

		// System.out.println(gramatica.getNaoTerminais());
		//
		// System.out.println(gramatica.getProducoes());

		gramatica.calcularFirst();
		 gramatica.calculaFollow();
		for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
			System.out.println("First( " + cabeca.getFirst() + " ) = " + cabeca.obterFirst());
			
		
		}
		for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
			System.out.println("Follow( " + cabeca.getFirst() + " ) = " + cabeca.obterFollow());
			
		
		}
	}

}
