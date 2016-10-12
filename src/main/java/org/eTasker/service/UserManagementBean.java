package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;
import org.eTasker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementBean implements UserManagementService {
	
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
		if (userRepository.findByEmail(user.getEmail()) != null) {
			return null;
		}
		return userRepository.save(user);
	}

	@Override
	public User update(User user) {
		User userToUpdate = findOne(user.getId());
		if (userToUpdate == null) {
			return null;
		}
		userToUpdate.setCompanyname(user.getCompanyname());
		//userToUpdate.setEmail(user.getEmail());
		userToUpdate.setName(user.getName());
		userToUpdate.setPassword(user.getPassword());
		return userRepository.save(userToUpdate);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public void validate(Long id) {
		User user = findOne(id);
		user.setIsver(true);
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
