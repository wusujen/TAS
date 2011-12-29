package sojamo.control.patch;

import processing.core.PApplet;
import processing.core.PVector;


public class PatchSelector
    implements PatchInputListener, PDrawable {

  final static int SELECT = 1;
  final static int SELECTED = 2;
  final static int MOVE = 3;

  final static int ADJUST = 0;
  final static int MOVEITEMS = 1;
  int mode = SELECT;
  int selectionMode = ADJUST;

  boolean isVisible = true;

  PVector startPosition = new PVector(0,0,0);
  PVector currentPosition = new PVector(0,0,0);
  float x1,y1,x2,y2;
  float width, height;

  PVector previousMousePosition = new PVector(0,0,0);
  PVector currentMousePosition = new PVector(0,0,0);
  PVector diffMousePosition = new PVector(0,0,0);

  private PatchGroup _mymaingroup;

  private boolean isMouseDown;

  private boolean isActiveKey;

  private boolean isActive;

  public PatchSelector(PatchGroup themaingroup) {
    _mymaingroup = themaingroup;
    _mymaingroup.inputhandler.add(this);
    _mymaingroup.inputhandler.setPatchSelector(this);
  }

  public int category() {
    return PatchGroup.NETWORK;
  }


  public boolean mousePressed() {
    if(_mymaingroup.isMouseBusy == false) {
      if (isActiveKey == false) {
        if (!inside(currentMousePosition.x, currentMousePosition.y)) {
          stopAction(true);
          return true;
        }
      }
//      _mymaingroup.isMouseBusy = true;
      isMouseDown = true;
      isActive = true;
      return true;
    }
    return false;
  }


  public void mouseReleased() {
      _mymaingroup.isMouseBusy = false;
      isMouseDown = false;
  }


  public void keyPressed(int theKeyCode) {
    if(theKeyCode==CTRL) {
      isActiveKey=true;
    }
  }

  public void keyReleased(int theKeyCode) {
    if(theKeyCode==CTRL) {
      stopAction(true);
      isActiveKey=false;
    }
  }


  public void update() {
    currentMousePosition.set(_mymaingroup.mouseX,_mymaingroup.mouseY,0);
    diffMousePosition.set(currentMousePosition);
    diffMousePosition.sub(previousMousePosition);
    x1 = (startPosition.x<currentPosition.x)? startPosition.x:currentPosition.x;
    x2 = (startPosition.x<currentPosition.x)? currentPosition.x:startPosition.x;
    y1 = (startPosition.y<currentPosition.y)? startPosition.y:currentPosition.y;
    y2 = (startPosition.y<currentPosition.y)? currentPosition.y:startPosition.y;
    width = x2-x1;
    height = y2-y1;
  }

  public void draw(PApplet thePApplet) {
    thePApplet.noFill();
    if (isMouseDown == true) {
      if(_mymaingroup.inputhandler.keysPressed[PatchInputListener.ALT]==false) {
        if (isVisible == false) {
          startAction(currentMousePosition.x, currentMousePosition.y);
        }

        if (mode == SELECT) {
          currentPosition.set(currentMousePosition);
          thePApplet.stroke(255);
        } else if (mode == SELECTED) {
          if (inside(
              currentMousePosition.x, currentMousePosition.y) ||
              selectionMode == MOVEITEMS) {
            selectionMode = MOVEITEMS;
            startPosition.add(diffMousePosition);
            currentPosition.add(diffMousePosition);
          } else {
            stopAction(true);
          }
        }
        drawRect(thePApplet);
      }
    } else if(isVisible) {
      stopAction(false);
      drawRect(thePApplet);
    }
    previousMousePosition.set(_mymaingroup.mouseX, _mymaingroup.mouseY,0);
  }



  private void drawRect(PApplet thePApplet) {
    if (selectionMode == MOVEITEMS) {
      thePApplet.stroke(255, 0, 0);
    } else {
      thePApplet.stroke(128, 128, 128);      
    }
    thePApplet.rect(
        startPosition.x,
        startPosition.y,
        currentPosition.x-(int)startPosition.x,
        currentPosition.y-(int)startPosition.y);
  }


  public boolean isActive() {
    return isActive;
  }

  public void stopAction(boolean isClear) {
    if(isClear) {
      startPosition.set(currentMousePosition.x, currentMousePosition.y, currentMousePosition.z);
      currentPosition.set(startPosition);
      isActive = false;
    }
    if(width+height>20) {
      mode = SELECTED;
      isVisible = true;
      selectionMode = ADJUST;
    } else {
      mode = SELECT;
      isVisible = false;
    }
  }

  public void startAction(float theX, float theY) {
    if(mode==SELECT) {
      startPosition.set(theX,theY,0);
      isVisible = true;
    }
  }

  public boolean inside(float theX, float theY) {
    return (theX>x1 && theX<x2 && theY>y1 && theY<y2);
  }
}
