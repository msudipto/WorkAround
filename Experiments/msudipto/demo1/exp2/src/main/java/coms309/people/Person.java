package coms309.people;

/**
 * Defines the structure of a person with validation and additional attributes.
 */

public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String telephone;
    private int age;
    private String email;

    public Person() { }

    public Person(String firstName, String lastName, String address, String telephone, int age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.age = age;
        this.email = email;
    }

    // Getter and Setter methods for all fields
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Custom validation method for telephone numbers (just an example)
    public boolean isValidTelephone() {
        return telephone.matches("\\d{10}");  // Validates that the phone number is 10 digits long
    }

    @Override
    public String toString() {
        return "Person[" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ']';
    }
}