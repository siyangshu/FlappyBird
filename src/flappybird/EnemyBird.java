package flappybird;

import processing.core.PApplet;
import processing.core.PImage;

public class EnemyBird extends MovingObject{
	PImage img;
	Bird bird;
	float v;      // enemy bird keep this velocity. And change its dirction
	
	public EnemyBird(PApplet c, float curTime, Bird b) {
		super(c, curTime);
		bird = b;
		v = 0.5f;
		x = windowWidth;
		y = 0;
		collisionRect = new Rect[]{new Rect(0,0, 5, 5)};
		img = canvas.loadImage("https://raw.githubusercontent.com/tfgit/CIT591/master/red_angry.png");
		// TODO Auto-generated constructor stub
	} 
	
	protected void update(int curTime){
		// update location and velocity
		if(x > bird.x + 50){
			float dis = (float)Math.sqrt((x-bird.x)*(x-bird.x) + (y-bird.y)*(y-bird.y));
			vx = v * (bird.x - x) / dis;
			vy = v * (bird.y - y) / dis;
		}
		super.update(curTime);
	}
	
	protected void drawObject(){
		// canvas.ellipse(x, y, 10, 10);
		canvas.image(img, x, y);
	}
}
