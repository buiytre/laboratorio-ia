import aima.search.framework.HeuristicFunction;

public class StateHeuristicFunction2 implements HeuristicFunction{

	@Override
	public double getHeuristicValue(Object arg0) {
		int deviation = 0;
		int average = 0;
		State actualState = new State((State) arg0);
		average = actualState.getAverage();
		for(int i = 0; i < State.getServersCount(); ++i){
			deviation += Math.abs(actualState.getServerTime(i) - average);
		}
		// Devolvemos la media de tiempo del servidor, mas la desviación estandar, mas el tiempo de penalización total del estado dividido por la cantidad de servidores. 
		return average + deviation + (actualState.getTotalPenalizationTime() / State.getServersCount());
	}

}
