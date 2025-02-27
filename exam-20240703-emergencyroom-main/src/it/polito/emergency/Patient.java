package it.polito.emergency;

import it.polito.emergency.EmergencyApp.*;

public class Patient {

    private String fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted, status;

	public Patient(String fiscalCode, String name, String surname, String dateOfBirth, String reason,
			String dateTimeAccepted, String status) {
		this.fiscalCode = fiscalCode;
		this.name = name;
		this.surname = surname;
		this.dateOfBirth = dateOfBirth;
		this.reason = reason;
		this.dateTimeAccepted = dateTimeAccepted;
        this.status = status;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public String getReason() {
		return reason;
	}

	public String getDateTimeAccepted() {
		return dateTimeAccepted;
	}

	public PatientStatus getStatus() {
		return PatientStatus.valueOf(status);
	}
	public void setStatus(String status){this.status=status;}

    
}
