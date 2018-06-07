package Modals;

import java.io.File;
import java.time.LocalDate;

public class Staffs {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String userName;
    private String password;
    private File userImage;
    private String gender;
    private String role;
    private LocalDate dob;

    public Staffs(String userName) {
        this.userName = userName;
    }

    public Staffs(String firstName, String lastName, String address, String phoneNumber, String email, String userName, String password, File userImage, String gender, String role, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userImage = userImage;
        this.gender = gender;
        this.role = role;
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public File getUserImage() {
        return userImage;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getRole() {
        return role;
    }
}
