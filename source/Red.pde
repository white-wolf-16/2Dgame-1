class Red extends Token {
  PVector pos;
  PImage[] red = new PImage[7];
  
  Red(PVector pos){
    super(pos);
    loadFrames(red, red.length, "red1", 16, 24);
  }
  
  
 void update() {
    frames = red; 
    updateFrame(red.length);
    
    if (redTime<500 && redTime>0){
        drawToken(p1);
      }
      if (redTime<1){
        tokens.remove(this);
      }
 }

  void drawToken(Character player) {
    super.drawToken(player);
  }
}
