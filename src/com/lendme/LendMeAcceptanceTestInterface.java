package com.lendme;


public class LendMeAcceptanceTestInterface {
	
	LendMeFacade system = new LendMeFacade();
	
	public void zerarSistema(){
		
		system.resetSystem();
	}
	
	public void encerrarSistema(){
		
	}
	
	public String abrirSessao(String login) throws Exception{
		
		return system.openSession(login);
	}
	
	public void criarUsuario(String login, String nome, String endereco) throws Exception {
		
		system.registerUser(login, nome, endereco);
	}
	
	public String getAtributoUsuario(String login, String atributo) throws Exception{
		
		return system.getUserAttribute(login, atributo);
	}

	public String cadastrarItem(String idSessao, String nome, String descricao,
			String categoria) throws Exception{
		
		return system.registerItem(idSessao, nome, descricao, categoria);
	}
	
	public String getAtributoItem(String idItem, String atributo) throws Exception{
		
		return system.getItemAttribute(idItem, atributo);
	}
	
	public void requisitarAmizade(String idSessao, String login) throws Exception{
		
		system.askForFriendship(idSessao, login);
	}
	
	public void aprovarAmizade(String idSessao, String login) throws Exception{
		
		system.acceptFriendship(idSessao, login);
	}
	
	public void negarAmizade(String idSessao, String login) throws Exception{
		
		system.declineFriendship(idSessao, login);
	}
	
	public void desfazerAmizade(String idSessao, String login) throws Exception{
		
		system.breakFriendship(idSessao, login);
	}
	
	public boolean ehAmigo(String idSessao, String login) throws Exception{
		
		return system.hasFriend(idSessao, login);
	}
	
	public String getAmigos(String idSessao) throws Exception{

		String[] resultado = system.getFriends(idSessao);
		if ( resultado.length == 0 ){
			return "O usuário não possui amigos";
		}
		return formatarASaida(resultado);
	}
	
	public String getAmigos(String idSessao, String login) throws Exception{

		String[] resultado = system.getFriends(idSessao, login);
		if ( resultado.length == 0 ){
			return "O usuário não possui amigos";
		}
		return formatarASaida(resultado);
	}
	
	public String getItens(String idSessao) throws Exception{

		String[] resultado = system.getItems(idSessao);
		if ( resultado.length == 0 ){
			return "O usuário não possui itens cadastrados";
		}
		return formatarASaida(resultado);
	}
	
	public String getItens(String idSessao, String login) throws Exception{

		String[] resultado = system.getItems(idSessao, login);
		if ( resultado.length == 0 ){
			return "O usuário não possui itens cadastrados";
		}
		return formatarASaida(resultado);
	}
	
	public String requisitarEmprestimo(String idSessao,  String idItem, int duracao) throws Exception{
		
		return LendMe.requestItem(idSessao, idItem, duracao);
	}
	
	public String localizarUsuario(String idSessao, String chave, String atributo) throws Exception{

		String[] resultado = system.searchUsersByAttributeKey(idSessao, chave, atributo);
		if ( resultado.length == 0 ){
			return "Nenhum usuário encontrado";
		}
		return formatarASaida(resultado);
	}
	
	public String getRequisicoesDeAmizade(String idSessao) throws Exception{

		String[] resultado = system.getFriendshipRequests(idSessao);

		if ( resultado.length == 0 ){
			return "Não há requisições";
		}
		return formatarASaida(resultado);
	}
	
	public String aprovarEmprestimo(String idSessao, String idRequisicaoEmprestimo) throws Exception{
		
			return system.approveLoan(idSessao, idRequisicaoEmprestimo);
	}
	
	public String devolverItem(String idSessao, String idEmprestimo) throws Exception{
		
		return system.returnItem(idSessao, idEmprestimo);
	}
	
	public String confirmarTerminoEmprestimo(String idSessao, String idEmprestimo) throws Exception{
		
		return system.confirmLendingTermination(idSessao, idEmprestimo);
	}
	
	public String negarTerminoEmprestimo(String idSessao, String idEmprestimo) throws Exception{
		
		return system.denyLendingTermination(idSessao, idEmprestimo);
	}

	public String requisitarDevolucao(String idSessao, String idEmprestimo) throws Exception{
		return system.askForReturnOfItem(idSessao, idEmprestimo);
	}
	
	public String getEmprestimos(String idSessao, String tipo) throws Exception{
	
		String[] resultado = system.getLendingRecords(idSessao, tipo);
		if ( resultado.length == 0 ){
			return "Não há empréstimos deste tipo";
		}
		return formatarASaida(resultado);
	}
	
	public String enviarMensagem(String idSessao, String destinatario, 
			String assunto, String mensagem) throws Exception {
			
		return system.sendMessage(idSessao, assunto, mensagem, destinatario);
	}
	
	public String enviarMensagem(String idSessao, String destinatario, 
			String assunto, String mensagem, String idRequisicaoEmprestimo) 
					throws Exception {
			
		return system.sendMessage(idSessao, assunto, mensagem, destinatario, 
				idRequisicaoEmprestimo);
	}
	
	public String lerTopicos(String idSessao, String tipo) throws Exception {
		
		String[] resultado = system.getTopics(idSessao, tipo);
		if ( resultado.length == 0 ) {
			return "Não há tópicos criados";
		}
		return formatarASaida(resultado);
	}
	
	public String lerMensagens(String idSessao, String idTopico) throws Exception {
		
		String[] resultado = system.getTopicMessages(idSessao, idTopico);
		return formatarASaida(resultado);
	}
	
	public void apagarItem(String idSessao, String idItem) throws Exception{
		
		system.deleteItem(idSessao, idItem);
	}
	
	public String pesquisarItem(String idSessao, String chave, String atributo, String tipoDeOrdenacao, String criterioDeOrdenacao) throws Exception{
		
		String[] resultado = system.searchForItems(idSessao, chave, atributo, tipoDeOrdenacao, criterioDeOrdenacao);
		if ( resultado.length == 0 ) {
			return "Nenhum item encontrado";
		}		
		return formatarASaida(resultado);
		
	}

	public String adicionarDias(int dias){
		
		return system.someDaysPassed(dias);
	}
	
	public void registrarInteresse( String idSessao, String idItem ) throws Exception{
		
		system.registerInterestForItem(idSessao, idItem);
	}
	
	public String getRanking(String idSession, String categoria) throws Exception{
		
		return system.getRanking(idSession, categoria);
	}
	
	private String formatarASaida(String[] resultado) {

		StringBuilder resultadoFormatado = new StringBuilder();
		for ( int i=0; i<resultado.length; i++ ){
			resultadoFormatado.append(resultado[i]);
			if ( i <resultado.length-1 ){
				resultadoFormatado.append("; ");
			}
		}
		return resultadoFormatado.toString();
	}
	
}