package nthu.nmsl.crowdsourcinggame.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import nthu.nmsl.crowdsourcinggame.R;

/**
 * Created by YingYi on 2016/4/7.
 */
public class OpenGLView extends GLSurfaceView {
    private GlRender mGlRender;
    private Context mContext;
    private Bitmap bitmap;
    int[] textures = new int[1];
    public OpenGLView(Context context){
        super(context);
        mContext = context;
        this.setEGLConfigChooser( 8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGlRender = new GlRender();
        setRenderer(mGlRender);



    }
    class GlRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl,EGLConfig config) {
            gl.glClearColor( 0.2f, 0.2f, 0.2f, 1.0f);// 3D背景色rgba

            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_FASTEST);
            //gl.glClearDepthf(1.0f);
            //gl.glDepthFunc(GL10.GL_LEQUAL);
            //gl.glEnable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl,int w,int h) {
            gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f);// 3D背景色rgba
            gl.glViewport(0, 0, w, h);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            float ratio;
            ratio = (float)w/h;
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 500);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            /*gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);// 3D背景色rgba
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();*/


            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);// 3D背景色rgba
            gl.glLoadIdentity();
            GLU.gluLookAt(gl, 0, 5, 9, 0, 0, 0, 0, 1, 0);
            gl.glFrontFace(GL10.GL_CCW);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            //設置光源
            /*gl.glEnable(GL10.GL_LIGHT1);
            FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.2f,0.0f,0.0f,1.0f});
            FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{0.8f,0.0f,0.0f,1.0f});
            FloatBuffer lightSpecular =  FloatBuffer.wrap(new float[]{1.0f,0.0f,0.0f,1.0f});
            FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{3.0f,4.0f,10.0f,1.0f});
            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);
            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);
            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, lightSpecular);
            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);
            gl.glEnable(GL10.GL_LIGHTING);*/

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, getFloatBuffer(cubeVtx));
            //透過索引緩衝來畫三角面
            gl.glDrawElements(GL10.GL_TRIANGLES,
                    cubeInx.length,
                    GL10.GL_UNSIGNED_SHORT,
                    getShortBuffer(cubeInx));
        }

        //頂點緩衝
        private FloatBuffer getFloatBuffer(float[] array ){
            ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(array);
            fb.position(0);
            return fb;
        }

        //索引緩衝
        private ShortBuffer getShortBuffer(short[] array ){
            ByteBuffer ib = ByteBuffer.allocateDirect(array.length * 2);
            ib.order(ByteOrder.nativeOrder());
            ShortBuffer sb = ib.asShortBuffer();
            sb.put(array);
            sb.position(0);
            return sb;
        }

        //頂點座標
        float cubeVtx[] = { -1.0f,  1.0f, -1.0f,   //v0
                -1.0f,  1.0f,  1.0f,   //v1
                1.0f,  1.0f,  1.0f,   //v2
                1.0f,  1.0f, -1.0f,   //v3
                -1.0f, -1.0f, -1.0f,   //v4
                -1.0f, -1.0f,  1.0f,   //v5
                1.0f, -1.0f,  1.0f,   //v6
                1.0f, -1.0f, -1.0f }; //v7
        //頂點索引
        short cubeInx[] = { 0, 1, 2,   //Top_Face1
                0, 2, 3,   //Top_Face2
                4, 6, 5,   //Bottom_Face1
                4, 7, 6,   //Bottom_Face2
                2, 6, 7,   //Left_face1
                2, 7, 3,   //Left_Face2
                0, 4, 1,   //Right_Face1
                1, 4, 5,   //Right_Face2
                1, 5, 6,   //Front_Face1
                1, 6, 2,   //Front_Face2
                0, 3, 4,   //Back_Face1
                3, 7, 4 }; //Back_Face2
    }
}

