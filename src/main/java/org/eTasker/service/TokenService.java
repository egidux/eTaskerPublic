package org.eTasker.service;

import org.eTasker.model.Token;

public interface TokenService {

	Token findOne(String worker);
	Token create(Token token);
}
