package POJOS;

public class Schedule {
	
	
	private int staff_id;
	
	private int task_id;
	
	private String weekDay; 
	
	private int elderly_id;
	
	private int schedule_id;
	
	
	public Schedule (int staff_id, int task_id, String weekDay, int elderly_id, int schedule_id) {
		this.setStaff_id(staff_id);
		this.setTask_id(task_id);
		this.setWeekDay(weekDay);
		this.setElderly_id(elderly_id);
		this.setSchedule_id(schedule_id);
	}


	public Schedule(String weekDay, int staff_id, int task_id, int elderly_id) {
		super();
		this.elderly_id=elderly_id;
		this.staff_id=staff_id;
		this.weekDay=weekDay;
		this.task_id=task_id;
	}


	public int getTask_id() {
		return task_id;
	}


	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}


	public int getStaff_id() {
		return staff_id;
	}


	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}


	public String getWeekDay() {
		return weekDay;
	}


	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}


	public int getElderly_id() {
		return elderly_id;
	}


	public void setElderly_id(int elderly_id) {
		this.elderly_id = elderly_id;
	}


	public int getSchedule_id() {
		return schedule_id;
	}


	public void setSchedule_id(int schedule_id) {
		this.schedule_id = schedule_id;
	}
	
	
	@Override
	public String toString() {
		return "\n Schedule [day of the week" + weekDay + ", staff_id=" + staff_id + ", task_id=" + task_id + ",elderly_id="
				+ elderly_id + "]";
	}

}
