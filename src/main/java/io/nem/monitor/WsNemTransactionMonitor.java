package io.nem.monitor;

import java.util.ArrayList;
import java.util.List;

import org.nem.core.model.NetworkInfos;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import io.nem.model.ChannelHandleModel;
import io.nem.monitor.handler.AbstractTransactionMonitorHandler;
import io.nem.monitor.handler.sample.TransactionMonitorHandler;
import io.nem.utils.DefaultSetting;
import io.nem.utils.ScannerUtil;


/**
 * The Class WsNemTransactionMonitor.
 */
public class WsNemTransactionMonitor {

	/**
	 * Instantiates a new ws nem transaction monitor.
	 */
	private WsNemTransactionMonitor() {
	}

	/**
	 * Inits the.
	 *
	 * @return the i network name
	 */
	public static INetworkName init() {
		return new WsNemTransactionMonitor.Builder();
	}

	/**
	 * Network name.
	 *
	 * @param name the name
	 * @return the i network name
	 */
	public static INetworkName networkName(String name) {
		return new WsNemTransactionMonitor.Builder(name);
	}

	/**
	 * The Interface INetworkName.
	 */
	public interface INetworkName {
		
		/**
		 * Host.
		 *
		 * @param name the name
		 * @return the i host
		 */
		IHost host(String name);
	}

	/**
	 * The Interface IHost.
	 */
	public interface IHost {
		
		/**
		 * Port.
		 *
		 * @param name the name
		 * @return the i port
		 */
		IPort port(String name);
	}

	/**
	 * The Interface IPort.
	 */
	public interface IPort {
		
		/**
		 * Ws port.
		 *
		 * @param port the port
		 * @return the i address
		 */
		IAddress wsPort(String port);
	}

	/**
	 * The Interface IAddress.
	 */
	public interface IAddress {
		
		/**
		 * Addresses to monitor.
		 *
		 * @param addresses the addresses
		 * @return the i channel
		 */
		IChannel addressesToMonitor(List<String> addresses);

		/**
		 * Addresses to monitor.
		 *
		 * @param addresses the addresses
		 * @return the i channel
		 */
		IChannel addressesToMonitor(String... addresses);

		/**
		 * Address to monitor.
		 *
		 * @param address the address
		 * @return the i channel
		 */
		IChannel addressToMonitor(String address);
	}

	/**
	 * The Interface IChannel.
	 */
	public interface IChannel {
		
		/**
		 * Subscribe.
		 *
		 * @param channel the channel
		 * @param handler the handler
		 * @return the i channel
		 */
		IChannel subscribe(String channel, AbstractTransactionMonitorHandler handler);

		/**
		 * Monitor.
		 */
		void monitor();
	}

	/**
	 * The Class Builder.
	 */
	public static class Builder implements INetworkName, IHost, IPort, IAddress, IChannel {

		/** The network name. */
		private String networkName;
		
		/** The host. */
		private String host;
		
		/** The port. */
		private String port;
		
		/** The ws port. */
		private String wsPort;
		
		/** The address. */
		private String address;
		
		/** The addresses. */
		private List<String> addresses = new ArrayList<String>();

		/** The channel handle list. */
		private List<ChannelHandleModel> channelHandleList = new ArrayList<ChannelHandleModel>();

		/**
		 * Instantiates a new builder.
		 */
		public Builder() {
		}

		/**
		 * Instantiates a new builder.
		 *
		 * @param name the name
		 */
		public Builder(String name) {
			this.networkName = name;
			NetworkInfos.setDefault(NetworkInfos.fromFriendlyName(name));
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IChannel#subscribe(java.lang.String, io.nem.monitor.handler.AbstractTransactionMonitorHandler)
		 */
		@Override
		public IChannel subscribe(String channel, AbstractTransactionMonitorHandler handler) {
			if (handler != null) {
				if (this.addresses != null && !this.addresses.isEmpty()) {
					for (String address : this.addresses) {
						handler.setAddress(address);
						this.channelHandleList.add(new ChannelHandleModel(channel, address, handler));
					}
				}
			}
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IChannel#monitor()
		 */
		@Override
		public void monitor() {
			DefaultSetting.setHostAndPort(this.host, this.port, this.wsPort);
			if (this.addresses != null && !this.addresses.isEmpty()) {
				for (final String address : this.addresses) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							monitor(address);
						}
					}).start();
				}
			} else {
				monitor(this.address);
			}

		}

		/**
		 * Monitor.
		 *
		 * @param address the address
		 */
		private void monitor(String address) {
			final String WS_URI = DefaultSetting.getWsUri();
			// create WebSocket client
			List<Transport> transports = new ArrayList<Transport>(1);
			transports.add(new WebSocketTransport(new StandardWebSocketClient()));
			WebSocketClient transport = new SockJsClient(transports);
			WebSocketStompClient stompClient = new WebSocketStompClient(transport);
			stompClient.setMessageConverter(new StringMessageConverter());
			StompSessionHandler handler = new WsMonitorIncomingSessionHandler(address, this.channelHandleList);
			stompClient.connect(WS_URI, handler);
			// block and monitor exit action
			ScannerUtil.monitorExit();
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IAddress#addressToMonitor(java.lang.String)
		 */
		@Override
		public IChannel addressToMonitor(String address) {
			this.address = address;
			this.addresses.add(address);
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IPort#wsPort(java.lang.String)
		 */
		@Override
		public IAddress wsPort(String wsPort) {
			this.wsPort = wsPort;
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IHost#port(java.lang.String)
		 */
		@Override
		public IPort port(String port) {
			this.port = port;
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.INetworkName#host(java.lang.String)
		 */
		@Override
		public IHost host(String host) {
			this.host = host;
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IAddress#addressesToMonitor(java.util.List)
		 */
		@Override
		public IChannel addressesToMonitor(List<String> addresses) {
			this.addresses = addresses;
			return this;
		}

		/* (non-Javadoc)
		 * @see io.nem.monitor.WsNemTransactionMonitor.IAddress#addressesToMonitor(java.lang.String[])
		 */
		@Override
		public IChannel addressesToMonitor(String... addresses) {
			for (String address : addresses) {
				this.addresses.add(address);
			}
			return this;
		}
	}

}
