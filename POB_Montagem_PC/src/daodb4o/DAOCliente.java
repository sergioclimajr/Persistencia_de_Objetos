package daodb4o;


import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectSet;
import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;

import modelo.Cliente;

public class DAOCliente extends DAO<Cliente>{

	//CPF é usado como campo unico 
	public Cliente readPorDescricao (Object chave) {
		String cpf = (String) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Cliente.class);
		q.descend("cpf").constrain(cpf);
		List<Cliente> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}


	/**********************************************************
	 * 
	 * TODAS AS CONSULTAS DE PESSOA
	 * 
	 **********************************************************/
	
	//Pesquisa pelo NOME ou partes do nome, podendo retornar uma lista com mais de um item
	
	public  List<Cliente> readAll(String caracteres) {
		Query q = manager.query();
		q.constrain(Cliente.class);
		q.descend("nome").constrain(caracteres).like();		//insensitive
		List<Cliente> result = q.execute(); 
		return result;
	}
	//------------------------------------------------
	
	
	//Pesquisa um cliente pelo ID do Orçamento
	
	public Cliente readByNumero(int idOrc){
		Query q = manager.query();
		q.constrain(Cliente.class);
		q.descend("orcamentos").descend("id").constrain(idOrc);
		List<Cliente> resultados = q.execute();
		if(resultados.size()==0)
			return null;
		else
			return resultados.get(0);
	}
	//------------------------------------------------
	
	
	//Pesquisa quais clientes possuem um determinado número de orçamentos passados como parâmetro
	
	public List<Cliente>  readByNOrcamentos(int numDeOrcamentos) {
		Query q = manager.query();
		q.constrain(Cliente.class);
		q.constrain(new Filtro(numDeOrcamentos));
		List<Cliente> result = q.execute();
		return result;
	}



	/*-------------------------------------------------*/
	@SuppressWarnings("serial")
	class Filtro  implements Evaluation {
		private int n;
		public Filtro (int n) {this.n=n;}
		public void evaluate(Candidate candidate) {
			Cliente cli = (Cliente) candidate.getObject();
			candidate.include( cli.listarOrcamentos().size()==n );
		}
	}

}


