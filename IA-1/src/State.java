import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class State {

	// Parte estatica
	private static Servers sServers = null;
	private static Requests sRequests = null;
	private static int sRequestsCount = 0;
	private static int sServersCount = 0;
	private static int sPenalization = 0;
	private static boolean sAddOperator = false;
	private static boolean sSwapOperator = false;
	private static boolean sRemoveOperator = false;
	private static int sOperatorsMode = -1;
	private static String sHeuristicMode = "";
	private static int sUsersCount = 0;
	private static int sMaxUserRequests = 0;
	private static int sSeed = 0;
	private static int sReplications = 0;

	// Parte dinamica
	private Link[] mServersRequests = null;
	private int[] mRequestsServers = null;
	private int mTotalTime = 0;
	private int mUnservedRequest = 0;
	private double mHeuristic1 = -1;
	private double mHeuristic2 = -1;

	public static void setProblemParameters(int nServers, int nReplications,
			int nUsers, int nRequestsUser, int seed) {
		try {
			sServers = new Servers(nServers, nReplications, seed);
		} catch (WrongParametersException e) {
			e.printStackTrace();
		}

		State.sUsersCount = nUsers;
		State.sMaxUserRequests = nRequestsUser;
		State.sSeed = seed;
		State.sReplications = nReplications;
		sRequests = new Requests(nUsers, nRequestsUser, seed);
		sRequestsCount = sRequests.size();
		sServersCount = nServers;
	}

	public State() {
		mServersRequests = new Link[sServersCount];
		for (int i = 0; i < mServersRequests.length; ++i) {
			mServersRequests[i] = new Link();
		}
		mRequestsServers = new int[sRequestsCount];
		for (int i = 0; i < sRequestsCount; ++i) {
			mRequestsServers[i] = -1;
		}

		mUnservedRequest = sRequestsCount; // Se cuenta
																	// por
																	// defecto
																	// todas los
																	// links sin
																	// asignar,
																	// sumamos
																	// penalizaciones
	}

	public State(State oldState) {
		mTotalTime = oldState.mTotalTime;
		mUnservedRequest = oldState.mUnservedRequest;
		this.mServersRequests = oldState.mServersRequests.clone();
		this.mRequestsServers = oldState.mRequestsServers.clone();
		for (int i = 0; i < mServersRequests.length; ++i) {
			this.mServersRequests[i] = new Link(oldState.mServersRequests[i]);
		}
		this.mTotalTime = oldState.mTotalTime;
	}

	public static Requests getRequests() {
		return sRequests;
	}

	public static Servers getServers() {
		return sServers;
	}

	public static boolean getAddOperator() {
		return sAddOperator;
	}

	public static boolean getSwapOperator() {
		return sSwapOperator;
	}

	public static boolean getRemoveOperator() {
		return sRemoveOperator;
	}

	public static int getServersCount() {
		return sServersCount;
	}

	public static int getRequestsCount() {
		return sRequestsCount;
	}

	public static String getHeuristicMode() {
		return sHeuristicMode;
	}

	public double getHeuristic1() {
		return mHeuristic1;
	}

	public double getHeuristic2() {
		return mHeuristic2;
	}

	public double getAverage() {
		return (mTotalTime / sServersCount);
	}

	public int getTotalTime() {
		return mTotalTime;
	}
	
	public double getStDev() {
		double avg = getAverage();
		double incr = 0;
		for (int i = 0; i < sServersCount; ++i) {
			incr += Math.pow(mServersRequests[i].getTotalTime() - avg, 2);
		}
		return Math.sqrt(incr / (sServersCount - 1));
	}

	public int getUnservedRequestsCount(){
		return mUnservedRequest;
	}
	
	public int getTotalPenalizationTime() {
		return mUnservedRequest*sPenalization;
	}

	public int getServerTime(int idServer) {
		return mServersRequests[idServer].getTotalTime();
	}

	public static void setHeuristicMode(String s) {
		sHeuristicMode = new String(s);
	}

	public static void setPenalizationTime(int penalization) {
		sPenalization = penalization;
	}

	private void heuristicGen() {
		StateHeuristicFunction1 heuristicFunction1 = new StateHeuristicFunction1();
		StateHeuristicFunction2 heuristicFunction2 = new StateHeuristicFunction2();
		mHeuristic1 = heuristicFunction1.getHeuristicValue(this);
		mHeuristic2 = heuristicFunction2.getHeuristicValue(this);
	}
	
	public static void setOperatorsMode(int n){
		if (n >= 0 && n < 4){
			switch (n){
			case 0:
				sAddOperator = false;
				sSwapOperator = true;
				sRemoveOperator = false;
				break;
			case 1:
				sAddOperator = false;
				sSwapOperator = true;
				sRemoveOperator = true;
				break;
			case 2:
				sAddOperator = true;
				sSwapOperator = false;
				sRemoveOperator = true;
				break;
			case 3:
				sAddOperator = true;
				sSwapOperator = true;
				sRemoveOperator = true;
				break;
			}
			sOperatorsMode = n;
		}
	}
	
	public static int getOperatorsMode(){
		return sOperatorsMode;
	}

	public State randomStateFullRequests(int seed) {
		Set<Integer> serverSet;
		Random rndGr = new Random(seed);
		int rnd = -1;
		int idFile = -1;
		int idServer = -1;
		Iterator<Integer> it;
		for (int i = 0; i < sRequestsCount; ++i) {
			idFile = sRequests.getRequest(i)[1];
			// Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);

			// Elegir aleatoriamente un servidor con el fichero
			rnd = rndGr.nextInt(serverSet.size());
			it = serverSet.iterator();
			for (int j = 0; j <= rnd; ++j) {
				idServer = it.next();
			}
			// Asignarle el fichero
			// canSwap es impl�cito al haber buscado un v�lido
			swapOperator(idServer, i);
		}
		heuristicGen();
		return this;
	}

	public State greedyStateFullRequests() {
		Set<Integer> serverSet;
		int idFile = -1;
		int idServer = -1;
		int nextServer = -1;
		int optimalTime = -1;

		Iterator<Integer> it;
		for (int i = 0; i < sRequestsCount; ++i) {
			idFile = sRequests.getRequest(i)[1];
			// Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);

			// Elegir el servidor con menos carga
			it = serverSet.iterator();
			idServer = it.next(); // it tendr� m�nimo un servidor, los files
			// tienen n�mero m�nimo de r�plicas
			optimalTime = mServersRequests[idServer].getTotalTime()
					+ sServers.tranmissionTime(idServer,
							sRequests.getRequest(i)[0]);
			while (it.hasNext()) {
				nextServer = it.next();
				if (optimalTime > mServersRequests[nextServer].getTotalTime()
						+ sServers.tranmissionTime(nextServer,
								sRequests.getRequest(i)[0])) {
					idServer = nextServer;
					optimalTime = mServersRequests[nextServer].getTotalTime()
							+ sServers.tranmissionTime(nextServer,
									sRequests.getRequest(i)[0]);
				}
			}
			// Asignarle el fichero
			// canSwap es impl�cito al haber buscado un v�lido
			swapOperator(idServer, i);
		}
		heuristicGen();
		return this;
	}

	public State randomState(int seed) {
		Set<Integer> serverSet;
		Random rndGr = new Random(seed);
		int rnd = -1;
		int idFile = -1;
		int idServer = -1;
		Iterator<Integer> it;
		for (int i = 0; i < sRequestsCount; ++i) {

			idFile = sRequests.getRequest(i)[1];
			// Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);

			// Elegir aleatoriamente un servidor con el fichero
			rnd = rndGr.nextInt(serverSet.size() + 1); // agregar caso sin
			// servir
			if (rnd < serverSet.size()) {
				it = serverSet.iterator();
				for (int j = 0; j <= rnd; ++j) {
					idServer = it.next();
				}
				// Asignarle el fichero
				// canSwap es impl�cito al haber buscado un v�lido
				swapOperator(idServer, i);
			}
		}
		heuristicGen();
		return this;
	}

	public State greedyState() {
		Set<Integer> serverSet;
		int idFile = -1;
		int idServer = -1;
		int nextServer = -1;
		int optimalTime = -1;

		Iterator<Integer> it;
		for (int i = 0; i < sRequestsCount; ++i) {
			idFile = sRequests.getRequest(i)[1];
			// Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);

			// Elegir el servidor con menos carga
			it = serverSet.iterator();
			idServer = it.next(); // it tendr� m�nimo un servidor, los files
			// tienen n�mero m�nimo de r�plicas
			optimalTime = mServersRequests[idServer].getTotalTime()
					+ sServers.tranmissionTime(idServer,
							sRequests.getRequest(i)[0]);
			while (it.hasNext()) {
				nextServer = it.next();
				if (optimalTime > mServersRequests[nextServer].getTotalTime()
						+ sServers.tranmissionTime(nextServer,
								sRequests.getRequest(i)[0])) {
					idServer = nextServer;
					optimalTime = mServersRequests[nextServer].getTotalTime()
							+ sServers.tranmissionTime(nextServer,
									sRequests.getRequest(i)[0]);
				}
			}

			if (optimalTime <= mServersRequests[idServer].getTotalTime()
					+ sPenalization) {
				// Asignarle el fichero
				// canSwap es impl�cito al haber buscado un v�lido
				swapOperator(idServer, i);
			}
		}
		heuristicGen();
		return this;
	}
	
	public State orderedState() {
		Set<Integer> serverSet;
		int idFile = -1;
		int idServer = -1;

		Iterator<Integer> it;
		for (int i = 0; i < sRequestsCount; ++i) {
			idFile = sRequests.getRequest(i)[1];
			// Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);

			// Elegir el servidor con menos carga
			it = serverSet.iterator();
			idServer = it.next(); // it tendr� m�nimo un servidor, los files
			swapOperator(idServer, i);
		}
		heuristicGen();
		return this;
	}

	public boolean canAdd(int idServer, int idRequest) {
		// Comprobar que la request existe
		if (idRequest >= sRequestsCount)
			return false;

		// Comprobar que el servidor objetivo contiene el fichero(log n) (y
		// se
		// comprueba que el servidor existe por extensi�n)
		if (!sServers.fileLocations(sRequests.getRequest(idRequest)[1])
				.contains(idServer)) {
			return false;
		}
		// Comprobar que estemos asignados
		if (mRequestsServers[idRequest] != -1) {
			return false;
		}

		return true;

	}

	public void addOperator(int idServer, int idRequest) {
		mRequestsServers[idRequest] = idServer;
		// Agregar a la lista de requests del servidor actual el request y su
		// tiempo
		mServersRequests[idServer].addLink(
				idRequest,
				sServers.tranmissionTime(idServer,
						sRequests.getRequest(idRequest)[0]));
		mTotalTime += sServers.tranmissionTime(idServer,
				sRequests.getRequest(idRequest)[0]);
		mUnservedRequest -= 1;
		// Recalculamos el heur�stico
		heuristicGen();
	}

	public void swapOperator(int idServer, int idRequest) {
		if (canRemove(idRequest))
			removeOperator(idRequest);
		mRequestsServers[idRequest] = idServer;
		// Agregar a la lista de requests del servidor actual el request y su
		// tiempo
		mServersRequests[idServer].addLink(
				idRequest,
				sServers.tranmissionTime(idServer,
						sRequests.getRequest(idRequest)[0]));
		mTotalTime += sServers.tranmissionTime(idServer,
				sRequests.getRequest(idRequest)[0]);
		mUnservedRequest -= 1;
		// Recalculamos el heur�stico
		heuristicGen();
	}

	public boolean canSwap(int idServer, int idRequest) {
		// Comprobar que la request existe
		if (idRequest >= sRequestsCount)
			return false;

		// Comprobar que el servidor objetivo contiene el fichero(log n) (y
		// se
		// comprueba que el servidor existe por extensi�n)
		if (!sServers.fileLocations(sRequests.getRequest(idRequest)[1])
				.contains(idServer)) {
			return false;
		}
		// Comprobar que no hagamos swap sobre nosotros mismos
		if (mRequestsServers[idRequest] == idServer) {
			return false;
		}
		return true;

	}

	public void removeOperator(int idRequest) {
		int oldServer = mRequestsServers[idRequest];
		mRequestsServers[idRequest] = -1;
		// Eliminar de la lista de request del servidor actual el request
		mServersRequests[oldServer].delLink(
				idRequest,
				sServers.tranmissionTime(oldServer,
						sRequests.getRequest(idRequest)[0]));
		mTotalTime -= sServers.tranmissionTime(oldServer,
				sRequests.getRequest(idRequest)[0]);
		mUnservedRequest += 1;
		// Recalculamos el heur�stico
		heuristicGen();
	}

	public boolean canRemove(int idRequest) {
		// Comprobar que la request existe
		if (idRequest >= sRequestsCount)
			return false;
		return mRequestsServers[idRequest] == -1 ? false : true;
	}

	@Override
	public String toString() {
		String ret = "###########\n";
		// int[] req = null;
		// Link ln = null;
		double deviation = this.getStDev();
		double average = this.getAverage();
		ret = ret.concat("###########\n");
		ret = ret.concat("N�mero de servidores: " + sServersCount + "\n");
		ret = ret.concat("N�mero de usuarios: " + sUsersCount + "\n");
		ret = ret.concat("N�mero m�ximo de peticiones por usuario: "
				+ sMaxUserRequests + "\n");
		ret = ret.concat("N�mero de replicaciones: " + sReplications + "\n");
		ret = ret.concat("N�mero de seed: " + sSeed + "\n");
		ret = ret.concat("###########\n");

		// for (int i = 0; i < sRequestsCount; ++i) {
		// ret = ret.concat("-----------\n");
		// ret = ret.concat("Request: " + i + "\n");
		// req = sRequests.getRequest(i);
		// ret = ret.concat("Usuario: " + req[0] + "\nFichero: " + req[1]
		// + "\n");
		// if (mRequestsServers[i] == -1) {
		// ret = ret.concat("Servidor no asignado\n");
		//
		// } else {
		// ret = ret.concat("Servidor: " + mRequestsServers[i] + "\n");
		// ret = ret.concat("Tiempo Usuario/Servidor: " +
		// sServers.tranmissionTime(mRequestsServers[i], req[0]));
		// }
		// Iterator<Integer> it = sServers.fileLocations(req[1]).iterator();
		// int idServer = -1;
		// while(it.hasNext()){
		// idServer = it.next();
		// ret = ret.concat("Tiempo usuario-server " + idServer + ": " +
		// sServers.tranmissionTime(idServer, req[0]) + "\n");
		// }
		// }
		// ret = ret.concat("-----------\n");
		// ret = ret.concat("###########\n");
		// for (int i = 0; i < sServersCount; ++i) {
		// ln = mServersRequests[i];
		// ret = ret.concat("Server: " + i + "\n");
		// ret = ret.concat("Carga: " + ln.getTotalTime() + "\n-----------\n");
		// }

		ret = ret.concat("###########\n");
		ret = ret.concat("Valor heur�stico1: " + mHeuristic1 + "\n");
		ret = ret.concat("Valor heur�stico2: " + mHeuristic2 + "\n");
		ret = ret.concat("Media de carga: " + average + "\n");
		ret = ret.concat("Stdev: " + deviation + "\n###########\n");

		return ret;
	}

}
