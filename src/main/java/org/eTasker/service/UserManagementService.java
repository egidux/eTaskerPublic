package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;

public interface UserManagementService {
	
	List<User> findAll();

    User findOne(Long id);

    User create(User user);

    User update(User user);

    void delete(User user);
    
    void validate(Long id);
}
