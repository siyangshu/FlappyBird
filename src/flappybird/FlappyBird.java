package flappybird;

import processing.core.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
//import java.awt.*;
//import java.awt.List;
import java.awt.event.*;

/*
 * units:
 * time: milliseconds (ms)
 * velocity: pixels per ms
 * drag: refer to the force which is drag*v*v
 * 
 * All the objects in the window extend MovingObject
 */
public class FlappyBird extends PApplet {
	int windowWidth = 480;
	int windowHeight = 640;
	final int frameRate = 60;
	Clock clock;
	int frameCount;
	double curTime;
	double frameTime;
	State curState = State.Init;    // current state of the game.
	int points;
	List<MovingObject> barrier;
	Bird bird;  // need reference when call jump()

	public void setup() {
		size(windowWidth, windowHeight);
		background(200, 255, 0);
		frameRate(frameRate);
	}

	public void draw() {
		checkWindowSize();
		switch(curState) {
		case Init:
			barrier = new CopyOnWriteArrayList<MovingObject>();
			barrier.add(new BackGround(this, 0));  // background object. it needs to be the first element of barrier
			barrier.add(new Ground(this, 0));
			bird = new Bird(this, 0);
			clock = new Clock();
			frameCount = 0;
			frameTime = 1000.0 / frameRate;
			curTime = 0;
			points = 0;
			
			curState = State.Waiting;
			break;
		case Waiting:
			// draw all objects
			for (MovingObject x : barrier){
				x.draw(0);
			}
			bird.draw(0);
			break;
		case Run:
			updateScene();
			// draw all objects
			for (MovingObject x : barrier){
				x.draw(clock.getTime());
			}
			bird.draw(clock.getTime());
			textSize(32);
			fill(0,0,0);
			text(""+points, windowWidth-32,32);
			fill(255,255,255);
			break;
		case Dead:
			fill(100, 100, 100);
			rect(0, 0, width, height);
			fill(100,0,0);
			text("Game over", 0.35f * windowWidth, 0.5f * windowHeight);
			fill(255, 255, 255);
			break;
		default:
			break;	
		}
	}

	private void updateScene(){
		frameCount++;
		curTime += frameTime;
//		curTime = 1000.0 * frameCount / frameRate;
		removeOutsideObject();
		if(isDead()){
			curState = State.Dead;
			clock.stop();
		}
		points += gainPoint();
	}

	private void removeOutsideObject(){
		// remove objects which are out of the screen
		for (int i = 0; i < barrier.size(); ){
			if(barrier.get(i).isOutOfScreen()){
				barrier.remove(i);
			}else{
				i++;
			}
		}
	}
	
	private int gainPoint(){
		int points = 0;
		for(MovingObject mo : barrier){
			if(bird.ifGainPoint(mo))
				points++;
		}
		return points;
	}
	
	private boolean isDead(){
		// if is end of game
		for (MovingObject i : barrier){
			if(bird.ifCollide(i)){
				return true;
			}
		}	
		return false;
	}
	
	private void checkWindowSize(){
		// if the screen size has changed
		if(barrier == null){
			return;
		}
		if(width != windowWidth || height != windowHeight){
			for (MovingObject x : barrier){
				windowWidth = width;
				windowHeight = height;
				x.resize(windowWidth, windowHeight);
		
			}			
		}
	}

	private void addColumn(){
		barrier.add(new Column(this, clock.getTime()));
	}
	
	private void addEnemyBird(){
		barrier.add(new EnemyBird(this, clock.getTime(), bird));
	}

	public void rect(Rect r){
		rect(r.getX1(), r.getY1(), r.getX2() - r.getX1(), r.getY2() - r.getY1());		
	}

	public void keyPressed(){
		if (key == ' '){
			switch(curState){
			case Waiting:
				clock.start();
				curState = State.Run;
				bird.jump(clock.getTime());
				break;
			case Dead:
				curState = State.Init;
				break;
			case Run:
				bird.jump(clock.getTime());
				break;
			}			
		}else if (key == CODED && keyCode == SHIFT){
			clock.setRate(2);
		}
	}
	
	public void keyReleased(){
		if (key == CODED && keyCode == SHIFT){
			clock.setRate(1);
		}
	}
		
	public class Clock {
		private double rate;   // time flow rate
		private double lastTime;
		private long lastSystemTime;
		private long lastRemindTime;
		private Timer timer;
		TaskAddColumn addColumn;
		TaskAddEnemyBird addEnemyBird;
		
		
		public Clock(){
			rate = 1;
			lastTime = 0;
			lastSystemTime = -1;
			timer = null;
		}
		
		public void start(){
			lastSystemTime = System.currentTimeMillis();
			lastRemindTime = System.currentTimeMillis();
			stop();
			timer = new Timer(true);
			addColumn = new TaskAddColumn();
			addEnemyBird = new TaskAddEnemyBird();
			timer.schedule(addColumn, 0, addColumn.taskPeriod);
			timer.schedule(addEnemyBird, 0, addEnemyBird.taskPeriod);
		}
		
		public void stop(){
			if(timer != null){
				timer.cancel();
				timer = null;
			}
			if(addColumn != null){
				addColumn = null;
			}
		}
			
		public void setRate(double r){
			rate = r;
			stop();
			addColumn = new TaskAddColumn();
			addEnemyBird = new TaskAddEnemyBird();
			timer = new Timer();
			long delay = addColumn.taskPeriod + lastRemindTime - System.currentTimeMillis();
			long period = (long)(addColumn.taskPeriod/rate);
			timer.schedule(addColumn, delay, period);
			timer.schedule(addEnemyBird, delay, addEnemyBird.taskPeriod);
		}
		
		

		public int getTime(){
			if(lastSystemTime < 0){
				lastSystemTime = System.currentTimeMillis();
			}
			long curSystemTime  = System.currentTimeMillis();
			lastTime += rate * (curSystemTime - lastSystemTime);
			lastSystemTime = curSystemTime;
			return (int)lastTime;
		}
		
		public class TaskAddColumn extends TimerTask{
			long taskPeriod = 2000;   // every taskPeriod, add a column
			public TaskAddColumn(){
				super();
			}
			public void run(){
				// add code here
				// every taskPeriod, call this function
				addColumn();
				lastRemindTime = System.currentTimeMillis();
			}
		}
		
		public class TaskAddEnemyBird extends TimerTask{
			long taskPeriod = 3000;   // every taskPeriod, add a column
			public TaskAddEnemyBird(){
				super();
			}
			public void run(){
				// add code here
				// every taskPeriod, call this function
				addEnemyBird();
				lastRemindTime = System.currentTimeMillis();
			}
		}
		

	}

}




