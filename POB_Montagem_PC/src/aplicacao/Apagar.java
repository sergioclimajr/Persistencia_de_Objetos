package aplicacao;

import java.util.List;
import aplicacao.Util;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Componente;
import modelo.Orcamento;

public class Apagar {
	protected ObjectContainer manager;

	public Apagar() {
		try {
			manager = Util.conectarBanco();
			System.out.println("excluindo");
			
			
			Query q = manager.query();
			q.constrain(Cliente.class);
			q.descend("cpf").constrain("12345678911");
			List<Cliente> resultados = q.execute();
			
			if(resultados.size()>0) {
				Cliente cliente = resultados.get(0);
				List<Orcamento> lista = cliente.listarOrcamentos();
				for (Orcamento orc : lista) {
					if(orc != null) {
						orc.getComponentes().clear();
						manager.delete(orc);
					}
				}
				lista.clear();
				
				manager.delete(cliente);
				manager.commit();
			}
			
			
			
			//---> APAGAR TODOS OS CLIENTES cadastrados
			
			
			Query q1 = manager.query();
			q1.constrain(Cliente.class);
			List<Cliente> resultados1 = q1.execute();
			if(resultados1.size() > 0) {
				for(Cliente cli : resultados1)
					manager.delete(cli);
				manager.commit();
			}
			
			
			
			//---> APAGAR TODOS OS COMPONENTES cadastrados 
			
			
			Query q2 = manager.query();
			q2.constrain(Componente.class);
			List<Componente> resultados2 = q2.execute();
			
			if(resultados2.size() > 0) {
				for(Componente comp : resultados2)
					manager.delete(comp);
				manager.commit();
			}
			
			
			
			//---> APAGAR ORCAMENTOS ORFAOS 
			
			
			Query q3 = manager.query();
			q3.constrain(Orcamento.class);
			List<Orcamento> resultados3 = q3.execute();
			
			if(resultados3.size() > 0) {
				for(Orcamento orc : resultados3)
					manager.delete(orc);
				manager.commit();
			}
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		Util.desconectar();
		System.out.println("\nfim do programa !");
	}
	
	
	public static void main(String[] args) {
		new Apagar();
	}
}
