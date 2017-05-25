package org.jaewanyun.wrapper.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jaewanyun.wrapper.Utility;
import org.jaewanyun.wrapper.etherscan.Etherscan;
import org.jaewanyun.wrapper.etherscan.data.Block;

/**
 * Write to files all block information, including every transaction that occurred on the Ethereum network since its inception, from 2015-07-30 to the latest block
 *
 * Warning; each block produces one file
 */
public class EthBlockchainImporter {

	public static final String DIRECTORY = "C:\\PATH\\TO\\FILE";

	public static void main(String[] args) {

		importBlockchain();

		//		System.out.println(checkBlockchain());

	}

	public static void importBlockchain() {

		Etherscan.Geth myGeth = new Etherscan.Geth();

		int blockNumber = myGeth.blockNumber();

		for(int j = 3556652; j < blockNumber; j++) {
			Block block = myGeth.blockByNumber(j);
			Utility.write(block.toString(), 8192, new File(DIRECTORY), Integer.toString(j), ".txt");
		}

	}

	public static boolean checkBlockchain() {

		Etherscan.Geth myGeth = new Etherscan.Geth();

		int blockNumber = myGeth.blockNumber();

		for(int j = 0; j < blockNumber; j++) {
			File file = new File(DIRECTORY, Integer.toString(j) + ".txt");

			try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
				if(!reader.readLine().startsWith("\"author"))
					return false;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		return true;

	}

}

