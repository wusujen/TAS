package sojamo.control.patch;

class PatchSignal implements Cloneable {

  int counter;

  boolean done = false;

  private PatchNode myCurrentNode;

  private PatchNode myPreviousNode;

  private PatchNode myNextNode;

  private float _myMillisScalar = 1;

  private int _myDirection;

  private int _myAge;

  private long _myNextMillisLimit;

  private long _myMillisInterval;

  private int _myMode;

  public final static int DYNAMIC = 0;

  public final static int STATIC = 1;

  public PatchSignal() {
    _myAge = 0;
    setTimeInterval(1000);
  }

  public int age() {
    return _myAge;
  }


  void update(PatchNode theNode) {
    if(System.currentTimeMillis()>_myNextMillisLimit) {
      setTimeInterval(_myMillisInterval);
      myCurrentNode.forwardSignal(this);
    }
  }

  public void setDirection(int theDirection) {
    _myDirection = theDirection;
  }

  public int direction() {
    return _myDirection;
  }


  public void reset() {
    counter = 0;
  }

  public int mode() {
    return _myMode;
  }

  public void setMode(int theMode) {
    if(theMode==STATIC) {
      _myMode = STATIC;
      return;
    }
    _myMode = DYNAMIC;
  }

  public void setTimeInterval(long theMillisInterval) {
    _myMillisInterval = (long)(theMillisInterval);
    _myNextMillisLimit = System.currentTimeMillis() + (int)(_myMillisInterval / _myMillisScalar);
  }


  public void setTimeIntervalScalar(float theMillisScalar) {
  _myMillisScalar = theMillisScalar;
}


  public void setCurrentNode(PatchNode theNode) {
    myPreviousNode = myCurrentNode;
    myCurrentNode = theNode;
  }

  public void setPreviousNode(PatchNode theNode) {
    myPreviousNode = theNode;
  }

  public void setNextNode(PatchNode theNode) {
    myNextNode = theNode;
  }

  public PatchNode getCurrentNode() {
    return myCurrentNode;
  }

  public PatchNode getNextNode() {
    return myNextNode;
  }


  public PatchNode getPreviousNode() {
    return myPreviousNode;
  }


  public Object clone()
  {
    _myAge++;
    try {
          return super.clone();
    } catch (CloneNotSupportedException e) { // Dire trouble!!!
         throw new InternalError("But we are Cloneable!!!");
      }
  }


}

