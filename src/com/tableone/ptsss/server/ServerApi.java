package com.tableone.ptsss.server;

/*
 * abstract server-side api implementation that all server api classes must extend!
 */

public abstract class ServerApi<T> {
	
	public T call(Object... args) throws Exception {
		
		if (args == null) throw new Exception("Server API call received null arguments");
		
		this.parseRequest(args);
		return this.completeRequest();
		
	}
	
	//parse and validate args given by the client
	protected abstract void parseRequest(Object[] args) throws Exception ;
	
	//fulfil the api call and return a value if applicable, return null otherwise
	protected abstract T completeRequest() throws Exception;
	
}
