// 
// commands available for irc chatting
// http://www.ircle.com/reference/commands.shtml

import sojamo.chat.irc.*;
import sojamo.chat.*;


SChat chat;
IRCChat irc;
String myChannel = "#Romance";
String myCommand ="";
ArrayList myCommandList = new ArrayList();
int myPosition = 1;

PFont font; 

void setup() {
  size(800,200);
  frameRate(25);
  font = loadFont("Georgia-24.vlw"); 
  
  // connect to an irc channel
  chat = new SChat(this);
  irc = chat.loginIRC("ircqnet.icq.com", "itsmyname", "");
  irc.join(myChannel);
  
}


void draw() {
  background(0);
  textFont(font, 24); 
  text("> "+myCommand, 20, 20);
}


void chatEvent(ChatMessage theMessage) {
  println("got a message from "+theMessage.from()+" : "+theMessage.message());
}

void chatStatus(ChatStatus theStatus) {
  switch(theStatus.id()) {
    case(322): // ChatNumerics.RPL_LIST
    String[] s = split(theStatus.message()," ");
    println("channel: "+s[0]+" users:"+s[1]);
    break;
  default:
    println("### "+theStatus.id()+"\t"+theStatus.message());
    break;
  }
}


// keyInput handling
void updateCommand() {
  if(keyCode==UP) {
    if(myCommandList.size()>0 && myPosition>0) {
      myPosition--;
      myCommand = (String)(myCommandList.get(myPosition));
    }
  } 
  else if (keyCode==DOWN) {
    myPosition++;
    if (myPosition>=myCommandList.size()) {
      myPosition = myCommandList.size()-1;
      myCommand = "";
    } 
    else {
      myCommand = (String)(myCommandList.get(myPosition));
    }
  }
  else if (keyCode == DELETE || keyCode == BACKSPACE) {
    String myString = "";
    if (myCommand.length()>0) {
      myString = myCommand.substring(0, myCommand.length()-1);
    }
    myCommand = myString;
  } 
  else if (keyCode == ENTER) {
    if(myCommand.length()>0) {
      irc.channel(myChannel).sendCommand(myCommand);
      myCommandList.add(myCommand);
      myPosition = myCommandList.size();
      myCommand = "";
    }
  } 
  else if (keyCode != SHIFT && keyCode != ALT && keyCode != TAB && keyCode != CONTROL) {
    myCommand += key;
  }
}


void keyPressed() {
  updateCommand(); 
}
