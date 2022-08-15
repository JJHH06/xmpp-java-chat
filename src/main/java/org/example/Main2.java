package org.example;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.Scanner;

public class Main2 {
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





        if (authenticated) {
            System.out.println("Usuario autenticado");
        } else {
            System.out.println("Usuario no autenticado");
        }
        Chat chatprueba = createChat(connection, "jjhh2@alumchat.fun");

        sendMessage(chatprueba, "Hola soy un mensaje ya bueno xd");

    }
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
    public static void sendMessage(Chat chat, String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
