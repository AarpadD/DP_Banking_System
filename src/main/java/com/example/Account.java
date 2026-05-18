package com.example;

import java.util.ArrayList;
import java.util.List;

public class Account implements Operations {

	public static enum TYPE {
		EUR, RON
	};

	String accountCode = null; // IBAN
	double amount = 0;
	Account.TYPE type = Account.TYPE.RON;
	String level = "BASIC";
	private List<TransferDateSaver> transfers = new ArrayList<>();

	protected Account(String accountCode, double amount, Account.TYPE type) {
		this.accountCode = accountCode;
		this.type = type;
		depose(amount);
	}

	//factory
	public static Account createAccount(Account.TYPE type, String accountCode, double amount) {
		switch (type) {
			case RON: return new RONAccount(accountCode, amount);
			case EUR: return new EURAccount(accountCode, amount);
			default: throw new IllegalArgumentException("Invalid account type");
		}
	}

	@Override
	public double getTotalAmount() {
		return amount + amount * getInterest();
	}

	public double getAmount() {
		return amount;
	}

	public String getLevel() {
    return level;
}

	@Override
	public void depose(double amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Amount must be greater than 0");
		this.amount += amount;
	}

	@Override
	public void retrieve(double amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Amount must be greater than 0");
		if (amount > this.amount)
			throw new IllegalArgumentException("Insufficient funds");
		this.amount -= amount;
	}

	@Override
	public String toString() {
		if (Account.TYPE.RON == this.type)
			return "[" + level + "] Account RON: code=" + accountCode + ", amount=" + amount;
		else
			return "[" + level + "] Account EUR: code=" + accountCode + ", amount=" + amount;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public double getInterest() {
		// if (Account.TYPE.RON == this.type) {
		// 	if (amount < 500)
		// 		return 0.03;
		// 	else
		// 		return 0.08;
		// } else {
		// 	return 0.01;
		// }
		return 0;
	}

	@Override
	public String transfer(Account c, double s) {
		if (this.type == c.type) {
			c.retrieve(s);
			depose(s);
		} else {
			double rate = (c.type == Account.TYPE.EUR) ? 5.0 : 0.2;
			c.retrieve(s);
			depose(s * rate);
		}
		TransferDateSaver t = new TransferDateSaver(this, c, s);
		transfers.add(t);
		return t.getTransferCode();
	}

	public void cancelTransfer(String transferCode){
		for (int i = 0; i < transfers.size(); i++) {
        if (transfers.get(i).transferCode.equals(transferCode)) {
            transfers.get(i).undo();
            transfers.remove(i);
            return;
        }
    }
    throw new IllegalStateException("No transfer found with code: " + transferCode);
	}

}
