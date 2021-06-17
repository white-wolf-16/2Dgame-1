int demonRemoved = 0;

class Enemy extends Character{

  PImage[] demon = new PImage[8];
  
  Enemy(PVector pos, PVector vel) {
    super(pos);
    this.vel = vel;
    loadFrames(demon, demon.length, "demon", 16, 32);
  }
  
 void update() {
    super.update();
    frames = demon;
    updateFrame(demon.length);
    demonDifficulty();
 }
  
  void drawMe(Character player) {
    pushMatrix();
    translate(-player.pos.x + tileSize + player.img.width/2 + pos.x,
     -player.pos.y + tileSize + player.img.height/2 + pos.y);
    if (vel.x < 0) {
      scale(-1, 1);
    }
    scale(1.1);
    image(img, -img.width/2, -img.height/2);
    popMatrix();
  }
}
