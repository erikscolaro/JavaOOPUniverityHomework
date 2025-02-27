package it.polito.emergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmergencyApp {

    private Map<String, Professional> profesionals = new HashMap<>();
    private Map<String, Integer> departments = new HashMap<>();
    private Map<Patient, String> patientsAndDepartment = new HashMap<>();
    private Map<String, Patient> patients = new HashMap<>();

    public enum PatientStatus {
        ADMITTED,
        DISCHARGED,
        HOSPITALIZED
    }
    
    /**
     * Add a professional working in the emergency room
     * 
     * @param id
     * @param name
     * @param surname
     * @param specialization
     * @param period
     * @param workingHours
     */
    public void addProfessional(String id, String name, String surname, String specialization, String period) {
        profesionals.put(id, new Professional(id, name, surname, specialization, period));
    }

    /**
     * Retrieves a professional utilizing the ID.
     *
     * @param id The id of the professional.
     * @return A Professional.
     * @throws EmergencyException If no professional is found.
     */    
    public Professional getProfessionalById(String id) throws EmergencyException {
        if (profesionals.containsKey(id)) return profesionals.get(id);
        else throw new EmergencyException();
    }

    /**
     * Retrieves the list of professional IDs by their specialization.
     *
     * @param specialization The specialization to search for among the professionals.
     * @return A list of professional IDs who match the given specialization.
     * @throws EmergencyException If no professionals are found with the specified specialization.
     */    
    public List<String> getProfessionals(String specialization) throws EmergencyException {
        List<String> temp = profesionals.values().stream().filter(p->p.getSpecialization().equals(specialization)).map(Professional::getId).collect(Collectors.toList());
        if (temp.isEmpty()) throw new EmergencyException(); else return temp;
    }

    /**
     * Retrieves the list of professional IDs who are specialized and available during a given period.
     *
     * @param specialization The specialization to search for among the professionals.
     * @param period The period during which the professional should be available, formatted as "YYYY-MM-DD to YYYY-MM-DD".
     * @return A list of professional IDs who match the given specialization and are available during the period.
     * @throws EmergencyException If no professionals are found with the specified specialization and period.
     */    
    public List<String> getProfessionalsInService(String specialization, String period) throws EmergencyException {
       
        List<String> temp = getProfessionals(specialization).stream()
        .filter(p->{
            if (profesionals.containsKey(p)){return profesionals.get(p).workInPeriod(period);} else return false;
        }).collect(Collectors.toList());

        if (temp.isEmpty()) throw new EmergencyException();
        else return temp;
        
    }

    /**
     * Adds a new department to the emergency system if it does not already exist.
     *
     * @param name The name of the department.
     * @param maxPatients The maximum number of patients that the department can handle.
     * @throws EmergencyException If the department already exists.
     */
    public void addDepartment(String name, int maxPatients) {
        departments.put(name, maxPatients);
    }

    /**
     * Retrieves a list of all department names in the emergency system.
     *
     * @return A list containing the names of all registered departments.
     * @throws EmergencyException If no departments are found.
     */
    public List<String> getDepartments() throws EmergencyException {
        List<String> temp = departments.keySet().stream().collect(Collectors.toList());
        if (temp.size()<=0) throw new EmergencyException();
        return temp;
    }

    /**
     * Reads professional data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a professional's ID, name, surname, specialization, period of availability, and working hours.
     * The expected format of each line is: matricola, nome, cognome, specializzazione, period, orari_lavoro
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of professionals successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */
    public int readFromFileProfessionals(Reader reader) throws IOException {
        if (reader==null) throw new IOException();
        BufferedReader r = new BufferedReader(reader);

        r.lines().skip(1).forEach(line->{
                Professional temp = new Professional(line);
                profesionals.put(temp.getId(), temp);
        });
        reader.reset(); return (int) r.lines().skip(1).count();
        
    }
        
    

    /**
     * Reads department data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a department's name and the maximum number of patients it can accommodate.
     * The expected format of each line is: nome_reparto, num_max
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of departments successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */    
    public int readFromFileDepartments(Reader reader) throws IOException {
            if (reader==null) throw new IOException();
            BufferedReader r = new BufferedReader(reader);
            r.lines().skip(1).forEach(line->{
                String[] temp = line.split(",");
                departments.put(temp[0], Integer.parseInt(temp[1]));
            });
            reader.reset(); return (int) r.lines().skip(1).count();
    }

    /**
     * Registers a new patient in the emergency system if they do not exist.
     * 
     * @param fiscalCode The fiscal code of the patient, used as a unique identifier.
     * @param name The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The birth date of the patient.
     * @param reason The reason for the patient's visit.
     * @param dateTimeAccepted The date and time the patient was accepted into the emergency system.
     */
    public Patient addPatient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        if (patients.containsKey(fiscalCode)) return patients.get(fiscalCode);
        else return patients.computeIfAbsent(fiscalCode, k-> new Patient(k, name, surname, dateOfBirth, reason, dateTimeAccepted, PatientStatus.ADMITTED.toString()));
    }

    /**
     * Retrieves a patient or patients based on a fiscal code or surname.
     *
     * @param identifier Either the fiscal code or the surname of the patient(s).
     * @return A single patient if a fiscal code is provided, or a list of patients if a surname is provided.
     *         Returns an empty collection if no match is found.
     */    
    public List<Patient> getPatient(String identifier) throws EmergencyException {
        return patients.values().stream().filter(p->p.getFiscalCode().equals(identifier) || p.getSurname().equals(identifier)).collect(Collectors.toList());
    }

    /**
     * Retrieves the fiscal codes of patients accepted on a specific date, 
     * sorted by acceptance time in descending order.
     *
     * @param date The date of acceptance to filter the patients by, expected in the format "yyyy-MM-dd".
     * @return A list of patient fiscal codes who were accepted on the given date, sorted from the most recent.
     *         Returns an empty list if no patients were accepted on that date.
     */
    public List<String> getPatientsByDate(String date) {
        return patients.values().stream().filter(p->p.getDateTimeAccepted().equals(date))
        .sorted(Comparator.comparing(Patient::getSurname).thenComparing(Patient::getName))
        .map(Patient::getFiscalCode)
        .collect(Collectors.toList());
    }

    /**
     * Assigns a patient to a professional based on the required specialization and checks availability during the request period.
     *
     * @param fiscalCode The fiscal code of the patient.
     * @param specialization The required specialization of the professional.
     * @return The ID of the assigned professional.
     * @throws EmergencyException If the patient does not exist, if no professionals with the required specialization are found, or if none are available during the period of the request.
     */
    public String assignPatientToProfessional(String fiscalCode, String specialization) throws EmergencyException {
        if (
            !(patients.containsKey(fiscalCode)) ||
            getProfessionals(specialization).isEmpty() ||
            getProfessionalsInService(specialization, patients.get(fiscalCode).getDateTimeAccepted()).isEmpty()) 
            throw new EmergencyException();
                
        Patient p = patients.get(fiscalCode);
        Professional pr = getProfessionalById(
            getProfessionalsInService(specialization, p.getDateTimeAccepted()).get(0)
        );
        pr.addPatient(p);
        return pr.getId();
    }

    public Report saveReport(String professionalId, String fiscalCode, String date, String description) throws EmergencyException {
        if (!profesionals.containsKey(professionalId)) throw new EmergencyException();
        return new Report(professionalId, fiscalCode, date, description);
    }

    /**
     * Either discharges a patient or hospitalizes them depending on the availability of space in the requested department.
     * 
     * @param fiscalCode The fiscal code of the patient to be discharged or hospitalized.
     * @param departmentName The name of the department to which the patient might be admitted.
     * @throws EmergencyException If the patient does not exist or if the department does not exist.
     */
    public void dischargeOrHospitalize(String fiscalCode, String departmentName) throws EmergencyException {
        if (!departments.containsKey(departmentName) || !patients.containsKey(fiscalCode)) throw new EmergencyException();
        if (getNumberOfPatientsHospitalizedByDepartment(departmentName)<departments.get(departmentName)){patients.get(fiscalCode).setStatus(PatientStatus.HOSPITALIZED.toString());} else patients.get(fiscalCode).setStatus(PatientStatus.DISCHARGED.toString()); patientsAndDepartment.put(patients.get(fiscalCode),departmentName);
    }

    /**
     * Checks if a patient is currently hospitalized in any department.
     *
     * @param fiscalCode The fiscal code of the patient to verify.
     * @return 0 if the patient is currently hospitalized, -1 if not hospitalized or discharged.
     * @throws EmergencyException If no patient is found with the given fiscal code.
     */
    public int verifyPatient(String fiscalCode) throws EmergencyException{
        if (!patients.containsKey(fiscalCode)) throw new EmergencyException(); else return patients.get(fiscalCode).getStatus()==PatientStatus.HOSPITALIZED?0:-1;
    }

    /**
     * Returns the number of patients currently being managed in the emergency room.
     *
     * @return The total number of patients in the system.
     */    
    public int getNumberOfPatients() {
        return (int) patients.values().stream().filter(p->p.getStatus()==PatientStatus.ADMITTED).count();
    }

    /**
     * Returns the number of patients admitted on a specified date.
     *
     * @param dateString The date of interest provided as a String (format "yyyy-MM-dd").
     * @return The count of patients admitted on that date.
     */
    public int getNumberOfPatientsByDate(String date) {
        return (int) getPatientsByDate(date).stream().filter(k->patients.get(k).getStatus()==PatientStatus.ADMITTED).count();
    }

    public int getNumberOfPatientsHospitalizedByDepartment(String departmentName) throws EmergencyException {
        if (!departments.containsKey(departmentName)) throw new EmergencyException(); return (int) patientsAndDepartment.entrySet().stream().filter(e->e.getValue().equals(departmentName)).filter(e->e.getKey().getStatus()==PatientStatus.HOSPITALIZED).count();
    }

    /**
     * Returns the number of patients who have been discharged from the emergency system.
     *
     * @return The count of discharged patients.
     */
    public int getNumberOfPatientsDischarged() {
        return (int) patients.values().stream().filter(p->p.getStatus()==PatientStatus.DISCHARGED).count();
    }

    /**
     * Returns the number of discharged patients who were treated by professionals of a specific specialization.
     *
     * @param specialization The specialization of the professionals to filter by.
     * @return The count of discharged patients treated by professionals of the given specialization.
     */
    public int getNumberOfPatientsAssignedToProfessionalDischarged(String specialization) {
        return (int) profesionals.values().stream().filter(pro->pro.getSpecialization().equals(specialization)).flatMap(pro->pro.getPatients().values().stream().filter(pat->pat.getStatus()==PatientStatus.DISCHARGED)).count();
    }
}
