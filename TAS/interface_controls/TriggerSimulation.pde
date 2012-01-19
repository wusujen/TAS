class TriggerSimulation {
  
  
void update() {
  if(keyPressed) {
    if(key == 'q'){
      //send osc for trigger 0
      checkTrigger(0);
    }
  }
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
     // setting address (static)
     // add properties to constriuct the data
   // actually send the message
}
  
}


