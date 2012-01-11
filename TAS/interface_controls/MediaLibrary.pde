/*==============  fileObject =============*
 when dropped onto the canvas, the Drop
 Canvas creates this object and puts it
 into an Arraylist of these objects.
 *=========================================*/
class MediaLibrary {
  ControlP5 controlP5 = new ControlP5(this);
  ArrayList mediaArray;
  String title;
  int id;
  int x;
  int y;
  int w;
  int h;


  MediaLibrary(ArrayList objArray, String objTitle, int objId, int objX, int objY, int objW, int objH) {
    mediaArray=objArray;
    title=objTitle;
    id=objId;
    x=objX;
    y=objY;
    w=objW;
    h=objH;
  }
  
  void drawLibrary(ArrayList objList, String objTitle, int objId, int objH){
    mediaList = controlP5.addListBox("mediaList", objX , objY , objW, objH);
    mediaList.setId(objId);
    mediaList.setItemHeight(15);
    mediaList.setBarHeight(15);
    mediaList.captionLabel().toUpperCase(true);
    mediaList.captionLabel().set(objTitle);
    
    mediaList.captionLabel().style().marginTop = 3;
    mediaList.valueLabel().style().marginTop = 3; // the +/- sign
    
    for(int i=0;i<mediaArray.size();i++) {
      String raisin =(String) media.get(i);
      mediaList.addItem(raisin,i);
    }
  
  }

}

