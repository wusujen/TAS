package sojamo.control.patch;

import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;


public abstract class AbstractPatchInputHandler {


  public float mouseX = 0, mouseY = 0;

//  public float offsetMouseX = 0, offsetMouseY = 0;

  public char currentKey = ' ';

  public boolean mousePressed = false;

  public boolean[] keysPressed = new boolean[256];

  protected Vector _myInputListeners;

  private PatchSelector _myPatchSelector;

  private PatchGroup _mymaingroup;

  private boolean isKeyPressed = false;

  public AbstractPatchInputHandler(PatchGroup themaingroup) {
    _mymaingroup = themaingroup;
    _mymaingroup.patch.papplet.registerMouseEvent(this);
    _mymaingroup.patch.papplet.registerKeyEvent(this);
    _myInputListeners = new Vector();
  }


  protected void setPatchSelector(PatchSelector theSelector) {
    _myPatchSelector = theSelector;
  }


  public void add(PatchInputListener theListener) {
    _myInputListeners.insertElementAt(theListener, 0);
  }


  public void remove(PatchInputListener theListener) {
    _myInputListeners.remove(theListener);
  }


  public void keyEvent(KeyEvent theKeyEvent) {
    if (theKeyEvent.getID() == KeyEvent.KEY_PRESSED) {
      setKey(theKeyEvent.getKeyCode());
    }
    if (theKeyEvent.getID() == KeyEvent.KEY_RELEASED) {
      resetKey(theKeyEvent.getKeyCode());
    }
    isKeyPressed = checkKeyPressed();
  }


  private boolean checkKeyPressed() {
    for(int i=0;i<keysPressed.length;i++) {
      if(keysPressed[i]==true) {
        return true;
      }
    }
    return false;
  }

  public void mouseEvent(MouseEvent theMouseEvent) {
    setMouse(theMouseEvent.getX(), theMouseEvent.getY());
    switch (theMouseEvent.getID()) {
      case (MouseEvent.MOUSE_RELEASED):
        mouseReleased();
        break;
      case (MouseEvent.MOUSE_PRESSED):
        mousePressed();
        break;

    }
  }


  public void setKey(int theKeyCode) {
    currentKey = (char) (theKeyCode);
    keysPressed[theKeyCode] = true;
    checkKey();
    for (int i = 0; i < _myInputListeners.size(); i++) {
      if(((PatchInputListener) _myInputListeners.get(i)).category()!=_mymaingroup.isBlocked) {
        ((PatchInputListener) _myInputListeners.get(i)).keyPressed(theKeyCode);
      }
    }

  }


  public void resetKey(int theKeyCode) {
    keysPressed[theKeyCode] = false;
    for (int i = 0; i < _myInputListeners.size(); i++) {
      if(((PatchInputListener) _myInputListeners.get(i)).category()!=_mymaingroup.isBlocked) {
        ((PatchInputListener) _myInputListeners.get(i)).keyReleased(theKeyCode);
      }
    }

  }


  public void setMouse(float theX, float theY) {
    mouseX = theX;
    mouseY = theY;
  }


  public void mousePressed() {
    mousePressed = true;
    if (_myPatchSelector.isActive()) {
      if(((PatchInputListener) _myInputListeners.lastElement()).category()!=_mymaingroup.isBlocked) {
        ((PatchInputListener) _myInputListeners.lastElement()).mousePressed();
        return;
      }
    }
    for (int i = 0; i < _myInputListeners.size(); i++) {
      if(((PatchInputListener) _myInputListeners.get(i)).category()!=_mymaingroup.isBlocked) {
        if (((PatchInputListener) _myInputListeners.get(i)).mousePressed()) {
          break;
        }
      }
    }
  }


  public void mouseReleased() {
    mousePressed = false;
    for (int i = 0; i < _myInputListeners.size(); i++) {
      ((PatchInputListener) _myInputListeners.get(i)).mouseReleased();
    }
  }


  abstract protected void checkKey();

  public boolean isKeyPressed() {
    return isKeyPressed;
  }
}

