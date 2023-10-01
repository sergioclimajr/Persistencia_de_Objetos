package daodb4o;

import java.util.List;

import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Componente;
import modelo.Orcamento;

public class DAOOrcamento extends DAO<Orcamento>  {
	
	//o ID do Or�amento � usado como campo unico 
	
	@Override
	public Orcamento read (Object chave) {
		int idOrc = (int) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Orcamento.class);
		q.descend("id").constrain(idOrc);
		List<Orcamento> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
	
	//metodo da classe DAO sobrescrito DAOAluguel para
	//criar "id" sequencial 
	public void create(Orcamento obj){
		int novoid = super.gerarId();	//gerar novo id da classe
		obj.setId(novoid);				//atualizar id do objeto antes de grava-lo no banco
		manager.store( obj );
	}
		
	/**********************************************************
	 * 
	 * TODAS AS CONSULTAS DE OR�AMENTO
	 * 
	 **********************************************************/
	
	//Pesquisa Or�amentos pelo m�s
	
	public List<Orcamento>  readByMes(String mes) {
		Query q = manager.query();
		q.constrain(Orcamento.class);
		q.descend("data").constrain("/"+mes+"/").contains();
		return q.execute();
	}
	
	//------------------------------------------------

	//Pesquisa Or�amentos por DATA espec�fica
	
	public List<Orcamento>  readByDate(String data) {
		Query q = manager.query();
		q.constrain(Orcamento.class);
		q.descend("data").constrain(data);
		return q.execute();
	}
	
	//------------------------------------------------
	
	//Pesquisa Or�amentos por DATA espec�fica (CORRIGIR)....................
	
	public List<Orcamento>  readByCliente(String cpfCli) {
		Query q = manager.query();
		q.constrain(Cliente.class);
		q.descend("cpf").constrain(cpfCli);
		return q.execute();
		
		
		//PRECISA RETORNAR A LISTA DE OR�AMENTOS DO CLIENTE
		
	}
	
	//------------------------------------------------
	
	
	
}
