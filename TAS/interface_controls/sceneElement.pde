/*==============  sceneElement =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class SceneElement {
  String name;
  //String transition;
  //int trigger; this int is no longer in use. Please refer to triggerList below
  float w;
  float h;
  int xPos;
  int yPos;
  String scene;

  boolean bActivate;
  int mStartX, mStartY;  //store the position of the mouse when mouse has been pressed
  int mEndX, mEndY;      //store the position of the mouse when mouse has been released
  

  CloseButton c;
  ArrayList triggerList; //this arraylist holds a set of integers, each corresponding to a trigger that has been turned "on".
                         //the max number is 9.
                         
  public PImage myPImage;  //this will hold the image associated with the sceneElement
  

  //SceneElement(String objFilename, int objWidth, int objHeight, int objX, int objY, int objScene, ArrayList objTriggers) {
  //SceneElement(int objHash, String objFilename, int objTrigger, float objWidth, float objHeight, int objX, int objY, int objScene, String objTransition) {
    SceneElement(String objFilename, float objWidth, float objHeight, int objX, int objY, String objScene,  ArrayList objTriggers) {
    //hash=objHash;
    name=objFilename;
    w=objWidth;
    h=objHeight;
    xPos=objX;
    yPos=objY;
    scene=objScene;
    triggerList = objTriggers;

    mEndX = 0;
    mEndY = 0;
    bActivate = false;
    mStartX = 0;
    mStartY = 0;
    c=new CloseButton(int(w)+xPos,yPos,1,1,18);
    
    // to select which image should be displayed, figure out
    // which type of file it is, and then based upon that get
    // the correct image
    // check for acceptable image formats
    if( (name.endsWith("png")) || (name.endsWith("jpg")) || (name.endsWith("jpeg")) || name.endsWith("gif") ){ 
      println("image");
      myPImage=loadImage(sketchPath+"/media/"+name);
    }
    // check for accetable audio formats
    else if( (name.endsWith("aif")) || (name.endsWith("mp3")) ){  
      println("audio");
      myPImage=loadImage(sketchPath+"/data/audio_icon.png");
    }
    // check for acceptable movie formats
    else if( (name.endsWith("mov")) || (name.endsWith("mp4")) ){  
      myPImage=loadImage(sketchPath+"/data/video_icon.png");
    }
    else{
      println("this is an unacceptable file");
    }
  }
  
  float scaleFactor(){
     return(h/float(myPImage.height));
  }
  
  void drawSceneElement(){   
    fill(0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    
    pushMatrix();
    if (this == activeElement){
      translate(xPos,yPos, 1);
    }
    else{
      translate(xPos,yPos, 0);
    }
      scale(scaleFactor());
      image(myPImage,0,0);
      popMatrix();
  }
  
  
  // this changes the visual appearance of the object
  void hasBeenSelected(){
    fill(255,0,0);
    textSize(10);
    textAlign(LEFT,BOTTOM);
    text(name,xPos,yPos);
    c.drawCloseButton();
  }
  
  // this function is namely for testing purposes to see
  // the stats of each object
  void displayProperties(){
    println("success");
    println("name: "+name);
    println("width: "+w);
    println("height: "+h);
    println("x-position: "+xPos);
    println("y-position: "+yPos);
    println("triggers: " + triggerList);
  }
  
  // this function returns the name of the given object
  String objName(){
    String nameOfObject=name;
    return nameOfObject;
  }

  void updateWidth(float newWidth){
    w=newWidth;
    h=float(myPImage.height)*newWidth/float(myPImage.width);
    println("name: " + activeElement.objName() + " w: " + w + " h: " + h);
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + int(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  void updateHeight(float newHeight){
    h=newHeight;
    w=float(myPImage.width)*newHeight/float(myPImage.height);
    println("name: " + activeElement.objName() + " w: " + w + " h: " + h);
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + int(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  void updateX(int newX){
    xPos=newX;
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + int(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  void updateY(int newY) {
    yPos=newY;
    //include the closeButton with its sceneElement
    activeElement.c.xPos = activeElement.xPos + int(activeElement.w);
    activeElement.c.yPos = activeElement.yPos;
  }
  
  // this boolean returns true if mouse is over the sceneElement
  boolean isMouseOver(int mX, int mY){
    if((mX>xPos) && (mX<xPos+w) && (mY>yPos) && (mY<yPos+h)){
      return true;
    }
    return false;
  }
  
  boolean isHoveringOverCloseButton(int mX, int mY){
    if(c.isMouseOver(mX,mY)){
      return true;
    }
    return false;
  }
  
  String getMyName(){
    return name;
  }
  int getMyX(){
    return xPos;
  }
  int getMyY(){
    return yPos;
  }
  float getMyWidth(){
    return w;
  }
  float getMyHeight(){
    return h;
  }
  ArrayList getMyTriggerList(){
    return triggerList;
  }
}
