package ai.cloudshelf.exponursdk;


/**
 * Interface representing the UART service's events.
 */
public interface UartServiceEvents {
	void onConnStateChanged();
	void onDataAvailable(byte []data);
	void onReadRemoteRssi(int rssi);
}
