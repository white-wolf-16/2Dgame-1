class Coins extends Token {
  PVector pos;
  PImage[] coin = new PImage[8];
  
  Coins(PVector pos){
    super(pos);
    loadFrames(coin, coin.length, "coins", 32, 32);
  }
  
  
 void update() {
    frames = coin; 
    updateFrame(coin.length);
    if (coinTime<3500 && coinTime>0){
        drawToken(p1);
      }
      if (coinTime<1){
        tokens.clear();
      }
 }
 
  void drawToken(Character player) {
    super.drawToken(player);
  }
}
