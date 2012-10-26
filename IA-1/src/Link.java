import java.util.ArrayList;


public class Link {
	private int mTotalTime;
	private ArrayList<Integer> mRequests;
	
	public Link(){
		mRequests = new ArrayList<Integer>();
		mTotalTime = 0;		
	}
	
	public void addLink(int idRequest, int time) {
		mRequests.add(idRequest);
		mTotalTime += time;
	}

	public void delLink(int idRequest, int time){
		int sizeArray = mRequests.size();
		for (int i=0; i < sizeArray; ++i){
			if (mRequests.get(i) == idRequest) {
				mRequests.remove(i);
				mTotalTime -= time;
				return ; //miau
			}
		}
		
	}
	
	public int getTotalTime() {
		 return mTotalTime;
	}
	
	
}
