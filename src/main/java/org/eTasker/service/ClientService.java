package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Client;


public interface ClientService {
	List<Client> findAll();

	Client findOne(Long id);

	Client create(Client client);

	Client update(Client client, Long id);

    void delete(Client client);
    
    Client findByEmail(String email);
}
