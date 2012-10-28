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
		//experiment2();
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
		//Primer criterio, con peticiones sin servir
		State.setHeuristicMode("max");
		criteria = new StateHeuristicFunction1();
		State.setPenalizationTime(2500);
		
		System.out.println("##########\nEjercicio 1\n##########");
		for (int i = 0; i < operatorsMode.length; ++i) {
			System.out.println("##########\nModo operadores: " + operatorsMode[i] + "\n##########");
			//El conjunto de operadores var�a
			State.setOperatorsMode(i);
			
			for (int j = 0; j < nTests; ++j) {
				System.out.println("Prueba " + j + ":");
				// El par�metro seed var�a
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (m�todo generador greedy con peticiones sin servir)
				d1 = new Date();
				start = new State().greedyState();
				
				//Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				//Primer criterio (minimizaci�n del servidor con m�s carga)
				problem = new Problem(start, successorAlgorythm, new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generaci�n: " + (d2.getTime() - d1.getTime()));

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
				System.out.println("Iteraciones: " + search.getMetrics().getInt("nodesExpanded"));
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
			System.out.println("Funci�n generadora: " + generatorFunc[i] + "\n##########");
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
				//Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				//Primer criterio (minimizaci�n del servidor con m�s carga)
				problem = new Problem(start, successorAlgorythm, new StateGoalTest(), criteria);
				d2 = new Date();
				System.out.println("Tiempo de generaci�n: "
						+ (d2.getTime() - d1.getTime()));

				System.out.println("Iteraciones: " + agent.getInstrumentation().getProperty("Iterations"));
				// Ejecutar
				d1 = new Date();
				try {
					agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				System.out.println("Tiempo de ejecuci�n: " + (d2.getTime() - d1.getTime()));
				System.out.println("Iteraciones: " + search.getMetrics().getInt("nodesExpanded"));				
				// Comparar heur�sticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heur�stica inicial: "
						+ start.getHeuristic1());
				System.out.println("Heur�stica final: " + end.getHeuristic1());
			}
		}
	}

}
