import chatP5.jabber.*;
import chatP5.*;

ChatP5 chatP5;
Jabber jabber;

int bg = color(0,0,0);

String myLoginName = "sojamobot";
String myPassword = "omasoj";
String myServer = "jabber.org";
String myChannel = "conference";

void setup() {
  framerate(25);
  size(400,400);
  // instantiate a new chatP5 object
  chatP5 = new ChatP5(this);
  println("chatP5 version "+chatP5.version());
  
  // login to AIM with your loginName and password
  // when logging into AIM, an object of chatP5.aim.AIM will be returned.
  // store this object in a variable.
  // Jabber chatP5.loginJabber(String theLoginName, String thePassword,String theServer);
  jabber = chatP5.loginJabber(myLoginName, myPassword,myServer);
}

void draw() {
  background(bg);
}

void mousePressed() {
  // send a message to an AIM buddy
  // void aim.send(String theReceiver, String theMessage);
  jabber.chat("sojamo").send("message content");
}

void keyPressed() {
  /*
  switch(key) {
    case('a'):
    // set your away status 
    aim.away("your away message");
    break;
    case('A'):
    // tell the AIM server that you are back and available
    aim.back();
    break;
    case('g'):
    // returns all groups contained in your configuration
    // aim.groups() return ChatGroup[]
    println(aim.groups());
    break;
    case('b'):
    // returns all buddies contained in your configuration
    // ChatBuddy[] aim.buddies();
    println(aim.buddies()); 
    break;
    case('p'):
    AIMBuddy myBuddy;
    // remove a buddy.
    // void aim.removeBuddy(String theBuddyName, String theBuddyGroup)
    myBuddy = aim.buddy("sojamobot");
    aim.removeBuddy(myBuddy.name());
    break;
     case('o'):
     // add a buddy.
     // void aim.addBuddy(String[] ListOfBuddyNames, String theGroupName);   
      aim.addBuddy(new String[] {"sojamo"},"Buddies");
      break;
    case('d'):
      // removes a group from your configuration.  currently only works if group is empty
      aim.removeGroup("AIM Bots");
      break;
    case('c'):
      // removes a group from your configuration.  currently only works if group is empty
      aim.join("testchatp5");
      break;
   case('v'):
      aim.send("testchatp5","","message content chat");
      break;
   case('m'):
     aim.moveBuddy("sojamo","friends");
     break;
  }
  */
}


public void chatEvent(ChatMessage theMessage) {
  // each incoming message from another aim user that is addressed to you,
  // is automatically forwarded to this method if available in your processing sketch. 
  // server messages are forwarded to method chatEvent. see below.
  
  /*
  a ChatMessage has the following methods:
  String message(); // the message that has been sent to you
  int type(); // type is an int of  ChatMessage.DEFAULT, ChatMessage.PRIVATE, or ChatMessage.CHAT
  String from(); // the buddy name of the sender
  String channel(); // if the message was sent from a chatroom, its name will be returned. if not "" will be returned
  AbstractChatP5 connection(); // returns the instance of an Object created with loginAIM
  */
  
  // aim messages are html formated you can remove the html with ChatP5.removehtml(theString)
  String myMessage = ChatP5.removehtml(theMessage.message());
  
  System.out.println("received FROM:"+theMessage.from()+
    "\t@ "+ theMessage.channel() + 
    "\tMSG:"+myMessage);
    
    
  if(myMessage.indexOf("green")!=-1) {
    bg = color(0,255,0);
  } 
  else if (myMessage.indexOf("red")!=-1) {
    bg = color(255,0,0);
  } 
  else if (myMessage.indexOf("white")!=-1) {
    bg = color(255,255,255);
  }  
  else if (myMessage.indexOf("blue")!=-1) {
    bg = color(0,0,255);
  } 
  else {
    bg = color(0,0,0);
  }
}


public void chatConnected(ChatClient theClient) {
  if(theClient==jabber) {
    println(theClient+" is connected.");
    //aim.join("testchatp5");
  }
}

public void chatStatus(ChatStatus theStatus) {
  // each incoming message from the server is forwarded to this method
  System.out.println("### STATUS TYPE:"+theStatus.type()+"  MSG: "+theStatus.message());
}
