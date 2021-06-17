class Player extends Character{
  float damp = 0.8;
  int hp = 100, coin = 0, key1 = 0;
  boolean dying = false, dead = false;
  
  PImage[] idle = new PImage[4];
  PImage[] run = new PImage[4];
  PImage[] ded = new PImage[4];
  
  
  Player(PVector pos) {
    super(pos);
    
    loadFrames(idle, idle.length, "p1idle", 16, 32);
    loadFrames(run, run.length, "p1run", 16, 32);
    loadFrames(ded, ded.length, "p1daze", 16, 32);
  }
  
  void idle(){
    frames = idle;  
  }
  
  void run(){
    frames = run;
  }
  
  void ded(){
    frames = ded;
    dying = true;
  }
  
  void killed(){
    dead = true;
    game = 1;
    playSound("sounds/ded.mp3");
  }
  
 void update() {
   if (!dying){
     super.update();
      vel.mult(damp);
      if (abs(vel.x) + abs (vel.y) < .3){
        idle();
        updateFrame(idle.length);
      }
      else{
        run();
        updateFrame(run.length);
      }
      if (!p1.dead) updateFrame(ded.length);
   }
 }
  
  void drawMe() {
    pushMatrix();
    translate(tileSize+img.width/2, tileSize+img.height/2);
    if (vel.x < 0) {
      scale(-1, 1);
    }
    //scale(1.5);
    image(img, -img.width/2, -img.height/2);
    popMatrix();
  }
}
