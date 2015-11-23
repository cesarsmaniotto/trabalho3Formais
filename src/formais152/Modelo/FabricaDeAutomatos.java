package formais152.Modelo;

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

}
