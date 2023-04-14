

package ResidencialArea;

import java.util.Objects;

public class Care {
	int elderly_id;
	int staff_id;
	
	public Care() {
		super();
	}
	
	
	public Care(int elderly_id, int staff_id) {
		super();
		this.elderly_id = elderly_id;
		this.staff_id = staff_id;
	}

	
	public int getElderly_id() {
		return elderly_id;
	}
	public void setElderly_id(int elderly_id) {
		this.elderly_id = elderly_id;
	}
	public int getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(elderly_id, staff_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Care other = (Care) obj;
		return elderly_id == other.elderly_id && staff_id == other.staff_id;
	}

	@Override
	public String toString() {
		return "Care [elderly_id=" + elderly_id + ", staff_id=" + staff_id + "]";
	}
}
	


