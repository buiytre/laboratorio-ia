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

		// Establecer los par�metros del problema
		// Primer criterio, con peticiones sin servir
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setPenalizationTime(2500);

		System.out.println("##########\nEjercicio 1\n##########");
		for (int i = 0; i < operatorsMode.length; ++i) {
			System.out.println("##########\nModo operadores: "
					+ operatorsMode[i] + "\n##########");
			// El conjunto de operadores var�a
			State.setOperatorsMode(i);

			for (int j = 0; j < nTests; ++j) {
				System.out.println("Prueba " + j + ":");
				// El par�metro seed var�a
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (m�todo generador greedy con
				// peticiones sin servir)
				d1 = new Date();
				start = new State().greedyState();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				// Primer criterio (minimizaci�n del servidor con m�s carga)
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generaci�n: "
						+ (d2.getTime() - d1.getTime()));

				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				System.out.println("Tiempo de ejecuci�n: "
						+ (d2.getTime() - d1.getTime()));
				System.out.println("Iteraciones: "
						+ search.getMetrics().getInt("nodesExpanded"));
				// Comparar heur�sticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heur�stica inicial: "
						+ start.getHeuristic1());
				System.out.println("Heur�stica final: " + end.getHeuristic1());
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

		// Establecer los par�metros del problema
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 2\n##########");
		for (int i = 0; i < generatorFunc.length; ++i) {
			System.out.println("Funci�n generadora: " + generatorFunc[i]
					+ "\n##########");
			for (int j = 0; j < nTests; ++j) {
				System.out.println("Prueba " + j + ":");
				// El par�metro seed var�a
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (m�todo generador)
				d1 = new Date();
				if (generatorFunc[i].equals("Random")) {
					start = new State().randomStateFullRequests(i);
				} else if (generatorFunc[i].equals("Greedy")) {
					start = new State().greedyStateFullRequests();
				} else {
					System.out.println("ERROR: FUNCI�N GENERADORA INV�LIDA");
					break;
				}
				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				// Primer criterio (minimizaci�n del servidor con m�s carga)
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generaci�n: "
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
				System.out.println("Tiempo de ejecuci�n: "
						+ (d2.getTime() - d1.getTime()));
				System.out.println("Iteraciones: "
						+ search.getMetrics().getInt("nodesExpanded"));
				// Comparar heur�sticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heur�stica inicial: "
						+ start.getHeuristic1());
				System.out.println("Heur�stica final: " + end.getHeuristic1());
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

		// Par�metros de Simulated Annealing
		int steps[] = {100000, 10000, 1000, 100};
		int stiter[] = {100000, 10000, 1000, 100, 10, 1};
		int k[] = {100000, 10000, 1000, 100, 10};
		double lamb[] = {0.0001, 0.001, 0.01, 0.1, 1};

		//Variables para calcular las medias de heur�stica y tiempo de ejecuci�n
		double climbingHeurAvg = 0;
		double climbingExecTimeAvg = 0;
		
		//Variables para almacenar las medias de heur�stica, desviaci�n m�xima y m�nima para cada media y su media de ejecuci�n
		//En este espacio cabr�a el Doom 3 entero
		double annealingHeurAvg[][][][]= new double[steps.length][stiter.length][k.length][lamb.length];
		double annealingMaxDev[][][][] = new double[steps.length][stiter.length][k.length][lamb.length];
		double annealingMinDev[][][][] = new double[steps.length][stiter.length][k.length][lamb.length];
		long execTime[][][][] = new long[steps.length][stiter.length][k.length][lamb.length];
		
		// Establecer los par�metros del problema
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
			// El par�metro seed var�a
			State.setProblemParameters(nServers, nReplications, nUsers, nRequestsUser, j);

			// Generar el estado inicial (m�todo generador greedy sin peticiones sin servir)
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
		System.out.println("Tiempo de ejecuci�n: " + climbingExecTimeAvg);
		System.out.println("Heur�stica media: " + climbingHeurAvg);
		
		//Simulated Annealing
		System.out.println("Simulated annealing");
		for (int loopSteps = 0; loopSteps < steps.length; ++loopSteps) {
			for (int loopStiter = 0; loopStiter < stiter.length; ++loopStiter) {
				for (int loopK = 0; loopK < k.length; ++loopK) {
					for (int loopLamb = 0; loopLamb < lamb.length; ++loopLamb) {
						//5 bucles anidados. He batido mi r�cord personal
						for (int j = 0; j < nTests; ++j) {
							System.out.println("Prueba " + j + ":");
							// El par�metro seed var�a
							State.setProblemParameters(nServers, nReplications, nUsers, nRequestsUser, j);

							// Generar el estado inicial (m�todo generador
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
							
							//Desviaciones m�ximas y m�nimas
							if (end.getHeuristic1() > annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb])
								annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb] = end.getHeuristic1();
							if (end.getHeuristic1() < annealingMinDev[loopSteps][loopStiter][loopK][loopLamb])
								annealingMinDev[loopSteps][loopStiter][loopK][loopLamb] = end.getHeuristic1();

							//Sumatorio de resultados de heur�stica y tiempos de ejecuci�n para cada caso
							annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb] += end.getHeuristic1();
							execTime[loopSteps][loopStiter][loopK][loopLamb] += d2.getTime() - d1.getTime();
						}
						//C�lculos de media de heur�stica y tiempos de ejecuci�n para cada caso usando el sumatorio
						annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb] /= nTests;
						execTime[loopSteps][loopStiter][loopK][loopLamb] /= nTests;
						System.out.print("steps=" + steps[loopSteps] + " ");
						System.out.print("stiter=" + stiter[loopStiter] + " ");
						System.out.print("k=" + k[loopK] + " ");
						System.out.println("lamb=" + lamb[loopLamb] + " ");
						System.out.println("Media heur�stica= " + annealingHeurAvg[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Desviaci�n m�xima= " + annealingMaxDev[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Desviaci�n m�nima= " + annealingMinDev[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("Tiempo medio de ejecuci�n = " + execTime[loopSteps][loopStiter][loopK][loopLamb]);
						System.out.println("##########");
					}
				}
			}
		}
		for (int i = 0; i < annealingParams.length; ++i) {
					}
	}
}
