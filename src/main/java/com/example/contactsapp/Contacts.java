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
        if (index < 0 || index >= getEmails().size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return getEmails().get(index);
    }


    // Standard setter methods
    public void setAreaCode(String areaCode) { this.postalCode.set(areaCode); }
    public void setLineNumber(String lineNumber) { this.number.set(lineNumber); }
    public void setEmails(ObservableList<String> emails) { this.emails.set(emails); }
    public void setName(String name) { this.name.set(name); }
    public void setIsMobile(boolean isMobile) { this.isMobile.set(isMobile); }
}


/*public class Contact{
    private int areaCode;
    private int telephonePrefix;
    private int lineNumber;
    private String email;
    private String name;
    private boolean isMobile;

    //Constructor
    public Contact(int aC, int tP, int lN, String e, String n, boolean iM) {
        this.areaCode = aC;
        this.telephonePrefix = tP;
        this.lineNumber = lN;
        this.email = e;
        this.name = n;
        this.isMobile = iM;
    }
    //Accessors
    public int getAreaCode(){return areaCode;}
    public int getTelephonePrefix(){return telephonePrefix;}
    public int getLineNumber() {return lineNumber;}
    public String getEmail() {return email;}
    public String getName() {return name;}
    public boolean getIsMobile() {return isMobile;}

    //Mutators
    public void setAreaCode(int areaCode) {this.areaCode = areaCode;}
    public void setTelephonePrefix(int telephonePrefix) {this.telephonePrefix = telephonePrefix;}
    public void setLineNumber(int lineNumber) {this.lineNumber = lineNumber;}
    public void setEmail(String email) {this.email = email;}
    public void setName(String name) {this.name = name;}
    public void setIsMobile(boolean isMobile) {this.isMobile = isMobile;}

    public static void main (String args[]){
        Contact person1 = new Contact(647,983, 5161, "ericmao@crescentschool.org", "Eric Mao", true);
        person1.setTelephonePrefix(5161);
    }
}*/