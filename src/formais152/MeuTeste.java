/**
 * 
 */
package formais152;

import java.util.ArrayList;

import formais152.Modelo.AnalisadorSintatico;
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

//		GramaticaLivreContexto gramatica = new GramaticaLivreContexto();
//			
//		gramatica = FabricaDeAutomatos.criarGLC();
//		
//		gramatica.calcularFirst();
//		 gramatica.calculaFollow();
//		 
//		 for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
//				System.out.println("First( " + cabeca.getFirst() + " ) = " + cabeca.obterFirst());
//			//	System.out.println("Follow( " + cabeca.getFirst() + " ) = " + cabeca.obterFollow());
//			}
//		for (SimboloGLC cabeca : gramatica.getProducoes().keySet()) {
//		//	System.out.println("First( " + cabeca.getFirst() + " ) = " + cabeca.obterFirst());
//			System.out.println("Follow( " + cabeca.getFirst() + " ) = " + cabeca.obterFollow());
//		}
		
		
		
	/*============FIZ ISSO PRA TESTAR A TABELA POIS JA CONHECIA ESSA GRAMATICA 
	 * 
	 * ******************** EH A MESMA DA LISTA DE EXERCICIOS QUE CAIU NA PROVA
	 * 
	 * O FOLLOW NAO ESTÁ COMPUTANDO CORRETAMENTE, FIRST ESTÁ OK
	 * 
	 * */
		
		
		
		GramaticaLivreContexto gramatica = new GramaticaLivreContexto();	
		
		SimboloGLC S = new SimboloGLC("S", false);
		SimboloGLC A = new SimboloGLC("A", false);
		SimboloGLC B = new SimboloGLC("B", false);
		SimboloGLC C = new SimboloGLC("C", false);
		SimboloGLC D = new SimboloGLC("D", false);
		
		SimboloGLC a = new SimboloGLC("a", true);
		SimboloGLC b = new SimboloGLC("b", true);
		SimboloGLC c = new SimboloGLC("c", true);
		SimboloGLC d = new SimboloGLC("d", true);
		SimboloGLC epsilon = new SimboloGLC("&", true);
		
		gramatica.setSimboloInicial(S);
		
		ArrayList<SimboloGLC> prod1 = new ArrayList<>();
		prod1.add(B);
		prod1.add(A);
		gramatica.adicionaProducao(S, new ProducaoGLC(prod1));
		
		ArrayList<SimboloGLC> prod2 = new ArrayList<>();
		prod2.add(a);
		prod2.add(B);
		prod2.add(A);
		gramatica.adicionaProducao(A, new ProducaoGLC(prod2));
		
		ArrayList<SimboloGLC> prod3 = new ArrayList<>();
		prod3.add(epsilon);
		gramatica.adicionaProducao(A, new ProducaoGLC(prod3));
		
		ArrayList<SimboloGLC> prod4 = new ArrayList<>();
		prod4.add(D);
		prod4.add(C);
		gramatica.adicionaProducao(B, new ProducaoGLC(prod4));
		
		ArrayList<SimboloGLC> prod5 = new ArrayList<>();
		prod5.add(b);
		prod5.add(D);
		prod5.add(C);
		gramatica.adicionaProducao(C, new ProducaoGLC(prod5));
		
		ArrayList<SimboloGLC> prod6 = new ArrayList<>();
		prod6.add(epsilon);
		gramatica.adicionaProducao(C, new ProducaoGLC(prod6));
		
		ArrayList<SimboloGLC> prod7 = new ArrayList<>();
		prod7.add(c);
		prod7.add(S);
		prod7.add(C);
		gramatica.adicionaProducao(D, new ProducaoGLC(prod7));
		
		ArrayList<SimboloGLC> prod8 = new ArrayList<>();
		prod8.add(d);
		gramatica.adicionaProducao(D, new ProducaoGLC(prod8));
		
		
		gramatica.calcularFirst();
		gramatica.calculaFollow();
		
		for(SimboloGLC simbolo : gramatica.getNaoTerminais()){
			
			System.out.println("First de "+ simbolo+": " + simbolo.obterFirst() + "\n");
			System.out.println("Follow de "+ simbolo+": " + simbolo.obterFollow() + "\n");	
			
		}
		
		
		
		AnalisadorSintatico anSintatico = new AnalisadorSintatico(gramatica);
		
		System.out.println(anSintatico.getTabela());
		
	}

}