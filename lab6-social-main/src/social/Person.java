package social;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Person {
    private String name;
    private String code;
    private String surname;
    private Integer friendsNumber;

    private Map<Integer, Post> posts = new HashMap<>();

    public Collection<Post> getPosts() {
        return posts.values().stream().sorted(Comparator.comparing(Post::getSerial).reversed()).collect(Collectors.toList());
    }

    public Post addPost(String message, Long time){
        Post p = new Post(message, time);
        posts.put(p.getSerial(), p);
        return p;
    }

    public Post getPost(Integer pId){
        return posts.get(pId);
    }

    public Integer getFriendsNumber() {
        return friendsNumber;
    }

    private List<Person> friendsList = new ArrayList<>();

    public Person(String name, String code, String surname){
        this.code=code;
        this.name=name;
        this.surname=surname;
        friendsNumber=0;
    }

    public Collection<Person> getFriendList(){
        return friendsList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String print(){
        return code+" "+name+" "+surname;
    }

    public boolean isFriend(Person target){
        return friendsList.contains(target);
    }

    public boolean addFriendship(Person friend){
        if (isFriend(friend)) return false;
        else {friendsList.add(friend); friendsNumber++; return true;}
    }
}
