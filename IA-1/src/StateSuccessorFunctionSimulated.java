import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.Date;

public class StateSuccessorFunctionSimulated implements SuccessorFunction {

	@SuppressWarnings("unchecked")
	@Override
	public List getSuccessors(Object arg0) {
		Requests totalRequests = State.getRequests();
		Servers totalServers = State.getServers();
		State padre = (State) arg0;
		State hijo = null;
		Date date = new Date();
		Random rdm = new Random(date.getTime());
		int random = -1;
		ArrayList<Successor> successor = new ArrayList<Successor>();
		Iterator<Integer> it = null;
		int idServer = -1;
		int idRequest = -1;
		String str = "Sucesor creado con el operador ";

		while (hijo == null) {
			idRequest = rdm.nextInt(State.getRequestsCount());
			it = totalServers.fileLocations(
					totalRequests.getRequest(idRequest)[1]).iterator();
			random = rdm.nextInt(totalServers.fileLocations(
					totalRequests.getRequest(idRequest)[1]).size());
			for (int i = 0; i <= random; ++i) {
				idServer = it.next();
			}
			switch (State.getOperatorsMode()) {
			case 0: // Swap
				if (padre.canSwap(idServer, idRequest)) {
					hijo = new State(padre);
					hijo.swapOperator(idServer, idRequest);
					str = str.concat("swap "
							+ getSuccessorComment(hijo, idRequest));
				}
				break;
			case 1: // Swap+Del
				switch (rdm.nextInt(2)) {
				case 0:
					if (padre.canSwap(idServer, idRequest)) {
						hijo = new State(padre);
						hijo.swapOperator(idServer, idRequest);
						str = str.concat("swap "
								+ getSuccessorComment(hijo, idRequest));
					} else if (padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.removeOperator(idRequest);
						str = str.concat("remove "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				case 1:
					if (padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.removeOperator(idRequest);
						str = str.concat("remove "
								+ getSuccessorComment(hijo, idRequest));
					} else if (padre.canSwap(idServer, idRequest)) {
						hijo = new State(padre);
						hijo.swapOperator(idServer, idRequest);
						str = str.concat("swap "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				}
				break;
			case 2: // Add+Del
				switch (rdm.nextInt(2)) {
				case 0:
					if (padre.canAdd(idServer, idRequest)) {
						hijo = new State(padre);
						hijo.addOperator(idServer, idRequest);
						str = str.concat("add "
								+ getSuccessorComment(hijo, idRequest));
					} else if (padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.removeOperator(idRequest);
						str = str.concat("remove "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				case 1:
					if (padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.removeOperator(idRequest);
						str = str.concat("remove "
								+ getSuccessorComment(hijo, idRequest));
					} else if (padre.canAdd(idServer, idRequest)) {
						hijo = new State(padre);
						hijo.addOperator(idServer, idRequest);
						str = str.concat("add "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				}
				break;
			case 3: // Add+Swap+Del
				switch (rdm.nextInt(3)) {
				case 0:
					if (State.getSwapOperator()
							&& padre.canSwap(idServer, idRequest)) {
						hijo = new State(padre);
						hijo.swapOperator(idServer, idRequest);
						str = str.concat("swap "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				case 1:
					if (State.getRemoveOperator() && padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.removeOperator(idRequest);
						str = str.concat("remove "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				case 2:
					if (State.getAddOperator() && padre.canRemove(idRequest)) {
						hijo = new State(padre);
						hijo.addOperator(idServer, idRequest);
						str = str.concat("add "
								+ getSuccessorComment(hijo, idRequest));
					}
					break;
				}
			}
		}
		successor.add(new Successor(str, hijo));
		return successor;
	}

	private String getSuccessorComment(State successor, int idRequest) {
		double heuristic = -1;
		StateHeuristicFunction1 heuristicMax = new StateHeuristicFunction1();
		StateHeuristicFunction2 heuristicStdev = new StateHeuristicFunction2();
		if (State.getHeuristicMode().equals("max")) {
			heuristic = heuristicMax.getHeuristicValue(successor);
		} else if (State.getHeuristicMode().equals("stdev")) {
			heuristic = heuristicStdev.getHeuristicValue(successor);
		}
		return ("sobre la request '" + idRequest + "' con valor heurístico '"
				+ Double.toString(heuristic) + "'.");
	}

}
