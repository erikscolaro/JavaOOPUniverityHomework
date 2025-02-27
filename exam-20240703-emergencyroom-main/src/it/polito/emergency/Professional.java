package it.polito.emergency;

import java.util.HashMap;
import java.util.Map;

public class Professional {

    private String id, name, surname, specialization, workingHours;
    private String beginningPeriod, endingPeriod;
    private Map<String, Patient> patients = new HashMap<>();
    public Map<String, Patient> getPatients(){return patients;}

    public Professional(String id, String name, String surname, String specialization, String period) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.specialization = specialization;

        String[] temp= period.split(" to ");

		this.beginningPeriod=temp[0];
        this.endingPeriod=temp[1];
	}

    public void addPatient(Patient p){
        patients.put(p.getFiscalCode(), p);
    }

    public Professional(String csvLine) {

        String[] parms = csvLine.split(",");

		this.id = parms[0];
		this.name = parms[1];
		this.surname = parms[2];
		this.specialization = parms[3];

        String[] temp= parms[4].split(" to ");

		this.beginningPeriod=temp[0];
        this.endingPeriod=temp[1];
	}

	public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPeriod() {
        return beginningPeriod+" to "+endingPeriod;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public boolean workInPeriod(String period){
        String[] temp = period.split(" to ");
        if (temp[0].compareTo(this.beginningPeriod)>=0 && temp[temp.length==1?0:1].compareTo(this.endingPeriod)<=0) return true;
        else return false;
    }
}
