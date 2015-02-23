package flappybird;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class Column extends MovingObject{
	PImage topPic, botPic;
	float width;
	float gapLocation = windowHeight * 0.7f;
	float gapHeight = 150;
	float r = new Random().nextFloat();

	public Column(PApplet c, float curTime){
		super(c, curTime);
		width = 0.15f * windowWidth;
		x = windowWidth;
		y = 0;
		vx = -0.15f;
		collisionRect = new Rect[2];
		collisionRect[0] = new Rect(0, 0, width, r * gapLocation);
		collisionRect[1] = new Rect(0, r * gapLocation + gapHeight, width, windowHeight);
		pointRect = new Rect[1];
		pointRect[0] = new Rect(width, 0, width * 2, windowHeight);
		topPic = canvas.loadImage("https://raw.githubusercontent.com/tfgit/FlappySwift/master/FlappyBird/Images.xcassets/PipeDown.imageset/PipeDown.png");
		botPic = canvas.loadImage("https://raw.githubusercontent.com/tfgit/FlappySwift/master/FlappyBird/Images.xcassets/PipeUp.imageset/PipeUp.png");
	}

	public void drawObject(){
		for(Rect r : collisionRect){
			((FlappyBird)canvas).rect(r.move(x, y));
		}
	}

}

