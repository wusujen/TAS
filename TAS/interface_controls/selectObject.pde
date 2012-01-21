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
      // if an element has been selected, then change
      // the visual properties of that element
      if (activeElement!=null) {
        activeElement.hasBeenSelected();
        drawPropertyPanel();
      }
    }
  }
}


/*=============   selectSceneElement   ==============*
 This is triggered in MouseClicked, when the mouse is
 clicked in the dropCanvas.
 ====================================================*/
void selectSceneElement(SceneElement elementOnTop) {
   if (elementOnTop==null) {
     // otherwise, if the user didn't click anything, then set activeElement to null,
     // and clear the property panel
        activeElement=null;
        resetPropertyPanel();
    }
    // If the active element doesn't equal the topMostElement that the user has clicked on
    if (activeElement != elementOnTop) {
      //then set the activeElement to the topMostElement and return that element
      activeElement=elementOnTop;
      // and set the propertyPanel to the properties of that topMostElement.
      populatePropertyPanel();
    }
}


/*==================   getTopMostElement  =================*
  This function will return the SceneElement object that is
  on TOP of the other scene elements, based on its position
  in the sceneElement Array. This is run in mouseClicked.
 =========================================================*/
SceneElement getTopMostElement(){
    ArrayList overlappingElements=new ArrayList();
    // loop through the sceneElementArray
    for (int i=0;i<sceneElementArray.size();i++) {
      SceneElement element=(SceneElement) sceneElementArray.get(i);
      String elementScene=element.scene;
      // make sure if the element is in the actual scene
      if (elementScene.compareTo(activeScene)==0) {
        // check and see if mouse is over ANY of them
          if(element.isMouseOver(mouseX,mouseY)){
            // store the elements into an arraylist if mouse is over it
            overlappingElements.add(element);
          }
        continue;
      }
    }
    // make sure that overlapping elements is not null
    if(!overlappingElements.isEmpty()){
      // return the LAST element in the array, because that is the one that is on top
      SceneElement returnElement=(SceneElement) overlappingElements.get(overlappingElements.size()-1);
      return returnElement;
    }
    //else, return null
    return null;
 }
