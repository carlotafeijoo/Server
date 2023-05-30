package POJOS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Staff")
@XmlType(propOrder= {"name","phone", "dob", "address"})
public class Staff {
	
	
	@XmlAttribute
	private String name;
	@XmlTransient //this means i dont want the id to be shown in xml
	private int staff_id;
	@XmlTransient
	private String email;
	@XmlTransient
	private String field;
	@XmlElement
	private Integer phone;
	@XmlElement
	private Date dob;
	@XmlElement
	private String address;
	@XmlTransient 
	private List<Elderly> elderlies; // Many to many relationship


	//Empty constructor
	public Staff() {
		super();
		elderlies= new ArrayList<Elderly>();
	}


	//Constructor with all the class Task´s attributes as parameters
	public Staff(String name, int staff_id, String field, Integer phone, Date dob, String address, 
			List<Elderly> elderlies, String email) {
		super();
		this.name = name;
		this.staff_id = staff_id;
		this.field = field;
		this.phone = phone;
		this.dob = dob;
		this.address = address;
		this.elderlies = elderlies;
		this.email=email;
	}
	
	public Staff(String name, Integer phone, Date dob, String address) {
		super();
		this.name = name;
		this.phone = phone;
		this.dob = dob;
		this.address = address;
	}


	public Staff(String name, Integer phone, Date dob, String address, String field, String email) {
		this.email=email;
		this.field=field;
		this.address=address;
		this.name=name;
		this.phone=phone;
		this.dob=dob;
	}
	
	

	public Staff(int staff_id, String name,  String field, Date dob, String address, Integer phone, String email) {
		this.staff_id=staff_id;
		this.email=email;
		this.field=field;
		this.address=address;
		this.name=name;
		this.phone=phone;
		this.dob=dob;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getStaff_id() {
		return staff_id;
	}


	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public String getAddress() {
		return address;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public List<Elderly> getElderlies() {
		return elderlies;
	}


	public void setElderlies(List<Elderly> elderlies) {
		this.elderlies = elderlies;
	}


	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getEmail() {
		return email;
	}


	// Has an equals (uses only name)
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Staff other = (Staff) obj;
		return Objects.equals(name, other.name);
	}


	@Override
	public String toString() {
		return "\n [Staff [name=" + name + ", staff_id=" + staff_id + ", email=" + email + ", field=" + field + ", phone="
				+ phone + ", dob=" + dob + ", address=" + address + ", elderlies=" + elderlies + "]]";
	}
	
	


	
	
}

