package aplicacao;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;


import modelo.Cliente;
import modelo.Orcamento;
import modelo.Componente;

public class Consultar {
	protected ObjectContainer manager;

	public Consultar() {
		try {
			manager = Util.conectarBanco();
			
			System.out.println("Consultas... \n");
			
			
			System.out.println("\nComponentes com 'Estoque' acima de 15 unidades: ");
			
			Query q = manager.query();
			q.constrain(Componente.class);
			q.descend("estoque").constrain(15).greater();
			List<Componente> resultados1 = q.execute();
			for (Componente c : resultados1)
				System.out.println(c);
						
			
			System.out.println("\nOrçamentos contendo o componente de descrição 'Monitor': ");
			
			q = manager.query();
			q.constrain(Orcamento.class);
			q.descend("componentes").constrain(new Componente("nitor").getDescricao()).like();
			List<Orcamento> resultados2 = q.execute();
			for (Orcamento o : resultados2)
				System.out.println(o);
			
			
			System.out.println("\nClientes que possuem mais de 1 Orçamento: ");
			q = manager.query();
			q.constrain(Cliente.class);
			q.constrain(new Filtro1());
			List<Cliente> resultados3 = q.execute();
			for (Cliente cli : resultados3)
				System.out.println(cli);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Util.desconectar();
		System.out.println("\nfim do programa !");
	}

	public static void main(String[] args) {
		new Consultar();
	}
}

//classe interna
class Filtro1 implements Evaluation {
	@Override
	public void evaluate(Candidate candidate) {
		Cliente cli = (Cliente) candidate.getObject();
		if(cli.listarOrcamentos().size() > 1)
			candidate.include(true);
		else
			candidate.include(false);
	}
}