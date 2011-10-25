package com.lendme;


public class LendMeAcceptanceTestInterface {
	
	LendMeAdapter system = new LendMeAdapter();

	/**
	 * Apaga todos os dados do sistema, como
	 * usuários, itens, categorias...
	 */
	public void zerarSistema(){
		
		system.resetSystem();
	}
	
	public void encerrarSistema(){
		
	}
	
	/**
	 * Abre a sessão do usuário cujo login é
	 * passado como parâmetro, sua sessão informa ao
	 * sistema que ele está logado e permite que ele
	 * navegue pelos perfis de outros usuários do
	 * cadastrados no Lend-me.
	 * @param login Login do usuário cuja sessão se deseja abrir.
	 * @return Retorna um String que representa o ID da Sessão do
	 * usuário.   
	 * 
	 */
	public String abrirSessao(String login) throws Exception{
		
		return system.openSession(login);
	}
	
	
	/**
	 * Cadastra um usuário no sistema.
	 * @param String que representa o login do usuáio a ser cadastrado.
	 * @param String que representa o nome do usuário a ser cadastrado.
	 * @param endereco String que representa o endereço do usuário
	 * a ser cadastrado.
	 *
	 */
	public void criarUsuario(String login, String nome, String endereco) throws Exception {
		
		system.registerUser(login, nome, endereco);
	}
	

	/**
	 * 
	 * @param login Login do usuário cujo atributo quer ser 
	 * retornado.
	 * @param atributo Tipo de atributo que se quer pesquisar
	 * no usuário. Os únicos atributas aceitos são: "nome", "endereco" e
	 * "login".
	 * @return Retorna uma String que representa o atributo 
	 * do usuário cujo login foi passado como parâmetro.
	 */
	public String getAtributoUsuario(String login, String atributo) throws Exception{
		
		return system.getUserAttribute(login, atributo);
	}

	
	/**
	 * Cadastra um Item em um usuário.
	 * @param idSessao String que representa o ID da sessão
	 * do usuário em que se quer cadastrar o item. 
	 * @param nome Nome do item que se quer cadastrar.
	 * @param descricao String que representa uma pequeno
	 * comentario sobre o Item.
	 * @param categoria Categora a que o Item está associado.
	 * O item pode ter mais de uma categoria.
	 * @return Retorna uma String que representa o ID do Item cadastrado
	 */
	public String cadastrarItem(String idSessao, String nome, String descricao,
			String categoria) throws Exception{
		
		return system.registerItem(idSessao, nome, descricao, categoria);
	}

	/**
	 * @param idItem ID do Item.
	 * @param atributo Tipo de atributo do Item. 
	 * Os únicos atributas aceitos são: "nome", "descricao" e "categoria".
	 * @return Retorna uma String que representa o valor do atributo
	 * que foi pesquisado no item identificado pelo ID. 
	 */
	public String getAtributoItem(String idItem, String atributo) throws Exception{
		
		return system.getItemAttribute(idItem, atributo);
	}
	
	/**
	 * Faz a requisição de uma amizade.
	 * @param idSessao ID da sessão do usuário requisitente da amizade.
	 * @param login Login do usuáio cuja amizade está sendo solicitada.
	 */
	public void requisitarAmizade(String idSessao, String login) throws Exception{
		
		system.askForFriendship(idSessao, login);
	}
	
	/**
	 *Aprova (aceita) uma amizade requisitada pelo usuário cujo
	 *login foi informado ao usuário cujo ID da Sessão foi passsado como parâmetro.  
	 * @param idSessao ID da sessão do usuário.
	 * @param login Login do usuário requisitante da amizade.
	 */
	public void aprovarAmizade(String idSessao, String login) throws Exception{
		
		system.acceptFriendship(idSessao, login);
	}
	
	/**
	 * Nega um pedido de amizade.
	 * @param idSessao ID da sessão do usuário que negará a amizade.
	 * @param login Login do usuário cuja solicitação de amaizade será negada.
	 */
	public void negarAmizade(String idSessao, String login) throws Exception{
		
		system.declineFriendship(idSessao, login);
	}

	/**
	 * Desfaz uma amizade entre dois usuários.
	 * @param idSessao ID da sessão do usuário que desfará a amizade.
	 * @param login Login do usuário cuja amizade vai ser desfeita.
	 */
	public void desfazerAmizade(String idSessao, String login) throws Exception{
		
		system.breakFriendship(idSessao, login);
	}

	/**
	 * @param idSessao ID da sessão do usuário que verificará se 
	 * o outro usuário é seu amigo.
	 * @param login Login do usuário cuja amizade quer ser identificada.
	 * @return Retorna true se ambos os usuários são amigos e false caso contrário.
	 * @throws Exception
	 */
	public boolean ehAmigo(String idSessao, String login) throws Exception{
		
		return system.hasFriend(idSessao, login);
	}
	
