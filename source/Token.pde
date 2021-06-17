/*
Token Rules: 
4 different types: coins, blue potion - increase hp by 10%, red potion - increase hp by 50% and the key.
Time on screen: coins- around 60 seconds, blue- around 20s, red- around 10s and keys- around 8s.
Red Potions only spawn in enclosed spaces.
*/


class Token {
  PVector pos;
  int currentFrame=0;
  PImage img; 
  PImage[] frames;
  
  Token(PVector pos) {
    this.pos = pos;
  }
  
 void update() {
 // to be overriden in respective subclasses;
 }

 boolean collision(Character p1) {
    if (dist(pos.x, pos.y, p1.pos.x, p1.pos.y) < img.width/2 + img.width/2) {
      return true;
    }
      return false;
    }


 void updateFrame(int n){
    if (frameCount % 6 == 0) {
      currentFrame++;
    }
    if (currentFrame == n) {
      currentFrame = 0;
    }
    currentFrame = currentFrame % frames.length;
    img = frames[currentFrame];
 }
 

 void drawToken(Character player) {
   pushMatrix();
   translate(-player.pos.x + tileSize + player.img.width/2 + pos.x,
    -player.pos.y + tileSize + player.img.height/2 + pos.y);
   //scale(1);
   image(img, -img.width/2, -img.height/2);
   popMatrix();
 }
}
