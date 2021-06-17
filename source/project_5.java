import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class project_5 extends PApplet {

// Badhan Dey - v.5(Sat).3(Nt) - Final


ControlP5 controlP5;
  








//Global variable
Minim m;
AudioPlayer sound;

Player p1;
Stats stat;
Button play, cont;

ArrayList<Enemy> demons = new ArrayList<Enemy>();
ArrayList<Token> tokens = new ArrayList<Token>();
ArrayList<Map> tiles = new ArrayList<Map>();
int tileSize = 32, numCoin = 15, game = 2, fx= 0, fx2= 1, fx3= 0, demonNum = 5;
boolean block, demBlock;
int coinTime = PApplet.parseInt(random(3540,3600)), keyTime = -1, redTime = PApplet.parseInt(random(540,600)), blueTime = PApplet.parseInt(random(1140,1200)), tkeyTime = 610;
float coinx, coiny, redx, redy, bluex, bluey, keyx, keyy;



//-----------------SETUP---------------------
public void setup() {
  
  frameRate(60);
  stroke(200);
  strokeWeight(2);
  fill(63);
   
  controlP5 = new ControlP5(this);
  m = new Minim(this);
  play = controlP5.addButton("Insert", 0, width/2-50, height-100, 100, 50);
  cont = controlP5.addButton("Continue", 0, width/2-50, height-100, 100, 50);
  play.setColorLabel(color(0));
  play.setColorBackground(color(0, 250, 250));
  
  stat = new Stats();
  
  for (int i = 0; i < map.length; i++) {
    for (int j = 0; j < map[i].length; j++) {
      if(map[i][j]==0  || map[i][j]==43 || map[i][j]==44 ||
         map[i][j]==45 || map[i][j]==47  || map[i][j]==75 ) block = false;
      else block = true;
      if(map[i][j]==0  || map[i][j]==43 || map[i][j]==44 ||
         map[i][j]==45 || map[i][j]==47  || map[i][j]==75 || map[i][j]==76 ) demBlock = false;
      else demBlock = true;
      String tile = "tile" + map[i][j] + ".jpg";
      tiles.add(new Map(tile, new PVector(i * tileSize, j * tileSize), block, demBlock));
    }
  }
  for (int i = 33; i< tiles.size(); i++) {
    Map startTile = tiles.get(i);
    if (startTile.block==false) {
      p1 = new Player(new PVector(startTile.pos.x, startTile.pos.y));
      for (int j = 0; j < demonNum; j++)  spawnDemon();
      break;
    }
  }
  // Demons for enclosed spaces 
  demons.add(new Enemy(new PVector(65,1280), new PVector(1,0)));
  demons.add(new Enemy(new PVector(1248,1200), new PVector(2,0)));
}




//-----------DRAW------------

public void draw() {
  switch (game) {
  case 0: 
    gamePlay(); 
    break;
  case 1: 
    gameOver("Fin!"); 
    break;
  case 2: 
    gameStart("How To Play \n\n• Arrow keys for movement.\n• Blue potion boosts 10% hp\n• Red potion boosts 50% hp.\n• Use key to unlock gates using 'k'.\n• Use spacebar to increase speed.\n• Alert! Loss of hp when speeding.\n• Avoid demons & collect 20 coins to level up."); 
    break;
  case 3: 
    gameKey("'You've used a key'\n\n• Locked gates shall unlock for 10 seconds.\n\nINITIAL WARNIG!!\n• Before passing through the gates make sure\nyou possess a key to unlock the gates again\nin order for your escape. Or you'll get trapped."); 
    fx3=1;
    break;
  case 4: 
    gameKey("'You've used a key'\n\n• Gates shall unlock for 10 seconds.\n• If already unlocked the timer shall reset again."); 
    break;
  case 5: 
    gameOver("Win!"); 
    break;
  }
}


//-------------GamePlay (Method)-------------

public void gamePlay() {
  background(45);
  controlP5.getController("Insert").hide();
  controlP5.getController("Continue").hide();
  controls();
  p1.update();
  
  
  //Player-Map:
  for (int i = 0; i < tiles.size(); i++) {
    Map tile = tiles.get(i);
    tile.collision(p1);
    if (tile.inWindow()) {
      tile.drawMe(p1);
    }
  }
  if (!p1.dead) p1.drawMe();
  
  //Demon-Map:
  for (int i = 0; i < demons.size(); i++) {
    Enemy dem = demons.get(i);
    dem.update();
    dem.drawMe(p1);
    for (int j = 0; j < tiles.size(); j++) {
      Map tile = tiles.get(j);
      tile.dmnCollision(dem);
    }
    if (dem.hitChar(p1)){
      dem.resolveHit(p1);
      p1.hp-=10;
      if(p1.hp < 0) p1.updateFrame(4);
    }
  }
  
  //Death & Win
  if(p1.hp <= 0) p1.killed();
  if(p1.coin == 20) game=5;
  if(p1.hp > 100) p1.hp = 100;
  
  
  // Tokens:
  
  // Timers
  decTimer();
  
  //Spawn Tokens
  spawnTokens();
  
  //Timer resets
  timerReset();
  
  //Coin check
  coinCheck();
  
  //Red check
  redCheck();
  
  //Blue check
  blueCheck();
  
  //Key check
  keyCheck();
 
  //Score draw
  stat.drawS();
}
class Blue extends Token {
  PVector pos;
  PImage[] blue = new PImage[7];
  
  Blue(PVector pos){
    super(pos);
    loadFrames(blue, blue.length, "blue1", 16, 24);
  }
  
  
 public void update() {
    frames = blue; 
    updateFrame(blue.length);
    
    if (blueTime<1100 && blueTime>0){
        drawToken(p1);
      }
      if (blueTime<1){
        tokens.remove(this);
      }
 }

  public void drawToken(Character player) {
    super.drawToken(player);
  }
}
class Character {
  PVector pos, vel;
  int currentFrame=0;
  PImage img;
  PImage[] frames;
  
  Character(PVector pos) {
    this.pos = pos;
    vel = new PVector(); 
  }
  
  public void move(PVector acc) {
    vel.add(acc);
  }
  
 public void update() {
    pos.add(vel);
 }
 
 public boolean hitChar(Character plya) {
    if (dist(pos.x, pos.y, plya.pos.x, plya.pos.y) < img.height/2 + plya.img.height/2) {              
      return true;
    }
    return false;
  }
  
  public void resolveHit(Character plya) {
      float angle = atan2(pos.y - plya.pos.y, pos.x - plya.pos.x);
      float avgSpeed = (vel.mag() + plya.vel.mag())/2.5f;
      vel.x = avgSpeed * cos(angle);
      vel.y = avgSpeed * sin(angle);
      plya.vel.x = avgSpeed * cos(angle - PI);
      plya.vel.y = avgSpeed * sin(angle - PI);
  }
  
 
 public void updateFrame(int n){
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
  
  
  public void drawMe() {
  }
}
class Coins extends Token {
  PVector pos;
  PImage[] coin = new PImage[8];
  
  Coins(PVector pos){
    super(pos);
    loadFrames(coin, coin.length, "coins", 32, 32);
  }
  
  
 public void update() {
    frames = coin; 
    updateFrame(coin.length);
    if (coinTime<3500 && coinTime>0){
        drawToken(p1);
      }
      if (coinTime<1){
        tokens.clear();
      }
 }
 
  public void drawToken(Character player) {
    super.drawToken(player);
  }
}
float speed = 1;
int spdChk = 0;
PVector upForce = new PVector(0, -speed);
PVector leftForce = new PVector(-speed, 0);
PVector rightForce = new PVector(speed, 0);
PVector downForce = new PVector(0, speed); 
boolean up, left, right, down, space;

public void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {up = true; if(spdChk%2 != 0) p1.hp-=1;}
    else if (keyCode == LEFT){ left = true; if(spdChk%2 != 0) p1.hp-=1;}
    else if (keyCode == RIGHT){ right = true; if(spdChk%2 != 0) p1.hp-=1;}
    else if (keyCode == DOWN){ down = true; if(spdChk%2 != 0) p1.hp-=1;}
  }
  
  if (key == ' '){
    spdCheck();
    keyPressed = false; 
    spdChk++;
  }
    
  if (key == 'k'){
    if(p1.key1 > 0){
      playSound("sounds/key.mp3");
      keyTime = 610;
      stat.deckey();
      onLock();
      if (fx3==0)  game = 3;
      if (fx3==1)  game = 4;
      keyPressed=false;
    }
  }
  
}

