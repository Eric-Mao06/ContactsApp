package com.example.contactsapp;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
        TableColumn<Contacts, Boolean> isMobileColumn = new TableColumn<>("Is Mobile");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        areaCodeColumn.setCellValueFactory(cellData -> cellData.getValue().areaCodeProperty());
        lineNumberColumn.setCellValueFactory(cellData -> cellData.getValue().lineNumberProperty());
        isMobileColumn.setCellValueFactory(cellData -> cellData.getValue().isMobileProperty().asObject());

        TableColumn<Contacts, Contacts> emailColumn = new TableColumn<>("Emails");
        emailColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        emailColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Show Emails");

            @Override
            protected void updateItem(Contacts item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btn);
                btn.setOnAction(event -> showEmailsDialog(item));
            }
        });

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

    private void showEmailsDialog(Contacts contact) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Emails of " + contact.getName());

        ListView<String> emailListView = new ListView<>(FXCollections.observableArrayList(contact.getEmails()));
        dialog.getDialogPane().setContent(emailListView);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }

    private void addContact() {
        Contacts newContact = new Contacts("0", "0",FXCollections.observableArrayList(), "", false);
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
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String name = data[0];
                    String areaCode = data[1];
                    String lineNumber = data[3];
                    ObservableList<String> emails = FXCollections.observableArrayList(data[4].split(";"));
                    boolean isMobile = Boolean.parseBoolean(data[5]);
                    contactsData.add(new Contacts(areaCode, lineNumber, emails, name, isMobile));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private void saveContacts(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Name,Postal Code,Phone Number,Email,Is Mobile\n"); //header line
            for (Contacts contact : contactsData) {
                writer.write(String.join(",", contact.getName(),
                        contact.getAreaCode(),
                        contact.getLineNumber(),
                        String.join(";", contact.getEmails()),
                        String.valueOf(contact.getIsMobile())) + "\n");
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
        TextField lineNumberField = new TextField(String.valueOf(contact.getLineNumber()));
        ListView<String> emailListView = new ListView<>(FXCollections.observableArrayList(contact.getEmails()));
        Button addEmailButton = new Button("Add Email");
        addEmailButton.setOnAction(e -> {
            TextInputDialog emailDialog = new TextInputDialog();
            emailDialog.setTitle("Add Email");
            emailDialog.setHeaderText(null);
            emailDialog.setContentText("Email:");
            Optional<String> result = emailDialog.showAndWait();
            result.ifPresent(email -> emailListView.getItems().add(email));
        });
        CheckBox isMobileField = new CheckBox();
        isMobileField.setSelected(contact.getIsMobile());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Postal Code:"), 0, 1);
        grid.add(areaCodeField, 1, 1);
        grid.add(new Label("Phone Number:"), 0, 3);
        grid.add(lineNumberField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailListView, 1, 4);
        grid.add(addEmailButton, 2, 4);
        grid.add(new Label("Is Mobile:"), 0, 5);
        grid.add(isMobileField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (!(/*areaCodeField.getText().matches("\\d+") && */lineNumberField.getText().matches("\\d+"))) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Number Error");
                    alert1.setContentText("You must put in only numbers in the telephone, line number, and area code field");
                    alert1.showAndWait();
                    return null;
                }
                for (String email : emailListView.getItems()) {
                    if (!email.contains("@")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Email Address Error");
                        alert.setContentText("The format of the email is incorrect (include a @)");
                        alert.showAndWait();
                        return null;
                    }
                }
                contact.setName(nameField.getText());
                contact.setAreaCode(areaCodeField.getText());
                contact.setLineNumber(lineNumberField.getText());
                contact.setEmails(FXCollections.observableArrayList(emailListView.getItems()));
                contact.setIsMobile(isMobileField.isSelected());
                return contact;
            }
            return null;
        });

        Optional<Contacts> result = dialog.showAndWait();
        return result.isPresent();
    }
}