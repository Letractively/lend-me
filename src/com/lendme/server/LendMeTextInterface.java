package com.lendme.server;

import java.util.Scanner;

public class LendMeTextInterface {
	
	private static LendMeImpl lendMeAdapter = new LendMeImpl();
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
	private static final int EVALUATE_FRIENDSHIP = 3;
	private static final int VISUALIZE_PROFILE = 4;
	private static final int BREAK_FRIENDSHIP = 5;
	private static final int VIEW_USERS_RANKING = 6;
	private static final int VIEW_MY_ACTIVITY_HISTORY = 7;
	private static final int VIEW_JOINT_ACTIVITY_HISTORY = 8;
	private static final int GO_BACK_USERS_TOOLS = 9;
	
	//Item Tools
	private static final int REGISTER_ITEM = 1;
	private static final int SEARCH_ITEM = 2;
	private static final int REQUEST_ITEM = 3;
	private static final int EVALUATE_ITEM_REQUEST = 4;
	private static final int RETURN_ITEM = 5;
	private static final int EVALUATE_ITEM_RETURN = 6;
	private static final int ASK_FOR_ITEM_RETURN = 7;
	private static final int DELETE_ITEM = 8;
	private static final int REGISTER_INTEREST_IN_ITEM = 9;
	private static final int PUBLISH_ITEM_REQUEST = 10;
	private static final int VIEW_FRIENDS_PUBLISHED_ITEM_REQ = 11;
	private static final int OFFER_ITEM = 12;
	private static final int GO_BACK_ITEMS_TOOLS = 13;
	
	//Messages Tools
	private static final int SEND_OFF_TOPIC_MESSAGE = 1;
	private static final int SEND_NEGOTIATION_MESSAGE = 2;
	private static final int READ_TOPICS = 3;
	private static final int READ_MESSAGES = 4;
	private static final int GO_BACK_MESSAGES_TOOLS = 5;
	
