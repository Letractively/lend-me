package com.lendme;

import java.util.Scanner;

public class LendMeTextInterface {
	
	private static LendMeFacade lendMeFacade = new LendMeFacade();
	private static Scanner scanner = new Scanner(System.in);
	private static boolean sair;
	private static String currentUserSessionId; //Current user of the system
	
	//System Constants
	private static final int LOG_IN = 1;
	private static final int REGISTER_USER = 2;
	private static final int LEAVE_UNLOGGED = 3;
	private static final int REGISTER_ITEM = 1;
	private static final int SEARCH_USER = 2;
	private static final int REQUEST_FRIENDSHIP = 3;
	private static final int ACCEPT_FRIENDSHIP = 4;
	private static final int LOG_OUT = 5;
	
	public static void main(String[] args) {
		try {
			//TODO Take this admin register out. It is here just for testing the text interface.
			LendMe.registerUser("admin","Administrador do Sistema", "Rua Montevideo", "33", "Monte Santo", "CG",
				"PB", "BR", "58102000");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		while (!sair){
			
			if ( currentUserSessionId == null ){
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME----------\n\n"+postStepsSeparator()+
						"\n \t Escolha uma opcao a seguir:\n \t [1]Logar\t[2]Cadastrar Usuário \t[3]Sair\n",3)){
					case LOG_IN:{
						try{
							currentUserSessionId = logIn(); //Logs into the system and returns the logged user
						}
						catch ( Exception e ){
							System.out.println(e.getMessage());
						}
						break;
					}
					case REGISTER_USER:{
						try{
							registerUser();
						}
						catch ( Exception e ){
							System.out.println(e.getMessage());
						}
						break;
					}
					case LEAVE_UNLOGGED:{
						System.out.println(preStepsSeparator()+"\n\n\t----------LENDME----------\n\n"+postStepsSeparator());
						sair = true;
						System.out.println("\n \t Saindo do sistema...\n");
						break;
					}
				}
			}
			else {
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME----------\n\n"+postStepsSeparator()+
						"\n \t Escolha uma opcao a seguir:\n \t [1]Cadastrar Item \t" +
						"[2]Localizar Usuario \t[3]Requisitar Amizade \t[4]Aceitar Amizade \t" +
						"[5]Deslogar\n [5]Deslogar\n",5)){
				case REGISTER_ITEM:{
						try{
						}
						catch ( Exception e ){
							System.out.println(e.getMessage());
						}
						break;
					}
					
				case SEARCH_USER:{
						try{
							searchUsersByAttribute();	
						}
						catch ( Exception e ){
							System.out.println(e.getMessage());
						}
						break;
						}
				case REQUEST_FRIENDSHIP:{
						try{
							requestFriendship();
						}
						catch ( Exception e ){
							System.out.println(e.getMessage());
						}
						break;
						}
				case ACCEPT_FRIENDSHIP:{
					try{
						acceptFriendship();
					}
					catch ( Exception e ){
						System.out.println(e.getMessage());
					}
					break;
					}
				
				case LOG_OUT:{
						try {
							lendMeFacade.closeSession(currentUserSessionId);
							currentUserSessionId = null;
							System.out.println("\n\tUsuário deslogado!");
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
						break;
					}
			}
		}
		}
		
	}	
	
	
	private static void registerUser() throws Exception {
		
		System.out.println("\t\t========================");
		System.out.println("\t\t Cadastrando Locador");
		System.out.println("\t\t========================");
		
		String newUserLogin = lendMeFacade.registerUser(returnCorrectString("\n \t Informe o login : "),
		returnCorrectString("\n \t Informe o nome : "), returnCorrectString("\n \t Informe o endereço : "));
		
		System.out.println(String.format("\n \tO usuário com login %s foi cadastrado com sucesso!", newUserLogin));
	}
	
	private static void searchUsersByAttribute() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de usuários com o atributo especificado",
			lendMeFacade.searchUsersByAttributeKey(currentUserSessionId,
				returnCorrectString("\n \t Informe o atributo: "),
					returnCorrectString("\n \t Informe o tipo do atributo: "))));
	}
	
	private static void requestFriendship() throws Exception {
		String requestedFriendLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		lendMeFacade.askForFriendship(currentUserSessionId, requestedFriendLogin);
		System.out.println("\n \t Requisição de amizade enviada para o usuário "
				+ requestedFriendLogin);
	}
	
	private static void acceptFriendship() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de amizade",
				lendMeFacade.getFriendshipRequests(currentUserSessionId)));
		
		String friendLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		lendMeFacade.acceptFriendship(currentUserSessionId, friendLogin);
		System.out.println("\n \t Amizade com o usuário " + friendLogin + " aceita.");
	}
	
	private static String logIn() throws Exception{
		
		String login = returnCorrectString("\n \t Informe o login : ");
		currentUserSessionId = lendMeFacade.openSession(login);
		System.out.println("\n \t Login realizado com sucesso!");
		return currentUserSessionId;
	}
	
	public static String listObjectsInArray(String message, Object[] array){
		
		String arrayObjectsStr = "\n \t " + message + ": \n";
		
		int lineBreak = 3;
		
		for(int i = 0; i < array.length; i++) {
			if ((i + 1) % lineBreak == 0) {
				arrayObjectsStr += "\t [" + (i + 1) + "]" + array[i] + "\n\n";
				
			} else {			
				arrayObjectsStr += "\t [" + (i + 1) + "]" + array[i];
			}
		}
		
		return arrayObjectsStr;
	}
	
	
	private static String returnCorrectString(String message) throws Exception{
			
			System.out.print(message);
			String input = scanner.nextLine();
			
			if ( message.equals("\n \t Informe a observacao desejada: ") ){
				scanner = new Scanner(System.in);
				if ( input.equals("voltar") ){
					throw new Exception("Cancelado pelo usuário");
				}
				else if ( input.trim().isEmpty() ){
					return " ";
				}
			}
			
			while(!validateString(input)){
				System.out.println("\n \t <Digite novamente!>");
				System.out.print(message);
				input = scanner.nextLine();
			}
			
			return input;
	}
	
	public static boolean validateString(String input) throws Exception{
		
		scanner = new Scanner( System.in );
		
		if ( input.trim().isEmpty() ){
			System.out.println("\n \t <Voce deve inserir alguma entrada!>");
			return false;
		}
		
		verifyIfCancelled((input));
		return true;
	}
		
	private static String preStepsSeparator(){
		return "\n\t==============================================================\n";
	}
	
	private static String postStepsSeparator(){
		return "\n\t==============================================================\n\t==============================================================\n";
	}
	
	public static void verifyIfCancelled(String command) throws Exception{
		if ( command.equals("voltar") ){
			throw new Exception("Cancelado pelo usuário");
		}
	}
	
	public static int chooseOption(String message, int maximum) {
		
		System.out.print(message+"\n\t>: ");

		String chosenString = scanner.nextLine(); 
		
		if (chosenString.isEmpty()){
			System.out.println("\n \t <Voce deve inserir alguma entrada!>\n");
			return chooseOption(message, maximum);
		}
		
		try{
			verifyIfCancelled(chosenString);
		}
		catch (Exception e){
			System.out.println("\n \t <Comando de retorno nao funciona aqui!>");
			return chooseOption(message, maximum);
		}
		
		for (int i=0; i<chosenString.length(); i++){
			if(!Character.isDigit(chosenString.charAt(i))){
				System.out.println("\n \t <Entrada nao eh um inteiro valido!>\n");
				return chooseOption(message, maximum);
			}			
		}		
		
		int chosenInt = Integer.parseInt(chosenString);
		
		if ( chosenInt <= 0 || chosenInt > maximum ){
			System.out.println("\n \t <Entrada eh um inteiro valido mas nao esta no intervalo de 1 a "+maximum+"!>\n");
			return chooseOption(message, maximum);
		}
		
		return chosenInt;
	
	}
	
	

}
