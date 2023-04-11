package ResidencialArea;

import java.util.Date;

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

	//Constructor with all the class Task´s attributes as parameters
	public Staff (String name, String field, Integer phone, Date dob, String adress) {
		super();
		this.name = name;
		this.field=field;
		this.phone=phone;
		this.dob=dob;
		this.address;
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
}


