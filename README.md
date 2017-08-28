# NEM Transaction Monitor

Java library for monitoring transactions in NEM Blockchain platform

<h2>Usage</h2>

Simply add the following code on your Java Application.

```java

WsNemTransactionMonitor.networkName("mijinnet").host("a1.nem.foundation").port("7895").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y") // address to monitor
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new TransactionMonitor()) // multiple subscription and a handler
	.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new UnconfirmedTransactionMonitor())
	.monitor(); // trigger the monitoring process
			
```

<h3>Custom Transaction Monitor</h3>
You can create your own handler to handle the incoming payload. 

```java
public class CustomTransactionMonitor implements TransactionMonitorHandler {
	@Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println(payload.toString()); // handle the payload.
	}
}
```

```java
WsNemTransactionMonitor.networkName("mijinnet").host("a1.nem.foundation").port("7895").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y")
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new CustomTransactionMonitor())
	.monitor();
```

<h2>Monitor and Handle Transactions</h2>
The library uses Gson/Guava to convert JSON to Java Objects seemlessly. There are built in Java Objects that developers can use for this purpose.
TBD
<h2>Monitor and Handle MultisigTransaction</h2>
TBD

<sub>Copyright (c) 2017</sub>
