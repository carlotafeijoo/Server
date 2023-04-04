package ResidencialArea;

import java.util.Date;

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



}

