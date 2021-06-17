float speed = 1;
int spdChk = 0;
PVector upForce = new PVector(0, -speed);
PVector leftForce = new PVector(-speed, 0);
PVector rightForce = new PVector(speed, 0);
PVector downForce = new PVector(0, speed); 
boolean up, left, right, down, space;

void keyPressed() {
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

void keyReleased() {
  if (key == CODED) {
    if (keyCode == UP) up = false;
    else if (keyCode == LEFT) left = false;
    else if (keyCode == RIGHT) right = false;
    else if (keyCode == DOWN) down = false;
  }
}

void controls(){
  if (up) p1.move(upForce);
  if (down) p1.move(downForce);
  if (left) p1.move(leftForce);
  if (right) p1.move(rightForce);
}


void spdCheck(){
  if (spdChk%2 == 0){
      upForce = new PVector(0, -2.5);
      leftForce = new PVector(-2.5, 0);
      rightForce = new PVector(2.5, 0);
      downForce = new PVector(0, 2.5);
    }
  if (spdChk%2 != 0){
    upForce = new PVector(0, -speed);
    leftForce = new PVector(-speed, 0);
    rightForce = new PVector(speed, 0);
    downForce = new PVector(0, speed);
  }
}
