package com.example;


public interface Operations {
	double getTotalAmount();

	double getInterest();

	void depose(double amount);

	void retrieve(double amount);

	String transfer(Account account, double amount);
}
