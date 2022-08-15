package org.example;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.ArrayList;
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

        Hashtable<String, Chat> chats = new Hashtable<>();
        Hashtable<String, ArrayList<String>> chatHistory = new Hashtable<>();
        recieveMessage(connection, chats, chatHistory);

        String chatWith = "";
        String chatOutgoingMessage = "";


        do{
            System.out.println(CONSOLE_REFRESHER+"Menú de opciones:");
            System.out.println("1. Abrir chat\n2. Ver estados de amigos\n3. Hablar en un grupo\n4. Aceptar solicitudes de amistad\n5. Aceptar solicitudes de chat en grupo");
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
    public static void recieveMessage(Connection connection, Hashtable<String, Chat> chats, Hashtable<String, ArrayList<String>> chatHistory) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                //chatHistory.add(packet.toXML());
                Message message = (Message) packet;
                //System.out.println(message.getFrom()+ ": " + message.getBody());
                // check if message is from groupchat
                if (message.getType() == Message.Type.groupchat) {
                    // check if message is not null
                    if (message.getBody() != null) {
                        // print the message
                        System.out.println(message.getFrom() + ": GC" + message.getBody());
                        //print the groupchat id
                        System.out.println("Groupchat id: " + message.getFrom().split("/")[0]);
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
                            System.out.println("(@Notificación chat nuevo de: "+message.getFrom().split("@")[0] +")");
                        }
                    }
                }

            }
        }, new PacketTypeFilter(Message.class));
    }

}
