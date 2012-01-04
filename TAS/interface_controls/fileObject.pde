/*==============  fileObject =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class FileObject {
  String name;
  String trigger;
  int w;
  int h;
  int xPos;
  int yPos;
  int scene;
  String transition;

  FileObject(String objFilename, String objTrigger, int objWidth, int objHeight, int objX, int objY, int objScene, String objTransition) {
    name=objFilename;
    trigger=objTrigger;
    w=objWidth;
    h=objHeight;
    xPos=objX;
    yPos=objY;
    scene=objScene;
    transition=objTransition;
  }
  
  void drawFileObject(){
    Button b;
    controlP5.addButton(name,val,xPos,yPos,w,h);
  }
  
  // this function is namely for testing purposes to see
  // the stats of each object
  void displayProperties(){
    println("success");
    println("name: "+name);
    println("trigger: "+trigger);
    println("width: "+w);
    println("height: "+h);
    println("x-position: "+xPos);
    println("y-position: "+yPos);
    println("scene: "+scene);
    println("transition: "+transition);
  }
  
  // this function returns the name of the given object
  String objName(){
    String nameOfObject=name;
    return nameOfObject;
  }
  
  // basic functions for updating the object
  void updateTrigger(String newTrigger){
    trigger=newTrigger;
  }
  void updateSize(int newWidth, int newHeight){
    w=newWidth;
    h=newHeight;
  }
  void updatePosition(int newX, int newY){
    xPos=newX;
    yPos=newY;
  }
  void updateScene(int newSceneNumber){
    scene=newSceneNumber;
  }
  void updateTransition(String newTransition){
    transition=newTransition;
  }
}

