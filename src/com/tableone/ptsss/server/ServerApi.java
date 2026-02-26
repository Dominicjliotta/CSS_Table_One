package com.tableone.ptsss.server;

/*
 * abstract server-side api implementation that all server api classes must extend!
 */

public abstract class ServerApi {

	public ServerApi(Object... args) throws Exception {
		this.parseRequest(args);
	}
	
	//parse and validate args given by the client
	protected abstract void parseRequest(Object[] args) throws Exception ;
	
	//fulfil the api call and return a value if applicable, return null otherwise
	public abstract Object call() throws Exception;
	
}
