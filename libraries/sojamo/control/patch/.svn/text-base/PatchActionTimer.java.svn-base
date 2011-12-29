package sojamo.control.patch;

public class PatchActionTimer extends PatchActionHandler {

  private long millisInterval;

  private long _myNextTimeLimit;


  public PatchActionTimer(long theMillisInterval) {
    setTimeInterval(theMillisInterval);
  }


  private void setNextTimeLimit() {
    _myNextTimeLimit = System.currentTimeMillis() + millisInterval;
  }


  public void setTimeInterval(long theMillisInterval) {
    millisInterval = theMillisInterval;
    setNextTimeLimit();
  }


  public void update() {
    if (System.currentTimeMillis() > _myNextTimeLimit) {
      _myNode.impulse(new PatchSignal());
      setNextTimeLimit();
    }
  }


}
