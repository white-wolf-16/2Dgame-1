class Blue extends Token {
  PVector pos;
  PImage[] blue = new PImage[7];
  
  Blue(PVector pos){
    super(pos);
    loadFrames(blue, blue.length, "blue1", 16, 24);
  }
  
  
 void update() {
    frames = blue; 
    updateFrame(blue.length);
    
    if (blueTime<1100 && blueTime>0){
        drawToken(p1);
      }
      if (blueTime<1){
        tokens.remove(this);
      }
 }

  void drawToken(Character player) {
    super.drawToken(player);
  }
}
