package io.nem.monitor;

import io.nem.monitor.handler.TransactionMonitorHandler;

public class MonitorIncomingTransaction {
	//	Entry
	public static void main(String[] args) {
		WsNemTransactionMonitor.networkName("mijinnet").host("a1.dfintech.com").port("7895").wsPort("7778")
				.addressToMonitor("<address with no dash>")
				.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitorHandler())
				.monitor();
	}
}
