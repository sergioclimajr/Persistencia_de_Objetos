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
			System.out.println("Alterando orçamento...");
			
			//Alterando uma peça de um orçamento (retirando um componente e colocando outro)
			//Iremos adicionar o componente "Gabinete" a um orçamento.
			
			//consultando se o componente está disponível em estoque:
			Query q1 = manager.query();
			q1.constrain(Componente.class);
			q1.descend("descricao").constrain("gabinete").like();
			q1.descend("estoque").constrain(1).greater();
			List<Componente> resultados1 = q1.execute();
			
			
			//consultando o orçamento de id nº 6
			Query q2 = manager.query();
			q2.constrain(Orcamento.class);
			q2.descend("id").constrain(6);
			List<Orcamento> resultados2 = q2.execute();
			
			if (resultados1.isEmpty()) {
			    System.out.println("Não há peça em estoque.");
			} else if (resultados2.isEmpty()) {
			    System.out.println("Orçamento não encontrado.");
			} else {
			    Orcamento orcamento = resultados2.get(0);
			    // Removendo o primeiro item da lista de componentes do orçamento
			    orcamento.remover(orcamento.getComponentes().get(0));
			    // Adicionando um novo componente
			    orcamento.adicionar(resultados1.get(0));

			    // Atualizar o orçamento no banco
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
