package cs.toronto.edu;

import controller.AccountController;
import models.Account;
import utils.Helper;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		// Listen to inputs
		while (true) {

			System.out.println("Please enter 1 for login or 2 for Register");
			String input = scanner.nextLine().trim();
			if (input != "1" || input != "2") {
				System.out.println("Invalid Input");
			}

			int status = Integer.parseInt(input);
			System.out.println("Username: ");
			String username = scanner.nextLine().trim();
			System.out.println("Password: ");
			String password = scanner.nextLine().trim();

			if (status == 1 && AccountController.login(username, password)) {
				System.out.println("Login Success");
				Helper.setAccount(new Account(username, password));
			} else if (status == 2 && AccountController.register(username, password)) {
				System.out.println("Register Success");
				Helper.setAccount(new Account(username, password));
			} else {
				System.out.println("Failing login or register");
			}

			System.out.println("1. Manage friends; 2. Manage stock list; 3. Go to Portfolio;");
			input = scanner.nextLine().trim();
			if (input != "1" || input != "2") {
				System.out.println("Invalid Input");
			}

			status = Integer.parseInt(input);
			if (status == 1) {
				System.out.println("Please input username to request: ");
				String receive_username = scanner.nextLine().trim();
				boolean sendResult = AccountController.sendRequest(receive_username);
				if(sendResult){
					System.out.println("Request Send Successfully!");
				}
			}
		}
	}
}
