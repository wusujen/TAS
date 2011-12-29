/**
 * chatP5irc example by andreas schlegel
 * this example shows a basic login into an irc server.
 * after connecting to the server was successful, you will
 * join a channel defined in myChannel.
 */
import sojamo.chat.*;
import sojamo.chat.irc.*;



SChat chatP5;
IRCChat irc;

String myChannel = "#Romance";
String myName = "itsmyname";
String myPassword = "";
String myIRCserver = "ircqnet.icq.com";

void setup() {
  size(400,400);
  frameRate(25);
  
  /* instantiate chatP5 */
  chatP5 = new SChat(this);
  
  /* connect to an irc server and login as "myName" */
  irc = chatP5.loginIRC(myIRCserver, myName, myPassword);
}


void draw() {
  background(0);  
}


void keyPressed() {
  switch(key) {
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
