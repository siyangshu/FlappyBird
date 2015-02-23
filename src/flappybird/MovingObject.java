package flappybird;

import processing.core.PApplet;
/*
 * Base class
 * 
 * When extending this class, these method need to be overloaded:
 * Constructor: 
 * 		take 2 parameters.
 * 		call super(,) and set initial values of x, y, and others if not 0
 * 		add Rect to collisionRect. They will be used when calculating collision.
 * drawObject:
 * 		use canvas.method() to draw object
 * 		location is (x, y), and size is proportional to (windowWidth, windowHeight)
 * 
 * These method might need to be overloaded:
 * resize:
 * 		will be called when the window size has changed.
 * 		
 */
public class MovingObject {
	protected int windowWidth;
	protected int windowHeight;
	protected int lastUpdateTime;
	// these parameters will be updated automatically
	protected float x;         // position of x
	protected float y;         // position of y
	protected float vx;         // velocity of x
	protected float vy;         // velocity of y
	protected float drag;       // air resistance
	protected float gravity;    // gravity in y axis
	// indicates the object's collision area
	protected Rect[] collisionRect = null;   
	// when it is overlapped with other's collision area, gain points and set pointRect to null
	// that is, although pointRect is an array, only 1 point can be gained from them.
	protected Rect[] pointRect = null;       
	// use this to draw geometry
	PApplet canvas;
	
	public MovingObject(PApplet c, float curTime){
		windowWidth = c.width;
		windowHeight = c.height;
		canvas = c;
		lastUpdateTime = (int)curTime;
	}
	
	public void resize(int width, int height){
		// Might need to be overload.
		// when the screen sized is changed, resize all the information.
		float xScale = 1.0f * width / windowWidth;
		float yScale = 1.0f * height / windowHeight;
		x *= xScale;
		y *= yScale;
		vx *= xScale;
		vy *= yScale;
		gravity *= yScale;
		// drag will not change
		// resize Rect in collisionRect
		collisionRect = resizeRects(collisionRect, xScale, yScale);
		pointRect = resizeRects(pointRect, xScale, yScale);
		this.windowWidth = width;
		this.windowHeight = height;
	}
	
	private Rect[] resizeRects(Rect[] r, float xScale, float yScale){
		if(r == null)
			return null;
		Rect[] cr = new Rect[r.length];
		for(int i = 0; i < r.length; i++){
			cr[i] = new Rect(r[i].getX1() * xScale, r[i].getY1() * yScale, r[i].getX2() * xScale, r[i].getY2() * yScale);
		}
		return cr;
	}

	public void draw(float curTime){
		update((int)curTime);
		drawObject();
	}
	
	protected void update(int curTime){
		// update location and velocity
		int dTime = curTime - lastUpdateTime;
		lastUpdateTime = (int)curTime;
		x += vx * dTime;
		y += vy * dTime;
		vx = calcV(vx, 0, drag, dTime);
		vy = calcV(vy, gravity, drag, dTime);
	}
	
	protected void drawObject() {}
		
	
	
	public boolean isOutOfScreen(){
		// if is out of screen, return true (and the parent function will remove this object)
		return x < -windowWidth
				|| x > 2 * windowWidth
				|| y < -windowHeight
				|| y > 2 * windowHeight;
	}
	
	public boolean ifCollide(MovingObject mo){
		return ifRectsCollide(collisionRect, x, y, mo.collisionRect, mo.x, mo.y);
	}
	
	public boolean ifGainPoint(MovingObject mo){
		if(ifRectsCollide(collisionRect, x, y, mo.pointRect, mo.x, mo.y)){
			mo.pointRect = null;
			return true;
		}
		if(ifRectsCollide(pointRect, x, y, mo.collisionRect, mo.x, mo.y)){
			pointRect = null;
			return true;
		}
		return false;
	}
	
	private boolean ifRectsCollide(Rect[] r1, float x1, float y1, Rect[] r2, float x2, float y2){
		// Rects in r1 will be translated to (x1, y1) (temporarily), Rects in r2 will be translated to (x2, y2)
		// if any Rect in r1 and r2 intersects return true. Else return false
		if(r1 == r2){
			return false;
		}
		if(r1 != null && r2 != null){
			for(int i = 0; i < r1.length; i++){
				for(int j = 0; j < r2.length; j++){
					if(r1[i].move(x1, y1).isIntersect(r2[j].move(x2, y2))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private float calcV(float v, float a, float drag, int dTime){
		// given velocity, acceleration, drag, what will velocity be after dTime?
		int divisionNum = 10;
		if(drag * v * v * dTime > 0.5){
			// drag is high, so we need to divide it to several parts and calculate. Just like the integral.
			if(dTime < divisionNum){
				System.err.println("drag is too high! please adjust it");
				return 0;
			}
			int dTimeAfterDivision = dTime / divisionNum;
			for(int i = 0; i < divisionNum; i++){
				v = calcV(v, a, drag, dTimeAfterDivision);
			}
			v = calcV(v, a, drag, dTime - dTimeAfterDivision * divisionNum);
		}else{
			v += a * dTime;
			v *= (1 - drag * v * v * dTime);
		}
		return v;
	}

}
