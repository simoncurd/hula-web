package com.hula.web.service.session;

import com.hula.web.model.HulaWebContext;
import com.hula.web.model.SessionContainer;

/**
 * Service for interacting with a session
 */
public interface SessionService
{

	/**
	 * Retrieve the session for the given session ID
	 * 
	 * @param sessionId
	 * @return
	 */
	SessionContainer getSession(boolean create, HulaWebContext hctx);

	/**
	 * Save the session
	 * 
	 * @param sessionContainer
	 */
	void saveSession(HulaWebContext hctx);

	/**
	 * Delete the session
	 * 
	 * @param sessionContainer
	 */
	void deleteSession(HulaWebContext hctx);
}
