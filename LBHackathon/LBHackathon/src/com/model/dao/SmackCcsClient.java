package com.model.dao;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
//import org.jivesoftware.smack.Roster;
//import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.roster.Roster;
public class SmackCcsClient {
	// private static final Logger logger = Logger.getLogger("SmackCssClient");

	  private AbstractXMPPConnection connection;

	  public SmackCcsClient(String apiKey, String username, String serviceName, String host, int port) {
	    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
	        .setServiceName(serviceName)
	        .setHost(host)
	        .setSocketFactory(SSLSocketFactory.getDefault())
	        .setSendPresence(false)
	        .setPort(port)
	    //    .setDebuggerEnabled(true)
	        .build();

	    connection = new XMPPTCPConnection(config);
	    Roster.getInstanceFor(connection).setRosterLoadedAtLogin(false);
	    

	    connection.addConnectionListener(new ConnectionListener() {
	      public void connected(XMPPConnection connection) {
	        System.out.println("Connected to CCS");
	      }
	
	      public void authenticated(XMPPConnection connection, boolean resumed) {
	    	  System.out.println("Authenticated with CCS");
	      }

	      public void connectionClosed() {
	    	  System.out.println("Connection to CCS closed");
	      }

	      public void connectionClosedOnError(Exception e) {
	    	  System.out.println("Connection closed because of an error.");
	      }

	      
	      public void reconnectionSuccessful() {
	    	  System.out.println("Reconnected to CCS");
	      }

	      
	      public void reconnectingIn(int seconds) {
	    	  System.out.println("Reconnecting to CCS in " + seconds);
	      }

	      
	      public void reconnectionFailed(Exception e) {
	    	  System.out.println("Reconnection to CCS failed");
	      }
	    });

	    try {
	      // Connect and authenticate with to XMPP server (GCM CCS in this case).
	      connection.connect();
	      System.out.println("*********"+username+" ******* "+apiKey);
	      connection.login(username, apiKey);
	    } catch (Exception e) {
	      System.out.println("Exception ");
	      e.printStackTrace();
	    }
	  }

	  /**
	   * Begin listening for incoming messages.
	   *
	   * @param stanzaListener Listener that handles accepted messages. This is defined in
	   *                       FriendlyPingServer.
	   * @param stanzaFilter Filter that determines what messages are handled by the listener.
	   */
	  public void listen(StanzaListener stanzaListener, StanzaFilter stanzaFilter) {
	    connection.addAsyncStanzaListener(stanzaListener, stanzaFilter);
	    System.out.println("Listening for incoming XMPP Stanzas...");
	  }

	  public void sendStanza(Stanza stanza) {
	    try {
	      connection.sendStanza(stanza);
	    } catch (SmackException.NotConnectedException e) {
	    	System.out.println("Error occurred while sending stanza.");
	    }
}
}
