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
		
		// Establecer los parámetros del problema
		// Primer criterio, sin peticiones sin servir
		State.setHeuristicMode("max");
		State.setOperatorsMode(0); // 0: Swap
		criteria = new StateHeuristicFunction1();
		System.out.println("##########\nEjercicio 3\n##########");
		for (int loopSteps = 0; loopSteps < steps.length; ++loopSteps) {
			for (int loopStiter = 0; loopStiter < stiter.length; ++loopStiter) {
				for (int loopK = 0; loopK < k.length; ++loopK) {
					//5 bucles anidados. He batido mi récord personal
					for (int loopLamb = 0; loopLamb < lamb.length; ++loopLamb) {
						System.out.println("##########");
						System.out.print("steps=" + steps[loopSteps] + " ");
						System.out.print("stiter=" + stiter[loopStiter] + " ");
						System.out.print("k=" + k[loopK] + " ");
						System.out.print("lamb=" + lamb[loopLamb]);
						System.out.println("\n##########");
						
						for (int j = 0; j < nTests; ++j) {
							System.out.println("Prueba " + j + ":");
							// El parámetro seed varía
							State.setProblemParameters(nServers, nReplications, nUsers, nRequestsUser, j);

							// Generar el estado inicial (método generador
							// greedy con peticiones sin servir)
							d1 = new Date();
							start = new State().greedyStateFullRequests();
							
							// Usamos Simulated Annealing
							successorAlgorythm = new StateSuccessorFunctionSimulated();
							search = new SimulatedAnnealingSearch(steps[loopSteps], 
																	stiter[loopStiter], 
																	k[loopK], 
																	lamb[loopLamb]);
							// Primer criterio (minimización del servidor con
							// más carga)
							problem = new Problem(start, successorAlgorythm, new StateGoalTest(), criteria);
							d2 = new Date();
							System.out.println("Tiempo de generación: " + (d2.getTime() - d1.getTime()));

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
							System.out.println("Iteraciones: " + search.getMetrics().getInt("nodesExpanded"));
							// Comparar heurísticas inicial y final
							end = (State) search.getGoalState();
							System.out.println("Heurística inicial: "
									+ start.getHeuristic1());
							System.out.println("Heurística final: " + end.getHeuristic1());
						}

					}
				}
			}
		}
		for (int i = 0; i < annealingParams.length; ++i) {
					}
	}
}
