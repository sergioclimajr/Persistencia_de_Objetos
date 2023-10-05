package modelo;

import java.util.ArrayList;
import java.util.List;

public class Componente {
	
	private int id;
	private String descricao;
	private Double preco;
	private int estoque;
	
	public Componente(int id, String descricao, Double preco, int estoque) {
		this.id = id;
		this.descricao = descricao;
		this.preco = preco;
		this.estoque = estoque;
		
	}
	
	public Componente (String descricao, double preco, int estoque) {
		this.descricao = descricao;
		this.preco = preco;
		this.estoque = estoque;
	}
	
	public Componente (String descricao) {
		this.descricao = descricao;
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
		
	
	@Override
	public String toString() {
		
		String texto =  "ID: " +  id + 
				", Descrição: " + descricao +
				", Preço: " + preco + 
				", Estoque: " + estoque + " unid.";
		return texto;
	}
	/*
	public List<Orcamento> localizarCompEmOrcamentos(int idComp) {
	    List<Orcamento> orcamentosComEsteComponente = new ArrayList<>();
	    List<Orcamento> todosOrcamentos = new ArrayList<>();
	    for (Orcamento orc : todosOrcamentos) {
	        if (orc.getComponentes().contains(this)) {
	            orcamentosComEsteComponente.add(orc);
	        }
	    }
	    return orcamentosComEsteComponente;
	}
	*/
}
