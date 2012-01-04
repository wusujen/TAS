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
}

