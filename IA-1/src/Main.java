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
		// experiment1(1);
		// experiment2(1);
		// experiment3(1);
		// experiment4(1);
		// experiment5(1);
		// experiment6(1);
		// experiment7(1);
		experiment8(1);
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
		// Criterio 1, sin peticiones sin servir
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

		// Parámetros de Simulated Annealing
		int steps[] = { 100000, 10000, 1000, 100 };
		int stiter[] = { 10000, 1000, 100, 10, 1 };
		int k[] = { 10000, 1000, 100, 10 };
		double lamb[] = { 0.0001, 0.001, 0.01, 0.1, 1 };

		/*
		 * int steps[] = {100000}; int stiter[] = {10000}; int k[] = {10000};
		 * double lamb[] = {0.0001};
		 */
		// Variables para calcular las medias de heurística y tiempo de
		// ejecución
		double climbingHeurAvg = 0;
		double climbingExecTimeAvg = 0;

		// Variables para calcular las medias de heurística, desviación máxima y
		// mínima para cada media y su media de ejecución
		double annealingHeurAvg = 0;
		double annealingMaxDev = 0;
		double annealingMinDev = 0;
		long execTime = 0;

		// Establecer los parámetros del problema
		// Primer criterio, sin peticiones sin servir
		State.setHeuristicMode("max");
		State.setOperatorsMode(0); // 0: Swap
		criteria = new StateHeuristicFunction1();

		System.out.println("##########\nEjercicio 3\n##########");
		System.out.println("");

		// Hill Climbing
		System.out.println("Hill Climbing");
		for (int j = 0; j < nTests; ++j) {
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers,
					nRequestsUser, j);

			// Generar el estado inicial (método generador greedy sin peticiones
			// sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();

			// Usamos Hill Climbing
			successorAlgorythm = new StateSuccessorFunctionHill();
			search = new HillClimbingSearch();

			problem = new Problem(start, successorAlgorythm,
					new StateGoalTest(), criteria);

			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			end = (State) search.getGoalState();

			d2 = new Date();
			climbingExecTimeAvg += d2.getTime() - d1.getTime();
			climbingHeurAvg += end.getHeuristic1();
		}
		climbingExecTimeAvg /= nTests;
		climbingHeurAvg /= nTests;
		System.out.println("Tiempo de ejecución: " + climbingExecTimeAvg);
		System.out.println("Heurística media: " + climbingHeurAvg);

		// Simulated Annealing
		System.out.println("Simulated annealing");
		for (int loopSteps = 0; loopSteps < steps.length; ++loopSteps) {
			for (int loopStiter = 0; loopStiter < stiter.length; ++loopStiter) {
				for (int loopK = 0; loopK < k.length; ++loopK) {
					for (int loopLamb = 0; loopLamb < lamb.length; ++loopLamb) {
						annealingHeurAvg = 0;
						annealingMinDev = Long.MAX_VALUE;
						annealingMaxDev = Long.MIN_VALUE;
						execTime = 0;
						// 5 bucles anidados. He batido mi récord personal
						for (int j = 0; j < nTests; ++j) {
							// El parámetro seed varía
							State.setProblemParameters(nServers, nReplications,
									nUsers, nRequestsUser, j);

							// Generar el estado inicial (método generador
							// greedy sin peticiones sin servir)
							d1 = new Date();
							start = new State().greedyStateFullRequests();

							// Usamos Simulated Annealing
							successorAlgorythm = new StateSuccessorFunctionSimulated();
							search = new SimulatedAnnealingSearch(
									steps[loopSteps], stiter[loopStiter],
									k[loopK], lamb[loopLamb]);

							problem = new Problem(start, successorAlgorythm,
									new StateGoalTest(), criteria);

							try {
								agent = new SearchAgent(problem, search);
							} catch (Exception e) {
								e.printStackTrace();
							}
							d2 = new Date();
							end = (State) search.getGoalState();

							// Desviaciones máximas y mínimas
							if (climbingHeurAvg - end.getHeuristic1() > annealingMaxDev)
								annealingMaxDev = climbingHeurAvg
										- end.getHeuristic1();
							if (climbingHeurAvg - end.getHeuristic1() < annealingMinDev)
								annealingMinDev = climbingHeurAvg
										- end.getHeuristic1();

							// Sumatorio de resultados de heurística y tiempos
							// de ejecución para cada caso
							annealingHeurAvg += end.getHeuristic1();
							execTime += d2.getTime() - d1.getTime();
						}
						// Cálculos de media de heurística y tiempos de
						// ejecución para cada caso usando el sumatorio
						annealingHeurAvg /= nTests;
						execTime /= nTests;
						System.out.print("steps=" + steps[loopSteps] + " ");
						System.out.print("stiter=" + stiter[loopStiter] + " ");
						System.out.print("k=" + k[loopK] + " ");
						System.out.println("lamb=" + lamb[loopLamb] + " ");
						System.out.println("Media heurística= "
								+ annealingHeurAvg);
						System.out.println("Desviación máxima= "
								+ annealingMaxDev);
						System.out.println("Desviación mínima= "
								+ annealingMinDev);
						System.out.println("Tiempo medio de ejecución = "
								+ execTime);
						System.out.println("##########");
					}
				}
			}
		}
	}

	private static void experiment4(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;
		long execTime = 0;
		long heurAvg = 0;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;
		
		// Establecer los parámetros del problema
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 4\n##########");
		System.out.println("##########\nIncremento de usuarios");
		nServers = 5;
		nReplications = 2;
		for (nUsers = 100; nUsers <= 500; nUsers += 100) {
			System.out.println("Número de usuarios: " + nUsers);

			heurAvg = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador
				// greedy sin peticiones sin servir)
				d1 = new Date();
				start = new State().greedyStateFullRequests();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();

				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				heurAvg += end.getHeuristic1();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			heurAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo de ejecución: " + execTime);
			System.out.println("Heurística media: " + heurAvg);
			System.out.println("----------");
		}
		System.out.println("##########\nIncremento de servidores");

		nReplications = 5;
		nUsers = 200;
		for (nServers = 50; nServers <= 500; nServers += 50) {
			System.out.println("Número de servidores: " + nServers);

			heurAvg = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador
				// greedy sin peticiones sin servir)
				d1 = new Date();
				start = new State().greedyStateFullRequests();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();

				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				heurAvg += end.getHeuristic1();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			heurAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo de ejecución: " + execTime);
			System.out.println("Heurística media: " + heurAvg);
			System.out.println("----------");
		}
	}

	private static void experiment5(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;
		long execTime = 0;
		long totalTimeAvg = 0;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;

		int penalizations[] = { 5000, 4000, 3000, 2000, 1000, 500, 0 };

		// Establecer los parámetros del problema
		// Primer criterio, sin peticiones sin servir
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 5\n##########");
		System.out.println("Hill Climbing, Heurística 1");
		for (int i = 0; i < nTests; ++i) {
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers,
					nRequestsUser, i);

			// Generar el estado inicial (método generador greedy con
			// peticiones sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();

			// Usamos Hill Climbing
			successorAlgorythm = new StateSuccessorFunctionHill();
			search = new HillClimbingSearch();

			// Primer criterio (minimización del servidor con más carga)
			State.setHeuristicMode("max");
			criteria = new StateHeuristicFunction1();
			problem = new Problem(start, successorAlgorythm,
					new StateGoalTest(), criteria);

			// Ejecutar
			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d2 = new Date();
			// Comparar heurísticas inicial y final
			end = (State) search.getGoalState();

			// Sumatorio de resultados de heurística y tiempos de ejecución
			// para cada caso
			totalTimeAvg += end.getTotalTime();
			execTime += d2.getTime() - d1.getTime();
		}
		// Cálculos de media de heurística y tiempos de ejecución para cada
		// caso usando el sumatorio
		totalTimeAvg /= nTests;
		execTime /= nTests;

		System.out.println("Tiempo medio de ejecución: " + execTime);
		System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
		System.out.println("----------");

		System.out.println("Hill Climbing, Heurística 2 sin soluciones sin servir");
		State.setOperatorsMode(0);
		totalTimeAvg  = 0;
		execTime = 0;
		for (int i = 0; i < nTests; ++i) {
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers,
					nRequestsUser, i);

			// Generar el estado inicial (método generador greedy con
			// peticiones sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();

			// Usamos Hill Climbing
			successorAlgorythm = new StateSuccessorFunctionHill();
			search = new HillClimbingSearch();

			// Primer criterio (minimización del servidor con más carga)
			State.setHeuristicMode("stdev");
			criteria = new StateHeuristicFunction2();
			problem = new Problem(start, successorAlgorythm,
					new StateGoalTest(), criteria);

			// Ejecutar
			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d2 = new Date();
			// Comparar heurísticas inicial y final
			end = (State) search.getGoalState();
			
			// Sumatorio de resultados de heurística y tiempos de ejecución
			// para cada caso
			totalTimeAvg += end.getTotalTime();
			execTime += d2.getTime() - d1.getTime();
		}

		// Cálculos de media de heurística y tiempos de ejecución para cada
		// caso usando el sumatorio
		totalTimeAvg /= nTests;
		execTime /= nTests;

		System.out.println("Tiempo medio de ejecución: " + execTime);
		System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
		System.out.println("----------");

		State.setOperatorsMode(1);
		System.out.println("Hill Climbing, Heurística 2 con soluciones sin servir");
		for (int i = 0; i < penalizations.length; ++i) {
			State.setPenalizationTime(penalizations[i]);
			System.out.println("Penalización: " + penalizations[i]);
			totalTimeAvg  = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
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
				State.setHeuristicMode("stdev");
				criteria = new StateHeuristicFunction2();
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				d2 = new Date();

				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				totalTimeAvg += end.getTotalTime();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			totalTimeAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo medio de ejecución: " + execTime);
			System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
			System.out.println("----------");
		}
	}

	private static void experiment6(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;
		long execTime = 0;
		long totalTimeAvg = 0;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;

		// Parámetros de Simulated Annealing
		//TODO: Generar
		int steps = 0;
		int stiter = 0;
		int k = 0;
		double lamb = 0;

		int penalizations[] = { 5000, 4000, 3000, 2000, 1000, 500, 0 };

		// Establecer los parámetros del problema
		// Primer criterio, sin peticiones sin servir
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 6\n##########");
		System.out.println("Simulated Annealing, Heurística 1");
		for (int i = 0; i < nTests; ++i) {
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers,
					nRequestsUser, i);

			// Generar el estado inicial (método generador greedy con
			// peticiones sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();

			// Usamos Simulated Annealing
			successorAlgorythm = new StateSuccessorFunctionSimulated();
			search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);

			// Primer criterio (minimización del servidor con más carga)
			State.setHeuristicMode("max");
			criteria = new StateHeuristicFunction1();
			problem = new Problem(start, successorAlgorythm,
					new StateGoalTest(), criteria);

			// Ejecutar
			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d2 = new Date();
			// Comparar heurísticas inicial y final
			end = (State) search.getGoalState();

			// Sumatorio de resultados de heurística y tiempos de ejecución
			// para cada caso
			totalTimeAvg += end.getTotalTime();
			execTime += d2.getTime() - d1.getTime();
		}
		// Cálculos de media de heurística y tiempos de ejecución para cada
		// caso usando el sumatorio
		totalTimeAvg /= nTests;
		execTime /= nTests;

		System.out.println("Tiempo medio de ejecución: " + execTime);
		System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
		System.out.println("----------");

		System.out.println("Simulated Annealing, Heurística 2 sin soluciones sin servir");
		State.setOperatorsMode(0);
		totalTimeAvg  = 0;
		execTime = 0;
		for (int i = 0; i < nTests; ++i) {
			// El parámetro seed varía
			State.setProblemParameters(nServers, nReplications, nUsers,
					nRequestsUser, i);

			// Generar el estado inicial (método generador greedy con
			// peticiones sin servir)
			d1 = new Date();
			start = new State().greedyStateFullRequests();

			// Usamos Simulated Annealing
			successorAlgorythm = new StateSuccessorFunctionSimulated();
			search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);

			// Primer criterio (minimización del servidor con más carga)
			State.setHeuristicMode("stdev");
			criteria = new StateHeuristicFunction2();
			problem = new Problem(start, successorAlgorythm,
					new StateGoalTest(), criteria);

			// Ejecutar
			try {
				agent = new SearchAgent(problem, search);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d2 = new Date();
			// Comparar heurísticas inicial y final
			end = (State) search.getGoalState();
			
			// Sumatorio de resultados de heurística y tiempos de ejecución
			// para cada caso
			totalTimeAvg += end.getTotalTime();
			execTime += d2.getTime() - d1.getTime();
		}

		// Cálculos de media de heurística y tiempos de ejecución para cada
		// caso usando el sumatorio
		totalTimeAvg /= nTests;
		execTime /= nTests;

		System.out.println("Tiempo medio de ejecución: " + execTime);
		System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
		System.out.println("----------");

		State.setOperatorsMode(1);
		System.out.println("Simulated Annealing, Heurística 2 con soluciones sin servir");
		for (int i = 0; i < penalizations.length; ++i) {
			State.setPenalizationTime(penalizations[i]);
			System.out.println("Penalización: " + penalizations[i]);
			totalTimeAvg  = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador greedy con
				// peticiones sin servir)
				d1 = new Date();
				start = new State().greedyState();

				// Usamos Simulated Annealing
				successorAlgorythm = new StateSuccessorFunctionSimulated();
				search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);

				// Primer criterio (minimización del servidor con más carga)
				State.setHeuristicMode("stdev");
				criteria = new StateHeuristicFunction2();
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				d2 = new Date();

				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				totalTimeAvg += end.getTotalTime();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			totalTimeAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo medio de ejecución: " + execTime);
			System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
			System.out.println("----------");
		}
	}

	private static void experiment7(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;
		long execTime = 0;
		long totalTimeAvg = 0;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;
		
		// Establecer los parámetros del problema
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setOperatorsMode(0);

		System.out.println("##########\nEjercicio 7\n##########");
		System.out.println("##########\nHeurística 1");
		for (nReplications = 5; nReplications <= 25; nReplications += 5) {
			System.out.println("Número de replicaciones: " + nReplications);

			totalTimeAvg = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador
				// greedy sin peticiones sin servir)
				d1 = new Date();
				start = new State().greedyStateFullRequests();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				
				// Primer criterio (minimización del servidor con más carga)
				State.setHeuristicMode("max");
				criteria = new StateHeuristicFunction1();
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				totalTimeAvg += end.getHeuristic1();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			totalTimeAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo de ejecución: " + execTime);
			System.out.println("Heurística media: " + totalTimeAvg);
			System.out.println("----------");
		}
		System.out.println("##########\nIncremento de servidores");

		System.out.println("##########\nHeurística 2");
		for (nReplications = 5; nReplications <= 25; nReplications += 5) {
			System.out.println("Número de replicaciones: " + nReplications);

			totalTimeAvg = 0;
			execTime = 0;
			for (int j = 0; j < nTests; ++j) {
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador
				// greedy sin peticiones sin servir)
				d1 = new Date();
				start = new State().greedyStateFullRequests();

				// Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();

				// Segundo criterio (minimización de media y desviación estándar)
				State.setHeuristicMode("stdev");
				criteria = new StateHeuristicFunction2();
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				totalTimeAvg += end.getHeuristic1();
				execTime += d2.getTime() - d1.getTime();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			totalTimeAvg /= nTests;
			execTime /= nTests;

			System.out.println("Tiempo de ejecución: " + execTime);
			System.out.println("Heurística media: " + totalTimeAvg);
			System.out.println("----------");
		}
		System.out.println("##########\nIncremento de servidores");

	}


	private static void experiment8(int nTests) {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
		HeuristicFunction criteria = null;
		SearchAgent agent = null;

		Date d1 = null;
		Date d2 = null;
		long execTime = 0;
		long totalTimeAvg = 0;
		int unservedAvg = 0;

		int nServers = 50;
		int nReplications = 5;
		int nUsers = 200;
		int nRequestsUser = 5;
		int seed = 10;

		int penalizations[] = { 5000, 4000, 3000, 2000, 1000, 500, 0 };

		// Establecer los parámetros del problema
		// Segundo criterio, con peticiones sin servir

		State.setOperatorsMode(1);
		
		System.out.println("##########\nEjercicio 8\n##########");
		for (int i = 0; i < penalizations.length; ++i) {
			State.setPenalizationTime(penalizations[i]);
			System.out.println("Penalización: " + penalizations[i]);
			totalTimeAvg  = 0;
			execTime = 0;
			unservedAvg = 0;
			for (int j = 0; j < nTests; ++j) {
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
				State.setHeuristicMode("stdev");
				criteria = new StateHeuristicFunction2();
				problem = new Problem(start, successorAlgorythm,
						new StateGoalTest(), criteria);

				d2 = new Date();

				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();

				// Sumatorio de resultados de heurística y tiempos de ejecución
				// para cada caso
				totalTimeAvg += end.getTotalTime();
				execTime += d2.getTime() - d1.getTime();
				unservedAvg += end.getUnservedRequestsCount();
			}
			// Cálculos de media de heurística y tiempos de ejecución para cada
			// caso usando el sumatorio
			totalTimeAvg /= nTests;
			execTime /= nTests;
			unservedAvg /= nTests;

			System.out.println("Tiempo medio de ejecución: " + execTime);
			System.out.println("Tiempo total de carga medio: " + totalTimeAvg);
			System.out.println("Cantidad media de peticiones sin servir: " + unservedAvg);

			System.out.println("----------");
		}
	}
	
	
}