public void keyReleased() {
  if (key == CODED) {
    if (keyCode == UP) up = false;
    else if (keyCode == LEFT) left = false;
    else if (keyCode == RIGHT) right = false;
    else if (keyCode == DOWN) down = false;
  }
}

public void controls(){
  if (up) p1.move(upForce);
  if (down) p1.move(downForce);
  if (left) p1.move(leftForce);
  if (right) p1.move(rightForce);
}


public void spdCheck(){
  if (spdChk%2 == 0){
      upForce = new PVector(0, -2.5f);
      leftForce = new PVector(-2.5f, 0);
      rightForce = new PVector(2.5f, 0);
      downForce = new PVector(0, 2.5f);
    }
  if (spdChk%2 != 0){
    upForce = new PVector(0, -speed);
    leftForce = new PVector(-speed, 0);
    rightForce = new PVector(speed, 0);
    downForce = new PVector(0, speed);
  }
}
int demonRemoved = 0;

class Enemy extends Character{

  PImage[] demon = new PImage[8];
  
  Enemy(PVector pos, PVector vel) {
    super(pos);
    this.vel = vel;
    loadFrames(demon, demon.length, "demon", 16, 32);
  }
  
 public void update() {
    super.update();
    frames = demon;
    updateFrame(demon.length);
    demonDifficulty();
 }
  
