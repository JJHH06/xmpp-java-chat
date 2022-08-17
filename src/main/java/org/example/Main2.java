// José Javier Hurtarte Hernández
// Universidad del valle de Guatemala
// 16-08-2022
// Java Main for Xmpp client

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
            System.out.println(CONSOLE_REFRESHER + "Welcome to AlumnchatXMPP19707");
            System.out.println("1. Login\n2. Create account\n3. Exit");
            authOption = scanner.nextLine();
            if(authOption.equals("1")){
                System.out.println(CONSOLE_REFRESHER+"Please enter your username:");
                username = scanner.nextLine();
                System.out.println("Please enter your password:");
                password = scanner.nextLine();
                try {
                    Authenticator.login(connection, username, password);
                } catch (XMPPException e) {
                    System.out.println("Login failed. Please try again.");
                }

                if (connection.isAuthenticated()) {
                    System.out.println("Login successful");
                    authenticated = true;
                }else{
                    System.out.println("Login failed. Please try again.");
                    connection = Authenticator.connect("alumchat.fun");
                    connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
                }

            } else if (authOption.equals("2")) {
                System.out.println(CONSOLE_REFRESHER+"Please enter the new username:");
                username = scanner.nextLine();
                System.out.println("Please enter the new password:");
                password = scanner.nextLine();
                System.out.println("Please enter the complete name of the new account:");
                fullname = scanner.nextLine();
                System.out.println("Please enter the email of the new account:");
                email = scanner.nextLine();
                Authenticator.createAccount(connection, username, password, fullname, email);
            } else if (authOption.equals("3")) {
                System.out.println("Ending the program...");
                connection.disconnect();
                System.exit(0);
            } else {
                System.out.println("Invalid option. Please try again.");
            }

        }while (!authenticated);
        System.out.println(CONSOLE_REFRESHER+"¡Welcome " + username + "!");
        String optionMenu = "";


        Hashtable<String, Chat> chats = new Hashtable<>();
        Hashtable<String, ArrayList<String>> chatHistory = new Hashtable<>();


        Hashtable<String, ArrayList<String>> groupChatHistory = new Hashtable<>();

        ArrayList<String> subscriptionRequests = new ArrayList<>();

        recieveMessage(connection, chats, chatHistory, groupChatHistory);

        receiveSubscriptions(connection);
        String chatWith = "";
        String chatOutgoingMessage = "";
        String confirmDelete = "";

        receiveRosterUpdates(connection);


        do {
            System.out.println(CONSOLE_REFRESHER + "Option menu:");
            System.out.println("1. Open chat\n2. Change my presence\n3. Join a group\n4. Chat in a group\n5. Subscribe to users\n6. Accept subscriptions\n7. Information of users\n8. Roster data\n9. Exit");
            System.out.println(connection.getUser() + ">");
            optionMenu = scanner.nextLine();
            if (optionMenu.equals("1")) {
                System.out.println(CONSOLE_REFRESHER + "Enter the name of the user to whom you want to talk:");
                chatWith = scanner.nextLine();
                if (chats.containsKey(chatWith)) {
                    System.out.println(CONSOLE_REFRESHER + "Opening chat con:  " + chatWith);
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
                    System.out.println(CONSOLE_REFRESHER + "Creating a chat with: " + chatWith);
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
                System.out.println(CONSOLE_REFRESHER + "Enter the new status of yout presence:");
                presenceStatus = scanner.nextLine();
                System.out.println("Enter the new type of presence:");
                System.out.println("1. Available\n2. Unavailable");
                presenceTypeOption = scanner.nextLine();
                System.out.println("Enter the new Show status of your presence:");
                System.out.println("1. Available\n2. Away\n3. Free to chat\n4. Do not disturb\n5. Extended away");
                presenceModeOption = scanner.nextLine();

                //Para el tipo de presencia
                if (presenceTypeOption.equals("1")) {
                    presenceType = Presence.Type.available;
                } else if (presenceTypeOption.equals("2")) {
                    presenceType = Presence.Type.unavailable;
                } else {
                    System.out.println("Invalid presence type (available will be used)");
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
                        System.out.println("Presence show status invalid (default will be used)");
                        break;
                }
                changeStatus(connection, presenceType, presenceMode, presenceStatus);

            } else if (optionMenu.equals("3")) {
                System.out.println(CONSOLE_REFRESHER + "Enter the Jabber ID of the groupchat you want to join:");
                groupID = scanner.nextLine();
                joinGroupChat(connection, groupID);
                groupChatHistory.put(groupID, new ArrayList<String>());

            } else if (optionMenu.equals("4")) {
                System.out.println(CONSOLE_REFRESHER + "Enter the Jabber ID of the groupchat you want to talk to:");
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
                    System.out.println("Group chat not found, please join it first\nPress enter to continue...");
                    scanner.nextLine();
                }
                isInsideChat = false;
            } else if (optionMenu.equals("5")) {
                System.out.println(CONSOLE_REFRESHER + "Enter the name of the user to whom you want to subscribe:");
                chatWith = scanner.nextLine();
                subscribeToUser(connection, chatWith);
            } else if (optionMenu.equals("6")) {
                System.out.println("Enter the name of the user that you want to accept his subscription:");
                chatWith = scanner.nextLine();
                acceptSubscription(connection, chatWith);

            }else if (optionMenu.equals("7")) {
                System.out.println(CONSOLE_REFRESHER + "Enter the jabber id of the user you want to know information:");
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

            } else if (optionMenu.equals("--del")) {
                System.out.println(CONSOLE_REFRESHER + "To delete the account:  "+connection.getUser()+" write YES in terminal to confirm");
                confirmDelete = scanner.nextLine();
                if(confirmDelete.equals("YES")){
                    Authenticator.deleteAccount(connection);
                    System.out.println("Account deleted succesfully, please authenticate again");
                    optionMenu = "9";
                }
                else{
                    System.out.println("Account not deleted");
                }
                System.out.println("\n Press enter to continue...");
                scanner.nextLine();


            }


        } while (!optionMenu.equals("9"));
        System.out.println(CONSOLE_REFRESHER + "Leaving the program");
        Authenticator.logout(connection);
        System.exit(0);

    }// end
    //Function that creates a chat
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

    // sends a message but recieves a chat object
    public static void sendMessage(Chat chat, String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    //packet listener that interprets the packets of type message
    public static void recieveMessage(Connection connection, Hashtable<String, Chat> chats, Hashtable<String, ArrayList<String>> chatHistory, Hashtable<String, ArrayList<String>> groupChatHistory) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;




                if (message.getType() == Message.Type.groupchat) {
                    // check if message is not null
                    if (message.getBody() != null && !connection.getUser().split("@")[0].equals(message.getFrom().split("/")[1])) {

                        groupChatHistory.get(message.getFrom().split("/")[0]).add(message.getFrom()+"-> "+message.getBody());
                        if(isInsideChat) {
                            System.out.println(message.getFrom() + "-> " + message.getBody());
                        } else {
                            System.out.println("(@Notification message from group chat: " + message.getFrom().split("/")[0]);
                        }


                    }
                } else{
                    // check if message is not null
                    if (message.getBody() != null) {

                        if (!chats.containsKey(message.getFrom().split("@")[0])) {
                            chatHistory.put(message.getFrom().split("@")[0], new ArrayList<String>(){
                                {

                                }
                            });
                            chats.put(message.getFrom().split("@")[0], createChat(connection, message.getFrom().split("@")[0]+"@alumchat.fun", chatHistory));
                            System.out.println("(@Notification chat created with: "+message.getFrom().split("@")[0] +")");
                        }
                    }
                }

            }
        }, new PacketTypeFilter(Message.class));
    }



    // Method that sends a Presence to a jabber id to join a groupchat
    public static void joinGroupChat(Connection connection, String jid) {
        Presence joinPresence = new Presence(Presence.Type.available);
        joinPresence.setTo(jid+"/"+connection.getUser().split("@")[0]);
        connection.sendPacket(joinPresence);
    }
    // Method that send Message to a jabber id to send a groupchat message
    public static void sendGroupMessage(Connection connection, String jid, String message) {
        Message groupMessage = new Message();
        groupMessage.setTo(jid);
        groupMessage.setBody(message);
        groupMessage.setType(Message.Type.groupchat);
        connection.sendPacket(groupMessage);
    }

    // Method that allows to subscribe to a user
    public static void subscribeToUser(Connection connection, String jid) {
        Presence subscribePresence = new Presence(Presence.Type.subscribe);
        subscribePresence.setTo(jid);
        connection.sendPacket(subscribePresence);
    }
    // Method that allows to accept a subscription request
    public static void acceptSubscription(Connection connection, String jid) {
        Presence subscribePresence = new Presence(Presence.Type.subscribed);
        subscribePresence.setTo(jid);
        connection.sendPacket(subscribePresence);
    }
    // Method that creates a listener to get subscription requests
    public static void receiveSubscriptions(Connection connection) {
        connection.addPacketListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Presence presence = (Presence) packet;
                if (presence.getType() == Presence.Type.subscribe) {
                    System.out.println("@Notificacion: "+presence.getFrom() + " wants to subscribe to you");
                }
            }
        }, new PacketTypeFilter(Presence.class));
    }
    // Method that gets all the users that want to subscribe to me
    public static ArrayList<String> getSubscriptions(Connection connection, RosterPacket.ItemType type) {
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        ArrayList<String> subscriptions = new ArrayList<String>();
        for (RosterEntry entry : entries) {

            if (entry.getType() == type) {
                subscriptions.add(getUserStatus(connection, entry.getUser(), type));
            }


        }
        return subscriptions;

    }


    // Method thats changes status and status message
    public static void changeStatus(Connection connection, Presence.Type type, Presence.Mode mode,String statusMessage) {
        Presence presence = new Presence(type);
        presence.setStatus(statusMessage);
        presence.setMode(mode);
        connection.sendPacket(presence);
    }
    // Method that gets all presence status, presence type and presence mode of a user
    public static String getUserStatus(Connection connection, String jid, RosterPacket.ItemType type) {
        Presence presence = connection.getRoster().getPresence(jid);
        String result = "";
        if (RosterPacket.ItemType.to == type) {
            result = "User: "+jid+"\nType: " + presence.getType();
        } else if (RosterPacket.ItemType.none == type) {
            result = "";
        } else{
            result = "User: "+jid+ "\nType: "+presence.getType() + (presence.getMode() != null?"\nShow: " + presence.getMode():"") + (presence.getStatus()!= null ? "\nStatus: "+presence.getStatus():"");
        }
        return result;
    }
    // Method that shows presence type, and if user is subscribed based on jid
    public static String getUnknownUserStatus(Connection connection, String jid) {
        Presence presence = connection.getRoster().getPresence(jid);
        return "User: "+ jid + "\nPresence: "+presence.getType();
    }
    // Method that creates a roster listener to get roster updates
    public static void receiveRosterUpdates(Connection connection) {
        Roster roster = connection.getRoster();
        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> addresses) {}
            @Override
            public void entriesUpdated(Collection<String> addresses) {
                for( String userId : addresses) {
                    System.out.println("@Notification: "+userId + " has changed its profile information");
                }
            }
            @Override
            public void entriesDeleted(Collection<String> addresses) {}
            @Override
            public void presenceChanged(Presence presence) {
                System.out.println("@Notification: "+presence.getFrom() + " has status updates");
            }
        });
    }



}