	/**
	 * 
	 * @param idSessao ID da sessão do usuário cujos amigos 
	 * serão retornados
	 * @return Retorna uma String com o toString() de todos os amigos
	 * do usuário separados por ponto e vírgula.
	 */
	public String getAmigos(String idSessao) throws Exception{

		String[] resultado = system.getFriends(idSessao);
		if ( resultado.length == 0 ){
			return "O usuário não possui amigos";
		}
		return formatarASaida(resultado);
	}
	
	
	/**
	 * Retorna para o usuário cujo ID da sessão foi informado
	 * uma String com o ToString() de todos 
	 * os amigos do usuário cujo login foi dado. 
	 * @param idSessao ID da sessão do usuário que está logado no sistema.
	 * @param login Login do usuário cujos amigos serão retornados.
	 */
	public String getAmigos(String idSessao, String login) throws Exception{

		String[] resultado = system.getFriends(idSessao, login);
		if ( resultado.length == 0 ){
			return "O usuário não possui amigos";
		}
		return formatarASaida(resultado);
	}
	
	
	/**
	 * Retorna uma String com o nome de todos os itens
	 * pertencentes ao usuário cujo ID da sessão foi dado como parâmetro.
	 * @param idSessao ID da sessão do usuário.
	 */
	public String getItens(String idSessao) throws Exception{

		String[] resultado = system.getItems(idSessao);
		if ( resultado.length == 0 ){
			return "O usuário não possui itens cadastrados";
		}
		return formatarASaida(resultado);
	}

	/**
	 * Retorna para o usuário cujo ID da sessão foi dqado como parâmetro
	 * uma String com o nome de todos os itens do usuário cujo login foi informado. 
	 * @param idSessao ID da sessão do usuário.
	 * @param login Login da sessão do usuário.
	 * @return
	 * @throws Exception
	 */
	public String getItens(String idSessao, String login) throws Exception{

		String[] resultado = system.getItems(idSessao, login);
		if ( resultado.length == 0 ){
			return "O usuário não possui itens cadastrados";
		}
		return formatarASaida(resultado);
	}
	
	/**
	 * Requisita o empréstimo de um item.
	 * @param idSessao ID da sessão do usuário requisitante.
	 * @param idItem Login do usuário cujo item será requisitado. 
	 * @param duracao Períodio estimado de duração do empréstimo. 
	 * @return String que representa o ID da requisição.
	 */
	public String requisitarEmprestimo(String idSessao,  String idItem, int duracao) throws Exception{
		
		return system.requestItem(idSessao, idItem, duracao);
	}

	/**
	 * Pesquisa por todos os usuários cujo atributo pesquisado nele
	 * possue a chave dada.
	 * 
	 * @param idSessao ID da sessão do usuário que fará a pesuisa.
	 * @param chave Os usuários que possuem essa String no atributo informado 
	 * serão retornados. 
	 * @param atributo Atributo pelo qual se quer fazer a pesquisa.
	 */
	public String localizarUsuario(String idSessao, String chave, String atributo) throws Exception{

		String[] resultado = system.searchUsersByAttributeKey(idSessao, chave, atributo);
		if ( resultado.length == 0 ){
			return "Nenhum usuário encontrado";
		}
		return formatarASaida(resultado);
	}
	
	
	/**
	 * Retorna uma String com os nomes de todos os usuários cadastrados
	 * no sistema ordenados pela distância entre cada um e o usuáio dono 
	 * do ID da sessão dada. 
	 * @param idSessao ID da sessão do usuário logado.
	 */
	public String localizarUsuario(String idSessao) throws Exception{
	    String[] resultado = system.listUsersByDistance(idSessao);	
	    
	    if ( resultado.length == 0 ){
			return "Nenhum usuário encontrado";
		}
		return formatarASaida(resultado);
	}
	
	/**
	 * 
	 * @param idSessao
	 * @return
	 * @throws Exception
	 */
	public String getRequisicoesDeAmizade(String idSessao) throws Exception{

		String[] resultado = system.getFriendshipRequests(idSessao);

		if ( resultado.length == 0 ){
			return "Não há requisições";
		}
		return formatarASaida(resultado);
	}
	
	public String aprovarEmprestimo(String idSessao, String idRequisicaoEmprestimo) throws Exception{
		
			return system.approveLending(idSessao, idRequisicaoEmprestimo);
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
	
	public String historicoAtividades(String idSessao) throws Exception{
		return formatarASaida(system.getActivityHistory(idSessao));
	}
	
	public String historicoAtividadesConjunto(String idSessao) throws Exception{
		return formatarASaida(system.getJointActivityHistory(idSessao));
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
	
	public String publicarPedido(String idSessao, String nomeItem, String descricaoItem) throws Exception{

		return system.publishItemRequest(idSessao, nomeItem, descricaoItem);
		
	}
	
	public void oferecerItem(String idSessao, String idPublicacaoPedido, String idItem)
		throws Exception{
		
		system.offerItem(idSessao, idPublicacaoPedido, idItem);
		
	}
	
	public void rePublicarPedido(String idSessao, String idPublicacaoPedido) throws Exception{
		
		system.republishItemRequest(idSessao, idPublicacaoPedido);
		
	}
	
}