  public void drawMe(Character player) {
    pushMatrix();
    translate(-player.pos.x + tileSize + player.img.width/2 + pos.x,
     -player.pos.y + tileSize + player.img.height/2 + pos.y);
    if (vel.x < 0) {
      scale(-1, 1);
    }
    scale(1.1f);
    image(img, -img.width/2, -img.height/2);
    popMatrix();
  }
}
//------------Methods (cont)------------

public void decTimer(){ 
  coinTime--;
  keyTime--;
  tkeyTime--;
  redTime--;
  blueTime--;
}


public void spawnTokens(){
  if(coinTime==3500){
    for (int i=0; i<numCoin; i++){
    coinx=random(0,1600); coiny=random(0,1600);
    if(map[floor(coinx/tileSize)][floor(coiny/tileSize)]==0 && 
       map[floor((coinx-24)/tileSize)][floor((coiny-24)/tileSize)]==0 && 
       map[floor((coinx+24)/tileSize)][floor((coiny+24)/tileSize)]==0)
     tokens.add(new Coins(new PVector(coinx,coiny)));
    }
  }
  if(redTime==500){
    redx=random(80,128); redy=random(1272,1510);
    if(map[floor(redx/tileSize)][floor(redy/tileSize)]==0)
      tokens.add(new Red(new PVector(redx,redy)));
    redx=random(1225,1353); redy=random(1225,1353);
    if(map[floor(redx/tileSize)][floor(redy/tileSize)]==0)
      tokens.add(new Red(new PVector(redx,redy)));
  }
  
  if(blueTime==1100){
    for (int i=0; i<2; i++){
      bluex=random(64,1536); bluey=random(64,1536);
      if(map[floor(bluex/tileSize)][floor(bluey/tileSize)]==0 && 
         map[floor((bluex-24)/tileSize)][floor((bluey-24)/tileSize)]==0 && 
         map[floor((bluex+24)/tileSize)][floor((bluey+24)/tileSize)]==0)
        tokens.add(new Blue(new PVector(bluex,bluey)));
      }
  }
  if (tkeyTime==550){
    for (int i=0; i<2; i++){
      keyx=random(64,1536); keyy=random(64,1536);
      if(map[floor(keyx/tileSize)][floor(keyy/tileSize)]==0 && 
         map[floor((keyx-24)/tileSize)][floor((keyy-24)/tileSize)]==0 && 
         map[floor((keyx+24)/tileSize)][floor((keyy+24)/tileSize)]==0)
        tokens.add(new Key(new PVector(keyx,keyy)));
      }
  }
}


public void timerReset(){
  if(coinTime < 0)   coinTime = PApplet.parseInt(random(3540,3600));
  if(redTime < 0)    redTime = PApplet.parseInt(random(540,600));
  if(blueTime < 0)   blueTime = PApplet.parseInt(random(1140,1200));
  if(tkeyTime < 0)   tkeyTime = 610;
}


public void coinCheck(){
  for (int i = 0; i < tokens.size(); i++) {
    Token t1 = tokens.get(i);
    t1.update();
    if(tokens.size() > 0 ){
      if (t1.collision(p1) && tokens.get(i).toString().indexOf("Coin")!=-1){
        playSound("sounds/coin.mp3");
        tokens.remove(t1);
        stat.incCoin();
      }
    }
  }
}


