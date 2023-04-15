package ResidencialArea;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Staff {
	//Attributes of Staff class
	private String name;
	private int staff_id;
	private String field;
	private Integer phone;
	private Date dob;
	private String address;
	private List<Elderly> elderlies; // Many to many relationship


	//Empty constructor
	public Staff() {
		super();
		elderlies= new ArrayList<Elderly>();
	}


	//Constructor with all the class Task´s attributes as parameters
	public Staff(String name, int staff_id, String field, Integer phone, Date dob, String address, 
			List<Elderly> elderlies) {
		super();
		this.name = name;
		this.staff_id = staff_id;
		this.field = field;
		this.phone = phone;
		this.dob = dob;
		this.address = address;
		this.elderlies = elderlies;
	}
	
	
	public int getStaff_id() {
		return staff_id;
	}

	//Getters and setters for the attributes
	public String getName() {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}
	public void setDob (Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress (String address) {
		this.address = address;
	}

	public String getField() {
		return field;
	}

	public void setField (String field) {
		this.field = field;
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
		return "Staff [name= " + name + " whose date of birth=" + dob +
				" ,address=" +address +" ,phone=" +phone +" in the field= " +field 
				+"and takes care of " + elderlies +"]";
	}
	
}

