package modelo;

import java.util.List;
import java.util.ArrayList;


public class Orcamento {
	
	private int id;
	private String data;
	private Cliente cliente;
	private Double valor = 0.00;
	private List<Componente> componentes = new ArrayList<Componente>();
	
	public Orcamento(int id, String data) {
		this.id = id;
		this.data = data;
	}
	
	public Orcamento() {
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		cliente.listarOrcamentos().add(this);
	}
	
	public int getId() {
		return id;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void adicionar(Componente componente){
		this.componentes.add(componente);
		componente.adicionar(this);
		this.valor += componente.getPreco();
	}
	
	public void remover(Componente componente){
		this.componentes.remove(componente);
		componente.remover(this);
		this.valor -= componente.getPreco();
	}
	
	public Componente localizar(int idComp) {
		for (Componente componente : componentes) {
			if (componente.getId() == idComp) {
				return componente;
			}
		}
		return null;
	}
	
	public List<Componente> getComponentes(){
		return componentes;
	}
	
	@Override
	public String toString() {
		String texto = "ID nº: " + id + 
				", Data: " + data +
				", Cliente: " + cliente.getNome() + 
				", Valor: R$ " + valor;
		texto += ", Lista de Peças: ";
		for (Componente componente : componentes) {
			texto += componente.getDescricao() + ", ";
		}
		return texto;
	}
	
}
