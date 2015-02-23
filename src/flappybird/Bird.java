package flappybird;

import processing.core.PApplet;
import processing.core.PImage;

public class Bird extends MovingObject{	
	PImage img;
	float upMax = 0;
	Bird(PApplet canvas, float curTime){
		super(canvas, curTime);
		x = 0.5f * windowWidth;
		y = 0.5f * windowHeight;
		drag = 0.005f;
		gravity = 0.0015f;
		collisionRect = new Rect[]{new Rect(0, 0, 10, 10)};
		img = canvas.loadImage("https://raw.githubusercontent.com/tfgit/FlappySwift/master/FlappyBird/bird.atlas/bird-01.png");
	}
	protected void drawObject(){
		// canvas.ellipse(x, y, 10, 10);
		canvas.image(img, x, y);
	}
	protected void update(int curTime){
		super.update(curTime);
		if(y < upMax){
			y = upMax;
			vy = 0;
		}
	}
	public void jump(int millisecond){
		this.draw(millisecond);
		vy = -0.5f;
	}
}
