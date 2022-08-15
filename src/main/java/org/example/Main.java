package org.example;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {


        System.out.println("Hello world!");
        Connection connection = connect();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nConnected to server");
        System.out.println("Por favor inicie sesión");
        System.out.println("Usuario: ");

        Scanner scanner = new Scanner(System.in);
        String user = scanner.nextLine();
        System.out.println("Contraseña: ");
        String password = scanner.nextLine();

        try {
            connection.login(user, password);
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        //creates a global listener for all messages
        recieveMessage(connection);
        sendPresence(connection);

        ArrayList<Chat> chats = new ArrayList<Chat>();
        //append the new chat to the arraylist
        chats.add(createChat(connection, "jjhh2@alumchat.fun"));

        sendMessage(chats.get(0), "Hola ya tas en un arraylist y global listener");
        System.out.println("toy chateando con "+chats.get(0).getParticipant());

        String opcionMenu = "";
        do {
            System.out.println("Chat XMPP\n\n1. Enviar mensaje directo\n\n");
            opcionMenu = scanner.nextLine();
            if (opcionMenu.equals("1")) {
                System.out.println("Por favor ingrese el usuario del destinatario");
                String destinatario = scanner.nextLine();
                System.out.println("Por favor ingrese el mensaje");
                String mensaje = scanner.nextLine();
                //sendMessage(destinatario, mensaje);
            }
        } while((!opcionMenu.equals("0")));
        connection.disconnect();


    }


    // create a login to the server alumchat.fun
    public static void login(Connection connection, String username, String password) {
        try {
            connection.login(username, password);
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }
    // send presence to the server alumchat.fun
    public static void sendPresence(Connection connection) {
        Presence presence = new Presence(Presence.Type.available);
        try {
            connection.sendPacket(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
//    // create a chat with the user orlando@alumchat.fun
    public static Chat createChat(Connection connection, String username) {
        Chat chat = connection.getChatManager().createChat(username, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                // if message.getBody() is not null, then print the message
//                if (message.getBody() != null) {
//                    System.out.println(message.getFrom() + ": "+message.getBody());
//                }

            }
        });
        return chat;
    }
    // send a message to the user
    public static void sendMessage(Chat chat, String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

//    //recieves all the messages from all the users
    public static void recieveMessage(Connection connection) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                System.out.println(message.getFrom()+ ": " + message.getBody());
            }
        }, new PacketTypeFilter(Message.class));
    }



//    //function that creates an account with all the parameters
//    public static void createAccount(Connection connection, String username, String password) {
//        try {
//            connection.getAccountManager().createAccount(username, password);
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
//    }

    //get all the groups of the user


    public static Connection connect() {
        ConnectionConfiguration config = new ConnectionConfiguration("alumchat.fun", 5222);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        Connection connection = new XMPPConnection(config);
        try {
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();


        }
        return connection;
    }
    //get all the group invites of the user
    public static void getGroupInvites(Connection connection) {
        try {
            Roster roster = connection.getRoster();
            for (RosterEntry entry : roster.getEntries()) {
                System.out.println(entry.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //accept a group invite
    public static void acceptGroupInvite(Connection connection, String groupName) {
        try {
            Roster roster = connection.getRoster();
            for (RosterEntry entry : roster.getEntries()) {
                if (entry.getName().equals(groupName)) {
                    roster.createEntry(entry.getUser(), entry.getName(), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}

    //print every new group invite
    public static void getGroupInvites(Connection connection, String groupName) {
        try {
            Roster roster = connection.getRoster();
            for (RosterEntry entry : roster.getEntries()) {
                if (entry.getName().equals(groupName)) {
                    System.out.println(entry.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create a listener for that prints the group chat invite name
    public static void createGroupInviteListener(Connection connection) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Presence presence = (Presence) packet;
                System.out.println(presence.getFrom() + " te MANDO ALGO XD");
                if (presence.getType() == Presence.Type.subscribe) {
                    System.out.println(presence.getFrom() + " te ha invitado a un grupo");
                }
            }
        }, new PacketTypeFilter(Presence.class));
    }

}