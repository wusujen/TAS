package sojamo.control.patch;

class PatchInputHandler extends AbstractPatchInputHandler {

    private final PatchGroup _mymaingroup;

    public PatchInputHandler(PatchGroup themaingroup) {
	super(themaingroup);
	_mymaingroup = themaingroup;
    }

    public void checkMousePressed() {
    }

    protected void checkKey() {
	if (keysPressed[PatchInputListener.ALT]) {
	    switch (currentKey) {
	    case (' '):
		_mymaingroup.patch.addNode(_mymaingroup.mouseX,
			_mymaingroup.mouseY,0);
		if (keysPressed[PatchInputListener.SHIFT]) {
		    _mymaingroup.patch
			    .connect(
				    _mymaingroup.patch
					    .getNodeById(_mymaingroup.patch._myLastNodeId),
				    _mymaingroup.patch
					    .getNodeById(_mymaingroup.patch._myCurrentNodeId));
		}
		break;
	    case ('1'):
		_mymaingroup.setTool(PatchGroup.DRAWEDGE);
		break;
	    case ('2'):
		_mymaingroup.setTool(PatchGroup.MOVENODE);
		break;
	    case ('3'):
		_mymaingroup.setTool(PatchGroup.IMPULSE);
		break;
	    case ('4'):
		_mymaingroup.setTool(PatchGroup.REMOVE);
		break;
	    case ('h'):
		_mymaingroup.isLineVisible = !_mymaingroup.isLineVisible;
	    	break;
	    }
	}
    }

    public boolean checkKeyPressed(int theKey) {
	return keysPressed[theKey];
    }

}
