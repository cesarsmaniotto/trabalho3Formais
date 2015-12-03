/**
 * 
 */
package formais152;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import formais152.Modelo.AnalisadorLexico;
import formais152.Modelo.AnalisadorSintatico;
import formais152.Modelo.FabricaDeAutomatos;
import formais152.Modelo.GramaticaLivreContexto;
import formais152.Modelo.InputOutput;
import formais152.Modelo.Pair;
import formais152.Modelo.TabelaDeSimbolos;
import formais152.Modelo.TipoToken;
import formais152.Modelo.Token;

/**
 * @author cesar
 *
 */
public class mainT2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> palavrasReservadas = new ArrayList<>();
		String[] palavras = { "for","string", "new", "float", "int", "const", "final", "static", "double", "char", "bool",
				"long", "short", "typedef", "class", "struct", "namespace", "using", "if", "else", "while", "return",
				"and", "auto", "case", "switch", "catch", "try", "continue", "break", "do", "enum", "explicit", "false",
				"true", "friend", "goto", "inline", "operator", "sizeof", "struct", "template", "this", "void", "xor" };

		for (String pr : palavras) {
			palavrasReservadas.add(pr);
		}

		TabelaDeSimbolos tabela = new TabelaDeSimbolos(palavrasReservadas);
		AnalisadorLexico analisadorLex = new AnalisadorLexico(tabela);
		JOptionPane.showConfirmDialog(null, "programa.txt Vai ser compilado");

		String input = "programa.txt";
    	input = InputOutput.readFile(input);
    //	System.out.println(input);

    
		analisadorLex.montaTabelaDeSimbolos(input);
			
		List<Token> tokens = tabela.getTokens();
		tokens.remove(tokens.size() - 1);
		for( Token a:tokens ){
		//	System.out.println(a);
		}
		
		
		String output = "";
		for (Pair<String, TipoToken> p : tabela.getTokens()) {
			output += p.toString();
			output += "\n";
		}
		InputOutput.writeToFile(output, "output.txt");
		//System.out.println(output);

	

		GramaticaLivreContexto glc =FabricaDeAutomatos.criarGLC();
		glc.calcularFirst();
		glc.calculaFollow();
	
		AnalisadorSintatico analisadorSint = new AnalisadorSintatico( glc);
		
		String a= analisadorSint.reconhecerPrograma(tokens) ? "Compilado sem erros" : "há erros de compilação";
		JOptionPane.showConfirmDialog(null, a);


	}

}
