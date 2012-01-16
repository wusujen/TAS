 // this draws all of the scene elements that
 // currently exist in the sceneElementArray
 void drawSceneElements(){
    SceneElement drawIt;
    for(int i=0;i<sceneElementArray.size();i++){
      drawIt=(SceneElement) sceneElementArray.get(i);
      
      // if the current sceneElement's scene property matches the
      // active scene, then draw the sceneElement.
      if(drawIt.scene==activeScene){
        drawIt.drawSceneElement();
      }
    }    
  }
  

/*=============   selectSceneElement   ==============*
   Check to see if mouse is over a sceneElement. If
   it is, then change the color properties of the
   element.
 ====================================================*/
  SceneElement selectSceneElement(){
    boolean mouseIsOver;
    for(int i=0;i<sceneElementArray.size();i++){
      SceneElement element=(SceneElement) sceneElementArray.get(i);
      if(element.scene!=activeScene){
        println("skipped Element:" + element.scene);
        continue;
      }
      println("check if on Element:" + element.scene);
      mouseIsOver=element.isMouseOver(mouseX,mouseY);
      if(mouseIsOver){
        if (activeElement != element) {
          println(element.objName());
          activeElement=element;
          populatePropertyPanel();
        }
        return element;
      }
    }
    if(activeElement!=null){
      println("deleselected"+activeElement.objName());
      activeElement=null;
      resetPropertyPanel();
    }
    return null;
  }

