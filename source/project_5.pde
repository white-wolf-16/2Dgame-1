// Badhan Dey - v.5(Sat).3(Nt) - Final

import controlP5.*;
ControlP5 controlP5;
  
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;


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
int coinTime = int(random(3540,3600)), keyTime = -1, redTime = int(random(540,600)), blueTime = int(random(1140,1200)), tkeyTime = 610;
float coinx, coiny, redx, redy, bluex, bluey, keyx, keyy;



//-----------------SETUP---------------------
void setup() {
  size(600, 600);
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

void draw() {
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

void gamePlay() {
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
