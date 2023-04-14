package ResidencialArea;

import java.util.Objects;

public class Elderly {
<<<<<<< HEAD
   
    
=======

	int elderly_id;
	String name;
	int age;
	
	public Elderly() {
		super();
	}
	public Elderly(int elderly_id, String name, int age) {
		super();
		this.elderly_id = elderly_id;
		this.name = name;
		this.age = age;
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
	
>>>>>>> 5f5a6c150889ea388125986443f7f02d9f09cedc
}

