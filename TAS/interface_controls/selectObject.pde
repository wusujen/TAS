// this draws all of the scene elements that
// currently exist in the sceneElementArray
void drawSceneElements() {
  SceneElement drawIt;
  for (int i=0;i<sceneElementArray.size();i++) {
    drawIt=(SceneElement) sceneElementArray.get(i);
    // if the current sceneElement's scene property matches the
    // active scene, then draw the sceneElement.
    //if (drawIt.scene==activeScene) { 
    if (drawIt.scene.equals(activeScene)) { 
        drawIt.drawSceneElement();

    }
  }
}


/*=============   selectSceneElement   ==============*
 Check to see if mouse is over a sceneElement. If it
 is, then change the color of the scene element text,
 and also draw the properties panel/set the activeElement.
 ====================================================*/
SceneElement selectSceneElement() {
  boolean mouseIsOver;
  for (int i=0;i<sceneElementArray.size();i++) {
    SceneElement element=(SceneElement) sceneElementArray.get(i);
    String elementScene = element.scene; 
    if (elementScene.compareTo(activeScene) != 0) {
      continue;
    }
    mouseIsOver=element.isMouseOver(mouseX, mouseY);
    if (mouseIsOver) {
      if (activeElement != element) {
        println(element.objName());
        activeElement=element;
        populatePropertyPanel();
      }
      return element;
    }
  }
  if (activeElement!=null) {
    activeElement=null;
    resetPropertyPanel();
  }
  return null;
}

