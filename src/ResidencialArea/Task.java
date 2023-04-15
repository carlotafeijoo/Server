package ResidencialArea;

import java.util.Objects;;
public class Task {
    //Attributes of Task class
    private String description;
    private int task_id;
    
    //Empty constructor
    public Task() {
            super();
    }

    //Constructor with all the class Task´s attributes as parameters
    public Task (String description, int task_id) {
            super();
            this.description = description;
            this.task_id=task_id;
    }

    //Getters and setters for the attributes
    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }


	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
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
        return "Task [description=" + description ;
    }
    
}

