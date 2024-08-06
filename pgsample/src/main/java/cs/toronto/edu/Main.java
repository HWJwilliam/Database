package cs.toronto.edu;

import cs.toronto.edu.controller.AccountController;
import cs.toronto.edu.models.Account;
import cs.toronto.edu.models.appModel;
import cs.toronto.edu.utils.Helper;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.util.Scanner;

public class Main extends Application {

	private TextField usernameField;
	private PasswordField passwordField;
	private Label statusLabel;

	@Override
	public void start(Stage stage) {
		stage.setTitle("Stock Management System");

		//VBox
		VBox vbox = new VBox(20);
		//title
		Label title = new Label("Stock Management System");
		title.styleProperty().bind(Bindings.concat("-fx-font-size: ", stage.widthProperty().divide(25).asString(), ";"));
		//username and password
		usernameField = new TextField();
		usernameField.setPromptText("Username");
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		//status label
		statusLabel = new Label();

		//VBox for buttons
		VBox v_button = new VBox(10);
		v_button.setStyle("-fx-alignment: center;");

		//Register button
		Button register = new Button("Register");
		register.prefWidthProperty().bind(stage.widthProperty().divide(6));
		register.styleProperty().bind(Bindings.concat("-fx-font-size: ", stage.widthProperty().divide(50).asString(), ";"));
		register.setOnAction(e -> handleRegister());

		//Login button
		Button login = new Button("Login");
		login.prefWidthProperty().bind(stage.widthProperty().divide(6));
		login.styleProperty().bind(Bindings.concat("-fx-font-size: ", stage.widthProperty().divide(50).asString(), ";"));
		login.setOnAction(e -> handleLogin());

		v_button.getChildren().addAll(register, login);

		//VBox for buttons
		VBox buttonContainer = new VBox();
		buttonContainer.setStyle("-fx-alignment: center;");
		buttonContainer.getChildren().add(v_button);

		//Adding title, input fields, and button container to the main VBox
		vbox.getChildren().addAll(title, usernameField, passwordField, statusLabel, buttonContainer);
		vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

		//Scene
		Scene scene = new Scene(vbox, 1200, 800);
		stage.setScene(scene);
		stage.show();
	}

	private void handleLogin() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();

		if (username.length() >= 3 && password.length() >= 3) {
			boolean success = AccountController.login(username, password);
			if (success) {
				Helper.setAccount(new Account(username, password));
				statusLabel.setText("Login successful. Redirecting to Homepage...");
				// Redirect to homepage or another scene
			} else {
				showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
			}
		} else {
			showAlert(AlertType.WARNING, "Input Error", "Username and password must be at least 3 characters long.");
		}
	}

	private void handleRegister() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText().trim();

		if (username.length() >= 3 && password.length() >= 3) {
			boolean success = AccountController.register(username, password);
			if (success) {
				Helper.setAccount(new Account(username, password));
				statusLabel.setText("Registration successful. Redirecting to Homepage...");
				// Redirect to homepage or another scene
			} else {
				showAlert(AlertType.ERROR, "Registration Failed", "Username already exists or registration failed.");
			}
		} else {
			showAlert(AlertType.WARNING, "Input Error", "Username and password must be at least 3 characters long.");
		}
	}

	private void showAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Create a model and run the model
	 * __________________________
	 */
	public static void main(String[] args) {
		appModel model = new appModel();
		Application.launch(args);
		model.runapp();
	}
}
