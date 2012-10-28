import sun.management.Agent;
import aima.search.framework.Metrics;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

@SuppressWarnings("unused")
public class Main {
	public static void main(String args[]) {
		Search search = null; 
		Problem problem = null;
		int nServers = 50;
		int nReplications = 5;
		// int nUsers = 200;
		int nUsers = 750;
		// int nRequestsUser = 5;
		int nRequestsUser = 5;
		int seed = 10;
		
		// Parametros tipo de ejecución
		boolean swapOperator = true;
		boolean removeOperator = false;
		// Valores posibles para heuristic: "max" y "stdev"
		String heuristic = "max";
		// Valores posibles para algorythm "Simulated Anneling" y "Hill Climbing"
		String algorythm = "Hill Climbing";
		// Valores posibles para statement "Full Requests" y "Partial Requests"
		String statement = "Partial Requests";
		boolean greedy = false;
		
		
		SuccessorFunction successorAlgorythm = null;
		
		// Parametros Simulated Anneling
		int steps = 2000;
		int stiter = 100;
		int k = 5;
		double lamb = 0.001;
		
		State estat = new State(nServers, nReplications, nUsers, nRequestsUser,
				seed, swapOperator, removeOperator, heuristic);
		
		// Decidimos estado inicial
		if(statement.equals("Full Requests")){
			if(greedy){
				estat.initialGreedyStateFullRequests();
			}else{
				estat.initialRandomStateFullRequests(seed);
			}
		}else if(statement.equals("Partial Requests")){
			if(greedy){
				estat.initialGreedyState();
			}else{
				estat.initialRandomState(seed);
			}
		}
		System.out.println(estat.toString());
		// Decidimos algoritmo
		if(algorythm.equals("Hill Climbing")){
			successorAlgorythm = new StateSuccessorFunctionHill();
			search = new HillClimbingSearch();
		}else if(algorythm.equals("Simulated Anneling")){
			successorAlgorythm = new StateSuccessorFunctionSimulated();
			search = new SimulatedAnnealingSearch(steps, stiter, k, lamb); 
			
		}
		
		
		// Decidimos heurístico		
		if (heuristic.equals("max")){
			problem = new Problem(estat, successorAlgorythm, new StateGoalTest(), new StateHeuristicFunction1());
		}else if(heuristic.equals("stdev")){
			problem = new Problem(estat, successorAlgorythm, new StateGoalTest(), new StateHeuristicFunction2());
		}

		try {
			SearchAgent agent = new SearchAgent(problem,search);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		State end = (State) search.getGoalState();
		System.out.println(end.toString());
		Metrics m = search.getMetrics();
		int iteraciones = m.getInt("nodesExpanded");
		System.out.println("Iteraciones: " + iteraciones + "\n");
		
		

	}
}
