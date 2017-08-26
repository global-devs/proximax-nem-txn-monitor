package io.nem.monitor.handler;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.nem.core.model.mosaic.MosaicFeeInformation;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.ncc.TransactionMetaData;
import org.nem.core.model.primitive.Amount;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import io.nem.model.ChannelHandleModel;

public class WsMonitorImcomingHandler implements StompSessionHandler {

	private String address = null;
	private List<ChannelHandleModel> channelHandleModels;
	public WsMonitorImcomingHandler(String address) {
		this.address = address;
	}
	
	public WsMonitorImcomingHandler(String address, List<ChannelHandleModel> channelHandleModels) {
		this.address = address;
		this.channelHandleModels = channelHandleModels;
	}

	@Override
	public Type getPayloadType(StompHeaders arg0) {
		System.out.println("getPayloadType");
		return null;
	}

	@Override
	public void handleFrame(StompHeaders arg0, Object arg1) {
		System.out.println("handleFrame");

	}

	@Override
	public void afterConnected(StompSession session, StompHeaders arg1) {
		String account = "{\"account\":\"" + this.address + "\"}";
		System.out.println(account);
		// the address should send to the server before subscribing
		session.send("/w/api/account/get", account);
		
		for(ChannelHandleModel channelHandleModel: this.channelHandleModels) {
			session.subscribe(channelHandleModel.getChannel() + "/" + this.address, channelHandleModel.getFrameHandler());
		}
//		
//		session.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS + "/" + this.address, new StompFrameHandler() {
//			public Type getPayloadType(StompHeaders stompHeaders) {
//				return String.class;
//	        }
//	        public void handleFrame(StompHeaders stompHeaders, Object result) {
//	        	System.out.println("handleFrame1");
//	        	System.out.println(result.toString());
//	        }
//		});
//		session.subscribe("/unconfirmed/" + this.address, new StompFrameHandler() {
//			public Type getPayloadType(StompHeaders stompHeaders) {
//				return String.class;
//	        }
//	        public void handleFrame(StompHeaders stompHeaders, Object result) {
//	        	System.out.println("handleFrame2");
//	        	
//	        	System.out.println(result.toString());
//	        }
//		});

	}

	@Override
	public void handleException(StompSession arg0, StompCommand arg1, StompHeaders arg2, byte[] arg3, Throwable arg4) {
		System.out.println("Exception");
		arg4.printStackTrace();

	}

	@Override
	public void handleTransportError(StompSession arg0, Throwable arg1) {
		System.out.println("Error");

	}

}
