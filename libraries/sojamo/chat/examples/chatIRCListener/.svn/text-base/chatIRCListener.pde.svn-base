/**
 * chatP5ircListener example by andreas schlegel
 * this example shows the use of ChatListener.
 * you can create classes that implement a ChatListener
 * that listen for a specific user of channel. 
 * chatMessages and chatStatus will be forwarded to these Classes.
 */
import sojamo.chat.irc.*;
import sojamo.chat.*;


SChat chat;
IRCChat irc;

String myChannel = "#Romance";
String myName = "itsmyname";
String myPassword = "";
String myIRCserver = "ircqnet.icq.com";

void setup() {
  size(400,400);
  frameRate(25);

  /* instantiate chat */
  chat = new SChat(this);

  /* connect to an irc server and login as "myName" */
  irc = chat.loginIRC(myIRCserver, myName, myPassword);
  new TestListener("autoP5user");
}



class TestListener implements ChatListener {
  String myTarget;

  TestListener(String theTarget) {
    myTarget = theTarget;
    irc.addListener(myTarget,this);
  }  

  void chatEvent(ChatMessage theChatMessage) {
    println("got a message @ listener:"+myTarget+" "+theChatMessage.message());
  }

  void chatStatus(ChatStatus theChatStatus) {
    println(theChatStatus);  
  }

}




void draw() {
  background(0);  
}


void keyPressed() {
  switch(key) {
    case('o'):
    println(irc.channel(myChannel).operator());
    break;
    case('s'):
    /* send a message to myChannel */
    irc.channel(myChannel).sendMessage("test");
    break;  
  }  
}


/* chatEvent is called when you received a message 
 * from an irc user or an irc channel
 */
void chatEvent(ChatMessage theMessage) {
  println("got a message from "+theMessage.from()+" : "+theMessage.message());
}

/* chatStatus is called when you received a message from
 * an irc server which is not a text message. a status message comes
 * with different parameters (please see the chatStatus docs)
 * with the ChatStatus.id() method you can identify the numeric
 * type of the message. there are a lot of numerics (http://www.mirc.net/raws/)
 * therefore its up to you how to handle a chatStatus message.
 */
void chatStatus(ChatStatus theStatus) {
  System.out.println(theStatus.toString());
}

/* the chatConnected method is called when you have successfully 
 * connected to an irc server.
 */
void chatConnected(ChatClient theChatClient) {
  irc.join(myChannel);  
}
