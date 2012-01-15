class MediaLibrary {
  ArrayList mediaArray;
  String title;
  int id;
  int x;
  int y;
  int w;
  int h;

  MediaLibrary(ArrayList objArray, String objTitle, int objId) {
    mediaArray=objArray;
    title=objTitle;
    id=objId;
    
    drawLibrary();
    
  }
  
  void drawLibrary(){
    mediaList = controlP5.addListBox(title, libX, libY , libW, libH);
    mediaList.setId(id);
    mediaList.setItemHeight(15);
    mediaList.setBarHeight(15);
    mediaList.captionLabel().toUpperCase(true);
    mediaList.captionLabel().set(title);
    
    mediaList.captionLabel().style().marginTop = 3;
    mediaList.valueLabel().style().marginTop = 3; // the +/- sign
    println("media array size: "+ mediaArray.size());
    
    for(int i=0;i<mediaArray.size();i++) {
      String raisin =(String) mediaArray.get(i);
      mediaList.addItem(raisin,i);
    }
    
    // save the height of the list as a global var
    // to ensure proper vertical spacing for the lists
    mediaList.getBackgroundHeight();
    libY += mediaList.getBackgroundHeight() + 30;
  }

}

