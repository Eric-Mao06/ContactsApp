package com.example.contactsapp;
import javafx.beans.property.*;

public class Contact{
    private IntegerProperty areaCode;
    private IntegerProperty telephonePrefix;
    private IntegerProperty lineNumber;
    private StringProperty email;
    private StringProperty name;
    private BooleanProperty isMobile;

    public Contact(int aC, int tP, int lN, String e, String n, boolean iM) {
        this.areaCode = new SimpleIntegerProperty(aC);
        this.telephonePrefix = new SimpleIntegerProperty(tP);
        this.lineNumber = new SimpleIntegerProperty(lN);
        this.email = new SimpleStringProperty(e);
        this.name = new SimpleStringProperty(n);
        this.isMobile = new SimpleBooleanProperty(iM);
    }
    public IntegerProperty areaCodeProperty() {return areaCode;}
    public IntegerProperty telephonePrefixProperty() {return telephonePrefix;}
    public IntegerProperty lineNumberProperty() {return lineNumber;}
    public StringProperty emailProperty() {return email;}
    public StringProperty nameProperty() {return name;}
    public BooleanProperty isMobileProperty() {return isMobile;}

    public int getAreaCode() {return areaCode.get();}
    public void setAreaCode(int areaCode) {this.areaCode.set(areaCode);}
    public int getTelephonePrefix() {return telephonePrefix.get();}
    public void setTelephonePrefix(int telephonePrefix) {this.telephonePrefix.set(telephonePrefix);}
    public int getLineNumber() {return lineNumber.get();}
    public void setLineNumber(int lineNumber) {this.lineNumber.set(lineNumber);}
    public String getEmail() {return email.get();}
    public void setEmail(String email) {this.email.set(email);}
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
