package io.nem.monitor;

import io.nem.monitor.handler.sample.CustomTransactionMonitorHandler1;
import io.nem.monitor.handler.sample.TransactionMonitorHandler;


/**
 * The Class MonitorIncomingTransaction.
 */
public class MonitorIncomingTransaction {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		WsNemTransactionMonitor.networkName("testnet").host("23.228.67.85").port("7890").wsPort("7778")
				.addressToMonitor("TDZQB4XV6ZQ3X7PXGWYL4KWEY7DY2RGSLIN7PA3F")
				.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitorHandler())
				.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new CustomTransactionMonitorHandler1())
				.monitor();

	}
}
