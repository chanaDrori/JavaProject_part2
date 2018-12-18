package com.project5779.javaproject2.model.entities;

public class Driver {

    private String name;
    private String lastName;
    private String id;
    private String phone;
    private String email;
    private String creditCard;

    /**
     * constructor of driver.
     * @param name string - The driver's name.
     * @param lastName string - The driver's last name.
     * @param id string - The driver's id.
     * @param phone string - The driver's phone number.
     * @param email string - The driver's mail address.
     * @param creditCard string - The credit card number of te driver.
     */
    public Driver ( String name, String lastName, String id, String phone, String email, String creditCard)
    {
        this.creditCard = creditCard;
        this.email = email;
        this.id = id;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
    }

    /**
     * default constructor
     */
    public Driver(){}

    /**
     * getter function
     * @return email. string - The driver's mail address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * getter function
     * @return name. string - The driver's name.
     */
    public String getName() {
        return name;
    }

    /**
     * getter function
     * @return id. string - The driver's id.
     */
    public String getId() {
        return id;
    }

    /**
     * getter function
     * @return creditCard. string - The credit card number of te driver.
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * getter function
     * @return lastName. string - The driver's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * getter function
     * @return phone. string - The driver's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setter function
     * @param creditCard string - The credit card number of te driver.
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * setter function
     * @param email string - The driver's mail address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * setter function
     * @param id string - The driver's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * setter function
     * @param lastName string - The driver's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * setter function
     * @param name string - The driver's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter function
     * @param phone string - The driver's phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
