/**
 * chatirc example by andreas schlegel
 * this example shows a basic login into an irc server.
 * after connecting to the server was successful, you will
 * join a channel defined in myChannel.
 */
import sojamo.chat.irc.*;
import sojamo.chat.*;


SChat chat;
IRCChat irc;

String myChannel = "#20_something";
String myName = "guest_"+((int)random(2000)+1000);
String myPassword = "";
String myIRCserver = "ircqnet.icq.com";

Hashtable myUsers = new Hashtable();

ArrayList myRemoveList = new ArrayList();
ArrayList myAddList = new ArrayList();

int cnt = 0;

void setup() {
  size(400,400);
  frameRate(25);

  /* instantiate chat */
  chat = new SChat(this);

  /* connect to an irc server and login as "myName" */
  irc = chat.loginIRC(myIRCserver, myName, myPassword);
  smooth();
  noStroke();
}


void draw() {
  background(0);
  updateUsers();
  cnt++;
  rect(cnt,0,10,10);
  cnt %= width;

}

void keyPressed() {
  switch(key) {
    case('s'):
    /* send a message to myChannel */
    irc.channel(myChannel).sendMessage("test");
    break;  
  }  
}



void updateUsers() {
String name;
  MyUser value;
  for (Enumeration e=myUsers.keys(); e.hasMoreElements();) {
    name = (String)e.nextElement();
    value = (MyUser)myUsers.get(name);
    value.draw();
  }
}



class MyUser implements ChatListener {
  IRCUser myUser;
  int myX = (int)random(width);
  int myY = (int)random(height);
  int myColor = 255;
  float mySize = 1;
  float myCurrentSize = 20;
  
  public MyUser(IRCUser theUser) {
    myUser = theUser;
    irc.addListener(myUser.name(),this);
    myUsers.put(myUser.name(), this);
  }

  void draw() {
    fill(255,255,255,myColor);
    ellipse(myX,myY,myCurrentSize,myCurrentSize);
    myColor += (128 - myColor)*0.04;
    myCurrentSize += (mySize-myCurrentSize)*0.04;
  }
  public void chatEvent(ChatMessage theMessage) {
    println(myUser.name()+" : "+theMessage.message());
    myColor = 255;
    mySize++;
    myCurrentSize = 40;
  }

  public void chatStatus(ChatStatus theStatus) {
    switch(theStatus.id()) {
      case(ChatNumerics.QUIT):
      case(ChatNumerics.PART):
      case(ChatNumerics.KICK):
      irc.removeListener(this);
      myUsers.remove(myUser.name());
      break;
      case(ChatNumerics.NICK):
      println(theStatus.message());
      break;
    }
  }
}


/* chatEvent is called when you received a message 
 * from an irc user or an irc channel
 */
void chatEvent(ChatMessage theMessage) {
  println("got message from "+theMessage.from()+" : "+theMessage.message());
}

/* chatStatus is called when you received a message from
 * an irc server which is not a text message. a status message comes
 * with different parameters (please see the chatStatus docs)
 * with the ChatStatus.id() method you can identify the numeric
 * type of the message. there are a lot of numerics (http://www.mirc.net/raws/)
 * therefore its up to you how to handle a chatStatus message.
 */
void chatStatus(ChatStatus theStatus) {
  if(theStatus.id()==ChatNumerics.RPL_ENDOFNAMES) {
    String[] s = irc.channel(myChannel).userlist(); 
    for(int i=0;i<s.length;i++) {
      addToList(s[i]);
    }
  } 
  else if(theStatus.id()==ChatNumerics.JOIN) {
    addToList(theStatus.user());
  }
}

void addToList(String theUserName) {
  if(!myUsers.containsKey(theUserName)) {
    MyUser myUser = new MyUser(irc.channel(myChannel).user(theUserName));
  }
}


/* the chatConnected method is called when you have successfully 
 * connected to an irc server.
 */
void chatConnected(ChatClient theChatClient) {
  irc.join(myChannel);  
}
