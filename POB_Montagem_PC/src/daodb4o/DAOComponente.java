package daodb4o;

import java.util.List;

import com.db4o.ObjectSet;
//import com.db4o.query.Candidate;
//import com.db4o.query.Evaluation;
import com.db4o.query.Query;

import modelo.Componente;

public class DAOComponente extends DAO<Componente> {
	
	//read através da descrição
	public Componente readPorDescricao (Object chave) {
		String descricao = (String) chave; // Casting para o tipo da chave
	    Query q = manager.query();
	    q.constrain(Componente.class);
	    q.descend("descricao").constrain(descricao).like();
	    ObjectSet<Componente> resultados = q.execute();

	    if (!resultados.isEmpty())
	        return resultados.get(0);
	    else
	        return null;
	}
	
	//read através do id
	public Componente read (Object chave) {
		int idComp = (int) chave;	//casting para o tipo da chave
		Query q = manager.query();
		q.constrain(Componente.class);
		q.descend("id").constrain(idComp);
		List<Componente> resultados = q.execute();
		if (resultados.size()>0)
			return resultados.get(0);
		else
			return null;
	}
	
	//metodo da classe DAO sobrescrito DAOAluguel para
	//criar "id" sequencial 
	public void create(Componente obj){
		int novoid = super.gerarId();  	//gerar novo id da classe
		obj.setId(novoid);			//atualizar id do objeto antes de grava-lo no banco
		manager.store( obj );
	}


	/**********************************************************
	 * 
	 * TODAS AS CONSULTAS DE COMPONENTE
	 * 
	 **********************************************************/
	
	
	
	//Pesquisa todos os Componentes no banco
	
	public  List<Componente> readAll(int estoque) {
		Query q = manager.query();
		q.constrain(Componente.class);
		List<Componente> result = q.execute();
		return result;
	}
	//------------------------------------------------
	
	
	//Pesquisa todos os Componentes cuja quantidade de peças no estoque seja maior que um número determinado
	
	public  List<Componente> readEstoqueGreaterThan(int estoque) {
		Query q = manager.query();
		q.constrain(Componente.class);
		q.descend("estoque").constrain(estoque).greater();		//insensitive
		List<Componente> result = q.execute();
		return result;
	}
	//------------------------------------------------
	
	//Pesquisa todos os Componentes cujo estoque seja maior que um número determinado
	
	public  List<Componente> readEstoqueLowerThan(int estoque) {
		Query q = manager.query();
		q.constrain(Componente.class);
		q.descend("estoque").constrain(estoque).smaller();		//insensitive
		List<Componente> result = q.execute();
		return result;
	}
	//------------------------------------------------	
	
	
	//Pesquisa os 3 Componentes mais caros do banco
	
	public List<Componente> getTop3ComponentesMaisCaros() {
	    Query q = manager.query();
	    q.constrain(Componente.class);
	    q.descend("preco").orderDescending();
	    List<Componente> result = q.execute();
	    
	    if (result.size() > 3) {
	        result = result.subList(0, 3);
	    }
	    
	    return result;
	}
	
	//Retornar id do Componente
	public int acharIdComp(String descricao) {
	    DAOComponente daoComp = new DAOComponente();
	    Componente componente = daoComp.readPorDescricao(descricao); // Lê o componente com base na descrição
	    if (componente != null) {
	        return componente.getId(); // Retorna o ID do componente encontrado
	    } else {
	        // Se nenhum componente com a descrição especificada for encontrado, pode retornar um valor padrão ou lançar uma exceção, dependendo dos requisitos do seu aplicativo.
	        // Por exemplo, você pode lançar uma exceção ou retornar -1 para indicar que o componente não foi encontrado.
	        return -1; // Ou você pode lançar uma exceção aqui, se preferir.
	    }
	}

	public List<Componente> listarComponentesComEstoqueAcimaDe(int quantidade) {
		Query q = manager.query();
	    q.constrain(Componente.class);
	    q.descend("estoque").constrain(quantidade).greater();
	    List<Componente> resultados = q.execute();
	    return resultados;
	}
	
}
