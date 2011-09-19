package com.lendme;

import java.util.Scanner;

public class LendMeTextInterface {
	
	private static LendMeFacade lendMeFacade = new LendMeFacade();
	private static Scanner scanner = new Scanner(System.in);
	private static boolean sair;
	private static boolean usersTools;
	private static boolean itemsTools;
	private static boolean messagesTools;
	private static String currentUserSessionId; //Current user of the system
	
	//System Constants
	//Welcome Screen
	private static final int LOG_IN = 1;
	private static final int REGISTER_USER = 2;
	private static final int LEAVE_UNLOGGED = 3;
	
	//System Functionalities
	private static final int USERS_TOOLS = 1;
	private static final int ITEMS_TOOLS = 2;
	private static final int MESSAGES_TOOLS = 3;
	private static final int LOG_OUT = 4;
	
	//User Tools
	private static final int SEARCH_USER = 1;
	private static final int REQUEST_FRIENDSHIP = 2;
	private static final int ACCEPT_FRIENDSHIP = 3;
	private static final int VISUALIZE_PROFILE = 4;
	private static final int BREAK_FRIENDSHIP = 5;
	private static final int VIEW_USERS_RANKING = 6;
	private static final int GO_BACK_USERS_TOOLS = 7;
	
	//Item Tools
	private static final int REGISTER_ITEM = 1;
	private static final int REQUEST_ITEM = 2;
	private static final int APPROVE_ITEM_REQUEST = 3;
	private static final int ASK_FOR_ITEM_RETURN = 4;
	private static final int DELETE_ITEM = 5;
	private static final int REGISTER_INTEREST_IN_ITEM = 6;
	private static final int SEARCH_ITEM = 7;
	private static final int GO_BACK_ITEMS_TOOLS = 8;
	
	//Messages Tools
	private static final int SEND_OFF_TOPIC_MESSAGE = 1;
	private static final int SEND_NEGOTIATION_MESSAGE = 2;
	private static final int READ_TOPICS = 3;
	private static final int READ_MESSAGES = 4;
	private static final int GO_BACK_MESSAGES_TOOLS = 5;
	
