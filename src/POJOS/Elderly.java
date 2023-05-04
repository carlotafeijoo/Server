package POJOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Elderly{
	private int elderly_id;
	private String name;
	private int age;
	private List<Staff> staffmembers;
	
	
	public Elderly() {
		super();
		setStaffmembers(new ArrayList<Staff>());
	}
	
	public Elderly(int elderly_id, String name, int age) {
		super();
		this.elderly_id = elderly_id;
		this.name = name;
		this.age = age;
	}   
	
	public Elderly(String name2, int age2) {
		// TODO Auto-generated constructor stub
	}

	public int getElderly_id() {
		return elderly_id;
	}
	public void setElderly_id(int elderly_id) {
		this.elderly_id = elderly_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Elderly [elderly_id=" + elderly_id + ", name=" + name + ", age=" + age + "]";
	}
	
	public List<Staff> getStaffmembers() {
		return staffmembers;
	}

	public void setStaffmembers(List<Staff> staffmembers) {
		this.staffmembers = staffmembers;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(age, elderly_id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Elderly other = (Elderly) obj;
		return age == other.age && elderly_id == other.elderly_id && Objects.equals(name, other.name);
	}	

}

