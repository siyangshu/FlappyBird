package flappybird;

import processing.core.PApplet;

public class BackGround extends MovingObject{
	public BackGround(PApplet c, float curTime) {
		super(c, curTime);
		// TODO Auto-generated constructor stub
	}
	public void drawObject(){
		canvas.background(200,255,0);
	}
}