	public static void main(String[] args) {
		try {
			//TODO Take this admin register out. It is here just for testing the text interface.
			LendMe.registerUser("admin","Administrador do Sistema", "Rua Montevideo", "33", "Monte Santo", "CG",
				"PB", "BR", "58102000");
			
		} catch (Exception e) {
			printException(e);
		}
		
		while (!sair){
			
			if ( currentUserSessionId == null ){
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME-" +
						"---------\n\n"+postStepsSeparator() + "\n \t Escolha uma" +
							" opcao a seguir:\n \t [1]Logar\t[2]Cadastrar Usuário \t" +
								"[3]Sair\n",3)){
					case LOG_IN:{
						try{
							currentUserSessionId = logIn(); //Logs into the system and returns the logged user
						}
						catch ( Exception e ){
							printException(e);
						}
						break;
					}
					case REGISTER_USER:{
						try{
							registerUser();
						}
						catch ( Exception e ){
							printException(e);
						}
						break;
					}
					case LEAVE_UNLOGGED:{
						System.out.println(preStepsSeparator()+"\n\n\t---------" +
								"-LENDME----------\n\n"+postStepsSeparator());
						sair = true;
						System.out.println("\n \t Saindo do sistema...\n");
						break;
					}
				}
			}
			else if (usersTools) {
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME-" +
						"---------\n\n"+postStepsSeparator()+ "\n \t Escolha uma" +
							" opcao a seguir:\n\t [1]Localizar Usuario \t" +
								"[2]Requisitar Amizade \t[3]Aceitar Amizade \t" +
									"\n\t [4]Visualizar Perfil \t[5]Desfazer" +
									" Amizade \t[6]Visualizar Ranking de Usuários" +
									"\n\t [7]Voltar\n",7)){
		
				case SEARCH_USER:{
					try{
						searchUsersByAttribute();	
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				case REQUEST_FRIENDSHIP:{
						try{
							requestFriendship();
						}
						catch ( Exception e ){
							printException(e);
						}
						break;
						}
				case ACCEPT_FRIENDSHIP:{
					try{
						acceptFriendship();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case VISUALIZE_PROFILE:{
					try{
						visualizeProfile();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case BREAK_FRIENDSHIP:{
					try{
						breakFriendship();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case VIEW_USERS_RANKING:{
					try{
						viewUsersRanking();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
		
				case GO_BACK_USERS_TOOLS:{
					usersTools = false;
					break;
					}
				}
			}
			
			else if (itemsTools) {
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME-" +
						"---------\n\n"+postStepsSeparator()+ "\n \t Escolha" +
							" uma opcao a seguir:\n\t [1]Cadastrar Item\t" +
								"[2]Requisitar Empréstimo \n\t [3]Aprovar" +
									" Empréstimo \t[4]Pedir devolução de item" +
										"\n\t [5]Apagar Item \t[6]Voltar\n",6)){
				
				case REGISTER_ITEM:{
					try{
						registerItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
				}
				
				case DELETE_ITEM:{
					try{
						deleteItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case REQUEST_ITEM:{
					try{
						requestItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
				}
				
				case APPROVE_ITEM_REQUEST:{
					try{
						approveItemRequest();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				case ASK_FOR_ITEM_RETURN:{
					try{
						askForItemReturn();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				case GO_BACK_ITEMS_TOOLS:{
					itemsTools = false;
					break;
					}
				}
			}
			else if (messagesTools) {
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME----------\n\n"+postStepsSeparator()+
						"\n \t Escolha uma opcao a seguir:\n\t [1]Enviar Mensagem" +
							" Off-Topic\t[2]Enviar Mensagem de Negociação \n\t [3]Ler" +
								" Tópicos \t[4]Ler Mensagens de um Tópico\t[5]Voltar\n",5)){
				
				case SEND_OFF_TOPIC_MESSAGE:{
					try{
						sendOffTopicMessage();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case SEND_NEGOTIATION_MESSAGE:{
					try{
						sendNegotiationMessage();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case READ_TOPICS:{
					try{
						readTopics();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				
				case READ_MESSAGES:{
					try{
						readMessages();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
				case GO_BACK_MESSAGES_TOOLS:{
					messagesTools = false;
					break;
					}
				}
			}
			
			else {
				switch(chooseOption(preStepsSeparator()+"\n\n\t----------LENDME----------\n\n"+postStepsSeparator()+
						"\n \t Escolha uma opcao a seguir:\n \t [1]Usuários \t" +
						"[2]Itens \t[3]Mensagens \t[4]Deslogar\n",4)){
				
				case USERS_TOOLS:{
					usersTools = true;
					break;
					}
				case ITEMS_TOOLS:{
					itemsTools = true;
					break;
					}
				case MESSAGES_TOOLS:{
					messagesTools = true;
					break;
					}
				case LOG_OUT:{
					try {
						lendMeFacade.closeSession(currentUserSessionId);
						currentUserSessionId = null;
						System.out.println("\n\tUsuário deslogado!");
					} catch (Exception e) {
						printException(e);
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
	
	private static void visualizeProfile() throws Exception {
		String solicitedUserLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		System.out.println(lendMeFacade.viewProfile(currentUserSessionId, solicitedUserLogin));
	}
	
	private static void registerItem() throws Exception {
		
		String itemName = returnCorrectString("\n \t Informe o nome do item: "); 
		
		lendMeFacade.registerItem(currentUserSessionId, 
				itemName, returnCorrectString("\n \t Informe a descrição do item: "),
					returnCorrectString("\n \t Informe a categoria do item (" +
						"LIVRO, FILME ou JOGO): "));
		
		System.out.println("\n \tItem " + itemName + " cadastrado com sucesso!");
	}
	
	private static void requestItem() throws Exception {
		
		String itemId = returnCorrectString("\n \tInforme o id do item desejado: ");
		
		lendMeFacade.requestItem(currentUserSessionId, itemId, Integer.parseInt(
				returnCorrectString("\n \tInforme a quantidade de dias esperada" +
						" do empréstimo: ")));
		
		System.out.println("\n \t Requisição de empréstimo do ítem com id " +
				itemId + " enviada.");
	}
	
	private static void approveItemRequest() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeFacade.getReceivedItemRequests(currentUserSessionId)));
		
		String requestId = returnCorrectString("\n \tInforme o id da requisição de" +
				" empréstimo: ");
		
		lendMeFacade.approveLending(currentUserSessionId, 
				requestId);
		
		System.out.println("\n\tRequisição de empréstimo com id " + requestId
				+ " aprovada!");
	}
	
	private static void sendOffTopicMessage() throws Exception {
		lendMeFacade.sendMessage(currentUserSessionId, 
				returnCorrectString("\n\tInforme o assunto da mensagem: "), 
					returnCorrectString("\n\tInforme o conteúdo da mensagem: "),
						returnCorrectString("\n\tInforme o login do destinatário: "));
		
		System.out.println("\n\tMensagem enviada com sucesso!");
	}
	
	private static void sendNegotiationMessage() throws Exception {
		lendMeFacade.sendMessage(currentUserSessionId, 
				returnCorrectString("\n\tInforme o assunto da mensagem: "), 
					returnCorrectString("\n\tInforme o conteúdo da mensagem: "),
						returnCorrectString("\n\tInforme o login do destinatário: "),
							returnCorrectString("\n\tInforme o id do empréstimo: "));
		
		System.out.println("\n\tMensagem enviada com sucesso!");
	}
	
	private static void readTopics() throws Exception {
		System.out.println(listObjectsInArray("Tópicos" +
				"", lendMeFacade.getTopicsWithIds(
				currentUserSessionId, returnCorrectString("\n\tInforme o" +
						" tipo do tópico: "))));
	}
	
	private static void readMessages() throws Exception {
		System.out.println(listObjectsInArray("Mensagens do Tópico Escolhido", lendMeFacade.getTopicMessages(currentUserSessionId, 
				returnCorrectString("\n\tInforme o id do tópico: "))));
	}
	
	private static void askForItemReturn() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeFacade.getLendingRecordsWithIds(currentUserSessionId, "emprestador")));
		
		String requestId = returnCorrectString("\n \tInforme o id da requisição de" +
				" empréstimo: ");
		
		lendMeFacade.askForReturnOfItem(currentUserSessionId, requestId);
		
		System.out.println("\n\tFoi feito o pedido da devolução do item" +
				" da requisição de id: " + requestId);
	}
	
	private static void deleteItem() throws Exception {
		System.out.println(listObjectsInArray("Objetos existentes", 
				lendMeFacade.getItemsWithIds(currentUserSessionId)));
		
		lendMeFacade.deleteItem(currentUserSessionId, returnCorrectString(
				"\n \t Informe o id do item: "));
		
		System.out.println("\n\t Item apagado com sucesso!");
	}
	
	private static void breakFriendship() throws Exception {
		System.out.println(listObjectsInArray("Amigos atuais", 
				lendMeFacade.getFriends(currentUserSessionId)));
		
		lendMeFacade.breakFriendship(currentUserSessionId, returnCorrectString(
				"\n \tInforme o login do usuário: "));
		
		System.out.println("\n\t Amizade desfeita!");
	}
	
	private static void viewUsersRanking() throws Exception {
		System.out.println("\n \tRanking:\n" + lendMeFacade.getRanking(
			currentUserSessionId, returnCorrectString("\n \tInforme" +
				" a categoria do ranking (global/amigos):")));
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
	
	private static void printException(Exception e) {
		System.out.println("\n \t" + e.getMessage());
	}

	
	

}
