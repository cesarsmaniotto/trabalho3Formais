package formais152.Modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class FabricaDeAutomatos {

	public static Automato automatoConstantes() {
		Automato aut = new Automato();
		String estInicCon = "estInicCon", estFinal1Con = "estFinal1Con", estFinal2Con = "estFinal2Con";

		aut.addEstado(estInicCon, TipoToken.CONSTANTE_NUMERICA);
		aut.addEstado(estFinal1Con, TipoToken.CONSTANTE_NUMERICA);
		aut.addEstado(estFinal2Con, TipoToken.CONSTANTE_NUMERICA);

		aut.addEstadoFinal(estFinal1Con);
		aut.addEstadoFinal(estFinal2Con);

		try {
			aut.setEstadoInicial(estInicCon);

			for (int i = 0; i <= 9; i++) {
				String simb = "" + i;

				aut.addTransicao(estInicCon, simb, estFinal1Con);
				aut.addTransicao(estFinal1Con, simb, estFinal1Con);
				aut.addTransicao(estFinal2Con, simb, estFinal2Con);
			}

			aut.addTransicao(estFinal1Con, ".", estFinal2Con);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato automatoString() {
		Automato aut = new Automato();
		String esInicStr = "esInicStr", estIntermStr = "estIntermStr", esFinalStr = "esFinalStr";

		aut.addEstado(esInicStr, TipoToken.STRING);
		aut.addEstado(estIntermStr, TipoToken.STRING);
		aut.addEstado(esFinalStr, TipoToken.STRING);

		aut.addEstadoFinal(esFinalStr);

		try {
			aut.setEstadoInicial(esInicStr);

			char c = '!';
			for (int i = 33; i <= 126; i++) {

				char c2 = c++;

				if (c2 != '"') {
					aut.addTransicao(estIntermStr, String.valueOf(c2),
							estIntermStr);
				}

			}

			String simb = "\"";
			aut.addTransicao(esInicStr, simb, estIntermStr);
			aut.addTransicao(estIntermStr, simb, esFinalStr);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato automatoVariaveis() {
		Automato aut = new Automato();
		String estInicialVar = "estInicialVar", estFinalVar = "estFinalVar";

		aut.addEstado(estInicialVar, TipoToken.IDENTIFICADOR);

		aut.addEstado(estFinalVar, TipoToken.IDENTIFICADOR);
		aut.addEstadoFinal(estFinalVar);

		try {
			aut.setEstadoInicial(estInicialVar);
			char minusc = 'a';
			char maiusc = 'A';
			for (int i = 0; i <= 25; i++) {

				char iterMin = minusc++;
				char iterMaiusc = maiusc++;

				aut.addTransicao(estInicialVar, String.valueOf(iterMin),
						estFinalVar);
				aut.addTransicao(estFinalVar, String.valueOf(iterMin),
						estFinalVar);

				aut.addTransicao(estInicialVar, String.valueOf(iterMaiusc),
						estFinalVar);
				aut.addTransicao(estFinalVar, String.valueOf(iterMaiusc),
						estFinalVar);
			}

			for (int i = 0; i <= 9; i++) {

				aut.addTransicao(estFinalVar, String.valueOf(i), estFinalVar);
			}
			String simb = "_";

			aut.addTransicao(estInicialVar, simb, estFinalVar);
			aut.addTransicao(estFinalVar, simb, estFinalVar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato automatoOperadoresBinarios() {
		Automato aut = new Automato();
		String esInicOp = "esInicOp", esFinalOp1 = "esFinalOp1", esFinalOp2 = "esFinalOp2";

		aut.addEstado(esInicOp, TipoToken.OPERADOR);
		aut.addEstado(esFinalOp1, TipoToken.OPERADOR);
		aut.addEstado(esFinalOp2, TipoToken.OPERADOR);

		aut.addEstadoFinal(esFinalOp1);
		aut.addEstadoFinal(esFinalOp2);

		String simbList[] = { "+", "-", "=", "*", "/", "<", ">" };
		try {
			aut.setEstadoInicial(esInicOp);

			for (String simb : simbList) {

				aut.addTransicao(esInicOp, simb, esFinalOp1);
			}
			aut.addTransicao(esFinalOp1, "=", esFinalOp2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato automatoMargens() {
		Automato aut = new Automato();
		String esInicMar = "esInicMar", esFinalMar = "esFinalMar";

		aut.addEstado(esInicMar, TipoToken.MARGEM);
		aut.addEstado(esFinalMar, TipoToken.MARGEM);

		aut.addEstadoFinal(esFinalMar);

		String simbList[] = { "(", ")", "[", "]", "{", "}" };

		try {
			aut.setEstadoInicial(esInicMar);

			for (String simb : simbList) {

				aut.addTransicao(esInicMar, simb, esFinalMar);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato automatoSeparadorSentenca() {
		Automato aut = new Automato();
		String esInicSep = "esInicSep", esFinalSep = "esFinalSep";

		aut.addEstado(esInicSep, TipoToken.SEPARADOR);
		aut.addEstado(esFinalSep, TipoToken.SEPARADOR);

		aut.addEstadoFinal(esFinalSep);

		try {
			aut.setEstadoInicial(esInicSep);

			aut.addTransicao(esInicSep, ";", esFinalSep);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aut;
	}

	public static Automato montaAutomato() {

		Automato aut = automatoVariaveis();

		aut = aut.uniao(automatoConstantes());
		aut = aut.uniao(automatoMargens());
		aut = aut.uniao(automatoOperadoresBinarios());
		aut = aut.uniao(automatoSeparadorSentenca());
		aut = aut.uniao(automatoString());

		aut = aut.removerEpsilonTransicoes();

		aut.getEstadoErro().setTipoToken(TipoToken.ERRO);
		aut = aut.completarEstadoErro();

		return aut;

	}

	
	public static GramaticaLivreContexto criarGLC(){
		HashMap<String,SimboloGLC> todosVN=InputOutput.letVN("gramatica.txt");
		
		
		GramaticaLivreContexto glc = new GramaticaLivreContexto();
	
		
			
		SimboloGLC ifvt = new SimboloGLC("if", true);
		SimboloGLC id = new SimboloGLC("id", true);
		SimboloGLC pr = new SimboloGLC("pr", true);
		SimboloGLC tipo = new SimboloGLC("tipo", true);
		SimboloGLC epsilon = new SimboloGLC("&", true);
		SimboloGLC whilevt = new SimboloGLC("while", true);
		SimboloGLC elsevt = new SimboloGLC("else", true);
		SimboloGLC forvt = new SimboloGLC("for", true);
		SimboloGLC stringvt = new SimboloGLC("string", true);
		SimboloGLC constnum = new SimboloGLC("ctenum", true);
		SimboloGLC separator = new SimboloGLC("sep", true);
		//SimboloGLC margem = new SimboloGLC("margem", true);
		SimboloGLC startP = new SimboloGLC("(",true);
		SimboloGLC startB = new SimboloGLC("{",true);
		SimboloGLC endP = new SimboloGLC(")",true);
		SimboloGLC endB = new SimboloGLC("}",true);
		SimboloGLC returnvt = new SimboloGLC("return",true);
		
		SimboloGLC op = new SimboloGLC("op", true);
		
	
		ArrayList<SimboloGLC> prod ;
		SimboloGLC vn ;
		
		prod = new ArrayList<>();
		vn = todosVN.get("S'");
		glc.setSimboloInicial(vn);
		prod.add(todosVN.get("S"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("S'");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod= new ArrayList<>();
		vn = todosVN.get("S");	
		prod.add(todosVN.get("MOD"));
		prod.add(todosVN.get("FUNC2"));
		prod.add(startB);
		prod.add(todosVN.get("LINE"));
		prod.add(endB);
		prod.add(todosVN.get("S'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		

		prod = new ArrayList<>();
		vn = todosVN.get("MOD");
		prod.add(tipo);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("MOD");
		prod.add(pr);
		prod.add(tipo);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		

		prod = new ArrayList<>();
		vn = todosVN.get("FUNC");
		prod.add(id);
		prod.add(startP);
		prod.add(todosVN.get("PAR"));
		prod.add(endP);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("FUNC2");
		prod.add(id);
		prod.add(startP);
		prod.add(todosVN.get("PAR2"));
		prod.add(endP);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
	
		prod = new ArrayList<>();
		vn = todosVN.get("PAR");
		prod.add(todosVN.get("VAL"));
		prod.add(todosVN.get("PAR'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
				
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR'");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR'");
		prod.add(separator);
		prod.add(todosVN.get("VAL"));
		prod.add(todosVN.get("PAR'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR2");
		prod.add(tipo);
		prod.add(id);
		prod.add(todosVN.get("PAR2'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));

		prod = new ArrayList<>();
		vn = todosVN.get("PAR2");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));

	
	
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR2'");
		prod.add(separator);
		prod.add(tipo);
		prod.add(id);
		prod.add(todosVN.get("PAR2'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("PAR2'");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL");
		prod.add(todosVN.get("VAL'"));
		prod.add(todosVN.get("B"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("B");
		prod.add(op);
		prod.add(todosVN.get("VAL'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("B");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		
		prod = new ArrayList<>();
		vn = todosVN.get("B");
		prod.add(todosVN.get("VAL'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL'");
		prod.add(todosVN.get("VAL\""));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL'");
		prod.add(startP);
		prod.add(todosVN.get("VAL"));
		prod.add(endP);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL\"");
		prod.add(id);
		prod.add(todosVN.get("B'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL\"");
		prod.add(stringvt);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL\"");
		prod.add(constnum);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("VAL\"");
		prod.add(todosVN.get("FUNC"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("B'");
		prod.add(startP);
		prod.add(todosVN.get("PAR"));
		prod.add(endP);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("B'");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE");
		prod.add(todosVN.get("LINE'"));
		prod.add(todosVN.get("LINE"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));

		prod = new ArrayList<>();
		vn = todosVN.get("LINE'");
		prod.add(startB);
		prod.add(todosVN.get("LINE"));
		prod.add(endB);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE'");
		prod.add(todosVN.get("LINE\""));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("DECL"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("ATT"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("FOR"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));

		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("WHILE"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("IF"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("FUNC"));
		prod.add(separator);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(todosVN.get("RET"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("LINE\"");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		

		prod = new ArrayList<>();
		vn = todosVN.get("DECL");
		prod.add(tipo);
		prod.add(id);
		prod.add(op);
		prod.add(todosVN.get("VAL"));
		prod.add(separator);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("ATT");
		prod.add(id);
		prod.add(op);
		prod.add(todosVN.get("VAL"));
		prod.add(separator);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("FOR");
		prod.add(forvt);
		prod.add(startP);
		prod.add(todosVN.get("F"));
		prod.add(todosVN.get("ATT"));
		prod.add(todosVN.get("VAL"));
		prod.add(separator);
		prod.add(id);
		prod.add(op);
		prod.add(todosVN.get("VAL"));
		prod.add(endP);
		prod.add(todosVN.get("LINE'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("F");
		prod.add(tipo);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("F");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		

		
		prod = new ArrayList<>();
		vn = todosVN.get("WHILE");
		prod.add(whilevt);
		prod.add(startP);
		prod.add(todosVN.get("VAL"));
		prod.add(endP);
		prod.add(todosVN.get("LINE'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("IF");
		prod.add(ifvt);
		prod.add(startP);
		prod.add(todosVN.get("VAL"));
		prod.add(endP);
		prod.add(todosVN.get("LINE'"));
		prod.add(todosVN.get("ELSE"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("ELSE");
		prod.add(elsevt);
		prod.add(todosVN.get("LINE'"));
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("ELSE");
		prod.add(epsilon);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));
		
		prod = new ArrayList<>();
		vn = todosVN.get("RET");
		prod.add(returnvt);
		prod.add(todosVN.get("VAL"));
		prod.add(separator);
		glc.adicionaProducao(vn, new ProducaoGLC(prod));

		
		return glc;
		
		
	}
}
