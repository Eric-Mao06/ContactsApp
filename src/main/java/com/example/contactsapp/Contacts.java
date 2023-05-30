package com.example.contactsapp;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that represents a contact, containing information such as postal code, phone number,
 * emails, name and mobile status.
 */
public class Contacts {

    private final StringProperty postalCode;
    private final StringProperty number;
    private final ListProperty<String> emails;
    private final StringProperty name;
    private final BooleanProperty isMobile;

    /**
     * Constructs a Contacts object with the given postal code, phone number, emails, name, and mobile status.
     *
     * @param postalCode The postal code of the contact.
     * @param phoneNumber The phone number of the contact.
     * @param emails The emails of the contact.
     * @param name The name of the contact.
     * @param isMobile Whether the contact is a mobile device.
     */
    public Contacts(String postalCode, String phoneNumber, ObservableList<String> emails, String name, boolean isMobile) {
        this.postalCode = new SimpleStringProperty(postalCode);
        this.number = new SimpleStringProperty(phoneNumber);
        this.emails = new SimpleListProperty<>(FXCollections.observableArrayList(emails));
        this.name = new SimpleStringProperty(name);
        this.isMobile = new SimpleBooleanProperty(isMobile);
    }

    // Property getter methods
    public StringProperty areaCodeProperty() { return postalCode; }
    public StringProperty lineNumberProperty() { return number; }
    public ListProperty<String> emailsProperty() { return emails; }
    public StringProperty nameProperty() { return name; }
    public BooleanProperty isMobileProperty() { return isMobile; }

    // Standard getter methods
    public String getAreaCode() { return postalCode.get(); }
    public String getLineNumber() { return number.get(); }
    public ObservableList<String> getEmails() { return this.emails.get(); }
    public String getName() { return name.get(); }
    public boolean getIsMobile() { return isMobile.get(); }
    public String getEmailAt(int index) {
        return getEmails().get(index);
    }

    // Standard setter methods
    public void setAreaCode(String areaCode) { this.postalCode.set(areaCode); }
    public void setLineNumber(String lineNumber) { this.number.set(lineNumber); }
    public void setEmails(ObservableList<String> emails) { this.emails.set(emails); }
    public void setName(String name) { this.name.set(name); }
    public void setIsMobile(boolean isMobile) { this.isMobile.set(isMobile); }
}