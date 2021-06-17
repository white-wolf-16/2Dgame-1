//------------Methods (cont)------------

void decTimer(){ 
  coinTime--;
  keyTime--;
  tkeyTime--;
  redTime--;
  blueTime--;
}


void spawnTokens(){
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


void timerReset(){
  if(coinTime < 0)   coinTime = int(random(3540,3600));
  if(redTime < 0)    redTime = int(random(540,600));
  if(blueTime < 0)   blueTime = int(random(1140,1200));
  if(tkeyTime < 0)   tkeyTime = 610;
}


void coinCheck(){
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


void redCheck(){
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


void blueCheck(){
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


void keyCheck(){
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


void spawnDemon(){
  demons.add(new Enemy(new PVector(random(80,1500),random(320,1500)), new PVector(random(3,5),random(-1,1))));
}


void loadFrames(PImage[] array, int n, String fname, int frameWidth, int frameHeight){
  PImage frame = loadImage(fname + ".png");
  for (int i = 0; i < n; i++) {
    PImage frame_tile = createImage(frameWidth, frameHeight, ARGB);
    frame_tile.copy(frame, i*frameWidth, 0, frameWidth, frameHeight, 0, 0, frameWidth, frameHeight);
    array[i] = frame_tile;
  }
}


void gameOver(String str){
  background(40);
  fill(220);
  textSize(50);
  textAlign(CENTER);
  text(str, width/2, height/2);
}


void gameStart(String str){
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


void gameKey(String str){
  controlP5.getController("Continue").show();
  controlP5.getController("Insert").hide();
  background(40);
  PFont mono1 = loadFont("Monospaced.plain-42.vlw");
  fill(220);
  textFont(mono1, 16);
  textAlign(CENTER);
  text(str, width/2, height/2-100);
}


void onLock(){
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
 
 
 void offLock(){
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
 
 void demonDifficulty(){
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
 
 
 void playSound(String name){
  sound = m.loadFile(name);
  sound.play();
}


void controlEvent(ControlEvent theEvent){
  if (theEvent.getController().getName() == "Insert"){
    game = 0;
  }
  if (theEvent.getController().getName() == "Continue"){
    game = 0; 
  }
}
