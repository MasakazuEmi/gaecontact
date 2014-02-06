package info.myspoon.gae.contacts.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import info.myspoon.gae.contacts.dao.annotaion.Column;
import info.myspoon.gae.contacts.dao.annotaion.Entity;
import info.myspoon.gae.contacts.dao.annotaion.Id;

@Entity("contact")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ContactEntity {

	@Id("id")
	@XmlElement
	private int id;
	@Column("name")
	@XmlElement
	private String name;
	@Column("kana")
	@XmlElement
	private String kana;
	@Column("tel")
	@XmlElement
	private String tel;
	@Column("email")
	@XmlElement
	private String email;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKana() {
		return kana;
	}
	public void setKana(String kana) {
		this.kana = kana;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
