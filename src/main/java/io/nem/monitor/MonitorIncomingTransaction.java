package io.nem.monitor;

import io.nem.monitor.handler.TransactionMonitorHandler;

public class MonitorIncomingTransaction {

	public static void main(String[] args) {
		WsNemTransactionMonitor.networkName("mijinnet").host("a1.dfintech.com").port("7895").wsPort("7778")
				.addressesToMonitor("MAWTL5CF4M3CU5HSFW6QV2VIEITI4Y6D34VSVXU6","MDVJCH6F5FXVUOFCC3PZTSXPQNPCULYQMWEGAOOW")
				.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitorHandler())
				.monitor();
	}
}
