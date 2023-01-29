package com.akhil.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {
	@NotNull(message = "First name can not be null")
	private String firstName;
	@NotNull(message = "Last name can not be null")
	private String lastName;
	@NotNull(message = "email can not be null")
	@Email
	private String email;
	@NotNull(message = "password  can not be null")
	@Size(min = 8, max = 16, message = "password should be of equal or greater than 8 charaters and less than 16 characters")
	private String password;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
