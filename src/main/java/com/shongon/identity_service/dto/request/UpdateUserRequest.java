package com.shongon.identity_service.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateUserRequest {

    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
