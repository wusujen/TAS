void controlEvent(ControlEvent theEvent) {
  if (theEvent.isGroup()) {
    
    println(theEvent.group().value()+" from "+theEvent.group());
    int val=int(theEvent.group().value());
    
    String clickedItemName=itemNames[val];
    println(clickedItemName);
    l.removeItem(clickedItemName);
  }
}
