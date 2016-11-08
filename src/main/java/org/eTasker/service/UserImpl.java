package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.repository.UserRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		if (users == null) {
			LOGGER.debug("Failed to retrieve users");
		}
		LOGGER.info("Users: " + users);
		return users;
	} 
	
	@Override
	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User create(User user) {
		User newUser = userRepository.save(user);
		if (newUser == null) {
			LOGGER.debug("Failed create new user: " + JsonBuilder.build(user));
		}
		LOGGER.debug("Created new user: " + JsonBuilder.build(newUser));
		return newUser;
	}

	@Override
	public User update(User user, String email) {
		User userUpdate = findByEmail(email);
		if (userUpdate == null) {
			LOGGER.info("User with email=" + email + " not exists");
			return null;
		}
		if (user.getName() != null && !user.getName().isEmpty()) {
			userUpdate.setName(user.getName());
			LOGGER.info("User with email=" + email + " updated name: " + user.getName());
		}
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			userUpdate.setEmail(user.getEmail());
			LOGGER.info("User with email=" + email + " updated email: " + user.getEmail());
		}
		LOGGER.info("User with email: " + email + " updated");
		return userRepository.save(userUpdate);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
		LOGGER.info("Deleted user: " + user.getEmail());
	}

	@Override
	public User validate(Long id) {
		User user = findOne(id);
		if (user == null) {
			LOGGER.debug("Failed validate id: " + id);
			return user;
		}
		user.setIsver(true);
		LOGGER.info("Id: " + id + " validated");
		return userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			LOGGER.debug("Not found user:" + email);
		}
		LOGGER.debug("Found user:" + email);
		return user;
	}

	@Override
	public User changePassword(String email, String currentPassword, String newPassword) {
		User user = findByEmail(email);
		if (user == null) {
			LOGGER.debug("Failed change password user:" + email + ", user not exists");
			return null;
		}
		if (!user.getPassword().equals(currentPassword)) {
			LOGGER.debug("Failed change password user:" + email + ", given currentpassword:" + currentPassword + 
					" doesn't match with existing password=" + user.getPassword());
			return null; 
		}
		user.setPassword(newPassword);
		LOGGER.info("Password changed user: " + user.getEmail() + " successful");
		return userRepository.save(user);
	}
}
