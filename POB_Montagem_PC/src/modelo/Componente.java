package modelo;

import java.util.ArrayList;

public class Componente {
	
	private int id;
	private String descricao;
	private Double preco;
	private int estoque;
	
	ArrayList <Orcamento> orcamentos = new ArrayList<Orcamento>();
	
	public Componente(int id, String descricao, Double preco, int estoque) {
		this.id = id;
		this.descricao = descricao;
		this.preco = preco;
		this.estoque = estoque;
	}
	
	public Componente (String descricao) {
		this.descricao = descricao;
	}
	
	public Componente (String descricao, double preco, int estoque) {
		this.descricao = descricao;
		this.preco = preco;
		this.estoque = estoque;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	public int getEstoque() {
		return estoque;
	}
	
	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}
	
	public ArrayList<Orcamento> getOrcamentos() {
		return orcamentos;
	}
	
	public void adicionar(Orcamento orc){
		orcamentos.add(orc);
	}
	
	public void remover(Orcamento orc){
		orcamentos.remove(orc);
	}
		
	
	@Override
	public String toString() {
		
		String texto =  "ID: " +  id + 
				", Descrição: " + descricao +
				", Preço: " + preco + 
				", Estoque: " + estoque + " unid.";
		return texto;
	}
	
}
