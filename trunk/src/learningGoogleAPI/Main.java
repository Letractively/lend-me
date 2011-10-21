package learningGoogleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{//To mandando jogar pra fora pq qualquer excecao eu vejo
		
		long time0 = System.currentTimeMillis(); 
		
		String address = "350+Joaquim+Caroca,+Bairro+Universitario,+Campina+Grande,+Paraíba,+Brasil";
		String strURL = "http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false";
		String sliceOfString = "";
		StringBuilder strJSON = new StringBuilder();
		//Objeto que representa a url que queremos acessar
		URL url = new URL(strURL);
		
		//Abre a conexao 
		URLConnection connection = (HttpURLConnection) url.openConnection();

		//Configura o methodo (GET/POST)
		connection.setRequestProperty("Request-Method", "GET");
		
		//Configura para receber os dados
		connection.setDoInput(true);
		connection.setDoOutput(false);
		
		//Conexao com a url		
		connection.connect();
		
		//Comeca a ler os dados da fonte
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
	    while((sliceOfString = br.readLine()) != null){
	    	strJSON.append(sliceOfString);
	    	System.out.println(sliceOfString);//Imprime o Json
	    }
	    
	    //Pensar numa estrategia eficiente para pegar o pedaco da str que interessa.
	    
	    String[] slices = strJSON.toString().split("\"location\"");
	    System.out.println(slices[0]);
	    System.out.println("============");
	    System.out.println(slices[1]);//Pedaco que comeca com as coordenadas
	    
	}

}
