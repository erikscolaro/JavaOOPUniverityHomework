package it.polito.emergency;

public class Report {

    private String professionalId, fiscalCode, date, description;
    private static int sharedSerial =1 ;
    private int serial;

	public Report(String professionalId, String fiscalCode, String date, String description) {
        this.professionalId = professionalId;
        this.fiscalCode = fiscalCode;
        this.date = date;
        this.description = description;
        serial = sharedSerial;
        sharedSerial++;

    }

    public String getId() {
		return String.valueOf(serial);
	}

	public String getProfessionalId() {
		return professionalId;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

    
}
