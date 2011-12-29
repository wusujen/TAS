package sojamo.network;

public interface NetworkListener {
	
	public void netEvent(NetworkMessage theNetMessage);
	
	// take this out and make a separate NetworkStatusListener
	public void netStatus(NetworkStatus theStatus);
	
}
