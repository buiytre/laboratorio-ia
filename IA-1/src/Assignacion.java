import java.util.ArrayList;


public class Assignacion {
	private int mTiempoTotal;
	private ArrayList<Integer> mRequests;
	
	public void addAsignacion(int idRequest, int tiempo) {
		mRequests.add(idRequest);
		mTiempoTotal += tiempo;
	}

	public boolean delAsignacion(int idRequest, int tiempo){
		int sizeArray = mRequests.size();
		for (int i=0; i < sizeArray; ++i){
			if (mRequests.get(i) == idRequest) {
				mRequests.remove(i);
				mTiempoTotal -= tiempo;
				return true;	// Lo ponemos por eficiencia, aunque sea contrario a mis principios
			}
		}
		return false;
	}
	
	public void getMTiempoTotal(int tiempoTotal) {
		 tiempoTotal = mTiempoTotal;
	}
	
	
}
