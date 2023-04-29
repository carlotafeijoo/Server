package Interfaces;

import POJOS.Performs;
import POJOS.Shift;
import POJOS.Staff;
import POJOS.Task;

public interface PerformsManager {
	public void assignShift (Shift shift, Staff staff);
	public void schedule (Performs performs, Staff staff, Task task);
	public void unassignShift (Shift shift, Staff staff);
}
