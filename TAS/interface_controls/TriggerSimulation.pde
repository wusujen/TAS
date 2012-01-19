import oscP5.*;
import netP5.*;

class TriggerSimulation {
  OscP5 oscP5;
  NetAddress myRemoteLocation;
  
  TriggerSimulation() {
    // setup osc listener with Receiving Port
    oscP5 = new OscP5(this, 31842);
    // setup Remote Location with sending port
    myRemoteLocation = new NetAddress("128.61.24.210", 31841);
  }
  
void triggerEvent() {
    if(key == 'q') { checkTrigger(0); }
    if(key == 'w') { checkTrigger(1); }
    if(key == 'e') { checkTrigger(2); }
    if(key == 'a') { checkTrigger(3); }
    if(key == 's') { checkTrigger(4); }
    if(key == 'd') { checkTrigger(5); }
    if(key == 'z') { checkTrigger(6); }
    if(key == 'x') { checkTrigger(7); }
    if(key == 'c') { checkTrigger(8); }
}

  
void checkTrigger(int trigger) {
   // loop through all SceneElements that have the scene property of the current scene
  for (int i=0; i< sceneElementArray.size(); i++) {
    SceneElement element =(SceneElement) sceneElementArray.get(i);
    // per scene element loop through all trigger (0-8) 
    if (element.scene.equals(activeScene)) {
      for(int n = 0; n<element.triggerList.size();n++){
        int myTrigger = (Integer)element.triggerList.get(n);
        // per trigger, check if it equals int trigger
        if(myTrigger == trigger) {
          // send sceneElement to sendOSC
          sendOSC(element);
        }
      }
    } 

  }
}

void sendOSC(SceneElement element) {
  println(element.name + "osc sent");
   // construct osc message consisting of
   OscMessage myMessage = new OscMessage("/filename/xpos/ypos/width/height");
   myMessage.add(element.name);
   myMessage.add(element.xPos - canvasX);
   myMessage.add(element.yPos - canvasY);
   myMessage.add(element.w);
   myMessage.add(element.h);
   oscP5.send(myMessage, myRemoteLocation);
     // setting address (static)
     // add properties to constriuct the data
   // actually send the message
}
  
void oscEvent(OscMessage theOscMessage) {
  println("yea!");
  Object[] myData=  theOscMessage.arguments();
  //println(myData.length);
    for(int i=1; i < myData.length; i++) {
      //int trig=(Integer)myData[1];
      println((Integer)myData[i]);
      checkTrigger((Integer)myData[i-1]);
    }
}  
  
  
}


