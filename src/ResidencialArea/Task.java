package ResidencialArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;;
public class Task {
    //Atributes of Task class
    private String description;
    private List<Staff> staffmembers; // Many to one relationship

    //Empty constructor
    public Task() {
            super();
            staffmembers = new ArrayList<Staff>();
    }

    //Constructor with all the class Task´s attributes as parameters
    public Task (String description) {
            super();
            this.description = description;
    }

    // Has an equals (uses only description)
    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
            return Objects.equals(description, other.description);
    }

    @Override
    public String toString() {
        return "Task [description=" + description + ", performs=" + staffmembers +"]";
    }

    //Getters and setters for the attributes
    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public List<Staff> getStaffmembers() {
        return staffmembers;
    }

    public void setStaffmembers(List<Staff> staffmembers) {
        this.staffmembers = staffmembers;
    }
}

