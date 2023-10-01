package regras_negocio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

import aplicacao.Util;
import daodb4o.DAO;
import daodb4o.DAOCliente;
import daodb4o.DAOOrcamento;
import daodb4o.DAOComponente;
import daodb4o.DAOUsuario;

import modelo.Cliente;
import modelo.Orcamento;
import modelo.Componente;
import modelo.Usuario;


public class Fachada {
	
	private static DAOCliente daocliente = new DAOCliente();
	private static DAOOrcamento daoorcamento = new DAOOrcamento();
	private static DAOComponente daocomponente = new DAOComponente();
	private static DAOUsuario daousuario = new DAOUsuario();
	public static Usuario logado;	//contem o objeto Usuario logado em TelaLogin.java
	
	private Fachada() {}
	
	public static void inicializar() {
		DAO.open();
	}

	public static void finalizar() {
		DAO.close();
	}
	
	
	//		Localizar Cliente através do CPF
	public static Cliente localizarCliente(String cpf) throws Exception {
		Cliente c = daocliente.read(cpf);
		if (c == null) {
			throw new Exception("nome inexistente: " + cpf);
		}
		return c;
	}
	
	//		Localizar Orçamento
	public static Orcamento localizarOrcamento(int idOrc) throws Exception {
		Orcamento orc = daoorcamento.read(idOrc);
		if (orc == null) {
			throw new Exception("Orçamento inexistente: " + orc);
		}
		return orc;
	}
	
	
	//		Localizar Componente
	public static Componente localizarComponente(int idComp) throws Exception {
		Componente comp = daocomponente.read(idComp);
		if (comp == null) {
			throw new Exception("Componente inexistente: " + idComp);
		}
		return comp;
	}
	
	
	/**********************************************************
	 * 
	 * CRIANDO OBJETOS
	 * 
	 **********************************************************/
	
	//Criando um Cliente
	
	public static Cliente criarCliente(String nome, String cpf) throws Exception {
		DAO.begin();
		if (validarCPF(cpf) == false) {
			DAO.rollback();
			throw new Exception("CPF inválido! " + cpf);
		}
		Cliente c = daocliente.read(cpf);
		if(c != null) {
			DAO.rollback();
			throw new Exception("Cliente já cadastrado " + cpf);
			}
		
		c = new Cliente(cpf, nome);
		
		daocliente.create(c);
		DAO.commit();
		return c;
	}
	
	
	//Criando um Componente
	
	public static Componente criarComponente(String descricao, double preco, int estoque) throws Exception {
	    DAO.begin();
	    
	    try {
	        // Verificando se já existe componente com a mesma descrição
	        Componente c = daocomponente.read(descricao);
	        if (c != null) {
	            DAO.rollback();
	            throw new Exception("Criar Componente - componente já existe: " + descricao);
	        }
	        
	        c = new Componente(descricao);
	        c.setPreco(preco);
	        c.setEstoque(estoque);
	        
	        daocomponente.create(c);
	        
	        DAO.commit();
	        
	        return c;
	    } catch (Exception e) {
	        DAO.rollback();
	        throw e;
	    }
	}
	
	
	//Criando um Orçamento
	
	public static Orcamento criarOrcamento(String data, String cpf) throws Exception {
	    DAO.begin();
	    Orcamento orc = new Orcamento();
	    
	    try {
			// validar a data
			LocalDate dt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			DAO.rollback();
			throw new Exception("Formato de data inválido: " + data);
		}
	    
	    Cliente cliente = daocliente.read(cpf);
	    if (cliente == null) {
	    	DAO.rollback();
	    	throw new Exception("Cliente não cadastrado: " + cpf);
	    } else {
	    	
	        orc.setData(data);
	        orc.setCliente(cliente);
	        daoorcamento.create(orc);
	        DAO.commit();
	    }
        
	    return orc;
	    }

	
	/**********************************************************
	 * 
	 * EXCLUSÃO ATRAVÉS DOS DAO
	 * 
	 **********************************************************/
	
	//			Excluir Cliente
	public static void excluirCliente(String cpf) throws Exception {
		DAO.begin();
		Cliente cli = daocliente.read(cpf);
		if (cli == null) {
			DAO.rollback();
			throw new Exception("Excluir Cliente - cliente inexistente: CPF nº " + cpf);
		}

		// desligar o Cliente de seus Orçamentos (órfãos) e apagá-los do banco
		for (Orcamento orc : cli.listarOrcamentos()) {
			orc.setCliente(null);
			daoorcamento.update(orc);
			daoorcamento.delete(orc); // deletar o orçamento órfão
		}
		
		daocliente.delete(cli); // apagar o Cliente
		DAO.commit();
	}
	
	
	//			Excluir Orçamento
	public static void excluirOrcamento(int idOrc) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.read(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Excluir Orçamento - orçamento inexistente: ID nº " + idOrc);
		}
		
