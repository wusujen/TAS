 // this draws all of the scene elements that
 // currently exist in the sceneElementArray
 void drawSceneElements(){
    SceneElement drawIt;
    for(int i=0;i<sceneElementArray.size();i++){
      drawIt=(SceneElement) sceneElementArray.get(i);
      drawIt.displayProperties();
      drawIt.drawSceneElement();
      println("error");
    }    
  }
  

/*=============   selectScenElement   ===============*
   Check to see if mouse is over a sceneElement. If
   it is, then change the color properties of the
   element.
 ====================================================*/
  SceneElement selectSceneElement(){
    boolean mouseIsOver;
    for(int i=0;i<sceneElementArray.size();i++){
      SceneElement element=(SceneElement) sceneElementArray.get(i);
      mouseIsOver=element.isMouseOver(mouseX,mouseY);
      if(mouseIsOver && mousePressed){
        println(element.objName());
        return element;
      }
    }
    return null;
  }

