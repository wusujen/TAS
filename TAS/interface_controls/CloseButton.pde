class CloseButton{
  int xPos;
  int yPos; 
  int wh; //width & height of the square closebutton
  int marginTop;
  int marginRight;
  boolean overState;
  boolean pressedState;
  
  CloseButton(int xPosition, int yPosition, int mT, int mR,int widthAndHeight){
    xPos = xPosition;
    yPos = yPosition;
    marginTop = mT;
    marginRight = mR;
    wh = widthAndHeight;
  }
  
  void drawCloseButton(){
    if(overState){
      drawHoverState();
    }else if(pressedState){
      drawPressedState();
    }else
    drawNormalState();
  }
  
  void drawNormalState(){
    overState = false;
    pressedState =false;
    pushMatrix();
    translate(0,0,1.5);
    stroke(0);
    fill(0);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
    popMatrix();
  }
  
  void drawHoverState(){
    overState = true;
    pressedState = false;
    
    stroke(0);
    fill(255,0,0);
    pushMatrix();
    translate(0,0,1.5);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
    popMatrix();
  }
  
  void drawPressedState(){
    overState = false;
    pressedState = true;
    
    stroke(0);
    fill(50);
    pushMatrix();
    translate(0,0,1.5);
    rect(xPos-(wh+marginTop),yPos+marginRight,wh,wh);
    
    fill(255);
    textAlign(CENTER,CENTER);
    textSize(wh-(wh/5));
    text("X",xPos-(wh/2),yPos+(wh/2));
    popMatrix();
  }
  
  // this boolean returns true of mouse is over the sceneElement
  boolean isMouseOver(int mX, int mY){
    if((mX>xPos-(wh+marginTop)) && (mX<xPos) && (mY>yPos+marginRight) && (mY<yPos+wh)){
    return true;
    }
    return false;
  }
}
