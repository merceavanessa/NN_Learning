import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class GA {
	// preset values for different behavior of the evolution; may be changed

	// keep the best individual from the previous generation;
	private static final boolean elitism = true;
	// allow mutationProbability to adapt
	private static final boolean adaptableMutation = true;
	// increase rate for the mutation
	private static final double mutationIncreaseRate = 1.2;
	// available mutations: displacement, swap or scramble
	private static final String preferredMutation = "displacement";
	// available crossovers: pmx, cx, ox or er
	private static final String preferredCrossover = "ox";
	// available selections: roulette or tournament
	private static final String preferredSelectionOfParents = "tournament";
	// max allowed no of repeating outputs before the mutation rate increases
	private static final int maxRepetitions = 5; 
	// size for the pool of the tournament selection
	private static final int tournamentSize = 6; 
	// probability of a crossover to occur
	private static double crossoverProbability = 1;
	// probability of mutation to occur (for adaptable, this is the starting value)
	private static double mutationProbabilty = 0.05;

	// variables dependent to the problem; may not be changed

	private static int repetitionsCount = 0;
	private static int prevBestDistance;
	private static int crossoverLeftCut = 3;
	private static int crossoverRightCut = 7;
	private static int maxPathLength = 10;

	// evolves a population over one generation and returns the new generation
	public static Population evolvePopulation(Population population) {
		int resultPopulationSize = population.getSize();
		int parentsPopulationSize = population.getSize() / 2;
		Population resultPopulation = new Population(resultPopulationSize, false, population.getPossibleCities());
		Path[] offspring = new Path[2], parents = new Path[2];
		
		adaptMutation();
		addElite(resultPopulation, population);

		for (int i = 0; i < parentsPopulationSize; i++) {
			applyParentSelection(parents, population);
			offspring = applyCrossover(parents);
			resultPopulation.addOffspring(offspring);
		}
		applyMutation(resultPopulation);
		updateNumberOfRepetitions(resultPopulation);
		return resultPopulation;
	}

	// update the number of repetitions and the previous best distance
	public static void updateNumberOfRepetitions(Population p) {
		if (adaptableMutation) {
			if (repeatingOutcome(p)) {
				repetitionsCount += 1;
			} else {
				repetitionsCount = 0;
			}
			prevBestDistance = bestDistance(p);
		}
	}
	
	// if best outcome of the current population is the same as the previous one
	public static boolean repeatingOutcome(Population p) {
		return p.getFittest().getPathDistance() == prevBestDistance;
	}
	
	// increase the probability of mutation using the increase rate
	public static void increaseMutationProbability() {
		mutationProbabilty += mutationProbabilty * mutationIncreaseRate;
	}
		
	// adapt mutation based on the repetitions of outcome
	public static void adaptMutation() {
		if (adaptableMutation) {
			if (repetitionsCount > maxRepetitions) {
				increaseMutationProbability();
				repetitionsCount = 0;
			}
		}
	}

	// get best distance from the given population
	public static int bestDistance(Population p) {
		return p.getFittest().getPathDistance();
	}

	
	// add the best individual of the previous population to the current one
	public static void addElite(Population newP, Population oldP) {
		if (elitism) {
			newP.addPath(oldP.getFittest());
		}
	}

	// selection of parents
	public static void parentSelection(Path[] parents, Population population) {
		switch (preferredSelectionOfParents) {
		case ("roulette"):
			parents[0] = rouletteWheelSelection(population);
			parents[1] = rouletteWheelSelection(population);
			break;
		case ("tournament"):
			parents[0] = tournamentSelection(population);
			parents[1] = tournamentSelection(population);
			break;
		}
	}

	// apply selection of parents considering crossover probability
	public static void applyParentSelection(Path[] parents, Population population) {
		parentSelection(parents, population);
		while (crossoverProbability < Math.random()) {
			parentSelection(parents, population);
		}
	}

	// apply crossover according to the preferred one
	public static Path[] applyCrossover(Path[] parents) {
		Path[] offspring = new Path[2];
		switch (preferredCrossover) {
		case ("pmx"):
			offspring = pmxCrossover(parents[0], parents[1]);
			break;
		case ("cx"):
			offspring = cxCrossover(parents[0], parents[1]);
			break;
		case ("ox"):
			offspring = oxCrossover(parents[0], parents[1]);
			break;
		case ("er"):
			offspring = erCrossover(parents[0], parents[1]);
			break;
		default:
		}
		return offspring;
	}

	// apply mutation according to the preferred one
	public static void applyMutation(Population p) {
		switch (preferredMutation) {
		case ("displacement"):
			for (Path path : p.getPaths()) {
				displacementMutation(path);
			}
			break;
		case ("swap"):
			for (Path path : p.getPaths()) {
				swapMutation(path);
			}
			break;
		case ("scramble"):
			for (Path path : p.getPaths()) {
				scrambleMutation(path);
			}
			;
			break;
		default:
		}
	}

	// partially mapped crossover
	public static Path[] pmxCrossover(Path parent1, Path parent2) {
		Path offspring1 = new Path();
		Path offspring2 = new Path();
		City mappedValue;
		int mappedIndex = 0;

		// in between cutting points copy parents into offspring
		for (int i = crossoverLeftCut; i < crossoverRightCut; i++) {
			offspring1.setCity(parent1.getCity(i), i);
			offspring2.setCity(parent2.getCity(i), i);
		}

		// to the left of the cut copy values from alternate parent
		for (int i = 0; i < crossoverLeftCut; i++) {
			// offspring 1
			// while the value to be inserted in offspring 1 from parent 2 is already in
			// offspring
			// try to insert the value from the same position from parent 1
			mappedValue = parent2.getCity(i);
			while (offspring1.containsCity(mappedValue)) {
				mappedIndex = parent1.getIndex(mappedValue);
				mappedValue = parent2.getCity(mappedIndex);
			}
			offspring1.setCity(mappedValue, i);

			// offspring 2
			mappedValue = parent1.getCity(i);
			while (offspring2.containsCity(mappedValue)) {
				mappedIndex = parent2.getIndex(mappedValue);
				mappedValue = parent1.getCity(mappedIndex);
			}
			offspring2.setCity(mappedValue, i);
		}

		// to the right of the cut copy values from alternate parent
		for (int i = crossoverRightCut; i < maxPathLength; i++) {
			// offspring 1
			mappedValue = parent2.getCity(i);
			while (offspring1.containsCity(mappedValue)) {
				mappedIndex = parent1.getIndex(mappedValue);
				mappedValue = parent2.getCity(mappedIndex);
			}
			offspring1.setCity(mappedValue, i);

			// offspring 2
			mappedValue = parent1.getCity(i);
			while (offspring2.containsCity(mappedValue)) {
				mappedIndex = parent2.getIndex(mappedValue);
				mappedValue = parent1.getCity(mappedIndex);
			}
			offspring2.setCity(mappedValue, i);
		}

		Path[] offspring = new Path[2];
		offspring[0] = offspring1;
		offspring[1] = offspring2;

		return offspring;
	}

	// order crossover ox1
	public static Path[] oxCrossover(Path parent1, Path parent2) {
		Path offspring1 = new Path();
		Path offspring2 = new Path();
		City mappedValue;
		int mappedIndex;

		// in between the cuts copy the parents into offspring
		for (int i = crossoverLeftCut; i < crossoverRightCut; i++) {
			offspring1.setCity(parent1.getCity(i), i);
			offspring2.setCity(parent2.getCity(i), i);
		}

		// offspring 1
		mappedIndex = crossoverRightCut;
		// start from the right of the crossover
		for (int i = crossoverRightCut; i < maxPathLength; i++) {
			// while the value to be inserted in offspring 1 from parent 2 is already in
			// offspring,
			// iterate through the values of parent 2 and continue from position 0 when
			// reaching the end
			mappedValue = parent2.getCity(mappedIndex);
			while (offspring1.containsCity(mappedValue)) {
				mappedIndex += 1;
				if (mappedIndex > maxPathLength - 1) {
					mappedIndex = 0;
				}
				mappedValue = parent2.getCity(mappedIndex);
			}
			offspring1.setCity(mappedValue, i);
			mappedIndex += 1;
			if (mappedIndex > maxPathLength - 1) {
				mappedIndex = 0;
			}
		}
		// continue with the part left of the crossover
		// the index remains where it was set by the previous loop
		for (int i = 0; i < crossoverLeftCut; i++) {
			mappedValue = parent2.getCity(mappedIndex);
			while (offspring1.containsCity(mappedValue)) {
				mappedIndex += 1;
				if (mappedIndex > maxPathLength - 1) {
					mappedIndex = 0;
				}
				mappedValue = parent2.getCity(mappedIndex);
			}
			offspring1.setCity(mappedValue, i);
			mappedIndex += 1;
			if (mappedIndex > maxPathLength - 1) {
				mappedIndex = 0;
			}
		}

		// offspring 2
		mappedIndex = crossoverRightCut;
		// right side of the crossover
		for (int i = crossoverRightCut; i < maxPathLength; i++) {
			mappedValue = parent1.getCity(mappedIndex);

			while (offspring2.containsCity(mappedValue)) {
				mappedIndex += 1;
				if (mappedIndex > maxPathLength - 1) {
					mappedIndex = 0;
				}
				mappedValue = parent1.getCity(mappedIndex);
			}
			offspring2.setCity(mappedValue, i);
			mappedIndex += 1;
			if (mappedIndex > maxPathLength - 1) {
				mappedIndex = 0;
			}
		}
		// left side of the crossover
		for (int i = 0; i < crossoverLeftCut; i++) {
			mappedValue = parent1.getCity(mappedIndex);

			while (offspring2.containsCity(mappedValue)) {
				mappedIndex += 1;
				if (mappedIndex > maxPathLength - 1) {
					mappedIndex = 0;
				}
				mappedValue = parent1.getCity(mappedIndex);
			}
			offspring2.setCity(mappedValue, i);
			mappedIndex += 1;
			if (mappedIndex > maxPathLength - 1) {
				mappedIndex = 0;
			}
		}

		Path[] offspring = new Path[2];
		offspring[0] = offspring1;
		offspring[1] = offspring2;

		return offspring;
	}

	// cycle crossover
	public static Path[] cxCrossover(Path parent1, Path parent2) {
		// compute offspring 1 starting from parent 1 at a random starting position
		ArrayList<City> cycle1 = new ArrayList<City>(maxPathLength);
		int mapped_index = (int) (maxPathLength * Math.random());
		City mapped_value = parent1.getCity(mapped_index);
		Path offspring1 = new Path();

		// while not closed a cycle continue filling result with values from parent 1
		offspring1.setCity(mapped_value, mapped_index);
		while ((!cycle1.contains(mapped_value)) && (cycle1.size() <= maxPathLength)) {
			cycle1.add(mapped_value);

			mapped_index = parent1.getIndex(mapped_value);
			mapped_value = parent2.getCity(mapped_index);

			offspring1.setCity(parent1.getCity(mapped_index), mapped_index);
		}

		// fill the values not contained in the cycle with those from parent 2
		for (int i = 0; i < maxPathLength; i++) {
			if (offspring1.getCity(i).getName() == "-") {
				offspring1.setCity(parent2.getCity(i), i);
			}
		}

		// compute offspring 2 starting from parent 2, at the same starting position
		ArrayList<City> cycle2 = new ArrayList<City>(maxPathLength);
		mapped_value = parent2.getCity(mapped_index);
		Path offspring2 = new Path();

		// while not closed a cycle continue filling result with values from parent 2
		offspring2.setCity(mapped_value, mapped_index);
		while (!cycle2.contains(mapped_value) && cycle2.size() <= maxPathLength) {
			cycle2.add(mapped_value);

			mapped_index = parent2.getIndex(mapped_value);
			mapped_value = parent1.getCity(mapped_index);

			offspring2.setCity(parent2.getCity(mapped_index), mapped_index);
		}

		// fill the values not contained in the cycle with those from parent 1
		for (int i = 0; i < maxPathLength; i++) {
			if (offspring2.getCity(i).getName() == "-") {
				offspring2.setCity(parent1.getCity(i), i);
			}
		}

		Path[] offspring = new Path[2];
		offspring[0] = offspring1;
		offspring[1] = offspring2;
		return offspring;
	}

	// edge recombination crossover
	public static Path[] erCrossover(Path parent1, Path parent2) {
		Path offspring1 = new Path();
		Path offspring2 = new Path();
		int cityIndex, prevCityIndex = 0, nextCityIndex = 0;

		// create the neighbour list for each offspring
		Hashtable<City, Set<City>> neighbors1 = new Hashtable<City, Set<City>>(4);
		Hashtable<City, Set<City>> neighbors2 = new Hashtable<City, Set<City>>(4);

		// add all cities as heads for the neighbour mapping
		for (City city : parent1.getPath()) {
			neighbors1.put(city, new HashSet<City>(4));
			neighbors2.put(city, new HashSet<City>(4));
		}

		// add neighbors for each city from parent 1
		for (City city : parent1.getPath()) {
			cityIndex = parent1.getIndex(city);
			if (cityIndex == (maxPathLength - 1)) {
				prevCityIndex = cityIndex - 1;
				nextCityIndex = 0;
			} else if (cityIndex == 0) {
				prevCityIndex = maxPathLength - 1;
				nextCityIndex = cityIndex + 1;
			} else {
				prevCityIndex = cityIndex - 1;
				nextCityIndex = cityIndex + 1;
			}
			neighbors1.get(city).add(parent1.getCity(prevCityIndex));
			neighbors1.get(city).add(parent1.getCity(nextCityIndex));
			neighbors2.get(city).add(parent1.getCity(prevCityIndex));
			neighbors2.get(city).add(parent1.getCity(nextCityIndex));
		}

		// add neighbors for each city from parent 2
		for (City city : parent2.getPath()) {
			cityIndex = parent2.getIndex(city);
			if (cityIndex == (maxPathLength - 1)) {
				prevCityIndex = cityIndex - 1;
				nextCityIndex = 0;
			} else if (cityIndex == 0) {
				prevCityIndex = maxPathLength - 1;
				nextCityIndex = cityIndex + 1;
			} else {
				prevCityIndex = cityIndex - 1;
				nextCityIndex = cityIndex + 1;
			}
			neighbors1.get(city).add(parent2.getCity(prevCityIndex));
			neighbors1.get(city).add(parent2.getCity(nextCityIndex));
			neighbors2.get(city).add(parent2.getCity(prevCityIndex));
			neighbors2.get(city).add(parent2.getCity(nextCityIndex));
		}

		// offspring 1
		// random select a starting city
		int r = (int) (maxPathLength * Math.random());
		City currentCity = parent1.getCity(r);
		City minNeighborsKey = null;
		int min;
		cityIndex = 0;
		// add city to offspring 1
		offspring1.setCity(currentCity, cityIndex);
		neighbors1.remove(currentCity);
		while (!neighbors1.isEmpty()) {
			min = 5;
			// remove city from all the other city neighbors
			for (City keyCity : neighbors1.keySet()) {
				neighbors1.get(keyCity).remove(currentCity);
			}
			// find the key city with minimum number of neighbors
			for (City keyCity : neighbors1.keySet()) {
				if (neighbors1.get(keyCity).size() < min) {
					minNeighborsKey = keyCity;
					min = neighbors1.get(keyCity).size();
				}
			}
			cityIndex += 1;
			// add city to offspring
			offspring1.setCity(minNeighborsKey, cityIndex);
			currentCity = minNeighborsKey;
			// remove city from keys of the map
			neighbors1.remove(currentCity);
		}

		// offspring 2
		// same, just choose another random starting point
		r = (int) (maxPathLength * Math.random());
		currentCity = parent1.getCity(r);
		cityIndex = 0;
		// add city to offspring 2
		offspring2.setCity(currentCity, cityIndex);
		// remove city from keys of the map
		neighbors2.remove(currentCity);
		while (!neighbors2.isEmpty()) {
			min = 5;
			// remove city from all the other city neighbors
			for (City keyCity : neighbors2.keySet()) {
				neighbors2.get(keyCity).remove(currentCity);
			}
			// find the key city with minimum number of neighbors
			for (City keyCity : neighbors2.keySet()) {
				if (neighbors2.get(keyCity).size() < min) {
					minNeighborsKey = keyCity;
					min = neighbors2.get(keyCity).size();
				}
			}
			cityIndex += 1;
			// add city to offspring
			offspring2.setCity(minNeighborsKey, cityIndex);
			currentCity = minNeighborsKey;
			// remove city from keys of the map
			neighbors2.remove(currentCity);
		}

		Path[] offspring = new Path[2];
		offspring[0] = offspring1;
		offspring[1] = offspring2;
		return offspring;
	}

	// swap mutation
	private static void swapMutation(Path path) {
		if (Math.random() < mutationProbabilty) {
			// get two random positions in the path (ignoring starting point)
			int i = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			int j = (int) ((Math.random() * (maxPathLength - 1)) + 1);

			// get the cities located at the random positions
			City city1 = path.getCity(i);
			City city2 = path.getCity(j);

			// swap the cities
			path.setCity(city1, j);
			path.setCity(city2, i);
		}
	}

	// scramble mutation
	private static void scrambleMutation(Path path) {
		if (Math.random() < mutationProbabilty) {
			// get two random positions in the path (ignoring starting point)
			int i = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			int j = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			
			// make sure that the positions are ordered and distinct
			while (i >= j) {
				i = (int) ((Math.random() * (maxPathLength - 1)) + 1);
				j = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			}
			
			ArrayList<City> toScramble = new ArrayList<City>(10);
			// get the cities located between the random positions
			for (int k = i; k <= j; k++) {
				toScramble.add(path.getCity(k));
			}

			// scramble the list
			Collections.shuffle(toScramble);

			// replace list in between the random positions with the scrambled list
			int l = 0;
			for (int k = i; k <= j; k++) {
				path.setCity(toScramble.get(l), k);
				l += 1;
			}
		}
	}

	// displacement mutation
	private static void displacementMutation(Path path) {
		if (Math.random() < mutationProbabilty) {
			// get two random positions in the path (ignoring starting point)
			int i = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			int j = (int) ((Math.random() * (maxPathLength - 1)) + 1);

			// make sure that the positions are ordered and distinct
			while (i >= j) {
				i = (int) ((Math.random() * (maxPathLength - 1)) + 1);
				j = (int) ((Math.random() * (maxPathLength - 1)) + 1);
			}

			ArrayList<City> displacement = new ArrayList<City>(crossoverRightCut - crossoverLeftCut);
			ArrayList<City> rest = new ArrayList<City>(maxPathLength);
			// add the cities in between the random positions to the displacement list
			// add the others to the rest list
			for (int k = 0; k < maxPathLength; k++) {
				if (k >= i && k <= j) {
					displacement.add(path.getCity(k));
				} else {
					rest.add(path.getCity(k));
				}
			}

			// generate a random position where to move the displacement (ignoring the starting point)
			int r = (int) ((Math.random() * (rest.size() - 1)) + 1);
			// insert the displacement in the rest list at the computed random position
			rest.addAll(r, displacement);
			// set the input path to rest
			path.setPath(rest);
		}
	}

	// roulette wheel selection
	public static Path rouletteWheelSelection(Population p) {
		double totalSum = 0.0;
		double partialSum = 0;
		double roulette = 0;
		
		Path selectedPath = null;

		// compute the sum of the fitnesses of the paths
		for (Path path : p.getPaths()) {
			totalSum += path.getFitness();
		}

		// set a probabilty to the path equal to path fitness / total sum
		for (Path path : p.getPaths()) {
			path.setPathProbability(totalSum, path.getFitness());
		}

		roulette = (double) (Math.random());

		// compute partial sum of the probabilities set above and compare them to a
		// random value
		for (Path path : p.getPaths()) {
			partialSum += path.getPathProbability();

			if (partialSum >= roulette) {
				selectedPath = path;
				break;
			}
		}

		return selectedPath;
	}

	// tournament selection
	private static Path tournamentSelection(Population p) {
		// create a new population
		Population tournament = new Population(tournamentSize, false, p.getPossibleCities());
		// add as many random candidates from the initial population as tournament size
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (int) (Math.random() * p.getSize());
			tournament.addPath(p.getPath(randomId));
		}
		// return the fittest path from the tournament population
		return tournament.getFittest();
	}

}
