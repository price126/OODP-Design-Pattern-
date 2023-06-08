package lane;

import user.Party;

public interface LaneEventInterface extends java.rmi.Remote {
	Party getParty() throws java.rmi.RemoteException;
}

