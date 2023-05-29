package com.example.contactsapp;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contacts{
    private StringProperty postalCode;
    private StringProperty Number;
    private ListProperty<String> emails;
    private StringProperty name;
    private BooleanProperty isMobile;


    public Contacts(String aC, String tP, String lN, ObservableList<String> e, String n, boolean iM) {
        this.postalCode = new SimpleStringProperty(aC);
        this.Number = new SimpleStringProperty(lN);
        this.emails = new SimpleListProperty<>(FXCollections.observableArrayList(e));
        this.name = new SimpleStringProperty(n);
        this.isMobile = new SimpleBooleanProperty(iM);
    }

    public StringProperty areaCodeProperty() {return postalCode;}
    public StringProperty lineNumberProperty() {return Number;}
    public ListProperty<String> emailsProperty() { return emails; }
    public StringProperty nameProperty() {return name;}
    public BooleanProperty isMobileProperty() {return isMobile;}

    public String getAreaCode() {return postalCode.get();}
    public void setAreaCode(String areaCode) {this.postalCode.set(areaCode);}
    public String getLineNumber() {return Number.get();}
    public void setLineNumber(String lineNumber) {this.Number.set(lineNumber);}
    public ObservableList<String> getEmails() { return emails.get(); }
    public void setEmails(ObservableList<String> emails) { this.emails.set(emails); }
    public String getName() {return name.get();}
    public void setName(String name) {this.name.set(name);}
    public boolean getIsMobile() {return isMobile.get();}
    public void setIsMobile(boolean isMobile) {this.isMobile.set(isMobile);}
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