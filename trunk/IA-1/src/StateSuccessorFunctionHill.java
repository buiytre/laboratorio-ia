import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import IA.DistFS.*;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class StateSuccessorFunctionHill implements SuccessorFunction{

	@SuppressWarnings("unchecked")
	@Override
	public List getSuccessors(Object arg0) {
		// TODO Auto-generated method stub
		int requestCount = State.getRequestsCount();
		Requests totalRequests = State.getRequests();
		Servers totalServers = State.getServers();
		State padre = (State) arg0;
		State hijo = null;
		StateHeuristicFunction1 heuristicMax = new StateHeuristicFunction1();
		StateHeuristicFunction2 heuristicStdev = new StateHeuristicFunction2();
		ArrayList<Successor> successor = new ArrayList<Successor>();
		Iterator<Integer> it = null;
		int idServer = 0;
		String str = "Sucesor creado con el operador ";
		double heuristic = -1;

		
		for(int i = 0; i < requestCount ; ++i){
			it = totalServers.fileLocations(totalRequests.getRequest(i)[1]).iterator();
			while(it.hasNext()){
				str = "Sucesor creado con el operador ";
				idServer = it.next();
				if(State.getRemoveOperator() && padre.canRemove(i)){
					hijo = new State(padre);
					hijo.removeOperator(i);
					if(State.getHeuristicMode().equals("max")){
						heuristic = heuristicMax.getHeuristicValue(hijo);
					}else if(State.getHeuristicMode().equals("stdev")){
						heuristic = heuristicStdev.getHeuristicValue(hijo);
					}
					str = str.concat("remove sobre la request '" + i + "' con valor heurístico '" + Double.toString(heuristic) + "'.");
					successor.add(new Successor(str, hijo));
				}
				if(State.getSwapOperator() && padre.canSwap(idServer, i)){
					hijo = new State(padre);
					hijo.swapOperator(idServer, i);
					if(State.getHeuristicMode().equals("max")){
						heuristic = heuristicMax.getHeuristicValue(hijo);
					}else if(State.getHeuristicMode().equals("stdev")){
						heuristic = heuristicStdev.getHeuristicValue(hijo);
					}
					str = str.concat("swap sobre la request '" + i + "' con valor heurístico '" + Double.toString(heuristic) + "'.");
					successor.add(new Successor(str, hijo));
				}
				if(State.getAddOperator() && padre.canAdd(idServer, i)){
					hijo = new State(padre);
					hijo.addOperator(idServer, i);
					if(State.getHeuristicMode().equals("max")){
						heuristic = heuristicMax.getHeuristicValue(hijo);
					}else if(State.getHeuristicMode().equals("stdev")){
						heuristic = heuristicStdev.getHeuristicValue(hijo);
					}
					str = str.concat("add sobre la request '" + i + "' con valor heurístico '" + Double.toString(heuristic) + "'.");
					successor.add(new Successor(str, hijo));					
				}
			}
			
		}		
		return successor;
	}

}
