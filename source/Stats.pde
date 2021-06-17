class Stats {
  
  PFont mono = loadFont("Monospaced.bold-42.vlw");
  
  Stats(){
    
  }
  
  void incCoin(){
      p1.coin+=1;
  }
  
  void inckey(){
      p1.key1+=1;
  }
  
  void deckey(){
      p1.key1-=1;
  }
  
  void drawS() {
    pushStyle();
    noStroke();
    fill(128, 67, 160, 120);
    quad(0,0,200,0,180,30,0,30);
    quad(width,0,width-200,0,width-170,30,width,30);
    
    fill(255, 255, 255, 170);
    textFont(mono,12);
    text( p1.hp + "% :HP", width-45, 10);
    text(int(frameRate) + " :FPS", width-42, 25);
    textAlign(LEFT);
    text("Coins Collected: " + p1.coin, 5, 10);
    text("Keys Collected: " + p1.key1, 5, 25);
    popStyle();
  }
}
