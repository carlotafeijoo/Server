package ResidencialArea;

import java.util.Objects;

public class FamilyContact {
	private String name;
	private String address;
	private int phone;
	private String email;
	private int family_id;
	private int elderly_id;

	public FamilyContact() {
		super();
	}
	public FamilyContact(String name, String address, int phone, String email, int family_id, int elderly_id) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.family_id = family_id;
		this.elderly_id = elderly_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getFamily_id() {
		return family_id;
	}
	public void setFamily_id(int family_id) {
		this.family_id = family_id;
	}
	public int getElderly_id() {
		return elderly_id;
	}
	public void setElderly_id(int elderly_id) {
		this.elderly_id = elderly_id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(address, elderly_id, email, family_id, name, phone);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FamilyContact other = (FamilyContact) obj;
		return Objects.equals(address, other.address) && elderly_id == other.elderly_id
				&& Objects.equals(email, other.email) && family_id == other.family_id
				&& Objects.equals(name, other.name) && phone == other.phone;
	}
	@Override
	public String toString() {
		return "Contact [name=" + name + ", address=" + address + ", phone=" + phone + ", email=" + email
				+ ", family_id=" + family_id + ", elderly_id=" + elderly_id + "]";
	}
	
	
}

