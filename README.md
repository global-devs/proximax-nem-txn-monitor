# NEM Transaction Monitor

Java library for monitoring transactions in NEM Blockchain platform

<h2>How to build</h2>

```bash
git clone https://github.com/NEMPH/nem-transaction-monitor.git
cd nem-transaction-monitor
mvn clean install
```

Import it as a maven dependency

```xml
<dependency>
    <groupId>io.nem</groupId>
    <artifactId>nem-transaction-monitor</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

<h2>Usage</h2>

Simply add the following code on your Java Application.

```java

WsNemTransactionMonitor.networkName("testnet").host("23.228.67.85").port("7890").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y") // address to monitor
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new CustomTransactionMonitorHandler1()) // multiple subscription and a handler
	.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new CustomTransactionMonitorHandler2())
	.monitor(); // trigger the monitoring process
			
```

You can also monitor multiple addresses

```java

WsNemTransactionMonitor.networkName("testnet").host("23.228.67.85").port("7890").wsPort("7778")
	.addressesToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y","MDYSYWVWGC6JDD7BGE4JBZMUED7BGE4JBD") // address to monitor
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new CustomTransactionMonitorHandler1()) // multiple subscription and a handler
	.subscribe(io.nem.utils.Constants.URL_WS_UNCONFIRMED, new CustomTransactionMonitorHandler2())
	.monitor(); // trigger the monitoring process
			
```

<h3>Custom Transaction Monitor</h3>
You can create your own handler to handle the incoming payload. 

```java
public class CustomTransactionMonitor extends AbstractTransactionMonitorHandler {
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
WsNemTransactionMonitor.networkName("testnet").host("23.228.67.85").port("7890").wsPort("7778")
	.addressToMonitor("MDYSYWVWGC6JDD7BGE4JBZMUEM5KXDZ7J77U4X2Y")
	.subscribe(io.nem.utils.Constants.URL_WS_TRANSACTIONS, new CustomTransactionMonitorHandler2())
	.monitor();
```


<sub>Copyright (c) 2018</sub>
