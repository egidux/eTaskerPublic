package org.eTasker.service;

import java.util.List;

import org.eTasker.model.User;

public interface RegisterService {
	
	List<User> findAll();

    User findOne(String email);

    User create(User user);

    User update(User user);

    void delete(String user);
}
