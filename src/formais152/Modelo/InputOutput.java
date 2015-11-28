package formais152.Modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputOutput {

	static private ArrayList<String> getParameters(String line2, String separator) {
		String line = line2;
		ArrayList<String> lista = new ArrayList<String>();
		do {
			String estate = line;
			if (line.contains(separator)) {
				int pos = line.indexOf(separator);
				estate = line.substring(0, pos);
				line = line.substring(pos + 1);
			} else {
				line = "";
			}
			estate = estate.trim();

			lista.add(estate);
		} while (line.trim().length() > 0);
		return lista;
	}

	static public void writeToFile(String text, String location) {
		try {
			PrintWriter out = new PrintWriter(location);
			out.print(text);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public GramaticaRegular criarGramatica(String location) {
		BufferedReader br = null;

		GramaticaRegular gr = new GramaticaRegular();
		try {
			br = new BufferedReader(new FileReader(location));

			String fullline = br.readLine();

			// /inicio da criao
			while (fullline != null) {
				String line = fullline;

				if (line.contains("->")) {

					int position = line.indexOf("->");
					String vn = line.substring(0, position);
					line = line.substring(position + 2);

					vn = vn.trim();
					ArrayList<String> lista = getParameters(line, "|");
					for (String vt : lista) {
						gr.adicionaProducao(vn, vt);
					}

				}

				fullline = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gr;
	}

	static public Expressao criarExpressao(String location) {
		BufferedReader br = null;

		String line = "";
		String text = "";
		try {
			br = new BufferedReader(new FileReader(location));
			line = br.readLine();
			while (line != null) {

				if (line.length() > 0) {
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) != ' ')
							text += line.charAt(i);
					}
				}

				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Expressao(text);
	}

	static public Automato criarAutomato(String location) {
		String line = "";
		Automato auto = new Automato();
		try {
			BufferedReader br = new BufferedReader(new FileReader(location));
			line = br.readLine();
			line = line.trim();

			while (line.equals("")) {
				line = br.readLine();
				line = line.trim();
				if (line == null)
					return null;
			}
			{
				/**
				 * Leitura de estados
				 */

				if (!line.contains("Q"))
					return null;
				int start = line.indexOf('[');
				int end = line.indexOf(']');
				if (start == -1 || end == -1)
					return null;

				String par = line.substring(start + 1, end);

				ArrayList<String> lista = getParameters(par, ",");
				for (String estado : lista) {
					auto.addEstado(estado);
				}
			}
			line = br.readLine();

			while (line.equals("")) {
				line = br.readLine();
				line = line.trim();
				if (line == null)
					return null;
			}
			{/**
				 * Leitura de estado inicial
				 */
				if (!line.contains("q0"))
					return null;
				int start = line.indexOf('=');

				if (start == -1)
					return null;

				String par = line.substring(start + 1);

				par = par.trim();
				auto.setEstadoInicial(par);
			}

			line = br.readLine();

			while (line.equals("")) {
				line = br.readLine();
				line = line.trim();
				if (line == null)
					return null;
			}
			{
				/**
				 * Leitura de estados finais
				 */
				if (!line.contains("F"))
					return null;
				int start = line.indexOf('[');
				int end = line.indexOf(']');
				if (start == -1 || end == -1)
					return null;

				String par = line.substring(start + 1, end);

				ArrayList<String> lista = getParameters(par, ",");
				for (String estado : lista) {
					auto.addEstadoFinal(estado);
				}
			}
			line = br.readLine();

			/**
			 * Leitura das transicoes
			 */
			while (line != null) {

				if (!line.trim().equals("")) {

					String par = line.trim();
					ArrayList<String> lista = getParameters(par, ",");
					if (lista.size() != 3)
						return null;
					auto.addTransicao(lista.get(0), lista.get(1), lista.get(2));
				}
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auto;
	}

	public static String readFile(String location) {
		String line = "";
		String all = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(location));
			String fullline = br.readLine();
			fullline = fullline.trim();
			while (fullline != null) {
				line = fullline;

		
				all += line;
				all += " ";

				fullline = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return all;

	}
	
	static public HashMap<String,SimboloGLC> letVN(String location){
		
		HashMap<String,SimboloGLC> todosVn = new HashMap<String,SimboloGLC>();
	
		ArrayList<String> todos= new ArrayList<>();
	
		try {

			BufferedReader br = new BufferedReader(new FileReader(location));
			String fullline = br.readLine();
			fullline = fullline.trim();
			
			while (fullline != null) {
				String line = "";
				
				//Eliminando espaço em branco entre | 
				for(int i =0;i<fullline.length();i++){
					if(fullline.charAt(i)!=' '){
						line+=fullline.charAt(i);
					}	
				}
			
				
				if(line.length()>0){	
					if (line.contains("->")) {
			
						String vn="";
						
						int pos = line.indexOf("->");
						vn = line.substring(0, pos);
					

						if(todosVn.get(vn)==null){
							todosVn.put(vn, new SimboloGLC(vn, false));
							todos.add(vn);
						}
						
					}
					
				}

				fullline = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for( String a: todos){
		//	System.out.println(a);
		}
		
	
		
		
	  return todosVn;

	}
}

