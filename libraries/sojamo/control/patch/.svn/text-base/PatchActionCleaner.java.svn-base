package sojamo.control.patch;


public class PatchActionCleaner extends PatchActionHandler {
  public void init() {
      for(PatchActionHandler o:_myNode.patchActionHandlers) {
	  o.setRemove(true);
    }
    setRemove(true);
  }
}