	public static void main(String[] args) throws Exception{
		try {
			//TODO Take this admin registration out. It is here just for testing the text interface.
			lendMeAdapter.registerUser("admin","Administrador do Sistema", "Rua Montevideo", "33", "Monte Santo", "CG",
				"PB", "BR", "58102000");
			
		} catch (Exception e) {
			printException(e);
		}
		
		while (!sair){
			
			if ( currentUserSessionId == null ){
				switch(chooseOption(preStepsSeparator()+logo() +
						postStepsSeparator() + "\n \t Escolha uma" +
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
						System.out.println(preStepsSeparator()+logo()+postStepsSeparator());
						sair = true;
						System.out.println("\n \t Saindo do sistema...\n");
						break;
					}
				}
			}
			else if (usersTools) {
				switch(chooseOption(preStepsSeparator()+logo()+postStepsSeparator()+ "\n \t Escolha uma" +
							" opcao a seguir:\n\t [1]Localizar Usuario " +
							"\t\t [2]Requisitar Amizade \n\t [3]Avaliar Req. de Amizade" +
							"\t [4]Visualizar Perfil \n\t [5]Desfazer Amizade" +
							"\t\t [6]Visualizar Ranking de Usuários" +
							"\n\t [7]Ver meu Hist. de Atividades" +
							"\t [8]Ver Hist. Conjunto de Atividades" +
							"\n\t [9]Voltar\n",9)){
		
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
				case EVALUATE_FRIENDSHIP:{
					try{
						evaluateFriendship();
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
				
				case VIEW_MY_ACTIVITY_HISTORY:{
					try{
						viewMyActivityHistory();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
					}
		
				case VIEW_JOINT_ACTIVITY_HISTORY:{
					try{
						viewJointActivityHistory();
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
				switch(chooseOption(preStepsSeparator()+logo()+postStepsSeparator()+ "\n \t Escolha" +
						" uma opcao a seguir:\n\t [1]Cadastrar Item \t\t\t" +
						"[2]Buscar Item \n\t [3]Requisitar Empréstimo \t\t[4]Avaliar" +
						" Empréstimo \n\t [5]Devolver Item \t\t\t[6]Avaliar Devolução de Item " +
						"\n\t [7]Pedir devolução de item \t\t[8]Apagar Item \n\t [9]Registrar Interesse" +
						" em Item \t[10]Publicar Pedido de Item \n\t [11]Ver Public de pedidos de amigos  " +
						"\t[12]OferecerItem \n\t [13]Voltar\n",13)){
				
				case REGISTER_ITEM:{
					try{
						registerItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
				}
				case SEARCH_ITEM:{
					try{
						searchItem();
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
				
				case EVALUATE_ITEM_REQUEST:{
					try{
						evaluateItemRequest();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case RETURN_ITEM:{
					try{
						returnItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case EVALUATE_ITEM_RETURN:{
					try{
						evaluateItemReturn();
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
				case DELETE_ITEM:{
					try{
						deleteItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case REGISTER_INTEREST_IN_ITEM:{
					try{
						registerInterestInItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case PUBLISH_ITEM_REQUEST:{
					try{
						publishItemRequest();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case OFFER_ITEM:{
					try{
						offerItem();
					}
					catch ( Exception e ){
						printException(e);
					}
					break;
						}
				
				case VIEW_FRIENDS_PUBLISHED_ITEM_REQ:{
					try{
						viewFriendsPublishedItemReq();
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
				switch(chooseOption(preStepsSeparator()+logo()+postStepsSeparator()+
						"\n \t Escolha uma opcao a seguir:\n\t [1]Enviar Mensagem" +
							" Off-Topic\t[2]Enviar Mensagem de Negociação \n\t [3]Ler" +
								" Tópicos \t\t[4]Ler Mensagens de um Tópico \n\t [5]Voltar\n",5)){
				
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
				switch(chooseOption(preStepsSeparator()+logo()+postStepsSeparator()+
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
						lendMeAdapter.closeSession(currentUserSessionId);
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
		System.out.println("\t\t Cadastrando Usuário");
		System.out.println("\t\t========================");
		
		String newUserLogin = lendMeAdapter.registerUser(returnCorrectString("\n \t Informe o login : "),
		returnCorrectString("\n \t Informe o nome : "), returnCorrectString("\n \t Informe o endereço : "));
		
		System.out.println(String.format("\n \tO usuário com login %s foi cadastrado com sucesso!", newUserLogin));
	}
	
	private static void searchUsersByAttribute() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de usuários com o atributo especificado",
			lendMeAdapter.searchUsersByAttributeKey(currentUserSessionId,
				returnCorrectString("\n \t Informe o atributo: "),
					returnCorrectString("\n \t Informe o tipo do atributo (nome,login,endereco) : "))));
	}
	
	private static void requestFriendship() throws Exception {
		String requestedFriendLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		lendMeAdapter.askForFriendship(currentUserSessionId, requestedFriendLogin);
		System.out.println("\n \t Requisição de amizade enviada para o usuário "
				+ requestedFriendLogin);
	}
	
	private static void evaluateFriendship() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de amizade",
				lendMeAdapter.getFriendshipRequests(currentUserSessionId)));
		
		String friendLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		String acceptOrDenyStr = returnCorrectString("\n \t Informe sua avaliação (aprovar,rejeitar): ");
		
		if (acceptOrDenyStr.toLowerCase().equals("aprovar")) {
			lendMeAdapter.acceptFriendship(currentUserSessionId, friendLogin);
			System.out.println("\n \t Amizade com o usuário " + friendLogin + " aprovada.");
		}
		else if (acceptOrDenyStr.toLowerCase().equals("rejeitar")) {
			lendMeAdapter.declineFriendship(currentUserSessionId, friendLogin);
			System.out.println("\n \t Amizade com o usuário " + friendLogin + " rejeitada.");
		}
		else {
			System.out.println("\n\t Opção inválida!");
		}
	}
	
	private static void visualizeProfile() throws Exception {
		String solicitedUserLogin = returnCorrectString("\n \t Informe o login do usuário: ");
		
		System.out.println(lendMeAdapter.viewProfile(currentUserSessionId, solicitedUserLogin));
	}
	
	private static void registerItem() throws Exception {
		
		String itemName = returnCorrectString("\n \t Informe o nome do item: "); 
		
		lendMeAdapter.registerItem(currentUserSessionId, 
				itemName, returnCorrectString("\n \t Informe a descrição do item: "),
					returnCorrectString("\n \t Informe a categoria do item (" +
						"LIVRO, FILME ou JOGO): "));
		
		System.out.println("\n \tItem " + itemName + " cadastrado com sucesso!");
	}
	
	private static void requestItem() throws Exception {
		
		String itemId = returnCorrectString("\n \tInforme o id do item desejado: ");
		
		lendMeAdapter.requestItem(currentUserSessionId, itemId, Integer.parseInt(
				returnCorrectString("\n \tInforme a quantidade de dias esperada" +
						" do empréstimo: ")));
		
		System.out.println("\n \t Requisição de empréstimo do ítem com id " +
				itemId + " enviada.");
	}
	
	private static void evaluateItemRequest() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeAdapter.getReceivedItemRequests(currentUserSessionId)));
		
		String requestId = returnCorrectString("\n \tInforme o id da requisição de" +
				" empréstimo: ");
		
		String acceptOrDenyStr = returnCorrectString("\n \t Informe sua avaliação (aprovar,rejeitar): ");
		
		if (acceptOrDenyStr.toLowerCase().equals("aprovar")) {
			lendMeAdapter.approveLending(currentUserSessionId,requestId);
			System.out.println("\n\tRequisição de empréstimo com id " + requestId
					+ " aprovada!");
		}
		else if (acceptOrDenyStr.toLowerCase().equals("rejeitar")) {
			lendMeAdapter.denyLending(currentUserSessionId, requestId);
			System.out.println("\n\tRequisição de empréstimo com id " + requestId
					+ " rejeitada!");
		}
		else {
			System.out.println("\n\t Opção inválida!");
		}
		
		
	}
	
	private static void sendOffTopicMessage() throws Exception {
		lendMeAdapter.sendMessage(currentUserSessionId, 
				returnCorrectString("\n\tInforme o assunto da mensagem: "), 
					returnCorrectString("\n\tInforme o conteúdo da mensagem: "),
						returnCorrectString("\n\tInforme o login do destinatário: "));
		
		System.out.println("\n\tMensagem enviada com sucesso!");
	}
	
	private static void sendNegotiationMessage() throws Exception {
		lendMeAdapter.sendMessage(currentUserSessionId, 
				returnCorrectString("\n\tInforme o assunto da mensagem: "), 
					returnCorrectString("\n\tInforme o conteúdo da mensagem: "),
						returnCorrectString("\n\tInforme o login do destinatário: "),
							returnCorrectString("\n\tInforme o id do empréstimo: "));
		
		System.out.println("\n\tMensagem enviada com sucesso!");
	}
	
	private static void readTopics() throws Exception {
		System.out.println(listObjectsInArray("Tópicos" +
				"", lendMeAdapter.getTopicsWithIds(
				currentUserSessionId, returnCorrectString("\n\tInforme o" +
						" tipo do tópico (offtopic,negociacao,todos): "))));
	}
	
	private static void readMessages() throws Exception {
		System.out.println(listObjectsInArray("Mensagens do Tópico Escolhido",
			lendMeAdapter.getTopicMessages(currentUserSessionId, 
				returnCorrectString("\n\tInforme o id do tópico: "))));
	}
	
	private static void askForItemReturn() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeAdapter.getLendingRecordsWithIds(currentUserSessionId, "emprestador")));
		
		String requestId = returnCorrectString("\n \tInforme o id da requisição de" +
				" empréstimo: ");
		
		lendMeAdapter.askForReturnOfItem(currentUserSessionId, requestId);
		
		System.out.println("\n\tFoi feito o pedido da devolução do item" +
				" da requisição de id: " + requestId);
	}
	
	private static void deleteItem() throws Exception {
		System.out.println(listObjectsInArray("Objetos existentes", 
				lendMeAdapter.getItemsWithIds(currentUserSessionId)));
		
		lendMeAdapter.deleteItem(currentUserSessionId, returnCorrectString(
				"\n \t Informe o id do item: "));
		
		System.out.println("\n\t Item apagado com sucesso!");
	}
	
	private static void breakFriendship() throws Exception {
		System.out.println(listObjectsInArray("Amigos atuais", 
				lendMeAdapter.getFriends(currentUserSessionId).keySet().toArray()));
		
		lendMeAdapter.breakFriendship(currentUserSessionId, returnCorrectString(
				"\n \tInforme o login do usuário: "));
		
		System.out.println("\n\t Amizade desfeita!");
	}
	
	private static void searchItem() throws Exception {
		
		System.out.println(listObjectsInArray("Itens encontrados",
			lendMeAdapter.searchForItemsWithIds(currentUserSessionId,
			returnCorrectString("\n \tInforme a chave a ser pesquisada: "), 
				returnCorrectString("\n \tInforme o atributo (descricao, nome, " +
					"id, categoria): "), returnCorrectString("\n \tInforme a" +
						" disposição dos resultados (crescente, descrescente): ")
						, returnCorrectString("\n \tInforme o critério de busca" +
							" (datacriacao, reputacao): "))));
	}
	
	private static void returnItem() throws Exception {
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeAdapter.getLendingRecordsWithIds(currentUserSessionId, "beneficiado")));
		
		String requestId = returnCorrectString("\n \tInforme o id da requisição de empréstimo: ");
		
		lendMeAdapter.returnItem(currentUserSessionId, requestId);
		
		System.out.println("\n\t Devolução do item da requisição de id " +
			requestId + " efetuada." + "\n\t Esperando aprovação do emprestador.");
	}
	
	private static void evaluateItemReturn() throws Exception {
		
		System.out.println(listObjectsInArray("Lista de requisições de empréstimo",
				lendMeAdapter.getLendingRecordsWithIds(currentUserSessionId, "emprestador")));
		
		String requestId = returnCorrectString("\n\t Informe o id da requisição de empréstimo: ");
		
		String acceptOrDenyStr = returnCorrectString("\n \t Informe sua avaliação (confirmar,negar): ");
		
		if (acceptOrDenyStr.toLowerCase().equals("confirmar")) {
			lendMeAdapter.confirmLendingTermination(currentUserSessionId, requestId);
			
			System.out.println("\n\t Devolução do item da requisição de empréstimo de id " + 
					requestId + "\n\t confirmada!");
		}
		else if (acceptOrDenyStr.toLowerCase().equals("negar")) {
			lendMeAdapter.denyLendingTermination(currentUserSessionId, requestId);
			
			System.out.println("\n\t Devolução do item da requisição de empréstimo de id " + 
					requestId + "\n\t negada!");		}
		else {
			System.out.println("\n\t Opção inválida!");
		}
	}
	
	
	private static void registerInterestInItem() throws Exception {
		
		String itemId = returnCorrectString("\n \tInforme o id do item: ");
		lendMeAdapter.registerInterestForItem(currentUserSessionId, 
				itemId);
		
		System.out.println("\n\t Interesse registrado no item de id " + itemId);
	}
	
	private static void viewUsersRanking() throws Exception {
		System.out.println("\n \tRanking:\n\t " + lendMeAdapter.getRanking(
			currentUserSessionId, returnCorrectString("\n \tInforme" +
				" a categoria do ranking (global/amigos):")));
	}
	
	private static void viewMyActivityHistory() throws Exception {
		System.out.println(listObjectsInArray("Meu Histórico de Atividades", 
				lendMeAdapter.getActivityHistory(currentUserSessionId)));
	}
	
	private static void viewJointActivityHistory() throws Exception {
		System.out.println(listObjectsInArray("Histórico Conjunto de Atividades", 
				lendMeAdapter.getJointActivityHistory(currentUserSessionId)));
	}
	
	private static void publishItemRequest() throws Exception {
		lendMeAdapter.publishItemRequest(currentUserSessionId, 
				returnCorrectString("\n \t Informe o nome do item: "),
				returnCorrectString("\n \t Informe a descrição do item: "));
		
		System.out.println("\n \t Pedido de item publicado com sucesso!");
	}
	
	private static void offerItem() throws Exception {
		lendMeAdapter.offerItem(currentUserSessionId, 
				returnCorrectString("\n \t Informe o id da publicação do pedido: "),
				returnCorrectString("\n \t Informe o id do item: "));
		
		System.out.println("\n \t Item oferecido com sucesso!");
	}
	
	private static void viewFriendsPublishedItemReq() throws Exception {
		System.out.println(listObjectsInArray("Publicações de pedidos dos amigos",
				lendMeAdapter.getFriendsPublishedItemRequests(currentUserSessionId)));
	}
	
	private static String logIn() throws Exception{
		
		String login = returnCorrectString("\n \t Informe o login : ");
		currentUserSessionId = lendMeAdapter.openSession(login);
		System.out.println("\n \t Login realizado com sucesso!");
		return currentUserSessionId;
	}
	
	public static String listObjectsInArray(String message, Object[] array){
		
		String arrayObjectsStr = "\n \t " + message + ": \n";
		
		int lineBreak = 1;
		
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
				else if ( input.trim().length() == 0 ){
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
		
		if ( input.trim().length() == 0 ){
			System.out.println("\n \t <Voce deve inserir alguma entrada!>");
			return false;
		}
		
		verifyIfCancelled((input));
		return true;
	}
		
	private static String logo() throws Exception{
		String logo =
			     "\t  _       _ _ _   __     _    _ _ _        _       _   _ _ _  " +
			   "\n\t | |     | |_|_| |  |   | |  |     |      | |     | | | |_|_|" +
			   "\n\t | |     | |     | | |  | |  |  __  |     |  |   |  | | |    " +
			   "\n\t | |     | |_ _  | || | | |  | |  |  | __ | | |_| | | | |_ _ " +
			   "\n\t | |     | |_|_| | | | || |  | |  |  ||__|| |     | | | |_|_|" +
			   "\n\t | |     | |     | |  | | |  | |__|  |    | |     | | | |    " +
			   "\n\t | |_ _  | |_ _  | |   |  |  |      |     | |     | | | |_ _ " +
			   "\n\t |_|_|_| |_|_|_| |_|    |_|  |_ _ _|      |_|     |_| |_|_|_|\n";
		String systemInfo = "\t"+lendMeAdapter.getSessionInfo(currentUserSessionId);
		return logo + preStepsSeparator() + systemInfo;
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
		
		if (chosenString.trim().length() == 0){
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
		
		int chosenInt = 0;
		try {
			chosenInt = Integer.parseInt(chosenString);
		} catch (NumberFormatException nfe) {
			System.out.println("\n \t <Entrada não é um inteiro ou não é um inteiro válido!>\n");
			return chooseOption(message, maximum);
		}
		
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
