package io.nem.monitor;

import java.lang.reflect.Type;
import java.util.List;
import org.pmw.tinylog.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import io.nem.model.ChannelHandleModel;


/**
 * The Class WsMonitorIncomingSessionHandler.
 */
public class WsMonitorIncomingSessionHandler implements StompSessionHandler {

	/** The address. */
	private String address = null;
	
	/** The channel handle models. */
	private List<ChannelHandleModel> channelHandleModels;
	
	/**
	 * Instantiates a new ws monitor incoming session handler.
	 *
	 * @param address the address
	 */
	public WsMonitorIncomingSessionHandler(String address) {
		this.address = address;
	}
	
	/**
	 * Instantiates a new ws monitor incoming session handler.
	 *
	 * @param address the address
	 * @param channelHandleModels the channel handle models
	 */
	public WsMonitorIncomingSessionHandler(String address, List<ChannelHandleModel> channelHandleModels) {
		this.address = address;
		this.channelHandleModels = channelHandleModels;
	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompFrameHandler#getPayloadType(org.springframework.messaging.simp.stomp.StompHeaders)
	 */
	@Override
	public Type getPayloadType(StompHeaders arg0) {
		System.out.println("getPayloadType");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompFrameHandler#handleFrame(org.springframework.messaging.simp.stomp.StompHeaders, java.lang.Object)
	 */
	@Override
	public void handleFrame(StompHeaders arg0, Object arg1) {
		System.out.println("handleFrame");

	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompSessionHandler#afterConnected(org.springframework.messaging.simp.stomp.StompSession, org.springframework.messaging.simp.stomp.StompHeaders)
	 */
	@Override
	public void afterConnected(StompSession session, StompHeaders arg1) {
		String account = "{\"account\":\"" + this.address + "\"}";
		System.out.println(account);
		// the address should send to the server before subscribing
		session.send("/w/api/account/get", account);
		
		for(ChannelHandleModel channelHandleModel: this.channelHandleModels) {
			session.subscribe(channelHandleModel.getChannel() + "/" + this.address, channelHandleModel.getFrameHandler());
		}

		//session.subscribe("/transactions/"+this.address, new CustomTransactionMonitorHandler1());

	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompSessionHandler#handleException(org.springframework.messaging.simp.stomp.StompSession, org.springframework.messaging.simp.stomp.StompCommand, org.springframework.messaging.simp.stomp.StompHeaders, byte[], java.lang.Throwable)
	 */
	@Override
	public void handleException(StompSession arg0, StompCommand arg1, StompHeaders arg2, byte[] arg3, Throwable arg4) {
		Logger.error("Exception Occured");
		Logger.error("Stomp Session Id: " + arg0.getSessionId());
		Logger.error("Stomp Command Message Type: " + arg1.getMessageType());
		Logger.error("Stomp Headers: " + arg2.toSingleValueMap());

	}

	/* (non-Javadoc)
	 * @see org.springframework.messaging.simp.stomp.StompSessionHandler#handleTransportError(org.springframework.messaging.simp.stomp.StompSession, java.lang.Throwable)
	 */
	@Override
	public void handleTransportError(StompSession arg0, Throwable arg1) {
		Logger.error("Exception Occured");
		Logger.error("Stomp Session Id: " + arg0.getSessionId());
		Logger.error("Error Message: " + arg1.getMessage());
		arg1.printStackTrace();

	}

}
