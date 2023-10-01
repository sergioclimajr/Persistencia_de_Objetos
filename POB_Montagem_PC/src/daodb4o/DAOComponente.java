package daodb4o;

import java.util.List;
import com.db4o.query.Query;

import modelo.Componente;

public class DAOComponente extends DAO<Componente> {
	
	//o ID do componente é usado como campo unico
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
	
	
	/*-------------------------------------------------
	@SuppressWarnings("serial")
	class Filtro  implements Evaluation {
		private int n;
		public Filtro (int n) {this.n=n;}
		public void evaluate(Candidate candidate) {
			Componente comp = (Componente) candidate.getObject();
			candidate.include( comp.getEstoque()==n );
		}
	}
	*/
}
