package aplicacao;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Orcamento;
import modelo.Componente;


public class Listar {
	protected ObjectContainer manager;

	public Listar(){
		manager = Util.conectarBanco();
		listarClientes();
		listarOrcamentos();
		listarComponentes();
		Util.desconectar();
		
		System.out.println("\n\n aviso: feche sempre o plugin OME antes de executar aplicação");
	}
	
	
	public void listarClientes(){
		System.out.println("\n\n---Listagem dos Clientes:\n");
		
		Query q = manager.query();
		q.constrain(Cliente.class);  				
		List<Cliente> resultados = q.execute();
		for(Cliente c: resultados)
			System.out.println(c);
	}
	
	
	public void listarOrcamentos(){
		System.out.println("\n\n---Listagem dos Orçamentos:\n");
		
		Query q = manager.query();
		q.constrain(Orcamento.class);  				
		List<Orcamento> resultados = q.execute();
		for(Orcamento a: resultados)
			System.out.println(a);
	}

	public void listarComponentes(){
		System.out.println("\n\n---Listagem dos Componentes:\n");
		
		Query q = manager.query();
		q.constrain(Componente.class);  				
		List<Componente> resultados = q.execute();
		for(Componente p: resultados)
			System.out.println(p);
	}


	//=================================================
	public static void main(String[] args) {
		new Listar();
	}
}

