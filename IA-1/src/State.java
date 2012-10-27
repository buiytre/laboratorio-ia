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
	private static boolean sSwapOperator = false;
	private static boolean sRemoveOperator = false;
	private static String sHeuristicMode = "";
	private static int sUsersCount = 0;
	private static int sMaxUserRequests = 0;
	private static int sSeed = 0;
	private static int sReplications = 0;

	// Parte dinamica
	private Link[] mServersRequests = null;
	private int[] mRequestsServers = null;
	private int mTotalTime = 0;
	private int mTotalPenalizationTime = 0;
	private double mHeuristic1 = -1;
	private double mHeuristic2 = -1;

	public State(int nServers, int nReplications, int nUsers,
			int nRequestsUser, int seed, boolean swapOperator,
			boolean removeOperator, String heuristic) {
		try {
			sServers = new Servers(nServers, nReplications, seed);
		} catch (WrongParametersException e) {
			e.printStackTrace();
		}
		State.sUsersCount = nUsers;
		State.sMaxUserRequests = nRequestsUser;
		State.sSeed = seed;
		State.sReplications = nReplications;
		State.sHeuristicMode = new String(heuristic);
		sRequests = new Requests(nUsers, nRequestsUser, seed);
		mServersRequests = new Link[nServers];
		for (int i = 0; i < mServersRequests.length; ++i) {
			mServersRequests[i] = new Link();
		}
		sRequestsCount = sRequests.size();
		sServersCount = nServers;
		mRequestsServers = new int[sRequestsCount];
		State.sSwapOperator = swapOperator;
		State.sRemoveOperator = removeOperator;
		for (int i = 0; i < sRequestsCount; ++i) {
			mRequestsServers[i] = -1;
		}
	}

	public State(int nServers, int nReplications, int nUsers,
			int nRequestsUser, int seed, boolean swapOperator,
			boolean removeOperator, String heuristic, int penalization) {
		this(nServers, nReplications, nUsers, seed, nRequestsUser,
				swapOperator, removeOperator, heuristic);
		sPenalization = penalization;
		mTotalPenalizationTime = sRequestsCount * sPenalization; // Se cuenta
																	// por
																	// defecto
																	// todas
																	// los links
																	// sin
																	// asignar,
																	// sumamos
																	// penalizaciones
	}

	public State(State oldState) {
		mTotalTime = oldState.mTotalTime;
		mTotalPenalizationTime = oldState.mTotalPenalizationTime;
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

	public static boolean getSwapOperator() {
		return sSwapOperator;
	}

	public static boolean getRemoveOperator() {
		return sRemoveOperator;
	}

	public static void getServerPerRequest() {

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

	public double getAverage() {
		return (mTotalTime / sServersCount);
	}

	public double getStDev() {
		double avg = getAverage();
		double incr = 0;
		for (int i = 0; i < sServersCount; ++i) {
			incr += Math.pow(mServersRequests[i].getTotalTime() - avg, 2);
		}
		return Math.sqrt(incr / (sServersCount - 1));
	}

	public int getTotalPenalizationTime() {
		return mTotalPenalizationTime;
	}

	public int getServerTime(int idServer) {
		return mServersRequests[idServer].getTotalTime();
	}
	
	public void initialRandomStateFullRequests(int seed) {
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
			// canSwap es implícito al haber buscado un válido
			swapOperator(idServer, i);
		}
		heuristicGen();
	}
	
	private void heuristicGen(){
		StateHeuristicFunction1 heuristicFunction1 = new StateHeuristicFunction1();
		StateHeuristicFunction2 heuristicFunction2 = new StateHeuristicFunction2();
		mHeuristic1 = heuristicFunction1.getHeuristicValue(this);
		mHeuristic2 = heuristicFunction2.getHeuristicValue(this);
	}



	public void initialGreedyStateFullRequests() {
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
			idServer = it.next(); // it tendrá mínimo un servidor, los files
			// tienen número mínimo de réplicas
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
			// canSwap es implícito al haber buscado un válido
			swapOperator(idServer, i);
		}
		heuristicGen();
	}

	public void initialRandomState(int seed) {
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
				// canSwap es implícito al haber buscado un válido
				swapOperator(idServer, i);
			}
		}
		heuristicGen();
	}

	public void initialGreedyState() {
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
			idServer = it.next(); // it tendrá mínimo un servidor, los files
			// tienen número mínimo de réplicas
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
				// canSwap es implícito al haber buscado un válido
				swapOperator(idServer, i);
			}
		}
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
		mTotalPenalizationTime -= sPenalization;
		// Recalculamos el heurístico
		heuristicGen();
	}

	public boolean canSwap(int idServer, int idRequest) {
		// Comprobar que la request existe
		if (idRequest >= sRequestsCount)
			return false;

		// Comprobar que el servidor objetivo contiene el fichero(log n) (y
		// se
		// comprueba que el servidor existe por extensión)
		if (!sServers.fileLocations(sRequests.getRequest(idRequest)[1])
				.contains(idServer)) {
			return false;
		}
		//Comprobar que no hagamos swap sobre nosotros mismos
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
		mTotalPenalizationTime += sPenalization;
		// Recalculamos el heurístico
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
		int[] req = null;
		Link ln = null;
		double deviation = this.getStDev();
		double average = this.getAverage();
		ret = ret.concat("###########\n");
		ret = ret.concat("Número de servidores: " + sServersCount + "\n");
		ret = ret.concat("Número de usuarios: " + sUsersCount + "\n");
		ret = ret.concat("Número máximo de peticiones por usuario: "
				+ sMaxUserRequests + "\n");
		ret = ret.concat("Número de replicaciones: " + sReplications + "\n");
		ret = ret.concat("Número de seed: " + sSeed + "\n");
		ret = ret.concat("###########\n");

//		for (int i = 0; i < sRequestsCount; ++i) {
//			ret = ret.concat("-----------\n");
//			ret = ret.concat("Request: " + i + "\n");
//			req = sRequests.getRequest(i);
//			ret = ret.concat("Usuario: " + req[0] + "\nFichero: " + req[1]
//					+ "\n");
//			if (mRequestsServers[i] == -1) {
//				ret = ret.concat("Servidor no asignado\n");
//				
//			} else {
//				ret = ret.concat("Servidor: " + mRequestsServers[i] + "\n");
//				ret = ret.concat("Tiempo Usuario/Servidor: " + sServers.tranmissionTime(mRequestsServers[i], req[0]));
//			}
//			Iterator<Integer> it = sServers.fileLocations(req[1]).iterator();
//			int idServer = -1;
//			while(it.hasNext()){
//				idServer = it.next();
//				ret = ret.concat("Tiempo usuario-server " + idServer + ": " + sServers.tranmissionTime(idServer, req[0]) + "\n");
//			}
//		}
//		ret = ret.concat("-----------\n");
//		ret = ret.concat("###########\n");
//		for (int i = 0; i < sServersCount; ++i) {
//			ln = mServersRequests[i];
//			ret = ret.concat("Server: " + i + "\n");
//			ret = ret.concat("Carga: " + ln.getTotalTime() + "\n-----------\n");
//		}
		
		ret = ret.concat("###########\n");
		ret = ret.concat("Valor heurístico1: " + mHeuristic1 + "\n");
		ret = ret.concat("Valor heurístico2: " + mHeuristic2 + "\n");
		ret = ret.concat("Media de carga: " + average + "\n");
		ret = ret.concat("Stdev: " + deviation + "\n###########\n");

		return ret;
	}

}
