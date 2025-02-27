package mountainhuts;

/**
 * Class representing a municipality that hosts a mountain hut.
 * It is a data class with getters for name, province, and altitude
 * 
 */
public class Municipality {
	private String name, province;
	private Integer altitude;

	public Municipality(String name, String province, Integer altitude) {
		this.altitude=altitude;
		this.name=name;
		this.province=province;
	}

	public String getName() {
		return name;
	}

	public String getProvince() {
		return province;
	}

	public Integer getAltitude() {
		return altitude;
	}

}
