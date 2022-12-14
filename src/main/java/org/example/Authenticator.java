// José Javier Hurtarte Hernández
// Universidad del valle de Guatemala
// 16-08-2022
// Java Authenticator class for Xmpp client


package org.example;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.util.HashMap;

public class Authenticator {
    /* @param servername the server to connect to
    */

    // "alumchat.fun" is the XMPP server address for this project
    public static Connection connect(String servername) {
        ConnectionConfiguration config = new ConnectionConfiguration(servername, 5222);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setDebuggerEnabled(true);
        Connection connection = new XMPPConnection(config);
        try {
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();


        }
        return connection;
    }
    // throws an exception when login fails
    static void login (Connection connection, String username, String password) throws XMPPException {
        try {
            connection.login(username, password);
        } catch (XMPPException e) {
            e.printStackTrace();
            throw e;


        }
    }
    // Logs out of connection
    static void logout (Connection connection) {
        connection.disconnect();
    }

    // creates an account with only username and password
    static void createAccount (Connection connection, String username, String password) {
        try {
            connection.getAccountManager().createAccount(username, password);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    //creates account with username, password, full name and email
    static void createAccount (Connection connection, String username, String password, String fullname, String email) {
        // create a hashmap to store the account information
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", fullname);
        attributes.put("email", email);
        try {
            connection.getAccountManager().createAccount(username, password, attributes);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    //deletes an account
    static void deleteAccount (Connection connection) {
        try {
            connection.getAccountManager().deleteAccount();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