public void redCheck(){
  for (int i = 0; i < tokens.size(); i++) {
    Token t1 = tokens.get(i);
    t1.update();
    if(tokens.size() > 0 ){
      if (t1.collision(p1) && tokens.get(i).toString().indexOf("Red")!=-1){
        playSound("sounds/red.mp3");
        tokens.remove(t1);
        p1.hp+=50;
      }
    }
  }
}


public void blueCheck(){
  for (int i = 0; i < tokens.size(); i++) {
    Token t1 = tokens.get(i);
    t1.update();
    if(tokens.size() > 0 ){
      if (t1.collision(p1) && tokens.get(i).toString().indexOf("Blue")!=-1){
        playSound("sounds/blue.mp3");
        tokens.remove(t1);
        p1.hp+=10;
      }
    }
  } 
}


public void keyCheck(){
  for (int i = 0; i < tokens.size(); i++) {
    Token t1 = tokens.get(i);
    t1.update();
    if(tokens.size() > 0 ){
      if (t1.collision(p1) && tokens.get(i).toString().indexOf("Key")!=-1){
        playSound("sounds/key.mp3");
        tokens.remove(t1);
        stat.inckey();
      }
    }
  } 
  
 if (keyTime==0){
  offLock();
 }
}


public void spawnDemon(){
  demons.add(new Enemy(new PVector(random(80,1500),random(320,1500)), new PVector(random(3,5),random(-1,1))));
}


public void loadFrames(PImage[] array, int n, String fname, int frameWidth, int frameHeight){
  PImage frame = loadImage(fname + ".png");
  for (int i = 0; i < n; i++) {
    PImage frame_tile = createImage(frameWidth, frameHeight, ARGB);
    frame_tile.copy(frame, i*frameWidth, 0, frameWidth, frameHeight, 0, 0, frameWidth, frameHeight);
    array[i] = frame_tile;
  }
}


public void gameOver(String str){
  background(40);
  fill(220);
  textSize(50);
  textAlign(CENTER);
  text(str, width/2, height/2);
}


public void gameStart(String str){
  controlP5.getController("Insert").show();
  controlP5.getController("Continue").hide();
  background(40);
  PFont mono1 = loadFont("Monospaced.plain-42.vlw");
  fill(220);
  textFont(mono1, 40);
  textAlign(CENTER);
  text("Insert Coin", width/2, 80);
  textFont(mono1, 18);
  text(str, width/2, height/2-100);
}


public void gameKey(String str){
  controlP5.getController("Continue").show();
  controlP5.getController("Insert").hide();
  background(40);
  PFont mono1 = loadFont("Monospaced.plain-42.vlw");
  fill(220);
  textFont(mono1, 16);
  textAlign(CENTER);
  text(str, width/2, height/2-100);
}


public void onLock(){
   for (int i = 0; i < map.length; i++) {
    for (int j = 0; j < map[i].length; j++) {
         if(map[i][j] == 76){
           if(fx2==1){
             tiles.remove(i*50+j-fx);
             fx++;
             map[i][j] = 75;
             String tile = "tile" + map[i][j] + ".jpg";
             tiles.add(new Map(tile, new PVector(i * tileSize, j * tileSize), false, demBlock));
           }
           if(fx2==2){
             tiles.remove(2496); 
             tiles.remove(2496);
             map[i][j] = 75;
             String tile = "tile" + map[i][j] + ".jpg";
             tiles.add(new Map(tile, new PVector(i * tileSize, j * tileSize), false, demBlock));
           }
         }
      }
    }
 }
 
 
 public void offLock(){
  for (int i = 0; i < map.length; i++) {
    for (int j = 0; j < map[i].length; j++) {
        if(map[i][j] == 75){
           map[i][j] = 76;
           String tile = "tile" + map[i][j] + ".jpg";
           tiles.add(new Map(tile, new PVector(i * tileSize, j * tileSize), true, demBlock));
         }
    }
  }
  fx2 = 2;
 }
 
 public void demonDifficulty(){
   if(demons.size()>0){
      if(p1.hp < 60 && demonRemoved==0) {
          demons.remove(0);
          demonRemoved = 1;
      }
      if(p1.hp < 30 && demonRemoved==1){
          demons.remove(0);
          demonRemoved = 2;
      }
   }
 }
 
 
 public void playSound(String name){
  sound = m.loadFile(name);
  sound.play();
}


