package regras_negocio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
	
	
	//		Localizar Cliente atrav�s do CPF
	public static Cliente localizarCliente(String cpf) throws Exception {
		Cliente c = daocliente.readPorDescricao(cpf);
		if (c == null) {
			throw new Exception("nome inexistente: " + cpf);
		}
		return c;
	}
	
	//		Localizar Or�amento
	public static Orcamento localizarOrcamento(int idOrc) throws Exception {
		Orcamento orc = daoorcamento.readPorDescricao(idOrc);
		if (orc == null) {
			throw new Exception("Or�amento inexistente: " + orc);
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
			throw new Exception("CPF inv�lido! " + cpf);
		}
		Cliente c = daocliente.readPorDescricao(cpf);
		if(c != null) {
			DAO.rollback();
			throw new Exception("Cliente j� cadastrado " + cpf);
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
	        // Verificando se j� existe componente com a mesma descri��o
	        Componente c = daocomponente.readPorDescricao(descricao);
	        if (c != null) {
	            DAO.rollback();
	            throw new Exception("Criar Componente - componente j� existe: " + descricao);
	        }
	        
	        c = new Componente(descricao, preco, estoque);
	        
	        daocomponente.create(c);
	        
	        DAO.commit();
	        
	        return c;
	    } catch (Exception e) {
	        DAO.rollback();
	        throw e;
	    }
	}
	
	
	//Criando um Or�amento
	
	public static Orcamento criarOrcamento(String data, String cpf) throws Exception {
	    DAO.begin();
	    Orcamento orc = new Orcamento();
	    
	    try {
			// validar a data
			LocalDate dt = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			DAO.rollback();
			throw new Exception("Formato de data inv�lido: " + data);
		}
	    
	    Cliente cliente = daocliente.readPorDescricao(cpf);
	    if (cliente == null) {
	    	DAO.rollback();
	    	throw new Exception("Cliente n�o cadastrado: " + cpf);
	    } else {
	    	
	        orc.setData(data);
	        orc.setCliente(cliente);
	        cliente.listarOrcamentos().add(orc);
	        daoorcamento.create(orc);
	        daocliente.update(cliente);
	        DAO.commit();
	    }
        
	    return orc;
	    }

	
	/**********************************************************
	 * 
	 * EXCLUS�O ATRAV�S DOS DAO
	 * 
	 **********************************************************/
	
	//			Excluir Cliente
	public static void excluirCliente(String cpf) throws Exception {
		DAO.begin();
		Cliente cli = daocliente.readPorDescricao(cpf);
		if (cli == null) {
			DAO.rollback();
			throw new Exception("Excluir Cliente - cliente inexistente: CPF n� " + cpf);
		}

		// desligar o Cliente de seus Or�amentos (�rf�os) e apag�-los do banco
		for (Orcamento orc : cli.listarOrcamentos()) {
			orc.setCliente(null);
			daoorcamento.update(orc);
			daoorcamento.delete(orc); // deletar o or�amento �rf�o
		}
		
		daocliente.delete(cli); // apagar o Cliente
		DAO.commit();
	}
	
	//--------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	//			Excluir Or�amento
	public static void excluirOrcamento(int idOrc) throws Exception {
	    DAO.begin();
	    
	    Orcamento orc = daoorcamento.readPorDescricao(idOrc);
	    
	    if (orc == null) {
	        DAO.rollback();
	        throw new Exception("Excluir Or�amento - or�amento inexistente: ID n� " + idOrc);
	    }

	    // Copiar a lista de componentes para evitar a modifica��o durante a itera��o
	    List<Componente> componentes = new ArrayList<>(orc.getComponentes());

	    // Remover o or�amento de cada componente
	    for (Componente comp : componentes) {
	        orc.remover(comp);
	    }
	    
	    componentes.clear();
	    
	    Cliente cli = daocliente.readPorDescricao(orc.getCliente().getCpf()); //....orc.getCliente().getCpf()
	    cli.remover(orc);
	    
	    daocliente.update(cli);
	    
	    daoorcamento.delete(orc); // Apagar o Or�amento
	    DAO.commit();
	}
	//--------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	//			Excluir Componente do Banco de Dados
	public static void excluirComponente(int idComp) throws Exception {
	    DAO.begin();
	    Componente comp = daocomponente.read(idComp);
	    if (comp == null) {
	        DAO.rollback();
	        throw new Exception("Excluir Componente - componente inexistente");
	    }

	    // Verifica se o componente est� presente em algum or�amento
	    ArrayList<Orcamento> orcamentosComEsteComponente = daoorcamento.encontrarOrcamentosPorComponente(idComp);
	    if (!orcamentosComEsteComponente.isEmpty()) {
	        DAO.rollback();
	        throw new Exception("Componente consta em um ou mais or�amentos");
	    }

	    daocomponente.delete(comp); // apagar o Componente
	    DAO.commit();
	}
	
	
	
	/**********************************************************
	 * 
	 * LISTAS ATRAV�S DOS DAO
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
		if (result.isEmpty()) {
	        System.out.println("A lista de or�amentos est� vazia.");
	    }
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
	 * ALTERA��O ATRAV�S DOS DAO
	 * 
	 **********************************************************/
	
	//		ALTERAR DATA DO OR�AMENTO
	public static void alterarDataOrcamento(int idOrc, String data) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.readPorDescricao(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Alterar data do or�amento - or�amento inexistente: ID n� " + idOrc);
		}
		
		orc.setData(data);
		daoorcamento.update(orc);
				
		DAO.commit();
	}
	
	
	
	//		ADICIONAR PE�A AO OR�AMENTO PELO ID
	public static void adicionarCompAoOrcamento(int idOrc, int idComp) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.readPorDescricao(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Adicionar componente ao or�amento - or�amento inexistente.");
		}
		
		Componente comp = daocomponente.read(idComp);
		if (comp == null) {
			DAO.rollback();
			throw new Exception("Adicionar componente ao or�amento - componente inexistente.");
		}
		
		orc.adicionar(comp);
		daoorcamento.update(orc);
				
		DAO.commit();
	}
	
	
	//			REMOVER PE�A DO OR�AMENTO PELO ID
	public static void removerCompDoOrcamento(int idOrc, int idComp) throws Exception {
		DAO.begin();
		Orcamento orc = daoorcamento.readPorDescricao(idOrc);
		if (orc == null) {
			DAO.rollback();
			throw new Exception("Remover componente do or�amento - or�amento inexistente: ID n� " + idOrc);
		}
		
		Componente comp = orc.localizar(idComp);
		if (comp == null) {
			DAO.rollback();
			throw new Exception("Remover componente do or�amento - componente n�o consta no or�amento");
		}
		
		orc.remover(comp);
		daoorcamento.update(orc);
	
		DAO.commit();
	}
	
	public static boolean validarCPF(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        return cpfNumerico.length() == 11;
    }
	
	// ---------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	
	public static ArrayList<Orcamento> listarOrcamentosDoComponente(int idComp) throws Exception {
		DAO.begin();
		ArrayList<Orcamento> resultado = daoorcamento.encontrarOrcamentosPorComponente(idComp);
		
		if (resultado.isEmpty()) {
			throw new Exception("Componente n�o faz parte de nenhum or�amento");
		}
		return resultado;
	}
	
	
	public static ArrayList<Orcamento> listarOrcamentosDoComponente(String descricao) throws Exception {
		DAO.begin();
		ArrayList<Orcamento> resultado = daoorcamento.encontrarOrcamentosPorComponente(descricao);
		
		if (resultado.isEmpty()) {
			throw new Exception("Componente n�o faz parte de nenhum or�amento");
		}
		return resultado;
	}
	
	
	
	// ---------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	
	
	
	//------------------Usuario------------------------------------
	public static Usuario cadastrarUsuario(String nome, String senha) throws Exception{
		DAO.begin();
		Usuario usu = daousuario.readPorDescricao(nome);
		if (usu!=null)
			throw new Exception("Usuario ja cadastrado:" + nome);
		usu = new Usuario(nome, senha);

		daousuario.create(usu);
		DAO.commit();
		return usu;
	}
	
	public static Usuario localizarUsuario(String nome, String senha) {
		Usuario usu = daousuario.readPorDescricao(nome);
		if (usu==null)
			return null;
		if (! usu.getSenha().equals(senha))
			return null;
		return usu;
	}

	public static List<Componente> listarComponentesComEstoqueAcimaDe(int quantidade) {
	    DAO.begin();
	    List<Componente> result = daocomponente.listarComponentesComEstoqueAcimaDe(quantidade);
	    DAO.commit();
	    return result;
	}

	public static List<Cliente> listarClientesComMaisDeNOrcamentos(int quantidade) {
	    DAO.begin();
	    List<Cliente> result = daocliente.readByNOrcamentos(quantidade);
	    DAO.commit();
	    return result;
	}
	
}
