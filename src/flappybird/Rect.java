package flappybird;

public class Rect {
	
	public Rect(float x1, float y1, float x2, float y2){
		this.x[0] = x1;
		this.y[0] = y1;
		this.x[1] = x2;
		this.y[1] = y2;
	}
	
	public float getX1(){
		return x[0];
	}
	public float getY1(){
		return y[0];
	}
	public float getX2(){
		return x[1];
	}
	public float getY2(){
		return y[1];
	}
	
	public Rect move(float dx, float dy){
		return new Rect(x[0] + dx, y[0] + dy, x[1] + dx, y[1] + dy);
	}
	
	public boolean isIntersect(Rect r){
		for(int i = 0; i <= 1; i++){
			for(int j = 0; j <= 1; j++){
				if(isInside(r.x[i], r.y[j])
						||r.isInside(x[i], y[j]))
					return true;
			}
		}
		return false;
	}
	
	private boolean isInside(float xx, float yy){
		return xx > x[0]
				&& xx < x[1]
				&& yy > y[0]
				&& yy < y[1];
	}
	
	private final float []x = new float[2];
	private final float []y = new float[2];	
}
