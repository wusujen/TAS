import chatP5.aim.*;
import chatP5.*;

ChatP5 chatP5;
AIMChat aim;

int bg = color(0,0,0);

String myLoginName = "sojamobot";
String myPassword = "sojamobot";

String myBuddyName = "sojamo";

void setup() {
  framerate(25);
  size(400,400);
  // instantiate a new chatP5 object
  chatP5 = new ChatP5(this);
  
  // login to AIM with your loginName and password
  // when logging into AIM, an object of chatP5.aim.AIM will be returned.
  // store this object in a variable.
  // AIM aim.loginAIM(String theLoginName, String thePassword);
  aim = chatP5.loginAIM(myLoginName, myPassword);
}

void draw() {
  background(bg);
}

void mousePressed() {
  // send a message to an AIM buddy
  // void aim.send(String theReceiver, String theMessage);
  aim.sendMessage("timmay2342","some text ..");
}

void keyPressed() {
  switch(key) {
    case('1'):
      aim.removeBuddy(myBuddyName);
      break;
    case('2'):
      aim.addBuddy(myBuddyName,"buddies");
      break;
    case('3'):
      aim.addGroup("newgroup");
      break;
    case('4'):
      aim.removeGroup("newgroup");
      break;
    case('5'):
      aim.removeDenyBuddy(myBuddyName);
      break;
    case('6'):
      aim.removePermitBuddy(myBuddyName);
      break;
    case('7'):
      aim.denyBuddy(myBuddyName);
      break;
    case('8'):
      aim.permitBuddy(myBuddyName);
      break;
    case('a'):
    // set your away status 
    aim.away("your away message");
    break;
    case('A'):
    // tell the AIM server that you are back and available again
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
    // remove a buddy.
    // void aim.removeBuddy(String theBuddyName, String theBuddyGroup)
    aim.removeBuddy(myBuddyName);
    break;
     case('o'):
     // add a buddy.
     // void aim.addBuddy(String[] ListOfBuddyNames, String theGroupName);   
      aim.addBuddy(myBuddyName,"Buddies");
      break;
    case('d'):
      // removes a group from your configuration.  currently only works if group is empty
      aim.removeGroup("AIM Bots");
      break;
    case('c'):
      // removes a group from your configuration.  currently only works if group is empty
      aim.join("udktalk");
      break;
   case('v'):
      aim.sendMessage("udktalk","","some text message to chat");
      break;
   case('m'):
     aim.moveBuddy(myBuddyName,"Family");
     break;
  }
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
  if(theClient==aim) {
    println(theClient+" is connected.");
  }
}

public void chatStatus(ChatStatus theStatus) {
  // each internal message from the server is forwarded to this method
  System.out.println("### STATUS ID: "+theStatus.id()+"  MSG: "+theStatus.message());
}
