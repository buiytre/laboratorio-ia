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
	
	// Parte dinamica
	private Link[] mServersRequests = null;
	private int[] mRequestsServers = null;
	

	public State(int nServers, int nReplications, int nUsers,
			int nRequestsUser, int seed) {
		try {
			sServers = new Servers(nServers, nReplications, seed);
		} catch (WrongParametersException e) {
			e.printStackTrace();
		}
		sRequests = new Requests(nUsers, nRequestsUser, seed);
		mServersRequests = new Link[nServers];
		sRequestsCount = sRequests.size();
		sServersCount = nServers;
		mRequestsServers = new int[sRequestsCount];
		for (int i = 0; i < sRequestsCount; ++i) {
			mRequestsServers[i] = -1;
		}
		// ESTADO INICIAL "FINAL VALIDO"
	}

	public void initialRandomStateFullRequests(int seed) {
		Set<Integer> serverSet;
		Random rndGr = new Random(seed);
		int rnd = -1;
		int idFile = -1;
		int idServer = -1;
		Iterator<Integer> it;
		for(int i = 0; i < sRequestsCount; ++i){
			idFile = sRequests.getRequest(i)[1];
			//Obtener la lista de servidores con ese fichero
			serverSet = sServers.fileLocations(idFile);
			
			//Elegir aleatoriamente un servidor con el fichero
			rnd = rndGr.nextInt(serverSet.size());
			it = serverSet.iterator();
			for (int j = 0; j <= rnd; ++j) {
				idServer = it.next();
			}
			//Asignarle el fichero
			//canSwap es implícito al haber buscado un válido
			swapOperator(idServer, i);
		}		
	}
	
	public void initialGreedyStateFullRequests() {
		
	}

	public void initialRandomState() {
		
	}
	
	public void initialGreedyState() {
		
	}

	public void swapOperator(int idServer, int idRequest) {
		
		if (canRemove(idRequest)) removeOperator(idRequest);
		mRequestsServers[idRequest] = idServer;
		//Agregar a la lista de requests del servidor actual el request y su tiempo
		mServersRequests[idServer].addLink(idRequest, sServers.tranmissionTime(idServer, sRequests.getRequest(idRequest)[0]));
	}

	public boolean canSwap(int idServer, int idRequest) {
		//Comprobar que la request existe
		if (idRequest >= sRequestsCount) return false;
		
		//Comprobar que el servidor objetivo contiene el fichero(log n) (y se comprueba que el servidor existe por extensión)
		if (!sServers.fileLocations(sRequests.getRequest(idRequest)[1]).contains(idServer)){
			return false;
		}
		return true;
	}

	public void removeOperator(int idRequest) {
		int oldServer = mRequestsServers[idRequest];
		mRequestsServers[idRequest]=-1;
		//Eliminar de la lista de request del servidor actual el request
		mServersRequests[oldServer].delLink(idRequest, sServers.tranmissionTime(oldServer, sRequests.getRequest(idRequest)[0]));
	}

	public boolean canRemove(int idRequest) {
		//Comprobar que la request existe
		if (idRequest >= sRequestsCount) return false;
		return mRequestsServers[idRequest]==-1 ? false : true;
	}



}
