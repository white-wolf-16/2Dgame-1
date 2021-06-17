class Key extends Token {
  PVector pos;
  PImage[] key1 = new PImage[12];
  int  keyTime = -1;
  
  Key(PVector pos){
    super(pos);
    loadFrames(key1, key1.length, "key", 24, 24);
  }
  
  
 void update() {
    frames = key1; 
    updateFrame(key1.length);
    
    if (tkeyTime<550 && tkeyTime>0){
        drawToken(p1);
      }
      if (tkeyTime<1){
        tokens.remove(this);
      }
 }

  void drawToken(Character player) {
    super.drawToken(player);
  }
}