public void controlEvent(ControlEvent theEvent){
  if (theEvent.getController().getName() == "Insert"){
    game = 0;
  }
  if (theEvent.getController().getName() == "Continue"){
    game = 0; 
  }
}
class Key extends Token {
  PVector pos;
  PImage[] key1 = new PImage[12];
  int  keyTime = -1;
  
  Key(PVector pos){
    super(pos);
    loadFrames(key1, key1.length, "key", 24, 24);
  }
  
  
 public void update() {
    frames = key1; 
    updateFrame(key1.length);
    
    if (tkeyTime<550 && tkeyTime>0){
        drawToken(p1);
      }
      if (tkeyTime<1){
        tokens.remove(this);
      }
 }

  public void drawToken(Character player) {
    super.drawToken(player);
  }
}
class Map {
  PVector pos, diff, absDiff;
  PImage img;
  boolean block, demBlock;

  Map(String tile, PVector pos, boolean block, boolean demBlock){
    img = loadImage(tile);
    this.pos = pos;
    this.block = block;
    this.demBlock = demBlock;
  }

  public void collision(Character p1){
    diff = PVector.sub(p1.pos, pos);
    absDiff = new PVector(abs(diff.x), abs(diff.y));
    if (block &&
    absDiff.x < p1.img.width/2+ img.width / 2 && 
    absDiff.y < p1.img.height/2 + img.height / 2) {
     p1.pos.x += diff.x*0.15f;
     p1.pos.y += diff.y*0.15f;
     p1.vel.mult(0.0f);
    }
  }
  
  public boolean inWindow(){
    if (absDiff.x < width && absDiff.y < height) {
      return true;
    }
    return false;
  }
  
  public void dmnCollision(Character p1){
    diff = PVector.sub(p1.pos, pos);
    absDiff = new PVector(abs(diff.x), abs(diff.y));
    if (demBlock && 
    absDiff.x < p1.img.width / 2 + img.width / 2 &&
    absDiff.y < p1.img.height / 2 + img.height / 2){
     p1.pos.x += diff.x*1;
     p1.pos.y += diff.y*1;
     p1.vel.mult(-1);
     }
  }

  public void drawMe(Character player){
    pushMatrix();
    translate( -player.pos.x + tileSize + player.img.width/2 + pos.x, 
     -player.pos.y + tileSize + player.img.height/2 + pos.y);

    scale(1.12f, 1.12f);
    image(img, -img.width/2, -img.height/2);
    popMatrix();
  }
}







