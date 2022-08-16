package org.example;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;

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
        connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
        System.out.println("Connected to XMPP server");
        Scanner scanner = new Scanner(System.in);
        String authOption = "";
        String username = "", password = "";
        String email = "", fullname  = "";
        String groupID = "";

        Presence.Type presenceType = Presence.Type.available;
        String presenceTypeOption = "";

        Presence.Mode presenceMode = Presence.Mode.available;
        String presenceModeOption = "";

        String presenceStatus = "";




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
                    connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
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

        ArrayList<String> subscriptionRequests = new ArrayList<>();

        recieveMessage(connection, chats, chatHistory, groupChatHistory);
        //createGroupInviteListener(connection);
        receiveSubscriptions(connection);
        String chatWith = "";
        String chatOutgoingMessage = "";


        do {
            System.out.println(CONSOLE_REFRESHER + "Menú de opciones:");
            System.out.println("1. Abrir chat\n2. Cambiar mi presencia\n3. Unirme a un grupo\n4. Chatear en grupo\n5. Suscribirme a usuarios\n6. Aceptar suscripciones\n7. Información de un usuario\n8. Datos de mi Rooster\n9. Salir");
            System.out.println(connection.getUser() + ">");
            optionMenu = scanner.nextLine();
            if (optionMenu.equals("1")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese el nombre de usuario del usuario con quien desea hablar:");
                chatWith = scanner.nextLine();
                if (chats.containsKey(chatWith)) {
                    System.out.println(CONSOLE_REFRESHER + "Abriendo chat con:  " + chatWith);
                    // print chat history with chatWith
                    for (String messageIterator : chatHistory.get(chatWith)) {
                        System.out.println(messageIterator);
                    }
                    isInsideChat = true;
                    do {
                        chatOutgoingMessage = scanner.nextLine();
                        if (!chatOutgoingMessage.equals("")) {
                            sendMessage(chats.get(chatWith), chatOutgoingMessage);
                            chatHistory.get(chatWith).add(chatOutgoingMessage);
                        }

                    } while (!chatOutgoingMessage.equals(""));


                } else {
                    System.out.println(CONSOLE_REFRESHER + "Creando un chat con: " + chatWith);
                    chats.put(chatWith, createChat(connection, chatWith + "@alumchat.fun", chatHistory));
                    chatHistory.put(chatWith, new ArrayList<String>());
                    isInsideChat = true;
                    do {
                        chatOutgoingMessage = scanner.nextLine();
                        if (!chatOutgoingMessage.equals("")) {
                            sendMessage(chats.get(chatWith), chatOutgoingMessage);
                            chatHistory.get(chatWith).add(chatOutgoingMessage);
                        }

                    } while (!chatOutgoingMessage.equals(""));

                }
                isInsideChat = false;

            } else if (optionMenu.equals("2")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese el nuevo estado de presencia:");
                presenceStatus = scanner.nextLine();
                System.out.println("Ingrese el nuevo tipo de presencia:");
                System.out.println("1. Available\n2. Unavailable");
                presenceTypeOption = scanner.nextLine();
                System.out.println("Ingrese el nuevo show de presencia:");
                System.out.println("1. Available\n2. Away\n3. Free to chat\n4. Do not disturb\n5. Extended away");
                presenceModeOption = scanner.nextLine();

                //Para el tipo de presencia
                if (presenceTypeOption.equals("1")) {
                    presenceType = Presence.Type.available;
                } else if (presenceTypeOption.equals("2")) {
                    presenceType = Presence.Type.unavailable;
                } else {
                    System.out.println("Tipo de presencia inválida (se usará available)");
                }

                switch (presenceModeOption) {
                    case "1":
                        presenceMode = Presence.Mode.available;
                        break;
                    case "2":
                        presenceMode = Presence.Mode.away;
                        break;
                    case "3":
                        presenceMode = Presence.Mode.chat;
                        break;
                    case "4":
                        presenceMode = Presence.Mode.dnd;
                        break;
                    case "5":
                        presenceMode = Presence.Mode.xa;
                        break;
                    default:
                        presenceMode = Presence.Mode.available;
                        System.out.println("Modo de presencia inválido (se usara default)");
                        break;
                }
                changeStatus(connection, presenceType, presenceMode, presenceStatus);

            } else if (optionMenu.equals("3")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese Jabber ID del group chat que desea entrar:");
                groupID = scanner.nextLine();
                joinGroupChat(connection, groupID);
                groupChatHistory.put(groupID, new ArrayList<String>());

            } else if (optionMenu.equals("4")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese Jabber ID del chat de grupo al que desee chatear:");
                groupID = scanner.nextLine();
                if (groupChatHistory.containsKey(groupID)) {
                    isInsideChat = true;
                    //print everything in the group chat history
                    for (String messageIterator : groupChatHistory.get(groupID)) {
                        System.out.println(messageIterator);
                    }
                    do {
                        chatOutgoingMessage = scanner.nextLine();
                        if (!chatOutgoingMessage.equals("")) {
                            sendGroupMessage(connection, groupID, chatOutgoingMessage);
                            groupChatHistory.get(groupID).add(chatOutgoingMessage);
                        }
                    } while (!chatOutgoingMessage.equals(""));
                } else {
                    System.out.println("No existe el chat de grupo, por favor unirse a este");
                }
                isInsideChat = false;
            } else if (optionMenu.equals("5")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese el nombre de usuario del usuario con quien desea suscribirse:");
                chatWith = scanner.nextLine();
                subscribeToUser(connection, chatWith);
            } else if (optionMenu.equals("6")) {
                System.out.println("Ingrese el nombre de usuario del usuario con quien desea aceptar la suscripción:");
                chatWith = scanner.nextLine();
                acceptSubscription(connection, chatWith);
                //System.out.println(CONSOLE_REFRESHER + "Usuarios con suscripción pendiente:");
                //subscriptionRequests = getSubscriptions(connection);
                //for (String pendUser : subscriptionRequests) {
                //    System.out.println(pendUser);
                //}
                //System.out.println("Ingrese el nombre de usuario del usuario con quien desea aceptar la suscripción:");
                // chatWith = scanner.nextLine();
                //if(subscriptionRequests.contains(chatWith)){
                //acceptSubscription(connection, chatWith);
                //}
                //else{
                //System.out.println("No existe la suscripción pendiente");
                //System.out.println("Presione enter para continuar");
                //scanner.nextLine();
                //}
            }else if (optionMenu.equals("7")) {
                System.out.println(CONSOLE_REFRESHER + "Ingrese el jabber id del usuario que desea conocer información:");
                chatWith = scanner.nextLine();
                System.out.println("\n"+getUnknownUserStatus(connection, chatWith));
                System.out.println("\n Press enter to continue...");
                scanner.nextLine();


            }else if (optionMenu.equals("8")) {
                subscriptionRequests = getSubscriptions(connection, RosterPacket.ItemType.from);
                System.out.println(CONSOLE_REFRESHER + "-Users to whom i am subscribed:");
                for(String userData : subscriptionRequests){
                    System.out.println(userData);
                    System.out.println();

                }
                System.out.println("\n -Users to whom we share a subscription:");
                subscriptionRequests = getSubscriptions(connection, RosterPacket.ItemType.both);
                for(String userData : subscriptionRequests){
                    System.out.println(userData);
                    System.out.println();

                }
                System.out.println("\n -Users to who are subscribed to me:");
                subscriptionRequests = getSubscriptions(connection, RosterPacket.ItemType.to);
                for(String userData : subscriptionRequests){
                    System.out.println(userData);
                    System.out.println();

                }
                System.out.println("\n Press enter to continue...");
                scanner.nextLine();

            }


        } while (!optionMenu.equals("9"));
        System.out.println(CONSOLE_REFRESHER + "Saliendo del programa");
        Authenticator.logout(connection);
        System.exit(0);

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
    // create a listener to get subscription requests
    public static void receiveSubscriptions(Connection connection) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Presence presence = (Presence) packet;
                if (presence.getType() == Presence.Type.subscribe) {
                    System.out.println(" @notificacion: "+presence.getFrom() + " Se quiere suscribir");
                }
            }
        }, new PacketTypeFilter(Presence.class));
    }
    // get all the users that want to subscribe to me
    public static ArrayList<String> getSubscriptions(Connection connection, RosterPacket.ItemType type) {
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        ArrayList<String> subscriptions = new ArrayList<String>();
        for (RosterEntry entry : entries) {
            //if entry is pending
            //entry.getType();
            if (entry.getType() == type) {
                subscriptions.add(getUserStatus(connection, entry.getUser(), type));
            }

            //if (entry.getType() == RosterPacket.ItemType.from) {
            //    subscriptions.add(entry.getUser());
            //}
        }
        return subscriptions;
        //get all the entries that want to subscribe to me

    }


    // change my status and status message
    public static void changeStatus(Connection connection, Presence.Type type, Presence.Mode mode,String statusMessage) {
        Presence presence = new Presence(type);
        presence.setStatus(statusMessage);
        presence.setMode(mode);
        connection.sendPacket(presence);
    }
    // Get all presence status, presence type and presence mode of a user
    public static String getUserStatus(Connection connection, String jid, RosterPacket.ItemType type) {
        Presence presence = connection.getRoster().getPresence(jid);
        //System.out.println(presence.getType() + " " + presence.getMode() + " " + presence.getStatus());
        String result = "";
        if (RosterPacket.ItemType.to == type) {
            result = "User: "+jid+"\nType: " + presence.getType();
        } else if (RosterPacket.ItemType.none == type) {
            result = "";
        } else{
            result = "User: "+jid+ "\nType: "+presence.getType() + (presence.getMode() != null?"\nShow: " + presence.getMode() + (presence.getStatus()!= null ? "\nStatus: "+presence.getStatus():""):"");
        }
        return result;
    }
    // show presence type, and if user is subscribed based on jid
    public static String getUnknownUserStatus(Connection connection, String jid) {
        Presence presence = connection.getRoster().getPresence(jid);
        return "User: "+ jid + "\nPresence: "+presence.getType();
    }



}
