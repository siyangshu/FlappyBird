package flappybird;

import processing.core.PApplet;

public class Ground extends MovingObject{
	public Ground(PApplet canvas, float curTime){
		super(canvas, curTime);
		x = 0;
		y = 0.9f * windowHeight;
		collisionRect = new Rect[]{new Rect(0, 0, windowWidth, 0.1f * windowHeight)};
	}
	public void drawObject(){
		for(Rect r : collisionRect){
			((FlappyBird)canvas).rect(r.move(x, y));
		}
	}
}
