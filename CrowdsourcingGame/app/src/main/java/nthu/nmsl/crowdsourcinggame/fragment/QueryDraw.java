package nthu.nmsl.crowdsourcinggame.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class QueryDraw extends View {

	int width = 250;
	int height = 250;
	int left;
	int top;
	int right;
	int bottom;
	
	public QueryDraw(Context context){
		super(context);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Log.d("test", "(" + getLeft() + "," + getTop() + ") (" + getRight() + "," + getBottom() + ")");
		Paint p1 = new Paint();
		p1.setAlpha(100);
		p1.setStyle(Paint.Style.STROKE);
		p1.setStrokeWidth(4);
		p1.setColor(0xFFAA0000);
		
		left = (getRight()-width)/2;
		top = (getBottom()-height)/2;
		right = left + width;
		bottom = top + height;
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRect(left, top, right, bottom, p1);
		
	}
	
}