		// desligar o Cliente de seus Orçamentos (órfãos) e apagá-los do banco
		for (Componente comp : orc.getComponentes()) {
			orc.remover(comp);
			daoorcamento.update(orc);
		}
		
		daoorcamento.delete(orc); // apagar o Orçamento
		DAO.commit();
	}
	
	
	//			Excluir Componente do Banco de Dados
	public static void excluirComponente(int idComp) throws Exception {
		DAO.begin();
		Componente comp = daocomponente.read(idComp);
		if (comp == null) {
			DAO.rollback();
			throw new Exception("Excluir Componente - componente inexistente: ID nº " + idComp);
		}
		
		for (Orcamento orc : comp.getOrcamentos()) {
			orc.remover(comp);
		}
		
		daocomponente.delete(comp); // apagar o Componente
		DAO.commit();
	}
	
	
	
	/**********************************************************
	 * 
	 * LISTAS ATRAVÉS DOS DAO
	 * 
	 **********************************************************/
	
	public static List<Cliente> listarClientes() {
		DAO.begin();
		List<Cliente> result = daocliente.readAll();
		DAO.commit();
		return result;
	}
	
	public static List<Orcamento> listarOrcamentos() {
		DAO.begin();
		List<Orcamento> result = daoorcamento.readAll();
		DAO.commit();
		return result;
	}
	
	public static List<Componente> listarComponentes() {
		DAO.begin();
		List<Componente> result = daocomponente.readAll();
		DAO.commit();
		return result;
	}
	
	public static List<Usuario>  listarUsuarios(){
		DAO.begin();
		List<Usuario> resultados =  daousuario.readAll();
		DAO.commit();
		return resultados;
	}
	
	public static List<Orcamento> listarOrcamentosPorCliente(String cpf) throws Exception {
		Cliente cli = localizarCliente(cpf);
		return cli.listarOrcamentos();
	}
	
	
	/**********************************************************
	 * 
	 * ALTERAÇÃO ATRAVÉS DOS DAO
	 * 
	 **********************************************************/
	
	//		ALTERAR DATA DO ORÇAMENTO
	public static void alterarDataOrcamento(int idOrc, String data) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.read(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Alterar data do orçamento - orçamento inexistente: ID nº " + idOrc);
		}
		
		orc.setData(data);
		daoorcamento.update(orc);
				
		DAO.commit();
	}
	
	
	//		ADICIONAR PEÇA AO ORÇAMENTO
	public static void adicionarCompAoOrcamento(int idOrc, int idComp) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.read(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Adicionar componente ao orçamento - orçamento inexistente: ID nº " + idOrc);
		}
		
		Componente comp = daocomponente.read(idComp);
		if (comp == null) {
			DAO.rollback();
			throw new Exception("Adicionar componente ao orçamento - componente inexistente: ID nº " + idOrc);
		}
		
		orc.adicionar(comp);
		daoorcamento.update(orc);
				
		DAO.commit();
	}
	
	
	//			REMOVER PEÇA DO ORÇAMENTO
	public static void removerCompDoOrcamento(int idOrc, int idComp) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.read(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Remover componente do orçamento - orçamento inexistente: ID nº " + idOrc);
		}
		
		Componente comp = orc.localizar(idComp);
		if (comp == null) {
			throw new Exception("Remover componente do orçamento - componente não consta no orçamento: ID nº " + idOrc);
		}
		
		orc.remover(comp);
		daoorcamento.update(orc);
		
		DAO.commit();
	}
	
	public static boolean validarCPF(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        return cpfNumerico.length() == 11;
    }
	
	
	//------------------Usuario------------------------------------
	public static Usuario cadastrarUsuario(String nome, String senha) throws Exception{
		DAO.begin();
		Usuario usu = daousuario.read(nome);
		if (usu!=null)
			throw new Exception("Usuario ja cadastrado:" + nome);
		usu = new Usuario(nome, senha);

		daousuario.create(usu);
		DAO.commit();
		return usu;
	}
	
	public static Usuario localizarUsuario(String nome, String senha) {
		Usuario usu = daousuario.read(nome);
		if (usu==null)
			return null;
		if (! usu.getSenha().equals(senha))
			return null;
		return usu;
	}
	
}
