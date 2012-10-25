import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;


public class Estat {
	
	
	//Parte estatica
	private static Servers sServers = null;
	private static Requests sRequests = null;
	
	
	//Parte dinamica
	private Asignacion[] mServersPeticiones = null;
	private int[] mPeticionesServer = null; 
	
	public Estat(int nServers, int nReplications, int nUsers, int nRequestsUser, int seed) {
		// TODO Auto-generated constructor stub
		try {
			sServers = new Servers(nServers, nReplications, seed);
		} catch (WrongParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sRequests = new Requests(nUsers, nRequestsUser, seed);
		mServersPeticiones = new Asignacion[nServers];
		mPeticionesServer = new int[sRequests.size()];
	}
	
	public void prueba(){
		for (int i=0; i < sRequests.size(); ++i){
			System.out.println(sRequests.getRequest(i)[0]);
		}
	}
		


}
