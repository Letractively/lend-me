package learningGoogleAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{//To mandando jogar pra fora pq qualquer excecao eu vejo
		
		long time0 = System.currentTimeMillis();
		final int LAT = 0;
		final int LOG = 1;
				
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
	    
	    System.out.println("==================");
	    
	    //Pensar numa estrategia eficiente para pegar o pedaco da str que interessa.
	    String lat = strJSON.toString().split("\"location\"")[1].split(",")[LAT].split(":")[LAT+2].replace("}", "").trim();
	    System.out.println(lat);
	    String log = strJSON.toString().split("\"location\"")[1].split(",")[LOG].split(":")[LOG].replace("}", "").trim();
	    System.out.println(log);
	    
	    //Ja em modo double
	    System.out.println(Double.parseDouble(lat));
	    System.out.println(Double.parseDouble(log));
	    
	    long time1 = System.currentTimeMillis();
	    System.out.println("Tempo medio de chamada do metodo sera: "+(time1 - time0)+" ms");
	}

}
