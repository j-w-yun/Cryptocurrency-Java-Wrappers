package org.jaewanyun.wrapper.etherscan.data;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Contract execution status data
 */
public class ContractExecutionStatus implements AutoDataAdder {

	public boolean isError;
	public String errDescription;

	public ContractExecutionStatus() {}

	public ContractExecutionStatus(boolean isError, String errDescription) {
		this.isError = isError;
		this.errDescription = errDescription;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("isError"))
			this.isError = data.getValue().toString().equals("1") ? true : false;
		else if(data.getName().toString().equals("errDescription"))
			this.errDescription = data.getValue().toString();

	}

	@Override
	public String toString() {
		return "{\"isError\":" + isError + "," + "\"errDescription\":" + errDescription + "}";
	}

}
