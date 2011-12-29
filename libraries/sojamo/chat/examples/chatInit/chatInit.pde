import chatP5.aim.*;
import chatP5.irc.*;
import chatP5.*;


ChatP5 chatP5;

void setup() {
  frameRate(25);
  chatP5 = new ChatP5(this);
}


void draw() {
  background(0);
}


void chatEvent(ChatMessage theMessage) {
  println("received a message.");
}


void chatConnected(ChatClient theClient) {
  if(theClient==aim) {
    println(theClient+" is an AIM client and is connected.");
    //aim.join("testchatp5");
  }
}


void chatStatus(ChatStatus theStatus) {
  println("reveived a chat status message.");
}

