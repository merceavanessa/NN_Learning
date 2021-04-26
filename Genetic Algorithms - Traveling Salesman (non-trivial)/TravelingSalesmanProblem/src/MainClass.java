import java.util.*;

public class MainClass {
	public static final int correctAnswer = 36;

	public static void main(String[] args) {
		ArrayList<City> cities = new ArrayList<City>(10);
		int generations, minDist, countCorrectAnswers;

		City cityA = new City("A");
		City cityB = new City("B");
		City cityC = new City("C");
		City cityD = new City("D");
		City cityE = new City("E");
		City cityF = new City("F");
		City cityG = new City("G");
		City cityH = new City("H");
		City cityI = new City("I");
		City cityJ = new City("J");

		cityA.addDistanceToOtherCities(cityB, 5);
		cityA.addDistanceToOtherCities(cityC, 4);
		cityA.addDistanceToOtherCities(cityD, 7);
		cityA.addDistanceToOtherCities(cityE, 6);
		cityA.addDistanceToOtherCities(cityF, 5);
		cityA.addDistanceToOtherCities(cityG, 7);
		cityA.addDistanceToOtherCities(cityH, 4);
		cityA.addDistanceToOtherCities(cityI, 2);
		cityA.addDistanceToOtherCities(cityJ, 9);

		cityB.addDistanceToOtherCities(cityA, 6);
		cityB.addDistanceToOtherCities(cityC, 5);
		cityB.addDistanceToOtherCities(cityD, 6);
		cityB.addDistanceToOtherCities(cityE, 4);
		cityB.addDistanceToOtherCities(cityF, 8);
		cityB.addDistanceToOtherCities(cityG, 5);
		cityB.addDistanceToOtherCities(cityH, 4);
		cityB.addDistanceToOtherCities(cityI, 3);
		cityB.addDistanceToOtherCities(cityJ, 8);

		cityC.addDistanceToOtherCities(cityA, 3);
		cityC.addDistanceToOtherCities(cityB, 5);
		cityC.addDistanceToOtherCities(cityD, 3);
		cityC.addDistanceToOtherCities(cityE, 5);
		cityC.addDistanceToOtherCities(cityF, 6);
		cityC.addDistanceToOtherCities(cityG, 9);
		cityC.addDistanceToOtherCities(cityH, 8);
		cityC.addDistanceToOtherCities(cityI, 7);
		cityC.addDistanceToOtherCities(cityJ, 6);

		cityD.addDistanceToOtherCities(cityA, 7);
		cityD.addDistanceToOtherCities(cityB, 5);
		cityD.addDistanceToOtherCities(cityC, 4);
		cityD.addDistanceToOtherCities(cityE, 3);
		cityD.addDistanceToOtherCities(cityF, 5);
		cityD.addDistanceToOtherCities(cityG, 7);
		cityD.addDistanceToOtherCities(cityH, 9);
		cityD.addDistanceToOtherCities(cityI, 8);
		cityD.addDistanceToOtherCities(cityJ, 3);

		cityE.addDistanceToOtherCities(cityA, 5);
		cityE.addDistanceToOtherCities(cityB, 4);
		cityE.addDistanceToOtherCities(cityC, 5);
		cityE.addDistanceToOtherCities(cityD, 3);
		cityE.addDistanceToOtherCities(cityF, 4);
		cityE.addDistanceToOtherCities(cityG, 6);
		cityE.addDistanceToOtherCities(cityH, 7);
		cityE.addDistanceToOtherCities(cityI, 8);
		cityE.addDistanceToOtherCities(cityJ, 7);

		cityF.addDistanceToOtherCities(cityA, 5);
		cityF.addDistanceToOtherCities(cityB, 6);
		cityF.addDistanceToOtherCities(cityC, 5);
		cityF.addDistanceToOtherCities(cityD, 5);
		cityF.addDistanceToOtherCities(cityE, 4);
		cityF.addDistanceToOtherCities(cityG, 5);
		cityF.addDistanceToOtherCities(cityH, 4);
		cityF.addDistanceToOtherCities(cityI, 3);
		cityF.addDistanceToOtherCities(cityJ, 2);

		cityG.addDistanceToOtherCities(cityA, 6);
		cityG.addDistanceToOtherCities(cityB, 7);
		cityG.addDistanceToOtherCities(cityC, 9);
		cityG.addDistanceToOtherCities(cityD, 7);
		cityG.addDistanceToOtherCities(cityE, 6);
		cityG.addDistanceToOtherCities(cityF, 6);
		cityG.addDistanceToOtherCities(cityH, 5);
		cityG.addDistanceToOtherCities(cityI, 7);
		cityG.addDistanceToOtherCities(cityJ, 9);

		cityH.addDistanceToOtherCities(cityA, 5);
		cityH.addDistanceToOtherCities(cityB, 4);
		cityH.addDistanceToOtherCities(cityC, 8);
		cityH.addDistanceToOtherCities(cityD, 7);
		cityH.addDistanceToOtherCities(cityE, 6);
		cityH.addDistanceToOtherCities(cityF, 4);
		cityH.addDistanceToOtherCities(cityG, 4);
		cityH.addDistanceToOtherCities(cityI, 6);
		cityH.addDistanceToOtherCities(cityJ, 5);

		cityI.addDistanceToOtherCities(cityA, 2);
		cityI.addDistanceToOtherCities(cityB, 3);
		cityI.addDistanceToOtherCities(cityC, 6);
		cityI.addDistanceToOtherCities(cityD, 9);
		cityI.addDistanceToOtherCities(cityE, 8);
		cityI.addDistanceToOtherCities(cityF, 3);
		cityI.addDistanceToOtherCities(cityG, 7);
		cityI.addDistanceToOtherCities(cityH, 5);
		cityI.addDistanceToOtherCities(cityJ, 7);

		cityJ.addDistanceToOtherCities(cityA, 9);
		cityJ.addDistanceToOtherCities(cityB, 7);
		cityJ.addDistanceToOtherCities(cityC, 5);
		cityJ.addDistanceToOtherCities(cityD, 4);
		cityJ.addDistanceToOtherCities(cityE, 8);
		cityJ.addDistanceToOtherCities(cityF, 3);
		cityJ.addDistanceToOtherCities(cityG, 9);
		cityJ.addDistanceToOtherCities(cityH, 5);
		cityJ.addDistanceToOtherCities(cityI, 7);

		cities.add(cityA);
		cities.add(cityB);
		cities.add(cityC);
		cities.add(cityD);
		cities.add(cityE);
		cities.add(cityF);
		cities.add(cityG);
		cities.add(cityH);
		cities.add(cityI);
		cities.add(cityJ);

		countCorrectAnswers = 0;
		for (int j = 0; j < 100; j++) {
			Population population = new Population(50, true, cities);
			Path best = new Path();
			generations = 80;
			minDist = 1000;
			for (int i = 1; i <= generations; i++) {
				population = GA.evolvePopulation(population);
				if (population.getFittest().getPathDistance() < minDist) {
					minDist = population.getFittest().getPathDistance();
					best.setPath(population.getFittest().getPath());
				}
			}
			if (minDist == correctAnswer) {
				countCorrectAnswers += 1;
			}
		}
		
		System.out.println("The output was " + countCorrectAnswers + "% correct.");
	}
}