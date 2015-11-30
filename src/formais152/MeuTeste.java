/**
 * 
 */
package formais152;

import java.util.ArrayList;

import formais152.Modelo.FabricaDeAutomatos;
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

	
		gramatica = FabricaDeAutomatos.criarGLC();
		
		gramatica.calcularFirst();
		 gramatica.calculaFollow();
		 
		 for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
				System.out.println("First( " + cabeca.getFirst() + " ) = " + cabeca.obterFirst());
			//	System.out.println("Follow( " + cabeca.getFirst() + " ) = " + cabeca.obterFollow());
			}
		for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
		//	System.out.println("First( " + cabeca.getFirst() + " ) = " + cabeca.obterFirst());
			System.out.println("Follow( " + cabeca.getFirst() + " ) = " + cabeca.obterFollow());
		}
		
	}

}