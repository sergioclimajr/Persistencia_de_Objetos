package aplicacao;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import modelo.Cliente;
import modelo.Componente;
import modelo.Orcamento;


public class Cadastrar {
	
	protected ObjectContainer manager;
	
	public Cadastrar(){
		manager = Util.conectarBanco();
		cadastrar();
		Util.desconectar();
		
		System.out.println("Fim da aplicacao");
	}
	
	public void cadastrar() {
		System.out.println("Cadastrando...");
		
		//criando os componentes
		
		
		Componente comp1 = new Componente(Util.gerarIdComponente(), "Placa-mãe", 787.00, 40);
		manager.store(comp1);
		manager.commit();
		
		Componente comp2 = new Componente(Util.gerarIdComponente(), "Processador Core i7", 1491.00, 20);
		manager.store(comp2);
		manager.commit();
		
		Componente comp3 = new Componente(Util.gerarIdComponente(), "Memória RAM 16GB", 260.00, 80);
		manager.store(comp3);
		manager.commit();
		
		Componente comp4 = new Componente(Util.gerarIdComponente(), "Placa de Video 8GB", 2420.00, 10);
		manager.store(comp4);
		manager.commit();
		
		Componente comp5 = new Componente(Util.gerarIdComponente(), "SSD 500GB", 150.00, 50);
		manager.store(comp5);
		manager.commit();
		
		Componente comp6 = new Componente(Util.gerarIdComponente(), "Cooler", 90.00, 25);
		manager.store(comp6);
		manager.commit();
		
		Componente comp7 = new Componente(Util.gerarIdComponente(), "Fonte", 598.00, 15);
		manager.store(comp7);
		manager.commit();
		
		Componente comp8 = new Componente(Util.gerarIdComponente(), "Mouse", 50.00, 25);
		manager.store(comp8);
		manager.commit();
		
		Componente comp9 = new Componente(Util.gerarIdComponente(), "Gabinete", 900.00, 5);
		manager.store(comp9);
		manager.commit();
		
		Componente comp10 = new Componente(Util.gerarIdComponente(), "Monitor 29pol", 1120.00, 10);
		manager.store(comp10);
		manager.commit();
		
		
		
		//realizando pesquisa dos COMPONENTES cadastrados
		
		Query q1 = manager.query();
		q1.constrain(Componente.class);
		List<Componente> componentes = q1.execute();		
		
		
		//criando os clientes, seus respectivos orçamentos e os componentes de cada um
		
		Cliente c1;

		c1 = new Cliente("12345678910", "Manuel Bandeira");
		manager.store(c1);
		manager.commit();
		
		Orcamento orc1 = new Orcamento(Util.gerarIdOrcamento(), "15/02/2023");
		c1.adicionar(orc1);
		manager.store(c1);
		manager.commit();
		
		Orcamento orc2 = new Orcamento(Util.gerarIdOrcamento(), "16/06/2023");
		c1.adicionar(orc2);
		manager.store(c1);
		manager.commit();
		
		orc1.adicionar(componentes.get(2));
		orc1.adicionar(componentes.get(4));
		orc1.adicionar(componentes.get(5));
		orc2.adicionar(componentes.get(9));
		
		manager.store(orc1);
		manager.store(orc2);
		
		manager.commit();
		
		//---
		
		Cliente c2;

		c2 = new Cliente("12345678911", "Vinicius de Morais");
		manager.store(c2);
		manager.commit();
		
		Orcamento orc3 = new Orcamento(Util.gerarIdOrcamento(), "17/03/2023");
		c2.adicionar(orc3);
		manager.store(c2);
		manager.commit();
		
		Orcamento orc4 = new Orcamento(Util.gerarIdOrcamento(), "18/04/2023");
		c2.adicionar(orc4);
		manager.store(c2);
		manager.commit();
		
		Orcamento orc5 = new Orcamento(Util.gerarIdOrcamento(), "19/07/2023");
		c2.adicionar(orc5);
		manager.store(c2);
		manager.commit();
		
		orc3.adicionar(componentes.get(0));
		orc3.adicionar(componentes.get(3));
		orc4.adicionar(componentes.get(7));
		orc4.adicionar(componentes.get(8));
		orc4.adicionar(componentes.get(2));
		orc5.adicionar(componentes.get(1));
		
		manager.store(orc3);
		manager.store(orc4);
		
		manager.commit();
		
		//---
		
		Cliente c3;

		c3 = new Cliente("12345678912", "Ariano Suassuna");
		manager.store(c3);
		manager.commit();
		
		Orcamento orc6 = new Orcamento(Util.gerarIdOrcamento(), "21/07/2023");
		c3.adicionar(orc6);
		manager.store(c3);
		manager.commit();
		
		orc6.adicionar(componentes.get(1));
		manager.store(orc6);
		
		manager.commit();
		
	}
	
	public static void main(String[] args) {
		new Cadastrar();
	}

}
