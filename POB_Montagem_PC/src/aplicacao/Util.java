package aplicacao;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Componente;
import modelo.Orcamento;


public class Util {
	
		private static ObjectContainer manager;
		
		public static ObjectContainer conectarBanco(){
			if (manager != null)
				return manager;		//ja tem uma conexao
			
			//---------------------------------------------------------------
			//configurar, criar e conectar banco local (na pasta do projeto
			//---------------------------------------------------------------
			
			EmbeddedConfiguration config =  Db4oEmbedded.newConfiguration(); 
			config.common().messageLevel(0);  // mensagens na tela 0(desliga),1,2,3...
			
			// habilitar cascata na alteração, remoção e leitura
			config.common().objectClass(Cliente.class).cascadeOnDelete(false);;
			config.common().objectClass(Cliente.class).cascadeOnUpdate(true);;
			config.common().objectClass(Cliente.class).cascadeOnActivate(true);
			config.common().objectClass(Orcamento.class).cascadeOnDelete(false);;
			config.common().objectClass(Orcamento.class).cascadeOnUpdate(true);;
			config.common().objectClass(Orcamento.class).cascadeOnActivate(true);
			config.common().objectClass(Componente.class).cascadeOnDelete(false);;
			config.common().objectClass(Componente.class).cascadeOnUpdate(true);;
			config.common().objectClass(Componente.class).cascadeOnActivate(true);
			
			//conexao local
			manager = Db4oEmbedded.openFile(config, "banco.db4o");
			return manager;
		}
	
	
	public static void desconectar() {
		if(manager!=null) {
			manager.close();
			manager=null;
		}
	}
	
	
	public static int gerarIdOrcamento() {
		if(manager.query(Orcamento.class).size()==0) {
			//classe nao registrada no banco
			return 1;
		} else {
			Query q = manager.query();
			q.constrain(Orcamento.class);
			q.descend("id").orderDescending();
			List<Orcamento> resultados = q.execute();
			Orcamento orcamento = resultados.get(0);    //max
			return orcamento.getId() + 1;
		}
	}
	
	
	
	public static int gerarIdComponente() {
		if(manager.query(Componente.class).size()==0)
			//classe nao registrada no banco
			return 1;
		
		Query q = manager.query();
		q.constrain(Componente.class);
		q.descend("id").orderDescending();
		List<Componente> resultados = q.execute();
		
		if(resultados.size()>0) {
			Componente componente = resultados.get(0);    //max
			return componente.getId() + 1;
		}
		else
			return 1; 	//nenhum objeto aluguel encontrado
	}
	
}
