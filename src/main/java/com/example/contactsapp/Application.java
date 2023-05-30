package com.example.contactsapp;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * Represents a Contacts Application using JavaFX.
 */
public class Application extends javafx.application.Application {
    private TableView<Contacts> contactsTable = new TableView<>();
    private ObservableList<Contacts> contactsData = FXCollections.observableArrayList();

    /**
     * Main method to launch the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application.
     * @param primaryStage The main window of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Set up the table columns
        TableColumn<Contacts, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Contacts, String> areaCodeColumn = new TableColumn<>("Area Code");
        TableColumn<Contacts, String> lineNumberColumn = new TableColumn<>("Line Number");
        TableColumn<Contacts, Boolean> isMobileColumn = new TableColumn<>("Is Mobile");

        // Set the cell value factories for each column
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        areaCodeColumn.setCellValueFactory(cellData -> cellData.getValue().areaCodeProperty());
        isMobileColumn.setCellValueFactory(cellData -> cellData.getValue().isMobileProperty().asObject());

        //Custom Rendering for Phone Number
        lineNumberColumn.setCellValueFactory(cellData -> cellData.getValue().lineNumberProperty());
        lineNumberColumn.setCellFactory(column -> {
            return new TableCell<Contacts, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        // Check if the item is 10 digits long, if not, just display it as is
                        if (item.matches("\\d{10}")) {
                            String formattedNumber = "(" + item.substring(0, 3) + ") "
                                    + item.substring(3, 6) + "-"
                                    + item.substring(6, 10);
                            setText(formattedNumber);
                        } else {
                            setText("Line Number: " + item);
                        }
                    }
                }
            };
        });

        // Set up the email column
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

        contactsTable.getColumns().addAll(nameColumn, areaCodeColumn, lineNumberColumn, emailColumn, isMobileColumn);
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

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Contacts App");
        primaryStage.show();

        // Load contacts from the CSV file
        File contactsFile = new File("contacts.csv");
        loadContacts(contactsFile);
    }

    /**
     * Show a dialog to display emails for the provided contact.
     * @param contact Contact whose emails need to be displayed.
     */
    private void showEmailsDialog(Contacts contact) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Emails of " + contact.getName());

        ListView<String> emailListView = new ListView<>(FXCollections.observableArrayList(contact.getEmails()));
        dialog.getDialogPane().setContent(emailListView);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }

    /**
     * Add a new contact to the application.
     */
    private void addContact() {
        Contacts newContact = new Contacts("", "",FXCollections.observableArrayList(), "", false);
        if (showContactDialog(newContact)) {
            contactsData.add(newContact);
            saveContacts(new File("contacts.csv"));
        }
    }

    /**
     * Edit the selected contact in the application.
     */
    private void editContact() {
        Contacts selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            if (showContactDialog(selectedContact)) {
                saveContacts(new File("contacts.csv"));
            }
        }
    }

    /**
     * Delete the selected contact from the application.
     */
    private void deleteContact() {
        int selectedIndex = contactsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            contactsData.remove(selectedIndex);
            saveContacts(new File("contacts.csv"));
        }
    }

    /**
     * Load contacts from the provided file.
     * @param file File containing the contact data.
     * @return true if contacts are loaded successfully, otherwise false.
     */
    private boolean loadContacts(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); //turn CSV into array of strings
                if (data.length >= 5) {
                    String name = data[0];
                    String areaCode = data[1];
                    String lineNumber = data[2];
                    ObservableList<String> emails = FXCollections.observableArrayList(data[3].split(";")); //split emails by ;
                    boolean isMobile = Boolean.parseBoolean(data[4]); //parse boolean
                    contactsData.add(new Contacts(areaCode, lineNumber, emails, name, isMobile));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Save contacts to the provided file.
     * @param file File to save the contact data to.
     */
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

    /**
     * Show a dialog to edit the provided contact.
     * @param contact Contact to show in the dialog.
     * @return true if dialog was shown successfully, otherwise false.
     */
    private boolean showContactDialog(Contacts contact) {
        // Set up the dialog
        Dialog<Contacts> dialog = new Dialog<>();
        dialog.setTitle("Contact");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Set up the fields
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
            result.ifPresent(email -> {
                if (isValidEmail(Collections.singletonList(email))) {
                    emailListView.getItems().add(email);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Email");
                    alert.setHeaderText(null);
                    alert.setContentText("The email address entered is invalid.");
                    alert.showAndWait();
                }
            });
        });

        CheckBox isMobileField = new CheckBox();
        isMobileField.setSelected(contact.getIsMobile());

        // Add the fields to the dialog
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
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType);

        // Get the reference to the "Save" button
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);

        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            if ((lineNumberField.getText().isEmpty()) ||
                    (!lineNumberField.getText().matches("\\d+") || !lineNumberField.getText().matches("\\d{10}")) ||
                    Pattern.compile("[\\p{So}]").matcher(areaCodeField.getText()).find() ||
                    !isValidEmail(emailListView.getItems())) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");

                if (!lineNumberField.getText().matches("\\d{10}")) {
                    alert.setHeaderText("Number Error");
                    alert.setContentText("You must put in only numbers in the telephone field, and exactly 10 digits");
                } else if (Pattern.compile("[\\p{So}]").matcher(areaCodeField.getText()).find()) {
                    alert.setHeaderText("Invalid Character Error");
                    alert.setContentText("You must not put invalid characters in area code");
                } else {
                    alert.setHeaderText("Email Address Error");
                    alert.setContentText("The format of the email is incorrect");
                }

                alert.showAndWait();
                event.consume();
            }
        });

//Use regex to check for invalid input
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
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

    //check if the email is valid with text@text format, return null if it's not and return the original string if it is.
    public static boolean isValidEmail(List<String> emails) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        for (String email : emails) {
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches() || hasEmoji(email)) {
                return false;
            }
        }
        return true;
    }
    public static boolean hasEmoji(String source) {
        for (int i = 0; i < source.length(); i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(source.charAt(i));

            if (block.equals(Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS)
                    || block.equals(Character.UnicodeBlock.EMOTICONS)
                    || block.equals(Character.UnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS)
                    || block.equals(Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS)
                    || block.equals(Character.UnicodeBlock.SMALL_FORM_VARIANTS)
                    || block.equals(Character.UnicodeBlock.DINGBATS)
                    || block.equals(Character.UnicodeBlock.ENCLOSED_ALPHANUMERIC_SUPPLEMENT)) {
                return true;
            }
        }
        return false;
    }


}