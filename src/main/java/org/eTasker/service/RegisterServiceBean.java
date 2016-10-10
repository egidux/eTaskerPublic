package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceBean implements RegisterService {
	
	@Autowired
	private UserRepository userRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findOne(String email) {
		return userRepository.findOne(email);
	}

	public User create(User user) {
		if (findOne(user.getEmail()) == null) {
			return null;
		}
		return userRepository.save(user);
	}

	public User update(User user) {
		User userToUpdate = findOne(user.getEmail());
		if (userToUpdate == null) {
			return null;
		}
		userToUpdate.setCompanyName(user.getCompanyName());
		//userToUpdate.setEmail(user.getEmail());
		userToUpdate.setName(user.getName());
		userToUpdate.setPassword(user.getPassword());
		return userRepository.save(userToUpdate);
	}

	public void delete(String user) {
		userRepository.delete(user);
	}
}
