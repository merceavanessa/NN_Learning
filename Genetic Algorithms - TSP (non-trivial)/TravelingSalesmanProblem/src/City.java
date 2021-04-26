import java.util.Hashtable;

public class City {
	// hash table that contains the distances to all the other cities, from the current one
	Hashtable<City, Integer> distanceToOtherCities = new Hashtable<City, Integer>();
	String name;
	
	// empty constructor
	public City() {
		this.name = "None";
	}
	
	// constructor - assigns name to the city
	public City(String name) {
		this.name = name;
	}

	// adds the distance from the current city to another one
	public void addDistanceToOtherCities(City city, Integer distanceTo) {
		distanceToOtherCities.put(city, distanceTo);
	}

	// gets the distance to the given city from the current city
	public Integer getDistanceTo(City city) {
		if (distanceToOtherCities.get(city) != null)
			return distanceToOtherCities.get(city);
		else 
			return 0;
	}

	// get the name of the current city
	public String getName() {
		return name;
	}

	// implementing this so that Collections.sort() can be used on ArrayLists of cities
	public static boolean isEqual(City c1, City c2) {
		boolean b = false;

		if (c1.getName() == c2.getName())
			b = true;

		return b;
	}
}
