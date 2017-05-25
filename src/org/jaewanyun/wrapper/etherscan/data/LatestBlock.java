package org.jaewanyun.wrapper.etherscan.data;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Latest block data
 */
public class LatestBlock implements AutoDataAdder {

	public String blockNumber;
	public String timeStamp;
	public String blockReward;

	public LatestBlock() {}

	public LatestBlock(String blockNumber, String timeStamp, String blockReward) {
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.blockReward = blockReward;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("blockNumber"))
			this.blockNumber = data.getValue().toString();
		else if(data.getName().toString().equals("timeStamp"))
			this.timeStamp = data.getValue().toString();
		else if(data.getName().toString().equals("blockReward"))
			this.blockReward = data.getValue().toString();

	}

	@Override
	public String toString() {
		return "{\"blockNumber\":" + blockNumber + "," +
				"\"timeStamp\":" + timeStamp + "," +
				"\"blockReward\":" + blockReward + "}";
	}

}
