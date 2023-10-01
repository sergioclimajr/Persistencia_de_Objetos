package aplicacao;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

//import modelo.Cliente;
import modelo.Orcamento;
import modelo.Componente;

public class Alterar {
	protected ObjectContainer manager;
	
	public Alterar() {
		try {
			manager = Util.conectarBanco();
			System.out.println("Alterando or�amento...");
			
			//Alterando uma pe�a de um or�amento (retirando um componente e colocando outro)
			//Iremos adicionar o componente "Gabinete" a um or�amento.
			
			//consultando se o componente est� dispon�vel em estoque:
			Query q1 = manager.query();
			q1.constrain(Componente.class);
			q1.descend("descricao").constrain("gabinete").like();
			q1.descend("estoque").constrain(1).greater();
			List<Componente> resultados1 = q1.execute();
			
			
			//consultando o or�amento de id n� 6
			Query q2 = manager.query();
			q2.constrain(Orcamento.class);
			q2.descend("id").constrain(6);
			List<Orcamento> resultados2 = q2.execute();
			
			if (resultados1.isEmpty()) {
			    System.out.println("N�o h� pe�a em estoque.");
			} else if (resultados2.isEmpty()) {
			    System.out.println("Or�amento n�o encontrado.");
			} else {
			    Orcamento orcamento = resultados2.get(0);
			    // Removendo o primeiro item da lista de componentes do or�amento
			    orcamento.remover(orcamento.getComponentes().get(0));
			    // Adicionando um novo componente
			    orcamento.adicionar(resultados1.get(0));

			    // Atualizar o or�amento no banco
			    manager.store(orcamento);
			    manager.commit();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Util.desconectar();
		System.out.println("\nfim do programa !");
	}

	public static void main(String[] args) {
		new Alterar();
	}
}
