package Interfaces;

import java.util.List;


import POJOS.Schedule;

public interface ScheduleManager {
	
	public void addSchedule (Schedule s);
	public List<Schedule> getDaySchedule (String weekDay);
	
	public Schedule searchScheduleById(int schedule_id) throws Exception;
	public void scheduleUpdate (Schedule schedule) ;

}
