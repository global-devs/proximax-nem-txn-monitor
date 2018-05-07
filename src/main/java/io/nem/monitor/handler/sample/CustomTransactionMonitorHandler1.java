package io.nem.monitor.handler.sample;

import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompHeaders;

import io.nem.monitor.handler.AbstractTransactionMonitorHandler;

public class CustomTransactionMonitorHandler1 extends AbstractTransactionMonitorHandler {


	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println(payload);
	}

}
