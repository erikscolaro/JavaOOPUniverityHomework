package mountainhuts;

import java.util.Optional;

/**
 * Represents a mountain hut
 * 
 * It includes a name, optional altitude, category,
 * number of beds and location municipality.
 *  
 *
 */
public class MountainHut {
	String name, category;
	Integer beds;
	Municipality municipality;
	Optional<Integer> altitude;


	public MountainHut(String name, Integer altitude, String category, Integer bedsNumber, Municipality municipality) {
		this.name=name;
		this.category=category;
		this.beds=bedsNumber;
		this.municipality=municipality;
		this.altitude= Optional.ofNullable(altitude);
	}

	public String getName() {
		return name;
	}

	public Optional<Integer> getAltitude() {
		return altitude;
	}

	public String getCategory() {
		return category;
	}

	public Integer getBedsNumber() {
		return beds;
	}

	public Municipality getMunicipality() {
		return municipality;
	}
}
