package sojamo.control.patch;


import processing.core.PApplet;
import java.awt.event.WindowEvent;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.WindowListener;
import java.awt.Insets;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


/**
 * PatchWindow description.
 *
 * @invisible
 */

public class PatchWindow extends PApplet implements WindowListener, ComponentListener {

  int width = 600;

  int height = 200;

  int x = 100;

  int y = 100;

  private String _myName;

  protected boolean isLoop = true;

  public final static int NORMAL = 0;

  public final static int ECONOMIC = 1;

  protected int _myMode = 0;

  public PatchWindow() {
    super();
  }


  /**
   * @param theName String
   * @param theWidth int
   * @param theHeight int
   */
  public PatchWindow(final String theName,
      final int theWidth,
      final int theHeight) {
    super();
    _myName = theName;
    width = theWidth;
    height = theHeight;
    launch();
  }


  /**
   * @param theName String
   * @param theX int
   * @param theY int
   * @param theWidth int
   * @param theHeight int
   */
  public PatchWindow(final String theName,
      final int theX,
      final int theY,
      final int theWidth,
      final int theHeight) {
    super();
    _myName = theName;
    width = theWidth;
    height = theHeight;
    x = theX;
    y = theY;
    launch();
  }


  public void pause() {
  }


  public void play() {
  }


  /**
   * @return String
   */
  public String name() {
    return _myName;
  }


  /**
   * show/hide the controller window.
   *
   * @param theValue boolean
   */
  protected void visible(boolean theValue) {
//        frame.setVisible(theValue);
//    frame.pack();
    if (theValue == true) {
      frame.show();
    } else {
      frame.hide();
    }
  }


  /**
   * resize controller window.
   *
   * @param theValue boolean
   */
  protected void resizeable(boolean theValue) {
    frame.setResizable(theValue);
  }


  /**
   * @invisible
   */
  public void setup() {
    try {
      Thread.sleep(100);
    } catch (Exception e) {
    }
    size(width, height);
    try {
      Thread.sleep(100);
    } catch (Exception e) {

    }
    /*
     * method framrate is called frameRate from processing version 0117 on.
     * therefore check for backwards compatibility.
     */
    String myFramerate = "frameRate";
    try {
      Method m = this.getClass().getMethod(myFramerate, new Class[] {float.class});
      m.invoke(this, new Object[] {new Float(15)});
    } catch (NoSuchMethodException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {}
  }

  /**
   * @invisible
   */
  public void draw() {}


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowActivated(WindowEvent e) {
    isLoop = true;
    loop();
  }


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowClosed(WindowEvent e) {
//    System.out.println("window closed");
  }


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowClosing(WindowEvent e) {
    dispose();
  }


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowDeactivated(WindowEvent e) {
    if (_myMode == ECONOMIC) {
      isLoop = false;
      noLoop();
    }
  }


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowDeiconified(WindowEvent e) {}


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowIconified(WindowEvent e) {}


  /**
   * @invisible
   * @param e WindowEvent
   */
  public void windowOpened(WindowEvent e) {}


  /**
   * @invisible
   * @param e ComponentEvent
   */
  public void componentHidden(ComponentEvent e) {}


  /**
   * @invisible
   * @param e ComponentEvent
   */
  public void componentMoved(ComponentEvent e) {
    Component c = e.getComponent();
    x = c.getLocation().x;
    y = c.getLocation().y;
    frame.setTitle(_myName + " x:" + x + " y:" + y + "   " + width + "x" + height);
  }


  /**
   * @invisible
   * @param e ComponentEvent
   */
  public void componentResized(ComponentEvent e) {
    Component c = e.getComponent();
//    System.out.println("componentResized event from " +
//        c.getClass().getName() + "; new size: " + c.getSize().width
//        + ", " + c.getSize().height);
  }


  /**
   * @invisible
   * @param e ComponentEvent
   */
  public void componentShown(ComponentEvent e) {
//    System.out.println("componentShown event from " + e.getComponent().getClass().getName());
  }


  public void setMode(int theValue) {
    if (theValue == ECONOMIC) {
      _myMode = ECONOMIC;
      return;
    }
    _myMode = NORMAL;
  }


  protected void dispose() {
    stop();
    removeAll();
    frame.removeAll();
    frame.dispose();
  }


  private void launch() {
    GraphicsDevice displayDevice = null;
    if (displayDevice == null) {
      GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
      displayDevice = environment.getDefaultScreenDevice();
    }

    frame = new Frame(displayDevice.getDefaultConfiguration());

    // remove the grow box by default
    // users who want it back can call frame.setResizable(true)
    frame.setResizable(false);
    init();

    frame.pack(); // get insets. get more.
    Insets insets = frame.getInsets();

    int windowW = Math.max(width, MIN_WINDOW_WIDTH) + insets.left + insets.right;
    int windowH = Math.max(height, MIN_WINDOW_HEIGHT) + insets.top + insets.bottom;

    frame.setSize(windowW, windowH);
    frame.setLayout(null);
    frame.add(this);
    frame.setBackground(Color.black);
    int usableWindowH = windowH - insets.top - insets.bottom;
    setBounds((windowW - width) / 2, insets.top + (usableWindowH - height) / 2, width, height);

    frame.addWindowListener(this);
    frame.addComponentListener(this);
    frame.setName(_myName);
    frame.setTitle(_myName + " x:" + x + " y:" + y + "   w:" + width + " h:" + height);
    frame.setVisible(true);
    requestFocus();
  }

}
