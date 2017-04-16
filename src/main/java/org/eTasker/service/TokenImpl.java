package org.eTasker.service;

import org.eTasker.model.Token;
import org.eTasker.repository.TokenRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TokenImpl implements TokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	public Token findOne(String worker) {
		return tokenRepository.findOne(worker);
	}
	
	@Override
	public Token create(Token token) {
		Token tokenCreated = tokenRepository.save(token);
		if (token == null) {
			LOGGER.debug("Failed create new token: " + JsonBuilder.build(token));
		}
		LOGGER.debug("Created new token: " + JsonBuilder.build(tokenCreated));
		return tokenCreated;
	}

}
