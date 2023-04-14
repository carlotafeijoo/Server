package ResidencialArea;

import java.util.Date;
import java.util.Objects;

public class Staff {
	//Atributes of Staff class
	private String name;
	private String field;
	private Integer phone;
	private Date dob;
	private String address;
	private List<Task> tasks; // Many to one relationship
	private List<Elderly> elderlies; // Many to many relationship


	//Empty constructor
	public Staff() {
		super();
		tasks = new ArrayList<Task>();
		elderlies= new ArrayList<Elderly>();
	}
<<<<<<< HEAD

	//Constructor with all the class Task´s attributes as parameters
	public Staff (String name, String field, Integer phone, Date dob, String adress) {
		super();
		this.name = name;
		this.field=field;
		this.phone=phone;
		this.dob=dob;
		this.address;
=======
	
	public Staff(int staff_id, String name, Date dob, String address, int phone, boolean field) {
		super();
		this.staff_id = staff_id;
		this.name = name;
		this.dob = dob;
		this.address = address;
		this.phone = phone;
		this.field = field;
	}

	public int getStaff_id() {
		return staff_id;
>>>>>>> 5f5a6c150889ea388125986443f7f02d9f09cedc
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
				" ,address=" +address +" ,phone=" +phone +" in the field= " +field +" ,performs "
				+tasks +"and takes care of " + elderlies +"]";
	}

	//Getters and setters for the attributes
	public String getName() {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getDob() {
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

<<<<<<< HEAD
	public Integer getPhone() {
		return phone;
	}

	public void setPhone (Integer phone) {
		this.phone = phone;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Elderly> getElderlies() {
		return elderlies;
	}

	public void setElderlies(List<Elderly> elderlies) {
		this.elderlies = elderlies;
	}
=======
	@Override
	public int hashCode() {
		return Objects.hash(address, dob, field, name, phone, staff_id);
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
		return Objects.equals(address, other.address) && Objects.equals(dob, other.dob) && field == other.field
				&& Objects.equals(name, other.name) && phone == other.phone && staff_id == other.staff_id;
	}

	@Override
	public String toString() {
		return "Staff [staff_id=" + staff_id + ", name=" + name + ", dob=" + dob + ", address=" + address + ", phone="
				+ phone + ", field=" + field + "]";
	}
	
>>>>>>> 5f5a6c150889ea388125986443f7f02d9f09cedc
}


