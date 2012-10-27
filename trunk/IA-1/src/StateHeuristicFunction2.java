import aima.search.framework.HeuristicFunction;

public class StateHeuristicFunction2 implements HeuristicFunction{

	@Override
	public double getHeuristicValue(Object arg0) {
		State s = (State) arg0;
		// Devolvemos la media de tiempo del servidor, mas la desviación estandar, mas el tiempo de penalización total del estado dividido por la cantidad de servidores. 
		return s.getAverage() + s.getStDev() + (s.getTotalPenalizationTime() / State.getServersCount());
	}

}
