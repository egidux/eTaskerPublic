package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Client;
import org.eTasker.repository.ClientRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientImpl implements ClientService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
	
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public List<Client> findAll() {
		List<Client> clients = clientRepository.findAll();
		if (clients == null) {
			LOGGER.debug("Failed to retrieve all clients");
		}
		LOGGER.info("Clients: " + clients);
		return clients;
	}
	
	@Override
	public Client findOne(Long id) {
		Client client = clientRepository.findOne(id);
		if (client == null) {
			LOGGER.debug("Not found client with id=" + id);
		}
		LOGGER.info("Found client with id=" + id);
		return client;
	}

	@Override
	public Client create(Client client) {
		Client newClient = clientRepository.save(client);
		if (newClient == null) {
			LOGGER.debug("Failed create new client: " + JsonBuilder.build(client));
		}
		LOGGER.debug("Created new client: " + JsonBuilder.build(newClient));
		return newClient;
	}

	@Override
	public Client update(Client client, Long id) {
		Client clientUpdate = findOne(id);
		if (clientUpdate == null) {
			LOGGER.info("Client with id=" + id + " not exists");
			return null;
		}
		if (client.getName() != null && !client.getName().isEmpty()) {
			clientUpdate.setName(client.getName());
			LOGGER.info("Client with id=" + id + " updated name= " + client.getName());
		}
		if (client.getEmail() != null && !client.getEmail().isEmpty()) {
			clientUpdate.setEmail(client.getEmail());
			LOGGER.info("Client with id=" + id + " updated email= " + client.getEmail());
		}
		if (client.getAddress()!= null && !client.getAddress().isEmpty()) {
			clientUpdate.setAddress(client.getAddress());
			LOGGER.info("Client with id=" + id + " updated addresse= " + client.getAddress());
		}
		if (client.getCode()!= null && !client.getCode().isEmpty()) {
			clientUpdate.setCode(client.getCode());
			LOGGER.info("Client with id=" + id + " updated code= " + client.getCode());
		}
		if (client.getPhone() != null && !client.getPhone().isEmpty()) {
			clientUpdate.setPhone(client.getPhone());
			LOGGER.info("Client with id=" + id + " updated phone= " + client.getPhone());
		}
		LOGGER.info("Client with id=" + id + " updated");
		return clientRepository.save(clientUpdate);
	}

	@Override
	public void delete(Client client) {
		clientRepository.delete(client);
		LOGGER.info("Deleted client id=" + client.getId());
	}

	@Override
	public Client findByEmail(String email) {
		Client client = clientRepository.findByEmail(email);
		if (client == null) {
			LOGGER.debug("Not found client with email=" + email);
		}
		LOGGER.debug("Found client with email=" + email);
		return client;
	}
	
	@Override
	public Client findByName(String name) {
		Client client = clientRepository.findByName(name);
		if (client == null) {
			LOGGER.debug("Not found client with name=" + name);
		}
		LOGGER.debug("Found client with name=" + name);
		return client;
	}
}