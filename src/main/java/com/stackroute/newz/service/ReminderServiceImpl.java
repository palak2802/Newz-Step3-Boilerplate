package com.stackroute.newz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.newz.model.Reminder;
import com.stackroute.newz.repository.ReminderRepository;
import com.stackroute.newz.service.ReminderService;
import com.stackroute.newz.util.exception.ReminderNotExistsException;

/*
 * This class is implementing the ReminderService interface. This class has to be annotated with 
 * @Service annotation.
 * @Service - is an annotation that annotates classes at the service layer, thus 
 * clarifying it's role.
 * 
 * */
@Service
public class ReminderServiceImpl implements ReminderService {

	/*
	 * Autowiring should be implemented for the ReminderRepository.
	 */
	
	@Autowired
	private ReminderRepository reminderRepo;
	/*
	 * Add a new reminder.
	 */
	public Reminder addReminder(Reminder reminder) {
		return reminderRepo.save(reminder);
	}

	/*
	 * Update an existing reminder by it's reminderId. Throw ReminderNotExistsException 
	 * if the reminder with specified reminderId does not exist.
	 */
	public Reminder updateReminder(Reminder reminder) throws ReminderNotExistsException {
		Optional<Reminder> reminderById = reminderRepo.findById(reminder.getReminderId());
		if(reminderById.isEmpty()) {
			throw new ReminderNotExistsException("Can not Update the reminder. The reminder with "+reminder.getReminderId() +" does not exists in the database.");
		}
		reminderById.get().setNews(reminder.getNews());
		reminderById.get().setSchedule(reminder.getSchedule());
		return reminderRepo.save(reminder);
	}

	/*
	 * Delete an existing reminder by it's reminderId. Throw ReminderNotExistsException if 
	 * the reminder with specified reminderId does not exist.
	 */
	public void deleteReminder(int reminderId) throws ReminderNotExistsException {
		Optional<Reminder> reminderById = reminderRepo.findById(reminderId);
		if(reminderById.isEmpty()) {
			throw new ReminderNotExistsException("Can not Delete the reminder. The reminder with "+reminderId +" does not exists in the database.");
		}
		reminderRepo.deleteById(reminderId);
	}

	/*
	 * Retrieve an existing reminder by it's reminderId. Throw ReminderNotExistsException 
	 * if the reminder with specified reminderId does not exist.
	 */
	public Reminder getReminder(int reminderId) throws ReminderNotExistsException {
		Optional<Reminder> reminderById = reminderRepo.findById(reminderId);
		if(reminderById.isEmpty()) {
			throw new ReminderNotExistsException("Can not Retreive the reminder. The reminder with "+reminderId +" does not exists in the database.");
		}
		return reminderById.get();
	}

	/*
	 * Retrieve all existing reminders
	 */
	public List<Reminder> getAllReminders() {
		return reminderRepo.findAll();
	}

}
