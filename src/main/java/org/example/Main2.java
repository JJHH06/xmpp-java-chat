package org.example;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Scanner;

public class Main2 {
    static boolean isInsideChat = false;
    public static void main(String[] args) {
        String CONSOLE_REFRESHER = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n";
        Boolean authenticated = false;
        System.out.println("Alumnchat");
        Connection connection = Authenticator.connect("alumchat.fun");
        System.out.println("Connected to XMPP server");
        Scanner scanner = new Scanner(System.in);
        String authOption = "";
        String username = "", password = "";
        String email = "", fullname  = "";
        String groupID = "";



        do {
            System.out.println(CONSOLE_REFRESHER + "Bienvenido al AlumnchatXMPP19707");
            System.out.println("1. Iniciar sesión\n2. Crear una cuenta\n3. Salir");
            authOption = scanner.nextLine();
            if(authOption.equals("1")){
                System.out.println(CONSOLE_REFRESHER+"Por favor ingrese su username:");
                username = scanner.nextLine();
                System.out.println("Por favor ingrese su password:");
                password = scanner.nextLine();
                try {
                    Authenticator.login(connection, username, password);
                } catch (XMPPException e) {
                    System.out.println("Login failed. Please try again.");
                }
                // if try was successful, print "Login successful"
                if (connection.isAuthenticated()) {
                    System.out.println("Login successful");
                    authenticated = true;
                }else{
                    System.out.println("Login failed. Please try again.");
                    connection = Authenticator.connect("alumchat.fun");
                }

            } else if (authOption.equals("2")) {
                System.out.println(CONSOLE_REFRESHER+"Por favor ingrese el nuevo username:");
                username = scanner.nextLine();
                System.out.println("Por favor ingrese el nuevo password:");
                password = scanner.nextLine();
                System.out.println("Por favor ingrese el nombre completo del usuario:");
                fullname = scanner.nextLine();
                System.out.println("Por favor ingrese el email del usuario:");
                email = scanner.nextLine();
                Authenticator.createAccount(connection, username, password, fullname, email);
            } else if (authOption.equals("3")) {
                System.out.println("Saliendo...");
                connection.disconnect();
                System.exit(0);
            } else {
                System.out.println("Opción inválida");
            }

        }while (!authenticated);
        System.out.println(CONSOLE_REFRESHER+"¡Bienvenido " + username + "!");
        String optionMenu = "";

        if (authenticated) {
            System.out.println("Usuario autenticado");
        } else {
            System.out.println("Usuario no autenticado");
        }
        //Chat chatprueba = createChat(connection, "jjhh2@alumchat.fun");

        //sendMessage(chatprueba, "Hola soy un mensaje ya bueno xd");
        //Chats normales
        Hashtable<String, Chat> chats = new Hashtable<>();
        Hashtable<String, ArrayList<String>> chatHistory = new Hashtable<>();

        //Chats de grupo
        //Hashtable<String, Chat> groupChats = new Hashtable<>();
        Hashtable<String, ArrayList<String>> groupChatHistory = new Hashtable<>();

        recieveMessage(connection, chats, chatHistory, groupChatHistory);
        //createGroupInviteListener(connection);

        String chatWith = "";
        String chatOutgoingMessage = "";


        do{
            System.out.println(CONSOLE_REFRESHER+"Menú de opciones:");
            System.out.println("1. Abrir chat\n2. Ver estados de amigos\n3. Unirme a un grupo\n4. Chatear en grupo\n5. Suscribirme a usuarios\n6. Salir");
            System.out.println(connection.getUser() + ">");
            optionMenu = scanner.nextLine();
            if(optionMenu.equals("1")){
                System.out.println(CONSOLE_REFRESHER+"Ingrese el nombre de usuario del usuario con quien desea hablar:");
                chatWith = scanner.nextLine();
                if(chats.containsKey(chatWith)) {
                    System.out.println(CONSOLE_REFRESHER + "Abriendo chat con:  " + chatWith);
                    // print chat history with chatWith
                    for (String messageIterator : chatHistory.get(chatWith)) {
                        System.out.println(messageIterator);
                    }
                    isInsideChat = true;
                    do{
                        chatOutgoingMessage = scanner.nextLine();
                        if(!chatOutgoingMessage.equals("")){
                            sendMessage(chats.get(chatWith), chatOutgoingMessage);
                            chatHistory.get(chatWith).add(chatOutgoingMessage);
                        }

                    }while(!chatOutgoingMessage.equals(""));



                } else {
                    System.out.println(CONSOLE_REFRESHER + "Creando un chat con: " + chatWith);
                    chats.put(chatWith, createChat(connection, chatWith+"@alumchat.fun", chatHistory));
                    chatHistory.put(chatWith, new ArrayList<String>());
                    isInsideChat = true;
                    do{
                        chatOutgoingMessage = scanner.nextLine();
                        if(!chatOutgoingMessage.equals("")){
                            sendMessage(chats.get(chatWith), chatOutgoingMessage);
                            chatHistory.get(chatWith).add(chatOutgoingMessage);
                        }

                    }while(!chatOutgoingMessage.equals(""));

                }
                isInsideChat = false;

            }
            if(optionMenu.equals("3")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese Jabber ID del group chat que desea entrar:");
                groupID = scanner.nextLine();
                joinGroupChat(connection, groupID);
                groupChatHistory.put(groupID, new ArrayList<String>());

            }
            else if(optionMenu.equals("4")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese Jabber ID del chat de grupo al que desee chatear:");
                groupID = scanner.nextLine();
                if(groupChatHistory.containsKey(groupID)){
                    isInsideChat = true;
                    //print everything in the group chat history
                    for (String messageIterator : groupChatHistory.get(groupID)) {
                        System.out.println(messageIterator);
                    }
                    do{
                        chatOutgoingMessage = scanner.nextLine();
                        if(!chatOutgoingMessage.equals("")){
                            sendGroupMessage(connection, groupID, chatOutgoingMessage);
                            groupChatHistory.get(groupID).add(chatOutgoingMessage);
                        }
                    }while(!chatOutgoingMessage.equals(""));
                }
                else{
                    System.out.println("No existe el chat de grupo, por favor unirse a este");
                }
                isInsideChat = false;
            } else if(optionMenu.equals("5")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese el nombre de usuario del usuario con quien desea suscribirse:");
                chatWith = scanner.nextLine();
                subscribeToUser(connection, chatWith);
            }


        } while (!optionMenu.equals("0"));





    }// end
    public static Chat createChat(Connection connection, String username, Hashtable<String, ArrayList<String>> chatHistory) {
        Chat chat = connection.getChatManager().createChat(username, new MessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                // if message.getBody() is not null, then print the message

                if (message.getBody() != null) {

                    chatHistory.get(message.getFrom().split("@")[0]).add(message.getFrom().split("@")[0]+"-> "+message.getBody());
                    if(isInsideChat){
                        System.out.println(message.getFrom().split("@")[0]+"-> "+message.getBody());
                    }

                }

            }
        });
        return chat;
    }
    public static void sendMessage(Chat chat, String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    public static void recieveMessage(Connection connection, Hashtable<String, Chat> chats, Hashtable<String, ArrayList<String>> chatHistory, Hashtable<String, ArrayList<String>> groupChatHistory) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                //chatHistory.add(packet.toXML());
                Message message = (Message) packet;
                //System.out.println(message.getFrom()+ ": " + message.getBody());
                // check if message is from groupchat
                //System.out.println(message.getFrom() + "EEEELTIPO"+ ": " + message.getBody());
                // get the jid of the message invite

                //accept conference invitation




                if (message.getType() == Message.Type.groupchat) {
                    // check if message is not null
                    if (message.getBody() != null && !connection.getUser().split("@")[0].equals(message.getFrom().split("/")[1])) {
                        // print the message
                        groupChatHistory.get(message.getFrom().split("/")[0]).add(message.getFrom()+"-> "+message.getBody());
                        if(isInsideChat) {
                            System.out.println(message.getFrom() + "-> " + message.getBody());
                        } else {
                            System.out.println("(@Notificación Mensaje chat de grupo: " + message.getFrom().split("/")[0]);
                        }


                    }
                } else{
                    // check if message is not null
                    if (message.getBody() != null) {
                        // print the message
                        if (!chats.containsKey(message.getFrom().split("@")[0])) {
                            chatHistory.put(message.getFrom().split("@")[0], new ArrayList<String>(){
                                {
                                    add(message.getFrom().split("@")[0]+"-> "+message.getBody());
                                }
                            });
                            chats.put(message.getFrom().split("@")[0], createChat(connection, message.getFrom().split("@")[0]+"@alumchat.fun", chatHistory));
                            System.out.println("(@Notificación chat creado de: "+message.getFrom().split("@")[0] +")");
                        }
                    }
                }

            }
        }, new PacketTypeFilter(Message.class));
    }

    // get all my contact status
    public static void getContactStatus(Connection connection) {
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getUser());
            System.out.println(entry.getUser() + " " + presence.getType());
        }
    }
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
    // send a Presence to a jabber id to join a groupchat
    public static void joinGroupChat(Connection connection, String jid) {
        Presence joinPresence = new Presence(Presence.Type.available);
        joinPresence.setTo(jid+"/"+connection.getUser().split("@")[0]);
        connection.sendPacket(joinPresence);
    }
    // send Message to a jabber id to send a groupchat message
    public static void sendGroupMessage(Connection connection, String jid, String message) {
        Message groupMessage = new Message();
        groupMessage.setTo(jid);
        groupMessage.setBody(message);
        groupMessage.setType(Message.Type.groupchat);
        connection.sendPacket(groupMessage);
    }

    // subscribe to a user
    public static void subscribeToUser(Connection connection, String jid) {
        Presence subscribePresence = new Presence(Presence.Type.subscribe);
        subscribePresence.setTo(jid);
        connection.sendPacket(subscribePresence);
    }
    // accept a subscription request
    public static void acceptSubscription(Connection connection, String jid) {
        Presence subscribePresence = new Presence(Presence.Type.subscribed);
        subscribePresence.setTo(jid);
        connection.sendPacket(subscribePresence);
    }
    
}
