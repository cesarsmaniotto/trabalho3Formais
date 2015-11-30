/**
 * 
 */
package formais152;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import jdk.nashorn.internal.parser.Token;
import formais152.Modelo.AnalisadorLexico;
import formais152.Modelo.AnalisadorSintatico;
import formais152.Modelo.FabricaDeAutomatos;
import formais152.Modelo.InputOutput;
import formais152.Modelo.Pair;
import formais152.Modelo.TabelaDeSimbolos;
import formais152.Modelo.TipoToken;

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
		String[] palavras = { "for", "new", "float", "int", "const", "final",
				"static", "double", "char", "bool", "long", "short", "typedef",
				"class", "struct", "namespace", "using", "if", "else", "while",
				"return", "and", "auto", "case", "switch", "catch", "try",
				"continue", "break", "do", "enum", "explicit", "false", "true",
				"friend", "goto", "inline", "operator", "sizeof", "struct",
				"template", "this", "void", "xor" };

		for (String pr : palavras) {
			palavrasReservadas.add(pr);
		}

		TabelaDeSimbolos tabela = new TabelaDeSimbolos(palavrasReservadas);
		AnalisadorLexico analisadorLex = new AnalisadorLexico(tabela);
		JOptionPane.showConfirmDialog(null, "programa.txt Vai ser compilado");

		String input = "programa.txt";
		input = InputOutput.readFile(input);

		analisadorLex.montaTabelaDeSimbolos(input);
		// anal.montaTabelaDeSimbolos("int a = 3 ; if ( a > 1 ) { a = 1 ; } else { a = 2 } double b = a + 4 * 2 ; char s = \"stringsemespacos\" ;");

		List<formais152.Modelo.Token> tokens = tabela.getTokens();
		tokens.remove(tokens.size() - 1);
		
		
		String output = "";
		for (Pair<String, TipoToken> p : tabela.getTokens()) {
			output += p.toString();
			output += "\n";
		}
		InputOutput.writeToFile(output, "output.txt");
		System.out.println(output);
		
		AnalisadorSintatico analisadorSint = new AnalisadorSintatico(FabricaDeAutomatos.criarGLC());

	}

}
