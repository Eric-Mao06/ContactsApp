package com.example.contactsapp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class Application extends javafx.application.Application {
    private TableView<Contacts> contactsTable = new TableView<>();
    private ObservableList<Contacts> contactsData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        TableColumn<Contacts, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Contacts, String> areaCodeColumn = new TableColumn<>("Area Code");
        TableColumn<Contacts, String> telephonePrefixColumn = new TableColumn<>("Telephone Prefix");
        TableColumn<Contacts, String> lineNumberColumn = new TableColumn<>("Line Number");
        TableColumn<Contacts, String> emailColumn = new TableColumn<>("Email");
        TableColumn<Contacts, Boolean> isMobileColumn = new TableColumn<>("Is Mobile");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        areaCodeColumn.setCellValueFactory(cellData -> cellData.getValue().areaCodeProperty());
        telephonePrefixColumn.setCellValueFactory(cellData -> cellData.getValue().telephonePrefixProperty());
        lineNumberColumn.setCellValueFactory(cellData -> cellData.getValue().lineNumberProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        isMobileColumn.setCellValueFactory(cellData -> cellData.getValue().isMobileProperty().asObject());

        contactsTable.getColumns().addAll(nameColumn, areaCodeColumn, telephonePrefixColumn, lineNumberColumn, emailColumn, isMobileColumn);
        contactsTable.setItems(contactsData);

        // Set up buttons and their event handlers
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addContact());

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editContact());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteContact());

        HBox buttons = new HBox(10, addButton, editButton, deleteButton);

        BorderPane root = new BorderPane();
        root.setCenter(contactsTable);
        root.setBottom(buttons);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Contacts App");
        primaryStage.show();

        // Load contacts from the CSV file
        File contactsFile = new File("contacts.csv");
        loadContacts(contactsFile);

    }
    private void addContact() {
        Contacts newContact = new Contacts("0","0","0","","",false);
        if (showContactDialog(newContact)) {
            contactsData.add(newContact);
            saveContacts(new File("contacts.csv"));
        }
    }

    private void editContact() {
        Contacts selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            if (showContactDialog(selectedContact)) {
                saveContacts(new File("contacts.csv"));
            }
        }
    }

    private void deleteContact() {
        int selectedIndex = contactsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            contactsData.remove(selectedIndex);
            saveContacts(new File("contacts.csv"));
        }
    }

    private boolean loadContacts(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String name = data[0];
                    String areaCode = data[1];
                    String telephonePrefix = data[2];
                    String lineNumber = data[3];
                    String email = data[4];
                    boolean isMobile = Boolean.parseBoolean(data[5]);
                    contactsData.add(new Contacts(areaCode, telephonePrefix, lineNumber, email, name, isMobile));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void saveContacts(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header line
            writer.write("Name,Area Code,Telephone Prefix,Line Number,Email,Is Mobile\n");

            for (Contacts contact : contactsData) {
                writer.write(contact.getName() + "," +
                        contact.getAreaCode() + "," +
                        contact.getTelephonePrefix() + "," +
                        contact.getLineNumber() + "," +
                        contact.getEmail() + "," +
                        contact.getIsMobile() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean showContactDialog(Contacts contact) {
        Dialog<Contacts> dialog = new Dialog<>();
        dialog.setTitle("Contact");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(contact.getName());
        TextField areaCodeField = new TextField(String.valueOf(contact.getAreaCode()));
        TextField telephonePrefixField = new TextField(String.valueOf(contact.getTelephonePrefix()));
        TextField lineNumberField = new TextField(String.valueOf(contact.getLineNumber()));
        TextField emailField = new TextField(contact.getEmail());
        CheckBox isMobileField = new CheckBox();
        isMobileField.setSelected(contact.getIsMobile());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Area Code:"), 0, 1);
        grid.add(areaCodeField, 1, 1);
        grid.add(new Label("Telephone Prefix:"), 0, 2);
        grid.add(telephonePrefixField, 1, 2);
        grid.add(new Label("Line Number:"), 0, 3);
        grid.add(lineNumberField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Is Mobile:"), 0, 5);
        grid.add(isMobileField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (!(areaCodeField.getText().matches("\\d+")&&telephonePrefixField.getText().matches("\\d+")&&lineNumberField.getText().matches("\\d+"))){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Number Error");
                    alert1.setContentText("You must put in only numbers in the telephone, line number, and area code field");
                    alert1.showAndWait();
                    return null;
                }
                if(!emailField.getText().contains("@")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Email Address Error");
                    alert.setContentText("The format of the email is incorrect (include a @)");
                    alert.showAndWait();
                    return null;
                }
                else{
                    contact.setName(nameField.getText());
                    contact.setAreaCode(areaCodeField.getText());
                    contact.setTelephonePrefix(telephonePrefixField.getText());
                    contact.setLineNumber(lineNumberField.getText());
                    contact.setEmail(emailField.getText());
                    contact.setIsMobile(isMobileField.isSelected());
                }
                return contact;
            }
            return null;
        });

        Optional<Contacts> result = dialog.showAndWait();
        return result.isPresent();
    }
}