package social;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private Set<Person> subscribed = new HashSet<>();
    private Integer subsNumber;

    public Integer getSubsNumber() {
        return subsNumber;
    }

    public Set<Person> getSubscribed() {
        return subscribed;
    }

    private String name;

    public String getName() {
        return name;
    }

    public Group(String name){this.name=name; subsNumber=0;}

    public boolean addPerson(Person p){
        if (subscribed.add(p)) {subsNumber++; return true;}
        return false;
    }
}
