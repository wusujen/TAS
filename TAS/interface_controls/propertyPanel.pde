ArrayList bangList=new ArrayList();
Textlabel titleLabel;
Textlabel nameLabel;
Textlabel triggerLabel;
Numberbox widthBox;
Numberbox heightBox;
Numberbox xBox;
Numberbox yBox;

int innerMarginX=propertyPanelX+15;

void setupPropertyPanel(){
  titleLabel=controlP5.addTextlabel("textLabel","PROPERTIES PANEL", innerMarginX, propertyPanelY+15);
  titleLabel.setWidth(100);
  nameLabel=controlP5.addTextlabel("nameLabel","empty",innerMarginX,propertyPanelY+45);
  widthBox=controlP5.addNumberbox("widthBox",0,innerMarginX,propertyPanelY+70,130,18);
  heightBox=controlP5.addNumberbox("heightBox",0,innerMarginX,propertyPanelY+110,130,18);
  xBox=controlP5.addNumberbox("xBox",0,innerMarginX,propertyPanelY+150,130,18);
  yBox=controlP5.addNumberbox("yBox",0,innerMarginX,propertyPanelY+190,130,18);
  triggerLabel=controlP5.addTextlabel("triggerLabel","TRIGGERS", innerMarginX, propertyPanelY+250);
  triggerLabel.setWidth(100);
  for(int i=0; i<9; i++){
    Bang b=controlP5.addBang("bang"+i, innerMarginX+(60*(i%3)), propertyPanelY+275+(60*floor(i/3)), 50, 50);
    bangList.add(b);
    b.setLabelVisible(false);
  }
  resetPropertyPanel();
}

void drawPropertyPanel(){
   stroke(255);
   fill(180);
   rect(propertyPanelX,propertyPanelY,200,600);
}

void populatePropertyPanel(){
  titleLabel.show();
  triggerLabel.show();
  nameLabel.show();
  nameLabel.setValue(activeElement.getMyName());
  widthBox.show();
  widthBox.setValue(activeElement.getMyWidth());
  heightBox.show();
  heightBox.setValue(activeElement.getMyHeight());
  xBox.show();
  xBox.setValue(activeElement.getMyX());
  yBox.show();
  yBox.setValue(activeElement.getMyY());
  ArrayList tList=activeElement.getMyTriggerList();
  for(int i=0; i<9; i++){
    Bang b=(Bang) bangList.get(i);
    b.show();
    b.setColorForeground(color(0,0,0));
  }
  for(int i=0; i<tList.size();i++){
    Bang b=(Bang) bangList.get((Integer) tList.get(i));
    b.setColorForeground(color(255,0,0));
  }
  
}

void resetPropertyPanel(){
  titleLabel.hide();
  nameLabel.hide();
  triggerLabel.hide();
  widthBox.hide();
  heightBox.hide();
  xBox.hide();
  yBox.hide();
  for(int i=0; i<9; i++){
    Bang b=(Bang) bangList.get(i);
    b.hide();
  }
}

void widthBox(){
  activeElement.updateWidth(int(widthBox.value()));
}
void heightBox(){
  activeElement.updateHeight(int(heightBox.value()));
}
void xBox(){
  activeElement.updateX(int(xBox.value()));
}
void yBox(){
  activeElement.updateY(int(yBox.value()));
}

