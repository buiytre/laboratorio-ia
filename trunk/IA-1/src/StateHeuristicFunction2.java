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
		//Hasta aquí sería para el caso sin penalización, los comentarios de abajo son para recordar que hemos hablado
		//actualState.getTotalPenalizationTime();
		// Hay que sumar la relacion entre numPeticiones <-> peticiones no servidas <-> numServidores
		return deviation;
	}

}
