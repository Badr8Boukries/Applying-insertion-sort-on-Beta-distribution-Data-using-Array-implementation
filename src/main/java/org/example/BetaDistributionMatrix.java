package org.example;

import org.apache.commons.math3.distribution.BetaDistribution;



public class BetaDistributionMatrix {

    // Fonction pour generer un tableau de nombres aleatoires à partir de la distribution beta
    public static double[] betaDataGenerate(double mean, double variance, int dataSize) {
        if (dataSize <= 0) {
            throw new IllegalArgumentException("La taille des données doit être supérieure à zéro.");
        }
        double[] data = new double[dataSize];
        BetaDistribution beta = new BetaDistribution((mean * (mean * (1 - mean) / variance - 1)), ((1 - mean) * (mean * (1 - mean) / variance - 1)));
        for (int i = 0; i < dataSize; i++) {
            data[i] = beta.sample();
        }
        return data;
    }

    // Fonction pour trier les donnees avec l'algorithme de tri par insertion
    public static void insertionSort(double[] arr) {
        // Vérifier si le tableau est vide ou contient un seul element
        if (arr == null || arr.length <= 1) {
            return;
        }

        for (int i = 0; i < arr.length; ++i) {
            int j;
            for (j = i ; j > 0 ; j--) {
                if(arr[j]<arr[j-1]) {

                  double tmp=arr[j];
                  arr[j]=arr[j-1];
                  arr[j-1]=tmp;

                }
                else break;
            }
        }
    }


    // Fonction pour chauffer le JVM
    public static void warmUpJVM() {
        System.out.println("Chauffage du JVM en cours...");
        // Exécuter quelques rounds de tri initiaux pour stabiliser les performances du JVM
        for (int i = 0; i < 5; i++) {
            double[] randomNumbers = betaDataGenerate(0.5, 0.1, 10); // Générer 10 nombres aléatoires
            insertionSort(randomNumbers); // Trier les nombres aleatoires avec le tri par insertion
        }
        System.out.println("Chauffage du JVM terminé.");
    }

    // Fonction pour creer et remplir les matrices de distribution beta
    public static double[][][] createBetaMatrices(double[] means, double[] variances, int initialDataSize) {
        if (means == null || variances == null || means.length == 0 || variances.length == 0) {
            throw new IllegalArgumentException("Les tableaux de moyennes et de variances ne peuvent pas être vides ou nuls.");
        }
        int numMeans = means.length;
        int numVariances = variances.length;

        double[][][] matrices = new double[numMeans][numVariances][];

        int dataSize = initialDataSize;

        for (int i = 0; i < numMeans; i++) {
            for (int j = 0; j < numVariances; j++) {
                matrices[i][j] = betaDataGenerate(means[i], variances[j], dataSize);
            }
            dataSize *= 2;  // Multiplier la taille des données par 2 après chaque itération de la boucle externe
        }

        return matrices;
    }

    // Fonction pour afficher les matrices  generees
    public static void printMatrix(double[][][] matrix, double[] means, double[] variances) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                double mean = means[j];
                double variance = variances[i];
                System.out.println("Cell [" + i + "][" + j + "], Mean: " + mean + ", Variance: " + variance + ":[");
                for (int k = 0; k < matrix[i][j].length; k++) {
                    System.out.print(matrix[i][j][k]);
                    if (k < matrix[i][j].length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
        }
    }
    public static void measureSortingTime(double[][][] betaMatrices) {

        for (int i = 0; i < betaMatrices.length; i++) {

            for (int j = 0; j < betaMatrices[i].length; j++) {

                double[] cell = betaMatrices[i][j];
                long startTime = System.nanoTime(); // Temps de debut du tri
                insertionSort(cell);
                long endTime = System.nanoTime(); // Temps de fin du tri
                long duration = (endTime - startTime) ;
                System.out.println("Temps d'exécution du tri de cell [" + i + "][" + j + "] : " + duration + " nanosecond");

                // Comparaison avec les complexite theoriques
                compareWithTheoreticalComplexity(duration, cell.length);
            }
        }
    }






    // Méthode pour comparer les temps d execution avec les complexites theorique pour une cellule
    public static void compareWithTheoreticalComplexity(long duration, int dataSize) {
        long theoreticalComplexityPire = dataSize * dataSize; // O(n^2) pour le pire cas

        if (duration > theoreticalComplexityPire) {
            System.out.println("La durée d'exécution dépasse la complexité théorique pour le pire et moyen cas.");

        } else if (duration < theoreticalComplexityPire) {
            System.out.println("La durée d'exécution est inférieure à la complexité théorique pour le pire et moyen cas.");

        } else {
            System.out.println("La durée d'exécution correspond à la complexité théorique pour le pire et moyen  cas.");
        }

        long theoreticalComplexityBest = dataSize; // O(n) pour le meillure cas
        if (duration > theoreticalComplexityBest) {
            System.out.println("La durée d'exécution dépasse la complexité théorique meilleure cas.");
        } else if (duration < theoreticalComplexityBest) {

            System.out.println("La durée d'exécution est inférieure à la complexité théorique pour le meilleure cas.");
        } else {

            System.out.println("La durée d'exécution correspond à la complexité théorique pour le meilleure cas.");
        }
    }

    public static void main(String[] args) {
        // worm le JVM
        warmUpJVM();

        // Definir les moyennes, les variances et les tailles de donnees initiale
        double[] means = {0.2, 0.5, 0.8};
        double[] variances = {0.1, 0.2, 0.3};
        int initialDataSize = 10000; // Taille initiale des donnees pour chaque cellule

        // Creer et remplir les matrices de distribution beta
        double[][][] betaMatrices = createBetaMatrices(means, variances, initialDataSize);


        printMatrix(betaMatrices, means, variances);
        measureSortingTime(betaMatrices);


    }
}



