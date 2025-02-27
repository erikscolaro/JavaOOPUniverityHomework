package social;

import java.util.*;
import java.util.stream.Collectors;


public class Social {

	private Map<String, Person> persons = new HashMap<>();
	private Map<String, Group> groups = new HashMap<>();

	/**
	 * Creates a new account for a person
	 * 
	 * @param code	nickname of the account
	 * @param name	first name
	 * @param surname last name
	 * @throws PersonExistsException in case of duplicate code
	 */
	public void addPerson(String code, String name, String surname)
			throws PersonExistsException {
				if (persons.containsKey(code)) throw new PersonExistsException();
				else {
					persons.put(code, new Person(name, code, surname));
				}

	}

	/**
	 * Retrieves information about the person given their account code.
	 * The info consists in name and surname of the person, in order, separated by blanks.
	 * 
	 * @param code account code
	 * @return the information of the person
	 * @throws NoSuchCodeException
	 */
	public String getPerson(String code) throws NoSuchCodeException {
		if (persons.containsKey(code)) return persons.get(code).print();
		else {
			throw new NoSuchCodeException();
		}
	}

	/**
	 * Define a friendship relationship between to persons given their codes.
	 * 
	 * Friendship is bidirectional: if person A is friend of a person B, that means that person B is a friend of a person A.
	 * 
	 * @param codePerson1	first person code
	 * @param codePerson2	second person code
	 * @throws NoSuchCodeException in case either code does not exist
	 */
	public void addFriendship(String codePerson1, String codePerson2)
			throws NoSuchCodeException {
				Person p1 = persons.get(codePerson1), p2 = persons.get(codePerson2);
				if (p1==null||p2==null) throw new NoSuchCodeException();
				else { 
					p1.addFriendship(p2);
					p2.addFriendship(p1);
				}
	}

	/**
	 * Retrieve the collection of their friends given the code of a person.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return the list of person codes
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> listOfFriends(String codePerson)
			throws NoSuchCodeException {
				Person p = persons.get(codePerson);
				if (p==null) throw new NoSuchCodeException();
				else {
					return p.getFriendList().stream().map(pp->pp.getCode()).collect(Collectors.toList());
				}
	}

	/**
	 * Retrieves the collection of the code of the friends of the friends
	 * of the person whose code is given, i.e. friends of the second level.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return collections of codes of second level friends
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> friendsOfFriends(String codePerson)
			throws NoSuchCodeException {
				Person p = persons.get(codePerson);
				if (p==null) throw new NoSuchCodeException();
				else {
					return p.getFriendList().stream()
					.flatMap(pp->pp.getFriendList().stream())
					.map(pp->pp.getCode())
					.filter(pp->pp!=codePerson)
					.collect(Collectors.toList());
				}
	}
	

	/**
	 * Retrieves the collection of the code of the friends of the friends
	 * of the person whose code is given, i.e. friends of the second level.
	 * The result has no duplicates.
	 * 
	 * 
	 * @param codePerson code of the person
	 * @return collections of codes of second level friends
	 * @throws NoSuchCodeException in case the code does not exist
	 */
	public Collection<String> friendsOfFriendsNoRepetition(String codePerson)
			throws NoSuchCodeException {
			try {
				return friendsOfFriends(codePerson).stream().distinct().collect(Collectors.toList());
			} catch (NoSuchCodeException e){
				throw e;
			}
	}

	/**
	 * Creates a new group with the given name
	 * 
	 * @param groupName name of the group
	 */
	public void addGroup(String groupName) {
		if (!groups.keySet().contains(groupName)) groups.put(groupName,new Group(groupName));
	}

	/**
	 * Retrieves the list of groups.
	 * 
	 * @return the collection of group names
	 */
	public Collection<String> listOfGroups() {
		return groups.keySet();
	}

	/**
	 * Add a person to a group
	 * 
	 * @param codePerson person code
	 * @param groupName  name of the group
	 * @throws NoSuchCodeException in case the code or group name do not exist
	 */
	public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
		Group g = groups.get(groupName);
		Person p = persons.get(codePerson);

