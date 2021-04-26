import java.util.ArrayList;

public class Population {
	// array containing the possible cities for the path
	private ArrayList<City> possibleCities = new ArrayList<City>(10);
	private ArrayList<Path> paths;
	private Integer size = 0;
	
	// constructor - needs the size, and the array of possible cities
	// initialize is used to differentiate the first population from the others
	// for the first population, the paths are randomly generated
	public Population(int size, boolean initialize, ArrayList<City> cities) {
		paths = new ArrayList<Path>();
		possibleCities = cities;
		this.size = size;
		if (initialize) {
			for (int i = 0; i < size; i++) {
				Path newPath = new Path();
				newPath.generateIndividual(cities);
				addPath(newPath);
			}
		}
	}

	// adds a path to the population
	public void addPath(Path path) {
		paths.add(path);
	}
	
	// check if path is already contained in the population
	public boolean pathAlreadyAdded(Path path) {
		for (Path p : paths) {
			if (p.getPath().equals(path.getPath())) {
				return true;
			}
		}
		return false;
	}
	
	// add pair of offspring to the population 
	public void addOffspring(Path[] offspring) {
		paths.add(offspring[0]);
		paths.add(offspring[1]);
	}

	// get the path at a certain index
	public Path getPath(int index) {
		return paths.get(index);
	}
	
	// get all the paths of the population
	public ArrayList<Path> getPaths() {
		return paths;
	}

	// get the fittest path of the population
	public Path getFittest() {
		Path fittest = paths.get(0);

		for (Path path : paths) {
			if (fittest.getFitness() < path.getFitness()) {
				fittest = path;
			}
		}
		return fittest;
	}
	
	// get the size of the population
	public int getSize() {
		return size;
	}
	
	// set the possible cities of the population
	public ArrayList<City> getPossibleCities() {
		return possibleCities;
	}
	
	@Override
	public String toString() {
		return "\n" + paths.toString();
	}
}