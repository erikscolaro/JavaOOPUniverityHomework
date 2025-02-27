package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {

	private String name;
	private List<String> ranges = new ArrayList<>();
	private Map<String, Municipality> muniMap = new HashMap<>();
	private Map<String, MountainHut> hutMap = new HashMap<>();


	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name=name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {

		this.ranges.addAll(Arrays.asList(ranges));

	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {

		Predicate<String[]> inrange = temp->Integer.parseInt(temp[0])<altitude && Integer.parseInt(temp[1])>=altitude;

		return ranges.stream()
			.map(s->s.split("-"))
			.filter(inrange)
			.map(s->String.join("-", s))
			.findFirst()
			.orElse("0-INF");
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return muniMap.values();
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return hutMap.values();
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		muniMap.putIfAbsent(name, new Municipality(name, province, altitude));
		return muniMap.get(name);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,
			Municipality municipality) {
		hutMap.putIfAbsent(name, new MountainHut(name, null, category, bedsNumber, municipality));
		return hutMap.get(name);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,
			Municipality municipality) {
		hutMap.putIfAbsent(name, new MountainHut(name, altitude, category, bedsNumber, municipality));
		return hutMap.get(name);
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {

		Region res = new Region(name);
		List<String> reading = readData(file);

		reading.stream().skip(1).forEach(s->{
			String[] ss =s.split(";");
			Municipality mun = res.createOrGetMunicipality(ss[1], ss[0], Integer.parseInt(ss[2]));
			if (ss[4].isEmpty()) res.createOrGetMountainHut(ss[3], ss[5], Integer.parseInt(ss[6]),mun);
			else res.createOrGetMountainHut(ss[3], Integer.parseInt(ss[4]), ss[5], Integer.parseInt(ss[6]),mun);
		});

		return res;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {

		return muniMap.values().stream()
		.collect(groupingBy(Municipality::getProvince, Collectors.counting()));
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		
		return hutMap.values().stream().collect(
			groupingBy(mh -> mh.getMunicipality().getProvince(), 
				groupingBy( mh2 -> mh2.getMunicipality().getName(), Collectors.counting())));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		
		Function<MountainHut, String> rangeFunction = hm -> {
			if (!hm.getAltitude().isEmpty()) return getAltitudeRange(hm.getAltitude().get());
			else{
				return getAltitudeRange(hm.getMunicipality().getAltitude());
			}
		};
		
		return hutMap.values().stream()
		.collect(
			groupingBy(rangeFunction, Collectors.counting())
		);
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {

		return hutMap.values().stream()
		.collect(groupingBy(hm -> hm.getMunicipality().getProvince(), summingInt(hm -> hm.getBedsNumber())));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		Function<MountainHut, String> rangeFunction = hm -> {
			if (!hm.getAltitude().isEmpty()) return getAltitudeRange(hm.getAltitude().get());
			else{
				return getAltitudeRange(hm.getMunicipality().getAltitude());
			}
		};
		
		return hutMap.values().stream()
		.collect(Collectors.groupingBy(
				rangeFunction,
				mapping(
					MountainHut::getBedsNumber,
					maxBy(Comparator.naturalOrder())
				)
			)
		);
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {

		return hutMap.values().stream()
		.collect(
			groupingBy(
				MountainHut::getMunicipality, counting())
				)
		.entrySet().stream()
		.collect(
			groupingBy(
				Map.Entry::getValue,
				mapping(entry -> entry.getKey().getName(),
				collectingAndThen( 
					toList(),
					names -> names.stream().sorted().toList())))
		);

	}

}
