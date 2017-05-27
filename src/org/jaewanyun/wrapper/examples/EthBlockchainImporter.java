package org.jaewanyun.wrapper.examples;

import java.io.File;

import org.jaewanyun.wrapper.Utility;
import org.jaewanyun.wrapper.etherscan.Etherscan;
import org.jaewanyun.wrapper.etherscan.data.Block;

/**
 * Write to files all block information, including every transaction that occurred on the Ethereum network since its inception, from 2015-07-30 to the latest block
 *
 * Warning; each block produces one text-file
 */
public class EthBlockchainImporter {

	private static final String DIRECTORY = "C:\\PATH\\TO\\FOLDER";

	public static void main(String[] args) {

		if(args.length == 1)
			importBlockchain(Integer.parseInt(args[0])); // May set first argument as another block number to start at
		else
			importBlockchain(0); // Initially start at block no. zero

		// TODO: test
		//		System.out.println(checkBlockchain());

	}

	private static void importBlockchain(int startAt) {

		Etherscan.Geth myGeth = new Etherscan.Geth();

		int blockNumber = myGeth.blockNumber();

		for(int j = startAt; j < blockNumber; j++) {
			Block block = myGeth.blockByNumber(j);

			try {
				File folder = new File(DIRECTORY);
				Utility.write(block.toString(), 8192, folder, Integer.toString(j), ".txt");
			} catch(NullPointerException e) {
				System.err.println("Path to FOLDER not found. Note that text-file names will correspond to block number, regardless of FOLDER location");
				System.exit(1);
			}

		}

	}

	// TODO: test
	//	public static boolean checkBlockchain() {
	//
	//		Etherscan.Geth myGeth = new Etherscan.Geth();
	//
	//		int blockNumber = myGeth.blockNumber();
	//
	//		for(int j = 0; j < blockNumber; j++) {
	//			File file = new File(DIRECTORY, Integer.toString(j) + ".txt");
	//
	//			try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
	//				if(!reader.readLine().startsWith("\"author"))
	//					return false;
	//			} catch (FileNotFoundException e) {
	//				return false;
	//			} catch (IOException e) {
	//				return false;
	//			}
	//		}
	//
	//		return true;
	//
	//	}

}

