
public class Main {
	public static void main(String args[]) {
		int nServers = 50;
		int nReplications = 5;
		//int nUsers = 200;
		int nUsers = 2;
		//int nRequestsUser = 5;
		int nRequestsUser = 4;
		int seed = 10;
		Estat estat = new Estat(nServers, nReplications, nUsers,nRequestsUser,seed);
		estat.prueba();
	}
}
