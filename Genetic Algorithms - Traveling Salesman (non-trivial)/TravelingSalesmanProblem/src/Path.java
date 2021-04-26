import java.util.ArrayList;
import java.util.Collections;

public class Path {
	private ArrayList<City> path;
	private double pathProbability;
	private double fitness = 0;
	private int distance = 0;

	// empty constructor
	// adds empty cities to the path so that they can be replaced afterwards
	// (otherwise, cities can't be added)
	public Path() {
		this.path = new ArrayList<City>(10);
		for (int i = 0; i < 10; i++) {
			path.add(new City("-"));
		}
		distance = 0;
	}
	
	// path constructor
	// sets a given tour as this path
	public Path(Path path) {
		this.path = new ArrayList<City>(10);
		for (int i = 0; i < 10; i++) {
			this.path.add(new City("-"));
		}
		for (int i = 0; i < 10; i++) {
			this.path.set(i, path.getCity(i));
		}
		distance = getPathDistance();
	}

	// generates a random valid path
	public void generateIndividual(ArrayList<City> cities) {
		this.path.clear();
		for (City city : cities) {
			this.path.add(city);
		}
		Collections.shuffle(this.path);
		distance = getPathDistance();
	}

	// necessary for the roulette wheel selection 
	// normalizes the fitnesses using the the total sum
	public void setPathProbability(double total, double fitness) {
		this.pathProbability = fitness / total;
	}
	
	// set a city at a certain index in the path
	public void setCity(City city, int index) {
		path.set(index, city);
		fitness = 0;
		distance = 0;
	}

	// get the city at a certain index in the path
	public City getCity(int index) {
		return path.get(index);
	}

	// check if city is in the path
	public boolean containsCity(City city) {
		boolean contains = false;
		for (City c : path) {
			if (c.getName().equals(city.getName())) {
				contains = true;
			}
		}
		return contains;
	}
	
	// get the index of city in the path
	public int getIndex(City city) {
		for (City c : path) {
			if (c.getName().equals(city.getName())) {
				return path.indexOf(c);
			}
		}
		return -1;
	}

	// set an array of cities as path
	public void setPath(ArrayList<City> p) {
		this.path.clear();
		for (City c : p) {
			this.path.add(c);
		}
		distance = getPathDistance();
	}

	// get the current path
	public ArrayList<City> getPath() {
		return path;
	}

	// get the fitness of the path
	public double getFitness() {
		fitness = 1 / (double) getPathDistance();
		return fitness;
	}

	// get the path probability 
	// used for roulette wheel selection
	public double getPathProbability() {
		return pathProbability;
	}

	// get the distance of the path
	// includes distance back to starting point
	public int getPathDistance() {
		int pathDistance = 0;

		for (int i = 0; i < 9; i++) {
			pathDistance += path.get(i).getDistanceTo(path.get(i + 1));
		}
		pathDistance += path.get(9).getDistanceTo(path.get(0));
		distance = pathDistance;
		return distance;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (City city : path) {
			s += city.getName() + " ";
		}
		s += path.get(0).getName();
		return s;
	}
}