//-----------------------The Map--------------------
int[][] map = {
{1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
{1,0,0,57,57,57,57,57,57,57,57,57,57,57,57,49,36,50,50,50,45,45,45,45,45,45,45,45,51,53,53,53,53,53,53,53,53,53,53,55,55,55,55,55,55,55,55,55,55,1},
{1,0,0,57,57,57,57,57,57,57,57,57,57,57,57,49,36,50,50,50,45,45,50,51,63,51,45,45,51,53,53,53,53,63,53,53,53,53,53,55,0,0,0,0,0,0,0,0,55,1},
{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,49,36,50,50,50,45,45,50,51,51,51,45,45,45,45,45,45,45,45,45,45,45,45,45,76,0,0,0,0,0,0,0,0,55,1},
{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,49,36,50,50,50,45,45,50,51,51,51,45,45,45,45,45,45,45,45,45,45,45,45,45,76,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,49,36,48,48,48,47,47,48,48,48,48,47,47,48,56,56,56,56,56,56,56,56,56,56,56,48,48,48,48,48,48,48,48,55,1},
{1,23,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,23,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,23,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,23,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,23,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,48,48,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,47,47,0,0,0,0,47,47,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,55,1},
{1,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,47,47,9,9,9,9,47,47,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,1},
{1,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,1},
{1,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,1},
{1,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,1},
{1,28,28,28,28,43,44,28,28,28,28,28,28,28,28,28,28,43,44,28,28,28,28,28,28,28,28,28,28,28,28,28,43,44,28,28,28,28,28,28,28,28,28,28,28,28,28,28,28,1},
{1,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,1},
{1,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,1},
{1,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,43,44,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,1},
{1,52,52,52,52,43,44,52,52,52,52,52,52,52,52,52,52,43,44,52,52,52,52,52,52,52,52,52,52,52,52,52,43,44,52,52,52,52,52,52,52,52,52,52,52,52,52,52,52,1},
{1,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,10,10,10,43,44,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,1},
{1,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,1},
{1,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,1},
{1,9,9,47,47,9,9,47,47,9,9,9,9,9,9,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,9,9,9,9,9,9,9,9,9,9,9,9,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,73,73,73,73,73,73,73,73,73,9,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,31,28,28,28,32,0,0,0,0,0,0,0,0,0,77,10,10,10,10,10,10,10,10,9,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,9,9,9,9,9,9,10,9,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,0,0,0,0,0,9,10,9,0,0,9,1},
{1,47,47,47,47,47,47,47,47,47,47,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,0,0,0,0,0,9,10,9,0,0,9,1},
{1,47,47,47,47,47,47,47,47,47,47,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,76,0,0,0,0,0,0,9,10,9,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,76,0,0,0,0,0,0,9,10,9,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,0,0,0,0,0,9,10,9,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,0,0,0,0,0,9,10,9,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,25,26,26,26,27,0,0,0,0,0,0,0,0,0,77,9,9,9,9,9,9,9,10,9,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,33,29,29,29,34,0,0,0,0,0,0,0,0,0,77,10,10,10,10,10,10,10,10,9,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,74,74,74,74,74,74,74,74,74,9,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,1},
{1,20,47,47,47,20,47,47,47,57,57,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,1},
{1,21,22,47,47,21,22,47,47,66,67,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,49,9,1},
{1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}};
class Player extends Character{
  float damp = 0.8f;
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
  
  public void idle(){
    frames = idle;  
  }
  
  public void run(){
    frames = run;
  }
  
  public void ded(){
    frames = ded;
    dying = true;
  }
  
  public void killed(){
    dead = true;
    game = 1;
    playSound("sounds/ded.mp3");
  }
  
 public void update() {
   if (!dying){
     super.update();
      vel.mult(damp);
      if (abs(vel.x) + abs (vel.y) < .3f){
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
  
  public void drawMe() {
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
class Red extends Token {
  PVector pos;
  PImage[] red = new PImage[7];
  
  Red(PVector pos){
    super(pos);
    loadFrames(red, red.length, "red1", 16, 24);
  }
  
  
 public void update() {
    frames = red; 
    updateFrame(red.length);
    
    if (redTime<500 && redTime>0){
        drawToken(p1);
      }
      if (redTime<1){
        tokens.remove(this);
      }
 }

  public void drawToken(Character player) {
    super.drawToken(player);
  }
}
class Stats {
  
  PFont mono = loadFont("Monospaced.bold-42.vlw");
  
  Stats(){
    
  }
  
  public void incCoin(){
      p1.coin+=1;
  }
  
  public void inckey(){
      p1.key1+=1;
  }
  
  public void deckey(){
      p1.key1-=1;
  }
  
  public void drawS() {
    pushStyle();
    noStroke();
    fill(128, 67, 160, 120);
    quad(0,0,200,0,180,30,0,30);
    quad(width,0,width-200,0,width-170,30,width,30);
    
    fill(255, 255, 255, 170);
    textFont(mono,12);
    text( p1.hp + "% :HP", width-45, 10);
    text(PApplet.parseInt(frameRate) + " :FPS", width-42, 25);
    textAlign(LEFT);
    text("Coins Collected: " + p1.coin, 5, 10);
    text("Keys Collected: " + p1.key1, 5, 25);
    popStyle();
  }
}
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
  
 public void update() {
 // to be overriden in respective subclasses;
 }

 public boolean collision(Character p1) {
    if (dist(pos.x, pos.y, p1.pos.x, p1.pos.y) < img.width/2 + img.width/2) {
      return true;
    }
      return false;
    }


 public void updateFrame(int n){
    if (frameCount % 6 == 0) {
      currentFrame++;
    }
    if (currentFrame == n) {
      currentFrame = 0;
    }
    currentFrame = currentFrame % frames.length;
    img = frames[currentFrame];
 }
 

 public void drawToken(Character player) {
   pushMatrix();
   translate(-player.pos.x + tileSize + player.img.width/2 + pos.x,
    -player.pos.y + tileSize + player.img.height/2 + pos.y);
   //scale(1);
   image(img, -img.width/2, -img.height/2);
   popMatrix();
 }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "project_5" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