		if (p==null||g==null) throw new NoSuchCodeException();
		else {
			g.addPerson(p);
		}
	}

	/**
	 * Retrieves the list of people on a group
	 * 
	 * @param groupName name of the group
	 * @return collection of person codes
	 */
	public Collection<String> listOfPeopleInGroup(String groupName) {
		return groups.get(groupName).getSubscribed().stream().map(Person::getCode).collect(Collectors.toList());
	}

	/**
	 * Retrieves the code of the person having the largest
	 * group of friends
	 * 
	 * @return the code of the person
	 */
	public String personWithLargestNumberOfFriends() {
		Person pp = persons.values().stream().max(Comparator.comparingInt(p->p.getFriendsNumber())).orElse(null);
		return pp==null?"<nofriends>":pp.print();
	}

	/**
	 * Find the code of the person with largest number
	 * of second level friends
	 * 
	 * @return the code of the person
	 */
	public String personWithMostFriendsOfFriends() {

		Map<String, Long> gg= persons.keySet().stream()
		.collect(
			Collectors.toMap(
				p->p, 
				p-> {try{
					return (long) friendsOfFriendsNoRepetition(p).size();
				} catch (NoSuchCodeException e){throw new RuntimeException();}}
				));
		String p = gg.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null).getKey();

		return p;
	}

	/**
	 * Find the name of group with the largest number of members
	 * 
	 * @return the name of the group
	 */
	public String largestGroup() {
		Map<String, Integer> gxn =  groups.entrySet().stream()
		.collect(Collectors.toMap(g->g.getKey(), g->g.getValue().getSubsNumber()));
		return gxn.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null).getKey();
	}

	/**
	 * Find the code of the person that is member of
	 * the largest number of groups
	 * 
	 * @return the code of the person
	 */
	public String personInLargestNumberOfGroups() {
		return groups.keySet().stream()
		.flatMap(k->listOfPeopleInGroup(k).stream())
		.collect(Collectors.groupingBy(k->k, Collectors.counting()))
		.entrySet().stream()
		.max(Map.Entry.comparingByValue())
		.orElse(null)
		.getKey()
		;
	}

	/**
	 * add a new post by a given account
	 * @param author the id of the post author
	 * @param text the content of the post
	 * @return a unique id of the post
	 */
    public String post(String author, String text) {
		return persons.get(author).addPost(text, System.currentTimeMillis()).getSerial().toString();
    }

	/**
	 * retrieves the content of the given post
	 * @param author	the author of the post
	 * @param pid		the id of the post
	 * @return the content of the post
	 */
    public String getPostContent(String author, String pid) {
		return persons.get(author).getPost(Integer.parseInt(pid)).getMessage();
    }

	/**
	 * retrieves the timestamp of the given post
	 * @param author	the author of the post
	 * @param pid		the id of the post
	 * @return the timestamp of the post
	 */
    public long getTimestamp(String author, String pid) {
		return persons.get(author).getPost(Integer.parseInt(pid)).getTime();
    }

	/**
	 * returns the list of post of a given author paginated 
	 * 
	 * @param author	author of the post
	 * @param pageNo	page number (starting at 1)
	 * @param pageLength page length
	 * @return the list of posts id
	 */
    public List<String> getPaginatedUserPosts(String author, int pageNo, int pageLength) {
		return persons.get(author).getPosts().stream().skip(pageLength*(pageNo-1)).limit(pageLength).map(p->p.getSerial().toString()).collect(Collectors.toList());
    }

	/**
	 * returns the paginated list of post of friends 
	 * 
	 * the returned list contains the author and the id of a post separated by ":"
	 * 
	 * @param author	author of the post
	 * @param pageNo	page number (starting at 1)
	 * @param pageLength page length
	 * @return the list of posts key elements
	 */
	public List<String> getPaginatedFriendPosts(String author, int pageNo, int pageLength) {
		return null;
	}
}