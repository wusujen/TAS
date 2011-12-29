import chatP5.aim.*;
import chatP5.*;

ChatP5 chatP5;
AIM aim;

int bg = color(0,0,0);

String myLoginName = "sojamobot";
String myPassword = "sojamobot";
String myChat = "sojamochat";

void setup() {
  framerate(25);
  size(400,400);
  // instantiate a new chatP5 object
  chatP5 = new ChatP5(this);
  
  // login to AIM with your loginName and password
  // when logging into AIM, an object of chatP5.aim.AIM will be returned.
  // store this object in a variable.
  aim = chatP5.loginAIM(myLoginName, myPassword);
}

void draw() {
  background(bg);
}

void mousePressed() {
  // send a message to an AIM chat channel
  aim.send(myChat,"","some text ..");
}

void keyPressed() {
  switch(key) {
    case('a'):
    // set your away status 
    aim.away("your away message");
    break;
    case('A'):
    // tell the AIM server that you are back and available
    aim.back();
    break;
    case('u'):
    // returns all users in a chatroom
    println(aim.channel(myChat).users()); 
    break;
    case('s'):
    aim.send("sojamo","hello");
    break;
  }
}


// chatConnected is called by chatP5 when a connection to a server was successful.
// this is when you can e.g. automatically join a chat channel .
public void chatConnected(ChatClient theClient) {
  if(theClient==aim) {
    println(theClient+" is connected.");
    aim.join(myChat);
  }
}

public void chatEvent(ChatMessage theMessage) {
  // aim messages are html formated you can remove the html with ChatP5.removehtml(theString)
  String myMessage = ChatP5.removehtml(theMessage.message());
  
  System.out.println("received FROM:"+theMessage.from()+
    "\t@ "+ theMessage.channel() + 
    "\tMSG:"+myMessage);    
}



public void chatStatus(ChatStatus theStatus) {
  // each incoming message from the server is forwarded to this method
  System.out.println("### EVENT TYPE:"+theStatus.type()+"  MSG: "+theStatus.message());
}
