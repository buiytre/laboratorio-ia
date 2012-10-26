import aima.search.framework.HeuristicFunction;

public class StateHeuristicFunction1 implements
		HeuristicFunction {

	@Override
	public double getHeuristicValue(Object arg0) {
		// TODO Auto-generated method stub
		int maxTime = 0;
		State oldState = new State((State) arg0);
		for (int i = 0; i < State.getServersCount(); ++i) {
			if (maxTime <= oldState.getServerTime(i)) {
				maxTime = oldState.getServerTime(i);
			}
		}
		return maxTime;
	}

}
