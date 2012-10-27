import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
	public static void main(String args[]) {
		Search search = null; 
		Problem problem = null;
		String statement = "";
		boolean greedy = false;
		int nServers = 50;
		int nReplications = 5;
		// int nUsers = 200;
		int nUsers = 2;
		// int nRequestsUser = 5;
		int nRequestsUser = 4;
		int seed = 10;
		boolean swapOperator = false;
		boolean removeOperator = false;
		String heuristic = "";
		String algorythm = "";
		SuccessorFunction successorAlgorythm = null;
		
		// Parametros Simulated Anneling
		int steps = -1;
		int stiter = -1;
		int k = -1;
		double lamb = -1;
		
		State estat = new State(nServers, nReplications, nUsers, nRequestsUser,
				seed, swapOperator, removeOperator, heuristic);
		// estat.initialRandomStateFullRequests(seed);
		// estat.initialGreedyState();
		// estat.initialRandomState(seed);
		
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

		

	}
}
