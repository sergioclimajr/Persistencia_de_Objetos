package modelo;

import java.util.ArrayList;
import java.util.List;


public class Cliente {
	
	private String cpf;
	private String nome;
	private List<Orcamento> orcamentos = new ArrayList<>();
	
	public Cliente (String cpf, String nome) {
		this.cpf = cpf;
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void adicionar(Orcamento orc){
		orc.setCliente(this);
		this.orcamentos.add(orc);
	}
	
	public void remover(Orcamento orc){
		orc.setCliente(null);
		this.orcamentos.remove(orc);
	}
	
	public Orcamento localizar(int idOrc) {
	    for (Orcamento orc : orcamentos) {
	        if (orc.getId() == idOrc) {
	            orc.setCliente(this); // Associe o cliente ao or�amento encontrado
	            return orc;
	        }
	    }
	    return null;
	}
	
	public List<Orcamento> listarOrcamentos() {
		return orcamentos;
	}
	
	@Override
	public String toString() {
		String texto =  "Nome: " +  nome + 
				", CPF: " + cpf;

		texto += ",  Or�amentos: ";
		
		if(orcamentos.size() > 0) {
			for(Orcamento orc : orcamentos)
				if (orc != null) {
					texto += orc.getId() + ", ";
				}
		}

		return texto;
	}
	
}
