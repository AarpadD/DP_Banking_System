package com.example;

import java.util.ArrayList;
import java.util.List;

//builder
public class Client {

	private String name;
	private String address;
	private String email;
	private String birthday;
	private List<Account> accounts = new ArrayList<>();

	// public Client(String nume, String adresa, Account.TYPE tip, String numarCont, double suma) {
	// 	this.name = nume;
	// 	this.address = adresa;
	// 	addAccount(tip, numarCont, suma);
	// }

	private Client(Builder builder) {
		this.name = builder.name;
		this.address = builder.address;
		this.email = builder.email;
		this.birthday = builder.birthday;
		this.accounts = builder.accounts;
	}

	public static class Builder {
		private String name;
		private String address;
		private List<Account> accounts = new ArrayList<>();
		private String email;
		private String birthday;

		public Builder(String name, String address, Account.TYPE type, String accountCode, double amount) {
			this.name = name;
			this.address = address;
			this.accounts.add(Account.createAccount(type, accountCode, amount));
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder birthday(String birthday) {
			this.birthday = birthday;
			return this;
		}

		public Client build() {
			return new Client(this);
		}
	}

	public void addAccount(Account.TYPE type, String accountCode, double amount) {
		accounts.add(Account.createAccount(type, accountCode, amount));
	}

	public void closeAccount(String accountCode) {
		for (Account account : accounts) {
			if (account.getAccountCode().equals(accountCode)) {
				accounts.remove(account);
				return;
			}
		}
	}

	public void upgradeAccount(String accountCode, String level) {
    for (int i = 0; i < accounts.size(); i++) {
        if (accounts.get(i).getAccountCode().equals(accountCode)) {
            Account base = accounts.get(i);
            switch (level.toUpperCase()) {
                case "SILVER": accounts.set(i, new SilverAcc(base)); break;
                case "GOLD":   accounts.set(i, new GoldAcc(base));   break;
                default: throw new IllegalArgumentException("Level invalid");
            }
            return;
        }
    }
}

	public Account getAccount(String accountCode) {
		for (Account account : accounts) {
			if (account.getAccountCode().equals(accountCode)) {
				return account;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "\n\tClient [name=" + name + ", address=" + address + ", accounts=" + accounts + ", email=" + email + ", birthday=" + birthday + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getBirthday() {
		return birthday;
	}
}
