package ResidencialArea;

import java.util.Date;
import java.util.Objects;

public class Staff {

	int staff_id;
	String name;
	Date dob;
	String address;
	int phone;
	boolean field;
	
	
	public Staff() {
		super();
	}
	
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
	}
	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public boolean isField() {
		return field;
	}
	public void setField(boolean field) {
		this.field = field;
	}

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
	
}

