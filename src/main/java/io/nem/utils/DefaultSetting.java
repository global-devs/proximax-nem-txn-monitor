package io.nem.utils;

import org.nem.core.model.NetworkInfos;



/**
 * The Class DefaultSetting.
 */
public class DefaultSetting {

	/**
	 * set default network.
	 */
	public static void setDefaultNetwork() {
		NetworkInfos.setDefault(NetworkInfos.fromFriendlyName("mijinnet"));
	}
	
	/**
	 * set host, port and websocket port.
	 *
	 * @param host the host
	 * @param port the port
	 * @param wsPort the ws port
	 */
	public static void setHostAndPort(String host, String port, String wsPort) {
		if(host!=null)
			HttpClientUtils.defaultHost = host;
		if(port!=null)
			HttpClientUtils.defaultPort = port;
		if(wsPort!=null)
			HttpClientUtils.defaultWsPort = wsPort;
	}
	
	/**
	 * get WebSocket Default URI.
	 *
	 * @return the ws uri
	 */
	public static String getWsUri(){
		StringBuilder builder = new StringBuilder();
		builder.append("ws://")
			.append(HttpClientUtils.defaultHost)
			.append(":")
			.append(HttpClientUtils.defaultWsPort)
			.append(Constants.URL_WS_W_MESSAGES);
		return builder.toString();
	}
}
