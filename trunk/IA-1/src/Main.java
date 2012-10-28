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
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

@SuppressWarnings("unused")
public class Main {
	static WritableWorkbook workbook = null;
	static Label label = null;
	static Number number = null;

	public static void main(String args[]) {
		try {
			workbook = Workbook.createWorkbook(new File("output.xls"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// experiment1(3);
		// experiment2(1);
		// experiment3(1);
		experiment4(1);
		// experiment5(1);
		// experiment6(1);
		// experiment7(1);
		// experiment8(1);
		// experiment9(1);
		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void experiment1(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 1", 0);
			label = new Label(0, 0, "Tipo");
			sheet.addCell(label);
			label = new Label(1, 0, "Mejora Heuristica media");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo de ejecucion medio");
			sheet.addCell(label);
			label = new Label(3, 0, "Cantidad de iteraciones media");
			sheet.addCell(label);
			State start = null;
			State end = null;
			Search search = null;
			Problem problem = null;
			SuccessorFunction successorAlgorythm = null;
			HeuristicFunction criteria = null;
			SearchAgent agent = null;

			Date d1 = null;
			Date d2 = null;
			double execTime = 0;
			double heurAvg = 0;
			double iterAvg = 0;

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
			State.setPenalizationTime(5000);

			System.out.println("##########\nEjercicio 1\n##########");
			for (int i = 0; i < operatorsMode.length; ++i) {
				System.out.println("##########\nModo operadores: "
						+ operatorsMode[i] + "\n##########");
				number = new Number(0, i + 1, i);
				sheet.addCell(number);
				// El conjunto de operadores varía
				State.setOperatorsMode(i);

				execTime = 0;
				heurAvg = 0;
				iterAvg = 0;

				for (int j = 0; j < nTests; ++j) {
					// El parámetro seed varía
					State.setProblemParameters(nServers, nReplications, nUsers,
							nRequestsUser, j);

					// Generar el estado inicial (método generador greedy con
					// peticiones sin servir)
					d1 = new Date();
					start = new State().randomState(j);

					// Usamos Hill Climbing
					successorAlgorythm = new StateSuccessorFunctionHill();
					search = new HillClimbingSearch();
					// Primer criterio (minimización del servidor con más carga)
					problem = new Problem(start, successorAlgorythm,
							new StateGoalTest(), criteria);

					// Ejecutar
					try {
						agent = new SearchAgent(problem, search);
					} catch (Exception e) {
						e.printStackTrace();
					}
					d2 = new Date();
					end = (State) search.getGoalState();

					execTime += d2.getTime() - d1.getTime();
					iterAvg += search.getMetrics().getInt("nodesExpanded");
					heurAvg += start.getHeuristic1() - end.getHeuristic1();
				}
				execTime /= nTests;
				iterAvg /= nTests;
				heurAvg /= nTests;
				System.out.println("Tiempo de ejecucion Medio = " + execTime);
				number = new Number(2, i + 1, execTime);
				sheet.addCell(number);
				System.out
						.println("Cantidad de iteraciones Media = " + iterAvg);
				number = new Number(3, i + 1, iterAvg);
				sheet.addCell(number);
				System.out.println("Mejora Heuristica Media = " + heurAvg);
				number = new Number(1, i + 1, heurAvg);
				sheet.addCell(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment2(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 2", 1);
			label = new Label(0, 0, "Estrategia Generacion");
			sheet.addCell(label);
			label = new Label(1, 0, "Num Pasos");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo de ejecucion");
			sheet.addCell(label);
			label = new Label(3, 0, "Tiempo de generacion");
			sheet.addCell(label);
			label = new Label(4, 0, "Heuristica final");
			sheet.addCell(label);

			State start = null;
			State end = null;
			Search search = null;
			Problem problem = null;
			SuccessorFunction successorAlgorythm = null;
			HeuristicFunction criteria = null;
			SearchAgent agent = null;

			Date d1 = null;
			Date d2 = null;
			double creatTime = 0;
			double execTime = 0;
			double heurAvg = 0;
			double iterAvg = 0;

			int nServers = 50;
			int nReplications = 5;
			int nUsers = 200;
			int nRequestsUser = 5;
			int seed = 10;
			String[] generatorFunc = { "Random", "Greedy", "Ordered" };

			// Establecer los parámetros del problema
			// Criterio 1, sin peticiones sin servir
			State.setHeuristicMode("max");
			criteria = new StateHeuristicFunction1();
			State.setOperatorsMode(0);

			System.out.println("##########\nEjercicio 2\n##########");
			for (int i = 0; i < generatorFunc.length; ++i) {
				System.out.println("Función generadora: " + generatorFunc[i]
						+ "\n##########");
				label = new Label(0, i + 1, generatorFunc[i]);
				sheet.addCell(label);
				creatTime = 0;
				execTime = 0;
				heurAvg = 0;
				iterAvg = 0;

				for (int j = 0; j < nTests; ++j) {
					// El parámetro seed varía
					State.setProblemParameters(nServers, nReplications, nUsers,
							nRequestsUser, j);

					// Generar el estado inicial (método generador)
					d1 = new Date();
					if (generatorFunc[i].equals("Random")) {
						start = new State().randomStateFullRequests(i);
					} else if (generatorFunc[i].equals("Greedy")) {
						start = new State().greedyStateFullRequests();
					} else if (generatorFunc[i].equals("Ordered")) {
						start = new State().orderedState();
					} else {
						System.out
								.println("ERROR: FUNCIÓN GENERADORA INVÁLIDA");
						break;
					}
					// Usamos Hill Climbing
					successorAlgorythm = new StateSuccessorFunctionHill();
					search = new HillClimbingSearch();
					// Primer criterio (minimización del servidor con más carga)
					problem = new Problem(start, successorAlgorythm,
							new StateGoalTest(), criteria);
					d2 = new Date();
					creatTime += d2.getTime() - d1.getTime();

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
					execTime += d2.getTime() - d1.getTime();
					iterAvg += search.getMetrics().getInt("nodesExpanded");
					heurAvg += end.getHeuristic1();
				}
				creatTime /= nTests;
				execTime /= nTests;
				iterAvg /= nTests;
				heurAvg /= nTests;
				System.out.println("Tiempo de genearacion: " + creatTime);
				number = new Number(3, i + 1, creatTime);
				sheet.addCell(number);
				System.out.println("Tiempo de ejecucion: " + execTime);
				number = new Number(2, i + 1, execTime);
				sheet.addCell(number);
				System.out.println("iteraciones: " + iterAvg);
				number = new Number(1, i + 1, iterAvg);
				sheet.addCell(number);
				System.out.println("Heuristica final: " + heurAvg);
				number = new Number(4, i + 1, heurAvg);
				sheet.addCell(number);

			}
		} catch (Exception e) {

		}
	}

	private static void experiment3(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 3", 2);
			label = new Label(0, 0, "Steps");
			sheet.addCell(label);
			label = new Label(1, 0, "Stiter");
			sheet.addCell(label);
			label = new Label(2, 0, "K");
			sheet.addCell(label);
			label = new Label(3, 0, "Lambda");
			sheet.addCell(label);
			label = new Label(4, 0, "Media heuristica");
			sheet.addCell(label);
			label = new Label(5, 0, "Desviacion Maxima");
			sheet.addCell(label);
			label = new Label(6, 0, "Desviacion Minima");
			sheet.addCell(label);
			label = new Label(7, 0, "Tiempo medio de ejecucion");
			sheet.addCell(label);
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
			 * int steps[] = {100000}; int stiter[] = {10000}; int k[] =
			 * {10000}; double lamb[] = {0.0001};
			 */
			// Variables para calcular las medias de heurística y tiempo de
			// ejecución
			double climbingHeurAvg = 0;
			double climbingExecTimeAvg = 0;

			// Variables para calcular las medias de heurística, desviación
			// máxima y
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

				// Generar el estado inicial (método generador greedy sin
				// peticiones
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
								State.setProblemParameters(nServers,
										nReplications, nUsers, nRequestsUser, j);

								// Generar el estado inicial (método generador
								// greedy sin peticiones sin servir)
								d1 = new Date();
								start = new State().greedyStateFullRequests();

								// Usamos Simulated Annealing
								successorAlgorythm = new StateSuccessorFunctionSimulated();
								search = new SimulatedAnnealingSearch(
										steps[loopSteps], stiter[loopStiter],
										k[loopK], lamb[loopLamb]);

								problem = new Problem(start,
										successorAlgorythm,
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

								// Sumatorio de resultados de heurística y
								// tiempos
								// de ejecución para cada caso
								annealingHeurAvg += end.getHeuristic1();
								execTime += d2.getTime() - d1.getTime();
							}
							// Cálculos de media de heurística y tiempos de
							// ejecución para cada caso usando el sumatorio
							annealingHeurAvg /= nTests;
							execTime /= nTests;
							System.out.print("steps=" + steps[loopSteps] + " ");
							System.out.print("stiter=" + stiter[loopStiter]
									+ " ");
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
							int indice = (loopLamb + 1)
									+ lamb.length
									* (loopK + k.length
											* (loopStiter + loopSteps
													* stiter.length));
							number = new Number(0, indice, steps[loopSteps]);
							sheet.addCell(number);
							number = new Number(1, indice, stiter[loopStiter]);
							sheet.addCell(number);
							number = new Number(2, indice, k[loopK]);
							sheet.addCell(number);
							number = new Number(3, indice, lamb[loopLamb]);
							sheet.addCell(number);
							number = new Number(4, indice, annealingHeurAvg);
							sheet.addCell(number);
							number = new Number(5, indice, annealingMaxDev);
							sheet.addCell(number);
							number = new Number(6, indice, annealingMinDev);
							sheet.addCell(number);
							number = new Number(7, indice, execTime);
							sheet.addCell(number);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment4(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 4.1", 3);
			label = new Label(0, 0, "Num Usuarios");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(2, 0, "Heuristica Media");
			sheet.addCell(label);
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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					heurAvg += end.getHeuristic1();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				heurAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo de ejecución: " + execTime);
				System.out.println("Heurística media: " + heurAvg);
				System.out.println("----------");
				number = new Number(0, nUsers / 100 + 1, nUsers);
				sheet.addCell(number);
				number = new Number(1, nUsers / 100 + 1, execTime);
				sheet.addCell(number);
				number = new Number(2, nUsers / 100 + 1, heurAvg);
				sheet.addCell(number);

			}
			sheet = workbook.createSheet("Experimento 4.2", 4);
			label = new Label(0, 0, "Num Servidores");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(2, 0, "Heuristica Media");
			sheet.addCell(label);

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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					heurAvg += end.getHeuristic1();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				heurAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo de ejecución: " + execTime);
				System.out.println("Heurística media: " + heurAvg);
				System.out.println("----------");
				number = new Number(0, nServers / 50 + 1, nServers);
				sheet.addCell(number);
				number = new Number(1, nServers / 50 + 1, execTime);
				sheet.addCell(number);
				number = new Number(2, nServers / 50 + 1, heurAvg);
				sheet.addCell(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment5(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet(
					"Experimento 5.1 Heuristica1", 5);
			label = new Label(0, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);

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
			number = new Number(1, 1, execTime);
			sheet.addCell(number);
			number = new Number(0, 1, totalTimeAvg);
			sheet.addCell(number);
			System.out
					.println("Hill Climbing, Heurística 2 sin soluciones sin servir");
			State.setOperatorsMode(0);
			totalTimeAvg = 0;
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
			sheet = workbook.createSheet("Experimento 5.2 Heuristica2 sin sol",
					6);
			label = new Label(0, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			number = new Number(1, 1, execTime);
			sheet.addCell(number);
			number = new Number(0, 1, totalTimeAvg);
			sheet.addCell(number);
			State.setOperatorsMode(1);
			sheet = workbook.createSheet("Experimento 5.3 Heuristica2 con pen",
					7);
			label = new Label(1, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(0, 0, "Tiempo penalizacion");
			sheet.addCell(label);
			System.out
					.println("Hill Climbing, Heurística 2 con soluciones sin servir");
			for (int i = 0; i < penalizations.length; ++i) {
				State.setPenalizationTime(penalizations[i]);
				System.out.println("Penalización: " + penalizations[i]);
				totalTimeAvg = 0;
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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					totalTimeAvg += end.getTotalTime();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				totalTimeAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo medio de ejecución: " + execTime);
				System.out.println("Tiempo total de carga medio: "
						+ totalTimeAvg);
				System.out.println("----------");
				number = new Number(2, i + 1, execTime);
				sheet.addCell(number);
				number = new Number(1, i + 1, totalTimeAvg);
				sheet.addCell(number);
				number = new Number(0, i + 1, penalizations[i]);
				sheet.addCell(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment6(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet(
					"Experimento 6.1 Heuristica1", 8);
			label = new Label(0, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
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
			// TODO: Generar
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
			number = new Number(1, 1, execTime);
			sheet.addCell(number);
			number = new Number(0, 1, totalTimeAvg);
			sheet.addCell(number);
			System.out
					.println("Simulated Annealing, Heurística 2 sin soluciones sin servir");
			State.setOperatorsMode(0);
			totalTimeAvg = 0;
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
			sheet = workbook.createSheet("Experimento 6.2 Heuristica2 sin sol",
					9);
			label = new Label(0, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			number = new Number(1, 1, execTime);
			sheet.addCell(number);
			number = new Number(0, 1, totalTimeAvg);
			sheet.addCell(number);
			State.setOperatorsMode(1);
			System.out
					.println("Simulated Annealing, Heurística 2 con soluciones sin servir");
			sheet = workbook.createSheet("Experimento 6.3 Heuristica2 con pen",
					10);
			label = new Label(1, 0, "Tiempo Carga");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(0, 0, "Tiempo penalizacion");
			sheet.addCell(label);
			for (int i = 0; i < penalizations.length; ++i) {
				State.setPenalizationTime(penalizations[i]);
				System.out.println("Penalización: " + penalizations[i]);
				totalTimeAvg = 0;
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
					search = new SimulatedAnnealingSearch(steps, stiter, k,
							lamb);

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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					totalTimeAvg += end.getTotalTime();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				totalTimeAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo medio de ejecución: " + execTime);
				System.out.println("Tiempo total de carga medio: "
						+ totalTimeAvg);
				System.out.println("----------");
				number = new Number(2, i + 1, execTime);
				sheet.addCell(number);
				number = new Number(1, i + 1, totalTimeAvg);
				sheet.addCell(number);
				number = new Number(0, i + 1, penalizations[i]);
				sheet.addCell(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment7(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 7.1", 11);
			label = new Label(0, 0, "Num Replicaciones");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo carga");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo ejecucion");
			sheet.addCell(label);
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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					totalTimeAvg += end.getHeuristic1();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				totalTimeAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo de ejecución: " + execTime);
				System.out.println("Heurística media: " + totalTimeAvg);
				System.out.println("----------");
				number = new Number(0, nReplications / 5 + 1, nReplications);
				sheet.addCell(number);
				number = new Number(1, nReplications / 5 + 1, totalTimeAvg);
				sheet.addCell(number);
				number = new Number(2, nReplications / 5 + 1, execTime);
				sheet.addCell(number);
			}
			System.out.println("##########\nIncremento de servidores");
			sheet = workbook.createSheet("Experimento 7.2", 12);
			label = new Label(0, 0, "Num Replicaciones");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo carga");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo ejecucion");
			sheet.addCell(label);
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

					// Segundo criterio (minimización de media y desviación
					// estándar)
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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					totalTimeAvg += end.getHeuristic1();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				totalTimeAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo de ejecución: " + execTime);
				System.out.println("Heurística media: " + totalTimeAvg);
				System.out.println("----------");
				number = new Number(0, nReplications / 5 + 1, nReplications);
				sheet.addCell(number);
				number = new Number(1, nReplications / 5 + 1, totalTimeAvg);
				sheet.addCell(number);
				number = new Number(2, nReplications / 5 + 1, execTime);
				sheet.addCell(number);
			}
			System.out.println("##########\nIncremento de servidores");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment8(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 8", 13);
			label = new Label(0, 0, "Tiempo penalizacion");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo carga");
			sheet.addCell(label);
			label = new Label(2, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(3, 0, "Peticiones no servidas");
			sheet.addCell(label);
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
				totalTimeAvg = 0;
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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					totalTimeAvg += end.getTotalTime();
					execTime += d2.getTime() - d1.getTime();
					unservedAvg += end.getUnservedRequestsCount();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				totalTimeAvg /= nTests;
				execTime /= nTests;
				unservedAvg /= nTests;

				System.out.println("Tiempo medio de ejecución: " + execTime);
				System.out.println("Tiempo total de carga medio: "
						+ totalTimeAvg);
				System.out.println("Cantidad media de peticiones sin servir: "
						+ unservedAvg);

				System.out.println("----------");
				number = new Number(0, i + 1, penalizations[i]);
				sheet.addCell(number);
				number = new Number(1, i + 1, totalTimeAvg);
				sheet.addCell(number);
				number = new Number(2, i + 1, execTime);
				sheet.addCell(number);
				number = new Number(3, i + 1, unservedAvg);
				sheet.addCell(number);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void experiment9(int nTests) {
		try {
			WritableSheet sheet = workbook.createSheet("Experimento 9", 14);
			label = new Label(0, 0, "Num Peticiones");
			sheet.addCell(label);
			label = new Label(1, 0, "Tiempo ejecucion");
			sheet.addCell(label);
			label = new Label(2, 0, "Heuristica Media");
			sheet.addCell(label);
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

			int nServers = 5;
			int nReplications = 5;
			int nUsers = 200;
			int nRequestsUser = 5;
			int seed = 10;

			// Establecer los parámetros del problema
			State.setHeuristicMode("max");
			criteria = new StateHeuristicFunction1();
			State.setOperatorsMode(0);

			System.out.println("##########\nEjercicio 9\n##########");
			System.out.println("##########\nIncremento de peticiones");
			nServers = 5;
			nReplications = 2;
			for (nRequestsUser = 5; nRequestsUser <= 25; nRequestsUser += 5) {
				System.out.println("Número de peticiones: " + nRequestsUser);

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

					// Sumatorio de resultados de heurística y tiempos de
					// ejecución
					// para cada caso
					heurAvg += end.getHeuristic1();
					execTime += d2.getTime() - d1.getTime();
				}
				// Cálculos de media de heurística y tiempos de ejecución para
				// cada
				// caso usando el sumatorio
				heurAvg /= nTests;
				execTime /= nTests;

				System.out.println("Tiempo de ejecución: " + execTime);
				System.out.println("Heurística media: " + heurAvg);
				System.out.println("----------");
				number = new Number(0, nRequestsUser / 5 + 1, nRequestsUser);
				sheet.addCell(number);
				number = new Number(1, nRequestsUser / 5 + 1, execTime);
				sheet.addCell(number);
				number = new Number(2, nRequestsUser / 5 + 1, heurAvg);
				sheet.addCell(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
