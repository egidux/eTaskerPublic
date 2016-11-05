package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.repository.UserRepository;
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
		return userRepository.findAll();
	}
	
	@Override
	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	public User update(User user, String email) {
		User userUpdate = findByEmail(email);
		if (userUpdate == null) {
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
		return userRepository.save(userUpdate);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public User validate(Long id) {
		User user = findOne(id);
		if (user == null) {
			return null;
		}
		user.setIsver(true);
		return userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User changePassword(String email, String currentPassword, String newPassword) {
		User user = findByEmail(email);
		if (user == null) {
			LOGGER.debug("Failed change password for user:" + email + ", user not exists");
			return null;
		}
		if (!user.getPassword().equals(currentPassword)) {
			LOGGER.debug("Failed change password for user:" + email + ", given currentpassword:" + currentPassword + 
					" doesn't match with existing password=" + user.getPassword());
			return null; 
		}
		user.setPassword(newPassword);
		return userRepository.save(user);
	}
}
