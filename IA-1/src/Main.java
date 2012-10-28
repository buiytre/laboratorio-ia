import sun.management.Agent;
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
		experiment2();
	}

	private static void experiment2() {
		State start = null;
		State end = null;
		Search search = null;
		Problem problem = null;
		SuccessorFunction successorAlgorythm = null;
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
		State.setSwapOperator(true);
		State.setRemoveOperator(false);
		State.setAddOperator(false);

		System.out.println("Ejercicio 2");
		for (int i = 0; i < generatorFunc.length; ++i) {
			System.out.println("Función generadora: " + generatorFunc[i] + "\n##########");
			for (int j = 0; j < 10; ++j) {
				System.out.println("Prueba 1:");
				// El parámetro seed varía
				State.setProblemParameters(nServers, nReplications, nUsers,
						nRequestsUser, j);

				// Generar el estado inicial (método generador)
				d1 = new Date();
				if (generatorFunc[i].equals("Random")) {
					start = new State().randomStateFullRequests(i);
				} else if (generatorFunc[i].equals("Greedy")) {
					start = new State().greedyStateFullRequests();
				} else
					break;
				//Usamos Hill Climbing
				successorAlgorythm = new StateSuccessorFunctionHill();
				search = new HillClimbingSearch();
				d2 = new Date();
				System.out.println("Tiempo de generación: "
						+ (d2.getTime() - d1.getTime()));

				// Ejecutar
				d1 = new Date();
				try {
					SearchAgent agent = new SearchAgent(problem, search);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d2 = new Date();
				System.out.println("Tiempo de ejecución: "
						+ (d2.getTime() - d1.getTime()));

				// Comparar heurísticas inicial y final
				end = (State) search.getGoalState();
				System.out.println("Heurística inicial: "
						+ start.getHeuristic1());
				System.out.println("Heurística final: " + end.getHeuristic1());
			}
		}
	}

}
