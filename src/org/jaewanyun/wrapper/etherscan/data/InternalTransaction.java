package org.jaewanyun.wrapper.etherscan.data;

import java.math.BigInteger;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Internal transaction data
 */
public class InternalTransaction implements AutoDataAdder {

	public BigInteger blockNumber;
	public BigInteger timeStamp;
	public String hash;
	public String from;
	public String to;
	public String value;
	public BigInteger gas;
	public boolean isError;
	public String input;
	public String contractAddress;
	public BigInteger gasUsed;
	public String type;
	public String traceId;
	public String errCode;

	public InternalTransaction() {}

	public InternalTransaction(BigInteger blockNumber,
			BigInteger timeStamp,
			String hash,
			String from,
			String to,
			String value,
			BigInteger gas,
			boolean isError,
			String input,
			String contractAddress,
			BigInteger gasUsed,
			String type,
			String traceId,
			String errCode) {
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.from = from;
		this.to = to;
		this.value = value;
		this.gas = gas;
		this.isError = isError;
		this.input = input;
		this.contractAddress = contractAddress;
		this.gasUsed = gasUsed;
		this.type = type;
		this.traceId = traceId;
		this.errCode = errCode;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("blockNumber"))
			this.blockNumber = new BigInteger(data.getValue().toString());
		else if(data.getName().toString().equals("timeStamp"))
			this.timeStamp = new BigInteger(data.getValue().toString());
		else if(data.getName().toString().equals("hash"))
			this.hash = data.getValue().toString();
		else if(data.getName().toString().equals("from"))
			this.from = data.getValue().toString();
		else if(data.getName().toString().equals("to"))
			this.to = data.getValue().toString();
		else if(data.getName().toString().equals("value"))
			this.value = data.getValue().toString();
		else if(data.getName().toString().equals("gas"))
			this.gas = new BigInteger(data.getValue().toString());
		else if(data.getName().toString().equals("isError"))
			this.isError = data.getValue().toString().equals("1") ? true : false;
		else if(data.getName().toString().equals("input"))
			this.input = data.getValue().toString();
		else if(data.getName().toString().equals("contractAddress"))
			this.contractAddress = data.getValue().toString();
		else if(data.getName().toString().equals("gasUsed"))
			this.gasUsed = new BigInteger(data.getValue().toString());
		else if(data.getName().toString().equals("type"))
			this.type = data.getValue().toString();
		else if(data.getName().toString().equals("traceId"))
			this.traceId = data.getValue().toString();
		else if(data.getName().toString().equals("errCode"))
			this.errCode = data.getValue().toString();

	}

	@Override
	public String toString() {
		return "{\"blockNumber\":" + blockNumber + "," +
				"\"timeStamp\":" + timeStamp + "," +
				"\"hash\":" + hash + "," +
				"\"from\":" + from + "," +
				"\"to\":" + to + "," +
				"\"value\":" + value + "," +
				"\"contractAddress\":" + contractAddress + "," +
				"\"input\":" + input + "," +
				"\"type\":" + type + "," +
				"\"gas\":" + gas + "," +
				"\"gasUsed\":" + gasUsed + "," +
				"\"traceId\":" + traceId + "," +
				"\"isError\":" + isError + "," +
				"\"errCode\":" + errCode + "}";
	}

}
