package main;

public class Item {
	private Cliente cliente;

	public Item(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getChave() {
		return this.cliente.getNome();
	}
}