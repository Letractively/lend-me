package main;

import java.util.Arrays;
import java.util.Set;

import entities.Item;
import entities.User;


public class LendMeFacade {
	
	public void zerarSistema(){
		LendMe.resetSystem();
	}
	
	public void encerrarSistema(){
		
	}
	
	public String abrirSessao(String login) throws Exception{
		return LendMe.openSession(login);
	}
	
	public void criarUsuario(String login, String nome, String endereco) throws Exception {
		LendMe.registerUser(login, nome, endereco);
	}
	
	public String getAtributoUsuario(String login, String atributo) throws Exception{
		return LendMe.getUserAttribute(login, atributo);
	}

	public String cadastrarItem(String idSessao, String nome, String descricao,
			String categoria) throws Exception{
		return LendMe.registerItem(idSessao, nome, descricao, categoria);
	}
	
	public String getAtributoItem(String idItem, String atributo) throws Exception{
		return LendMe.getItemAttribute(idItem, atributo);
	}
	
	public void requisitarAmizade(String idSessao, String login) throws Exception{
		LendMe.askForFriendship(idSessao, login);
	}
	
	public void aprovarAmizade(String idSessao, String login) throws Exception{
		LendMe.acceptFriendship(idSessao, login);
	}
	
	public void negarAmizade(String idSessao, String login) throws Exception{
		LendMe.declineFriendship(idSessao, login);
	}
	
	public void desfazerAmizade(String idSessao, String login) throws Exception{
		LendMe.breakFriendship(idSessao, login);
	}
	
	public boolean ehAmigo(String idSessao, String login) throws Exception{
		return LendMe.hasFriend(idSessao, login);
	}
	
	public String getAmigos(String idSessao) throws Exception{
		String saida = "";

		Set<User> amigos = LendMe.getFriends(idSessao);

		if ( amigos.isEmpty() ){
			return "O usuário não possui amigos";
		}
		
		User[] amigosParaSeremOrdenados = amigos.toArray(new User[amigos.size()]);
		Arrays.sort(amigosParaSeremOrdenados);
		for ( int j=0; j<amigosParaSeremOrdenados.length; j++ ) {
			saida += amigosParaSeremOrdenados[j].getLogin() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
	public String getAmigos(String idSessao, String login) throws Exception{
		String saida = "";

		Set<User> amigos = LendMe.getFriends(idSessao, login);

		if ( amigos.isEmpty() ){
			return "O usuário não possui amigos";
		}
		
		User[] amigosParaSeremOrdenados = amigos.toArray(new User[amigos.size()]);
		Arrays.sort(amigosParaSeremOrdenados);
		for ( int j=0; j<amigosParaSeremOrdenados.length; j++ ) {
			saida += amigosParaSeremOrdenados[j].getLogin() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
	public String getItens(String idSessao) throws Exception{
		String saida = "";

		Set<Item> itens = LendMe.getItems(idSessao);

		if ( itens.isEmpty() ){
			return "O usuário não possui itens cadastrados";
		}
		
		Item[] itensParaSeremSorteados = itens.toArray(new Item[itens.size()]);
		Arrays.sort(itensParaSeremSorteados);
		for ( int j=0; j<itensParaSeremSorteados.length; j++ ) {
			saida += itensParaSeremSorteados[j].getName() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
	public String getItens(String idSessao, String login) throws Exception{
		String saida = "";

		Set<Item> itens = LendMe.getItems(idSessao, login);

		if ( itens.isEmpty() ){
			return "O usuário não possui itens cadastrados";
		}
		
		Item[] itensToBeSorted = itens.toArray(new Item[itens.size()]);
		Arrays.sort(itensToBeSorted);
		for ( int j=0; j<itensToBeSorted.length; j++ ) {
			saida += itensToBeSorted[j].getName() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
	public String requisitarEmprestimo(String idSessao,  String idItem, int duracao){
		return null;
	}
	
	public String localizarUsuario(String idSessao, String chave, String atributo) throws Exception{

		String saida = "";

		Set<User> resultados = LendMe.searchUsersByAttributeKey(idSessao, chave, atributo);

		if ( resultados.isEmpty() ){
			return "Nenhum usuário encontrado";
		}
		
		User[] resultadosParaSeremOrdenados = resultados.toArray(new User[resultados.size()]);
		Arrays.sort(resultadosParaSeremOrdenados);
		for ( int j=0; j<resultadosParaSeremOrdenados.length; j++ ) {
			saida += resultadosParaSeremOrdenados[j].getName()
			+ " - " + resultadosParaSeremOrdenados[j].getAddress().getFullAddress() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
	public String getRequisicoesDeAmizade(String idSessao) throws Exception{

		String saida = "";

		Set<User> resultados = LendMe.getFriendshipRequests(idSessao);

		if ( resultados.isEmpty() ){
			return "Não há requisições";
		}
		
		User[] resultadosParaSeremOrdenados = resultados.toArray(new User[resultados.size()]);
		Arrays.sort(resultadosParaSeremOrdenados);
		for ( int j=0; j<resultadosParaSeremOrdenados.length; j++ ) {
			saida += resultadosParaSeremOrdenados[j].getLogin() + "; ";
		}
		saida = saida.substring(0, saida.length() - 2);
		return saida;
	}
	
}