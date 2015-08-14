package br.feevale.client;

import java.sql.Timestamp;

import br.feevale.utils.DontUpdate;
import br.feevale.utils.IgnoreField;
import br.feevale.utils.TableName;

@TableName("Clients")
public class Client {
	
	@DontUpdate
	private Integer id;
	private String name;
	private Timestamp birthday;
	private Double creditLimit;
	private Double height;
	private String gender;
	private byte[] image;
	
	@IgnoreField
	private String extra;
	
	public double getIMC() {
		if (this.height != null) {
			double result = (height * 1.88) / Math.sqrt(2);
			return result;
		}
		return 0;
	}
	

	public Integer getId() {
		return id;
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

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	
}
