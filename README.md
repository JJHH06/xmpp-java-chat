# Java Xmpp Chat Client
#### Author: [@JJHH06](https://github.com/JJHH06)
This is an XMPP protocol chat client for made with Java. Made for the Networks class at the Universidad Del Valle de Guatemala.
## Functionalities
- [x] Create a new account
- [x] Login to an existing account
- [x] Logout
- [x] Delete an account
- [x] Show all contacts information
- [x] Show information about a specified user
- [x] Subscribe to a user
- [x] Accept a user subscription
- [x] Create a chat with an user
- [x] Send a message to a User chat
- [x] Join a group chat
- [x] Send a message to a group chat
- [x] Change my presence status
- [x] Notifications of new messages and subscriptions
- [ ] Send a file to a User chat(pending)
# Usage
This is a project made with maven, son in order to install the dependencies you need to have Maven installed and run the command:
```
nvm install
```
Or use the generated .jar file

# Authentication
You need to have an account to authenticate. But you can also create an account through the application.

## Login
In order to correctly login, when your username is asked, type your username in the following format:
```
<username>@<domain of XMPP chat Server>
```
When your password is asked, type your password normal and press enter to try to login.  

Note: _You will only go through the authentication phase if your credentials exist or are written correctly_.

## Create an Account
In the Authentication you can create an account that you can use to authenticate in the login. For this when asked, you will need to enter a new username, password, complete name and email.
The restrictions for each field are the following:
- Username: it has to be only the username, without the @<XMPP server adress>, that you want your account to have.
- Password: it has to be a normal password, but have in mind that it can be seen through the clien interface.
- Complete Name: it has to be a complete name, that you want your account to have.
- Email: it has to be a valid email, that you want your account to have. It has the same sintax as the one used at the username in the authentication login phase.

# Chat functionalities
Once you are logged in, you can start using the rest of the features implemented in the client.

## Notifications
You can see the notifications of new messages and subscriptions in the client interface. Some of them, like the ones from chats, are shown in the system tray only if you are not inside a conversation.  
All the notifications will appear in the system tray with the following sintax:
```
@Notification: <message of notification>
```

## Chat with an user
There are 2 ways to create a chat with an user, the first one is if the user sent you a Message and there was no conversation yet, a chat with him will be created and the conversations with him will be stored for the sesion.  
  
The second way is to go in the option 1 of the menu and select type the username of the user you want to start a chat with. **Important: this username has to be only the username without the <@XMPP server address> part**. If the chat was already created and you do enter the username, you will be redirected to the chat and see the previous messages.

#### Send Messages
You can send messages to an user by being inside the chat typing the message and pressing enter.

#### Exit Chat
You can exit the chat by pressing enter without typing anything. After that you will be redirected to the main menu.

## Change my presence
You can change your presence status by going to the option 2 of the menu. The steps after that are the following:

1. Enter the status you want to have. This could be any text you want to write about how you are doing.
2. Enter the type of the presence you want to have. This could be:
    - Available: you are available to chat with other users.
    - Unavaliable: you are not available to chat with other users. 
    - 
   Take in mind that if you are unavailable, you will not be able to chat with other users. Also if your option is invalid, your Type will be set to Available.
3. Enter your presence Mode. This could be:
    - Available
    - Away
    - Free to chat
    - Do not disturb
    - Extended Away  
If you do not enter any of the above options, your Mode will be set to Available.

## Group chat
You can join a group chat by going to the option 3 of the menu. For this you need the following:
1. Have been previously invited to the group chat you want to join or make sure the group chat is public.
2. Have the group chat's JID.

If you have the above information, you can join the group chat by typing the Jabber Id of the group in console. If this is successful, you will receive all the previous messages from that group chat and its associated notifications in the command line interface.  
  
#### Chat in a group
If and only if you already joined a group chat you will be able to enter the group chat through option 4 in the menu.  
The rules for this messaging and exit this chat are the same as for the individual chat.

## Accept User Subscription
When users whant to see your presence status and the updates you make to it. A notification will pop up saying wich user wants to suscribe to you.  
To accept this subscription request, you need to enter the option 6 in the menu and insert the Jabber Id of the user that sent you the subscription request. This JID needs to be entered as following:  

```
<username>@<domain of XMPP chat Server>
```

## Subscribe to an user  
If you want to receive Presence updates of an user and add it to your roster, you need to subscribe to them.
to do this you need to enter the option 6 and enter the Jabber id of the user to whom you want to subscribe, this id needs to be entered as following: 
```
<username>@<domain of XMPP chat Server>
```
Once they accept your subscription request, and if they are not previously subscribed to you, they will appear in the chat Roster.

## Roster information
if you want to see the current presence of all of your contacts in roster, you need to enter the option 8 at the menu.  
  

It will first show status, type, and mode of the users that have subscription of type from, then of the ones that have subscription of the tipe both, and finally the subscription of the type to. Take in mind that you cannot see the full information of disconnected users and of the ones with subscription of type to because you dont have the clearance to do so.

## Information of User
In case you need to know information of an user thats not on your roster, use the option 7 of the menu.

To do this you need to enter the jabber id of the user you want to search, like the following: 
```
<username>@<domain of XMPP chat Server>
```
 This will print the Jabber ID of the user and its presence type.  
  
Take in mind that unless you are subscribed to this user and if this user is connected you will se always the status of the searched user as Unavaliable.

## Logout
To logout and close the connection you need to enter the option 9 of the menu.
