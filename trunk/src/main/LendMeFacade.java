package main;

import java.util.Set;

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
		LendMe.requestFriendShip(idSessao, login);
	}
	
	public void aprovarAmizade(String idSessao, String login) throws Exception{
		LendMe.acceptFriendshipRequeried(idSessao, login);
	}
	
	public void desfazerAmizade(String idSessao, String login) throws Exception{
		LendMe.declineFriendshipFacade(idSessao, login);
	}
	
	public boolean ehAmigo(String idSessao, String login) throws Exception{
		return LendMe.isFriend(idSessao, login);
	}
	
	public Set<User> getAmigos(String idSessao) throws Exception{
		return LendMe.getFriends(idSessao);
	}
	
	public String getAmigos(String idSessao, String login){
		
		
				
			String saida = "";
			
			try {
				
				for(User actUser : LendMe.getFriends(idSessao, login)){
					saida += actUser.getLogin()+"; ";
				}
				return saida;
				
			} catch (Exception e) {
				return "O usuário não possui amigos";
			}
		
	}
	
}