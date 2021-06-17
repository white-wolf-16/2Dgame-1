class Character {
  PVector pos, vel;
  int currentFrame=0;
  PImage img;
  PImage[] frames;
  
  Character(PVector pos) {
    this.pos = pos;
    vel = new PVector(); 
  }
  
  void move(PVector acc) {
    vel.add(acc);
  }
  
 void update() {
    pos.add(vel);
 }
 
 boolean hitChar(Character plya) {
    if (dist(pos.x, pos.y, plya.pos.x, plya.pos.y) < img.height/2 + plya.img.height/2) {              
      return true;
    }
    return false;
  }
  
  void resolveHit(Character plya) {
      float angle = atan2(pos.y - plya.pos.y, pos.x - plya.pos.x);
      float avgSpeed = (vel.mag() + plya.vel.mag())/2.5;
      vel.x = avgSpeed * cos(angle);
      vel.y = avgSpeed * sin(angle);
      plya.vel.x = avgSpeed * cos(angle - PI);
      plya.vel.y = avgSpeed * sin(angle - PI);
  }
  
 
 void updateFrame(int n){
    if (frameCount % 6 == 0) {
      currentFrame++;
    }
    if (currentFrame == n) {
      currentFrame = 0;
    }
    if (p1.dying && currentFrame == frames.length-1){
       p1.dead = true;
     }
     currentFrame = currentFrame % frames.length;
     img = frames[currentFrame];
 }
  
  
  void drawMe() {
  }
}
