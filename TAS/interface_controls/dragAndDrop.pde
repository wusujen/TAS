void controlEvent(ControlEvent theEvent) {
  if (theEvent.isGroup()) {
    
    println(theEvent.group().value()+" from "+theEvent.group());
    int val=int(theEvent.group().value());
    
    //removes the item selected
    String clickedItemName=itemNames[val];
    println(clickedItemName);
    l.removeItem(clickedItemName);
  }
}
