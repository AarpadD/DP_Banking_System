package com.example;

import java.util.ArrayList;
import java.util.List;

public class Bank {

	private List<Client> clients = new ArrayList<>();
	private String bankCode = null;

	public Bank(String bankCode) {
		this.bankCode = bankCode;
	}

	public void addClient(Client c) {
		clients.add(c);
	}

	public Client getClient(String name) {
		for (Client client : clients) {
			if (client.getName().equals(name)) {
				return client;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Bank [code=" + bankCode + ", clients=" + clients + "]";
	}

	public String getBankCode() {
		return bankCode;
	}

}
