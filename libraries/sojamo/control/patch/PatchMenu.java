package sojamo.control.patch;

import java.util.Vector;

public class PatchMenu {
    
    SPatch patch;
    
    Vector<PatchMenuItem> items;
    
    public final static int GROUPS = 1;
    
    public final static int ACTIONS = 2;
    
    public final static int TOOLS = 3;
    
    public final static int MAP = 4;
    
    
    
    PatchMenu(SPatch thePatch) {
	patch = thePatch;
	items = new Vector<PatchMenuItem>();
    }
    
    void update() {
	for (PatchMenuItem o : items) {
	    o.update();
	}
    }
    
}
