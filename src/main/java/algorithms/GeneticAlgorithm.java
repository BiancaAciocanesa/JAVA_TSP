package algorithms;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {

    int numberOfCities = 0;
    final int popSize = 200;
    final int generations = 2000;

    int elitism = 20;
    int elitismInvers = 20;
    double mutationProbability = 0.2;
    double crossoverProbability = 0.99;
    int generation;

    public static class Node {
        double x;
        double y;
        Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public  void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    List<Node> nodes = new ArrayList<>();

    double randomNumber() {
        return ThreadLocalRandom.current().nextDouble();
    }

    List<Integer> generateEncodedPermutation() {
        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < numberOfCities - 1; i++)
            permutation.add(ThreadLocalRandom.current().nextInt(numberOfCities - i));
        return permutation;
    }

    double calculateDistance(Node n1, Node n2) {
        return Math.sqrt((n1.x - n2.x) * (n1.x - n2.x) + (n1.y - n2.y) * (n1.y - n2.y));
    }

    List<Integer> decode(List<Integer> encoded) {
        List<Integer> decoded = new ArrayList<>();
        List<Integer> identicPermutation = new ArrayList<>();

        for (int i = 0; i < numberOfCities; i++)
            identicPermutation.add(i);

        for (int i = 0; i < numberOfCities - 1; i++) {
            decoded.add(identicPermutation.get(encoded.get(i)) + 1);
            identicPermutation.remove((int) encoded.get(i));
        }
        decoded.add(identicPermutation.getFirst() + 1);

        return decoded;
    }

    double calculateCost(List<Integer> decoded) {
        double cost = 0;
        for (int i = 0; i < numberOfCities - 1; i++) {
            cost += calculateDistance(nodes.get(decoded.get(i)), nodes.get(decoded.get(i + 1)));
        }
        cost += calculateDistance(nodes.get(decoded.get(numberOfCities - 1)), nodes.get(decoded.getFirst()));
        return cost;
    }

    void reverseMutation(List<List<Integer>> population) {
        for (int i = elitism; i < population.size(); i++) {
            double probability = randomNumber();
            if (probability < mutationProbability) {
                int pos1 = ThreadLocalRandom.current().nextInt(numberOfCities - 1);
                int pos2 = ThreadLocalRandom.current().nextInt(numberOfCities - 1);
                if (pos1 > pos2) {
                    int aux = pos1;
                    pos1 = pos2;
                    pos2 = aux;
                }
                Collections.reverse(population.get(i).subList(pos1, pos2));
            }
        }
    }

    void crossover(List<List<Integer>> population) {

        List<List<Integer>> newPopulation = new ArrayList<>();
        List<Integer> child = new ArrayList<>();
        List<Integer> chr1 = new ArrayList<>();
        List<Integer> chr2;

        int firstChromosome = 0, secondChromosome = 0, cnt = 0;
        int pop = population.size();

        for (int i = 0; i < pop; i++) {
            float random = (float) randomNumber();
            if (random < crossoverProbability) {
                cnt++;
                if (cnt == 1) {
                    firstChromosome = i;
                    chr1 = new ArrayList<>(population.get(i));
                } else if (cnt == 2) {
                    cnt = 0;
                    secondChromosome = i;
                    chr2 = new ArrayList<>(population.get(i));

                    List<Integer> permutation = new ArrayList<>();
                    for (int j = 0; j < numberOfCities; j++)
                        permutation.add(j + 1);
                    boolean[] visited = new boolean[numberOfCities + 1];
                    Arrays.fill(visited, false);

                    int randomStart = ThreadLocalRandom.current().nextInt(numberOfCities) + 1;
                    child.add(randomStart);
                    visited[randomStart] = true;

                    permutation.remove((int) randomStart - 1);

                    int currentValue = randomStart;
                    for (int j = 1; j < numberOfCities; j++) {
                        int left1 = 0, right1 = 0, left2 = 0, right2 = 0;
                        for (int k = 0; k < numberOfCities; k++) {
                            if (chr1.get(k) == currentValue) {
                                if (k == 0) {
                                    left1 = chr1.get(numberOfCities - 1);
                                    right1 = chr1.get(1);
                                } else if (k == numberOfCities - 1) {
                                    left1 = chr1.get(k - 1);
                                    right1 = chr1.getFirst();
                                } else {
                                    left1 = chr1.get(k - 1);
                                    right1 = chr1.get(k + 1);
                                }

                            }
                            if (chr2.get(k) == currentValue) {

                                if (k == 0) {
                                    left2 = chr2.get(numberOfCities - 1);
                                    right2 = chr2.get(1);
                                } else if (k == numberOfCities - 1) {
                                    left2 = chr2.get(k - 1);
                                    right2 = chr2.getFirst();
                                } else {
                                    left2 = chr2.get(k - 1);
                                    right2 = chr2.get(k + 1);
                                }

                            }
                        }


                        if (left1 == right2 && !visited[left1]) {

                            currentValue = left1;
                            child.add(currentValue);
                            visited[currentValue] = true;
                        } else if (left1 == left2 && !visited[left1]) {

                            currentValue = left1;
                            child.add(currentValue);
                            visited[currentValue] = true;
                        } else if (right1 == left2 && !visited[left2]) {

                            currentValue = right1;
                            child.add(currentValue);
                            visited[currentValue] = true;
                        } else if (right1 == right2 && !visited[right1]) {

                            currentValue = right1;
                            child.add(currentValue);
                            visited[currentValue] = true;
                        } else {

                            for (int l = 1; l <= numberOfCities; l++) {
                                if (!visited[l]) {
                                    currentValue = l;
                                    child.add(currentValue);
                                    visited[currentValue] = true;
                                    break;
                                }
                            }
                        }

                    }
                    newPopulation.add(new ArrayList<>(child));
                    child.clear();
                    chr1.clear();
                    chr2.clear();
                }
            }
        }
        population.addAll(newPopulation);
    }

    public  double emax(List<Double> eval) {
        double maxim = eval.getFirst();
        for (int i = 1; i < eval.size(); i++) {
            if (eval.get(i) > maxim) {
                maxim = eval.get(i);
            }
        }
        return maxim;
    }

    public  double emin(List<Double> eval) {
        double minim = eval.get(0);
        for (int i = 1; i < eval.size(); i++) {
            if (eval.get(i) < minim) {
                minim = eval.get(i);
            }
        }
        return minim;
    }

    public  List<List<Integer>> selection(List<Double> eval, List<List<Integer>> population) {
        List<List<Integer>> nextPopulation = new ArrayList<>();
        List<Double> fitness = new ArrayList<>();
        List<Double> probability = new ArrayList<>();

        double maxi = emax(eval);
        double mini = emin(eval);
        double T = 0;

        // Calculate fitness
        for (int i = 0; i < eval.size(); i++) {
            double fit = (maxi - eval.get(i)) / (maxi - mini + 0.00001) + 1;
            fitness.add(fit);
            T += fit;
        }
        for (int i = 0; i < fitness.size(); i++) {
            fitness.set(i, fitness.get(i) / T);
        }

        // Calculate probabilities
        probability.add(0.0);
        for (int i = 0; i < fitness.size() - 1; i++) {
            probability.add(probability.get(i) + fitness.get(i));
        }
        probability.add(1.0);

        // Selection
        // Elitism
        for (int i = 0; i < elitism; i++) {
            double minVal = eval.get(0);
            int minPos = 0;
            for (int j = 1; j < eval.size(); j++) {
                if (minVal > eval.get(j)) {
                    minVal = eval.get(j);
                    minPos = j;
                }
            }
            nextPopulation.add(population.get(minPos));
            eval.remove(minPos);
            population.remove(minPos);
            probability.remove(minPos);
        }

        // Inverse elitism
        Random rand = new Random();
        for (int i = 0; i < elitismInvers; i++) {
            double maxVal = eval.getFirst();
            int maxPos = 0;
            for (int j = 1; j < eval.size(); j++) {
                if (maxVal < eval.get(j)) {
                    maxVal = eval.get(j);
                    maxPos = j;
                }
            }

            if (rand.nextInt(2) == 1) {
                nextPopulation.add(population.get(maxPos));
            } else {
                nextPopulation.add(decode(generateEncodedPermutation()));
            }

            eval.remove(maxPos);
            population.remove(maxPos);
            probability.remove(maxPos);
        }

        // Remaining selection
        for (int i = 0; i < popSize - elitism - elitismInvers; i++) {
            double random = randomNumber();
            for (int j = 0; j < probability.size() - 1; j++) {
                if (random >= probability.get(j) && random < probability.get(j + 1)) {
                    nextPopulation.add(population.get(j));
                    break;
                }
            }
        }

        return nextPopulation;
    }

    public  List<Integer> Greedy() {

        int startCity = ThreadLocalRandom.current().nextInt(numberOfCities) + 1;
        List<Integer> cities = new ArrayList<>();
        boolean[] visited = new boolean[numberOfCities + 1];

        cities.add(startCity);
        visited[startCity] = true;
        int pushes = numberOfCities - 1;

        while (pushes-- > 0) {
            double minDistance = Double.MAX_VALUE;
            int closestCityPos = -1;
            for (int next = 1; next <= numberOfCities; next++) {
                if (!visited[next]) {
                    double distance = calculateDistance(nodes.get(next), nodes.get(cities.getLast()));
                    if (minDistance > distance) {
                        minDistance = distance;
                        closestCityPos = next;
                    }
                }
            }
            visited[closestCityPos] = true;
            cities.add(closestCityPos);
        }
        return cities;
    }

    public  List<Integer> geneticAlgorithm() {
        int t = 0;
        List<List<Integer>> startPopulation = new ArrayList<>();
        List<Integer> tour = new ArrayList<>();
        List<Integer> currentTour;
        List<Double> eval = new ArrayList<>();


        for (int i = 0; i < popSize; i++) {
            if (i % 100 == 0) {
                startPopulation.add(Greedy());
            } else {
                startPopulation.add(decode(generateEncodedPermutation()));
            }
        }

        for (int i = 0; i < popSize; i++) {
            eval.add(calculateCost(startPopulation.get(i)));
        }


        double best = Double.MAX_VALUE;
        generation = 0;
        while (t < generations) {
            startPopulation = selection(eval, startPopulation);
            reverseMutation(startPopulation);
            crossover(startPopulation);

            eval.clear();

            for (int i = 0; i < startPopulation.size(); i++) {
                eval.add(calculateCost(startPopulation.get(i)));
            }

            double current_best = eval.getFirst();
            currentTour = List.copyOf(startPopulation.getFirst());
            for (int i = 1; i < eval.size(); i++) {
                if (eval.get(i) < current_best) {
                    current_best = eval.get(i);
                    currentTour =  List.copyOf(startPopulation.get(i));
                }
            }

            if (current_best < best) {
                //System.out.println(current_best);
                best = current_best;
                generation = t;
                tour = List.copyOf(currentTour);
            }

            t++;

        }

        int b = eval.getFirst().intValue();
        int p = 0;
        for (int i = 1; i < eval.size(); i++) {
            if (eval.get(i) < b) {
                b = eval.get(i).intValue();
                p = i;
            }
        }

        System.out.println(best);

        List<Integer> list = new ArrayList<>(tour);
        list.add(tour.getFirst());
        System.out.println("TOUR FOUND BY THE GA: " + list);
        return list;
    }

    public List<Integer> run(List<Point2D.Double> newPoints){

        setNumberOfCities(newPoints.size());
        nodes.add(new Node(0, 0));
        for (Point2D.Double newPoint : newPoints) {

            nodes.add(new Node((int) (newPoint.x), (int) (newPoint.y)));
        }

        return geneticAlgorithm();
    }
}