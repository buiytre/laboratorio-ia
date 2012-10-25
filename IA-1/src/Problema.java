import IA.DistFS.*;

public class Problema {
	private static Problema sInstance = null;
	public Requests sRequests;
	public Servers sServers;
	
	
	public static synchronized Problema getInstance() {
		if (sInstance == null) sInstance = new Problema();
		return sInstance;
	}
	
	
}
