package br.feevale.product;

import br.feevale.utils.DontUpdate;

/**
 * @question Pq eu preciso definir um valor default para o id e não para os outros (exception NullPointerException)
 */
public class Product {

	@DontUpdate
	private Integer id = 0;
	private String name;
	private Double price;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double d) {
		this.price = d;
	}
	
}
