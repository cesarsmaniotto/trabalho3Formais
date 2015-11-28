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
	
	static public GramaticaLivreContexto criarGramaticaLivre(String location){
		
		GramaticaLivreContexto glc = new GramaticaLivreContexto();
		HashMap<String,SimboloGLC> todosVn = new HashMap<String,SimboloGLC>();
		HashMap<String,SimboloGLC> todosVt = new HashMap<String,SimboloGLC>();
		
		class Tupla{
			public String vt;
			public String vn;
		}
		
		ArrayList<Tupla> allLines = new ArrayList<Tupla>();
		
		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(location));
			String fullline = br.readLine();
			fullline = fullline.trim();
			
			while (fullline != null) {
				String line = "";
				
				//Eliminando espaço em branco entre | 
				for(int i =0;i<fullline.length();i++){
					boolean skip=false;
					
					if(i < fullline.length()-1 ){
						if(fullline.charAt(i+1)=='|' && fullline.charAt(i)==' '){
							skip=true;
							
						}
					}
					if( i > 0 ){
						if( (fullline.charAt(i-1)=='|' || fullline.charAt(i-1)=='>')  && fullline.charAt(i)==' ' ) {
							skip=true;
						}
				
					}
					if(!skip){
						line+=fullline.charAt(i);
					}	
				}
				////
				
				if(line.length()>0){	
					if (line.contains("->")) {
			
						String vn="";
						String vt="";
						
						int pos = line.indexOf("->");
						vn = line.substring(0, pos);
						vt = line.substring(pos + 2);
						
						
						if(todosVn.get(vn)==null){
							todosVn.put(vn, new SimboloGLC(vn, false));
							todosVt.put(vn, new SimboloGLC(vn, false));
						}
						
						Tupla novalinha= new Tupla();
						novalinha.vn = vn;
						novalinha.vt = vt;
						
						allLines.add(novalinha);
							
					}
					
					
			
					
				}

				fullline = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Tupla line:allLines){
			System.out.println(line.vt+"->"+line.vn);
			
		}
		//ArrayList<SimboloGLC> prod = new ArrayList<SimboloGLC>();
		/*
		for(int i=0; i<vt.length();i++){
			char c=   vt.charAt(i);
			if( c == '|'){
				glc.adicionaProducao(todosVn.get(vn), new ProducaoGLC(prod));
				
				prod = new ArrayList<SimboloGLC>();
				continue;
			}
			
			String novoVt=""+c;
			SimboloGLC novaProd = todosVt.get(novoVt);
			
			if(todosVt.get( novoVt )==null){
				todosVt.put(novoVt, new SimboloGLC(novoVt, true) );
			}
			
			prod.add(novaProd);
			
		
			
			
		}*/
		
		
		
	  return null;

	}
}

