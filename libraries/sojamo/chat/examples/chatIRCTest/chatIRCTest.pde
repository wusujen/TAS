import sojamo.chat.irc.*;
import sojamo.chat.*;



SChat chat;
IRCChat irc;
int bg = color(0,0,0);
String myChannel = "#Romance";
String myName = "itsmyname"; //"guest_" + (int) (Math.random() * 1000 + 2000);
String myPassword = "";
void setup() {
  frameRate(25);
  size(400,400);
  chat = new SChat(this);
  irc = chat.loginIRC("ircqnet.icq.com",myName, myPassword); // ircqnet.icq.com
}

void draw() {
  background(bg);
}

void keyPressed() {
  switch(key) {
    case('1'):
      //irc.serv().nick().register(myPassword,"news@sojamo.de");
      break;
    case('2'):
      //irc.serv().nick().identify(myPassword);
      break;
    case('3'):
      //irc.serv().nick().drop(myPassword);
      break;
    case('4'):
      irc.join("#peerage");
      break;
    case('9'):
      irc.chanserv().register("peerstest","sreep","some description about peers");
      break;
    case('h'):
      irc.sendCommand("msg NickServ HELP DROP");
      break;
  }
}



void mousePressed() {
  irc.channel(myChannel).sendMessage("hi ..");  
}
  
  

void chatEvent(ChatMessage theMessage) {

  String myMessage = SChat.removehtml(theMessage.message());

  System.out.println("received FROM:"+theMessage.from()+
    " @ "+ theMessage.channel() + 
    " MSG:"+myMessage);
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
  } else if (myMessage.indexOf("hi")!=-1) {
    bg = color(0,255,0);
  } else if (myMessage.indexOf("lol")!=-1) {
    bg = color(255,0,0);
  } 
  else {
    bg = color(0,0,0);
  }
}


public void chatStatus(ChatStatus theStatus) {
  System.out.println("### EVENT TYPE:"+theStatus.type()+"  MSG: "+theStatus.message());
}
