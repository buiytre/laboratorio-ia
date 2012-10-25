import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;


public class Estat {
	
	
	//Parte estatica
	public static Servers sServers = null;
	public static Requests sRequests = null;
	
	
	//Parte dinamica
	public ArrayList<> ServersPeticiones
	
	public Estat(int nServers, int nReplications, int nUsers, int nRequestsUser, int seed) {
		// TODO Auto-generated constructor stub
		try {
			sServers = new Servers(nServers, nReplications, seed);
		} catch (WrongParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sRequests = new Requests(nUsers, nRequestsUser, seed);
	}
	
	public void comprovar(){
		System.out.println(sServers.tranmissionTime(2, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));
		System.out.println(sServers.tranmissionTime(1, 1));		
	}

}
