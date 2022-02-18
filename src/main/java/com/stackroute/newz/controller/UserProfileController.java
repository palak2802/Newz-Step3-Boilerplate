package com.stackroute.newz.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.newz.model.UserProfile;
import com.stackroute.newz.service.UserProfileService;
import com.stackroute.newz.util.exception.UserProfileAlreadyExistsException;
import com.stackroute.newz.util.exception.UserProfileNotExistsException;


/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 * 
 * Please note that the default path to use this controller should be "/api/v1/news"
 */
@RestController
@RequestMapping("/api/v1")
public class UserProfileController {

	/*
	 * Autowiring should be implemented for the UserProfileService. Please note that we
	 * should not create any object using the new keyword
	 */
	@Autowired
	private UserProfileService userProfileService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * Define a handler method which will register a userProfile by reading the Serialized
	 * UserProfile object from request body and save the userProfile in UserProfile table in database.
	 * Please note that the userId has to be unique.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 201(CREATED) - In case of successful creation of the userProfile 
	 * 2. 409(CONFLICT) - In case of duplicate userId
	 * 
	 * This handler method should map to the URL "/api/v1/user" using HTTP POST
	 * method".
	 */
	@PostMapping("/user")
	public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) throws UserProfileAlreadyExistsException{
		try{
			for(UserProfile allUserProfile: userProfileService.getAllUserProfiles()) {
				if(allUserProfile.getUserId() == userProfile.getUserId()) {
					logger.info("In controller - {}", "User ID "+ userProfile.getUserId() + " already exists.");
					return new ResponseEntity<UserProfile>(HttpStatus.CONFLICT);
				}
			}
			userProfileService.registerUser(userProfile);
		}
		catch(Exception e) {
			throw new UserProfileAlreadyExistsException("User Profile already exists in DB.");
		}
		logger.info("In controller - {}", "User Profile created: " +userProfile);
		return new ResponseEntity<UserProfile>(userProfile, HttpStatus.CREATED);
	}

	/*
	 * Define a handler method which will get us all users.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If all news found successfully. 
	 * 
	 * 
	 * 
	 * This handler method should map to the URL "/api/v1/user" using HTTP GET
	 * method.
	 */
	@GetMapping("/user")
	public ResponseEntity<List<UserProfile>> getAllUserProfile(){
		try {
			List<UserProfile> usersList = userProfileService.getAllUserProfiles();
			logger.info("In controller - {}", "List of all User Profiles: "+usersList);
			return new ResponseEntity<List<UserProfile>>(usersList, HttpStatus.OK);
		}catch(Exception e) {
			
		}
		return null;
	}

	/*
	 * Define a handler method which will update a specific userProfile by reading the
	 * Serialized object from request body and save the updated userProfile details in
	 * the userProfile table in database and handle UserProfileNotExistsException as well. 
	 * 
	 * This handler method should return any one of the status
	 * messages basis on different situations: 
	 * 1. 200(OK) - If the userProfile is updated successfully. 
	 * 2. 404(NOT FOUND) - If the userProfile with specified userId is not found. 
	 * 
	 * This handler method should map to the URL "/api/v1/user/{userId}" using HTTP PUT
	 * method, where "userId" should be replaced by a valid userId without {}
	 */
	@PutMapping("/user/{userId}")
	public ResponseEntity<UserProfile> updateUserProfile(@PathVariable("userId") String userId, @RequestBody UserProfile userProfile) throws UserProfileNotExistsException{
		try {	
		if(userProfileService.getUserProfile(userId) != null) {
				userProfileService.updateUserProfile(userProfile, userId);
				logger.info("In controller - {}", "User Profile updated for User Id - " +userId + " is: " +userProfile);
				return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);
			}
		}catch(Exception e){
			throw new UserProfileNotExistsException("Can not Update the User Profile as it does not exists in the database.");
		}
		logger.info("In controller - {}", "User Profile not found for User Id - " +userId);
		return new ResponseEntity<UserProfile>(HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will get us the user by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user found successfully. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	 * 
	 * 
	 * This handler method should map to the URL "/api/v1/user/{userId}" using HTTP GET
	 * method, where "userId" should be replaced by a valid userId without {}
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<UserProfile> getUserProfileById(@PathVariable("userId") String userId){
		try {
		UserProfile userProfileById = userProfileService.getUserProfile(userId);
		if(userProfileById != null) {
			logger.info("In controller - {}", "User Profile retreived for User Id - " +userId + " is: " +userProfileById);
			return new ResponseEntity<UserProfile>(userProfileById, HttpStatus.OK);
		}}
		catch(Exception e) {
			e.printStackTrace();
		}
			logger.info("In controller - {}", "User Profile not found for User Id - " +userId);
			return new ResponseEntity<UserProfile>(HttpStatus.NOT_FOUND);
	}

	/*
	 * Define a handler method which will delete a userProfile from the database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the userProfile deleted successfully. 
	 * 2.404(NOT FOUND) - If the userProfile with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/user/{userId}" using HTTP
	 * Delete method" where "userId" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<UserProfile> deleteUserProfile(@PathVariable("userId") String userId)  {
		try {	
		if(userProfileService.getUserProfile(userId) != null) {
				userProfileService.deleteUserProfile(userId);
				logger.info("In controller - {}", "User Profile deleted for User Id - " +userId);
				return new ResponseEntity<UserProfile>(HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		logger.info("In controller - {}", "User Profile not found for User Id - " +userId);
		return new ResponseEntity<UserProfile>(HttpStatus.NOT_FOUND);
	}
}
