package com.example;

public class Main {

	public static void main(String[] args) {
		/**
		 * Create BCR bank with 2 clients
		 */
		Logger log = Logger.getInstance();

		Bank bcr = new Bank("BCR Bank");
		log.log("\n\n", "log.txt");
		log.log(bcr.toString(), "log.txt");
		// log.log("Created bank: " + bcr.getBankCode());
		// Client Ionescu has an EUR and a RON account
		Client cl1 = new Client.Builder("Ionescu Ion", "Timisoara", Account.TYPE.EUR, "EUR124", 200).email("ionescu.ion@gmail.com").build();
		bcr.addClient(cl1);
		cl1.addAccount(Account.TYPE.RON, "RON1234", 400);
		cl1.upgradeAccount("RON1234", "SILVER");
		log.log(cl1.toString(), "log.txt");
		// Client Marinescu has a RON account
		Client cl2 = new Client.Builder("Marinescu Marin", "Timisoara", Account.TYPE.RON, "RON126", 100).build();
		bcr.addClient(cl2);
		log.log(cl2.toString(), "log.txt");
		System.out.println(bcr);
		log.log(bcr.toString(), "log.txt");

		/**
		 * Create bank CEC with one client
		 */
		Bank cec = new Bank("CEC Bank");
		Client clientCEC = new Client.Builder("Vasilescu Vasile", "Brasov", Account.TYPE.EUR, "EUR128", 700).build();
		cec.addClient(clientCEC);
		System.out.println(cec);
		log.log(cec.toString(), "log.txt");

		/**
		 * Perform operations on client accounts
		 */
		// depose in account RON126 of client Marinescu
		Client cl = bcr.getClient("Marinescu Marin");
		if (cl != null) {
			cl.getAccount("RON126").depose(400);
			System.out.println(cl);
			log.log(cl.toString(), "log.txt");
		}

		// retrieve from account RON126 of Marinescu client
		if (cl != null) {
			cl.getAccount("RON126").retrieve(67);
			System.out.println(cl);
			log.log(cl.toString(), "log.txt");
		}

		// transfer between accounts RON126 and RON1234
		Account a1 = cl.getAccount("RON126");
		Account a2 = bcr.getClient("Ionescu Ion").getAccount("RON1234");
		a1.transfer(a2, 40);
		System.out.println(bcr);
		log.log(bcr.toString(), "log.txt");

	}

}
