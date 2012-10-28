import sun.management.Agent;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Metrics;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.util.Date;

@SuppressWarnings("unused")
public class Main {
	public static void main(String args[]) {
		experiment1(1);
		// experiment2();
	}

	private static void experiment1(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;
		String[] operatorsMode = { "Modo 0", "Modo 1", "Modo 2", "Modo 3" };

		// Establecer los parámetros del problema
		// Primer criterio, con peticiones sin servir
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setPenalizationTime(2500);

		System.out.println("##########\nEjercicio 1\n##########");
		for (int i = 0; i < operatorsMode.length; ++i) {
			System.out.println("##########\nModo operadores: "
					+ operatorsMode[i] + "\n##########");
			// El conjunto de operadores varía
			State.setOperatorsMode(i);

			for (int j = 0; j < nTests; ++j) {
				System.out.println("Prueba " + j + ":");
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador greedy con
				// peticiones sin servir)
				d1 = new Date();
				start = new State().greedyState();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				// Primer criterio (minimización del servidor con más carga)
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generación: "
						+ (d2.getTime() - d1.getTime()));

				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				System.out.println("Tiempo de ejecución: "
						+ (d2.getTime() - d1.getTime()));
				System.out.println("Iteraciones: "
						+ search.getMetrics().getInt("nodesExpanded"));
				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heurística inicial: "
						+ start.getHeuristic1());
				System.out.println("Heurística final: " + end.getHeuristic1());
			}
		}
	}

	private static void experiment2(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;
		String[] generatorFunc = { "Random", "Greedy" };

		// Establecer los parámetros del problema
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 2\n##########");
		for (int i = 0; i < generatorFunc.length; ++i) {
			System.out.println("Función generadora: " + generatorFunc[i]
					+ "\n##########");
			for (int j = 0; j < nTests; ++j) {
				System.out.println("Prueba " + j + ":");
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador)
				d1 = new Date();
				if (generatorFunc[i].equals("Random")) {
					start = new State().randomStateFullRequests(i);
				} else if (generatorFunc[i].equals("Greedy")) {
					start = new State().greedyStateFullRequests();
				} else {
					System.out.println("ERROR: FUNCIÓN GENERADORA INVÁLIDA");
					break;
				}
				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				// Primer criterio (minimización del servidor con más carga)
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generación: "
						+ (d2.getTime() - d1.getTime()));

				System.out.println("Iteraciones: "
						+ agent.getInstrumentation().getProperty("Iterations"));
				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				System.out.println("Tiempo de ejecución: "
						+ (d2.getTime() - d1.getTime()));
				System.out.println("Iteraciones: "
						+ search.getMetrics().getInt("nodesExpanded"));
				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heurística inicial: "
						+ start.getHeuristic1());
				System.out.println("Heurística final: " + end.getHeuristic1());
			}
		}
	}

	private static void experiment3(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;
		
		Date d1 = null;
		Date d2 = null;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;
		String[] annealingParams = { "", "", "", "" };

		// Parámetros de Simulated Annealing
		int steps[] = {100000, 10000, 1000, 100};
		int stiter[] = {100000, 10000, 1000, 100, 10, 1};
		int k[] = {100000, 10000, 1000, 100, 10};
		double lamb[] = {0.0001, 0.001, 0.01, 0.1, 1};

		//Variables para calcular las medias de heurística y tiempo de ejecución
		double climbingHeurAvg = 0;
		double climbingExecTimeAvg = 0;
		
		//Variables para almacenar las medias de heurística, desviación máxima y mínima para cada media y su media de ejecución
		//En este espacio cabría el Doom 3 entero
		double annealingHeurAvg[][][][]= new double[steps.length][stiter.length][k.length][lamb.length];
		double annealingMaxDev[][][][] = new double[steps.length][stiter.length][k.length][lamb.length];
		double annealingMinDev[][][][] = new double[steps.length][stiter.length][k.length][lamb.length];
		long execTime[][][][] = new long[steps.length][stiter.length][k.length][lamb.length];
		
		// Establecer los parámetros del problema
		// Primer criterio, sin peticiones sin servir
		State.setHeuristicMode("max");
		State.setOperatorsMode(0); // 0: Swap
		criteria = new StateHeuristicFunction1();
		
		
		System.out.println("##########\nEjercicio 3\n##########");
		System.out.println("");

		//Hill Climbing
		System.out.println("Hill Climbing");
		for (int j = 0; j < nTests; ++j) {
			System.out.println("Prueba " + j + ":");
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers, nRequestsUser, j);

			// Generar el estado inicial (método generador greedy sin peticiones sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();
			
			// Usamos Hill Climbing
			successorAlgorythm = new StateSuccessorFunctionHill();
			search = new HillClimbingSearch();

			problem = new Problem(start, successorAlgorythm, new StateGoalTest(), criteria);

			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d2 = new Date();
			climbingExecTimeAvg += d2.getTime() - d1.getTime();
			climbingHeurAvg += end.getHeuristic1();
		}
		climbingExecTimeAvg /= nTests;
		climbingHeurAvg /= nTests;
		System.out.println("Tiempo de ejecución: " + climbingExecTimeAvg);
		System.out.println("Heurística media: " + climbingHeurAvg);
		
		//Simulated Annealing
		System.out.println("Simulated annealing");
		for (int loopSteps = 0; loopSteps < steps.length; ++loopSteps) {
			for (int loopStiter = 0; loopStiter < stiter.length; ++loopStiter) {
				for (int loopK = 0; loopK < k.length; ++loopK) {
					for (int loopLamb = 0; loopLamb < lamb.length; ++loopLamb) {
						//5 bucles anidados. He batido mi récord personal
						for (int j = 0; j < nTests; ++j) {
							System.out.println("Prueba " + j + ":");
							// El parámetro seed varía
							State.setProblemParameters(nServers, nReplications, nUsers, nRequestsUser, j);

							// Generar el estado inicial (método generador
							// greedy sin peticiones sin servir)
							d1 = new Date();
							start = new State().greedyStateFullRequests();
							
							// Usamos Simulated Annealing
							successorAlgorythm = new StateSuccessorFunctionSimulated();
							search = new SimulatedAnnealingSearch(steps[loopSteps], 
																	stiter[loopStiter], 
																	k[loopK], 
																	lamb[loopLamb]);

							problem = new Problem(start, successorAlgorythm, new StateGoalTest(), criteria);

							try {
								agent = new SearchAgent(problem, search);
							} catch (Exception e) {
								e.printStackTrace();
							}
							d2 = new Date();
							
							//Desviaciones máximas y mínimas
							if (end.getHeuristic1() > annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb])
								annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb] = end.getHeuristic1();
							if (end.getHeuristic1() < annealingMinDev[loopSteps][loopStiter][loopK][loopLamb])
								annealingMinDev[loopSteps][loopStiter][loopK][loopLamb] = end.getHeuristic1();

							//Sumatorio de resultados de heurística y tiempos de ejecución para cada caso
							annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb] += end.getHeuristic1();
							execTime[loopSteps][loopStiter][loopK][loopLamb] += d2.getTime() - d1.getTime();
						}
						//Cálculos de media de heurística y tiempos de ejecución para cada caso usando el sumatorio
						annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb] /= nTests;
						execTime[loopSteps][loopStiter][loopK][loopLamb] /= nTests;
						System.out.print("steps=" + steps[loopSteps] + " ");
						System.out.print("stiter=" + stiter[loopStiter] + " ");
						System.out.print("k=" + k[loopK] + " ");
						System.out.println("lamb=" + lamb[loopLamb] + " ");
						System.out.println("Media heurística= " + annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Desviación máxima= " + annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Desviación mínima= " + annealingMinDev[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Tiempo medio de ejecución = " + execTime[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("##########");
					}
				}
			}
		}
		for (int i = 0; i < annealingParams.length; ++i) {
					}
	}
}
