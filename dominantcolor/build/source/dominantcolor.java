import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.lang.Math; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class dominantcolor extends PApplet {



PFont font;
// max colors
int maxCol = 256;
// number of palette colors
int numCol;
// more than maxCol on palette
boolean overColor;
// define palette
int[] palette = new int[maxCol];
double[] dist = new double[maxCol];
// Max distance between colors
float maxDist;
// image parameters
PImage img;
String imgName;
int imgW, imgH;
float scale;
int totPixels, numPixel;
// distance method
// 1 ->  CIE74
// 2 ->  CIE94 (graphic arts)
// 3 ->  CIE94 (textiles)
int distMethod;
float imgX, imgY;

int pCol;
//GUI

//double delta;

public void setup()
{
  
  

  // set globar var
  maxDist = 10;
  distMethod = 2;
  // load image
  imgName = "demo1.jpg";
  img = loadImage(imgName);
  // set image GUI values
  imgW = width;
  imgH = height;
  totPixels = imgW * imgH;
  numPixel = 0;
  imgX = 0;
  imgY = 170;

  init();

  createPalette(img);

}

public void init()
{

    numCol = 0;
  overColor = false;
  pCol = color(0);
  numPixel = 0;

  for (int i=0; i < maxCol; i++)
  {
    //palette[i] = (int)sqrt(-1);
    palette[i] = color(255);

    dist[i] = 0.0f;
  }

}

//*********************************
public void draw()
{
  image(img, 0, 0);

    // draw palette on canvas
    drawPalette();
    // draw picked color
    stroke(0xff93a1a1);
    fill(pCol);
    rect(width-46, imgY, 20, 20);
    fill(0);
    text("#" + hex(pCol,6), width-60, imgY + 32);
}


//*********************************
public void drawPalette()
{
  int s = 20;
  int r = 8;
  int c = 32;
  int x = 0;
  int y = 0;
  int k = 0;

  if (numCol != 0)
  {
  noStroke();
  for (int i = 0; i < r; i++)
  {
    if (numCol < k+1) { break; }
    for (int j = 0; j < c; j++)
    {
      if (numCol < k+1) { break; }
      fill(palette[k]);
      rect(x + (j*s), y + (i*s), s, s);
      k++;
    }
  }
  }
}


//*********************************
// pick some random good color
public int somecolor()
{
  return palette[PApplet.parseInt(random(0, numCol))];
}

//*********************************



public void createPalette(PImage imgPal)
{
  //PImage b;
  //int x=0, y=0;
  //b = loadImage(fn);
  //image(b,0,0);
  imgPal.loadPixels();
  imgW = width;
  imgH = height;
  totPixels = imgW * imgH;
  numPixel = 0;
  numCol = 0;

   palette[0] = pCol;
   numCol = 1;

  // scan image pixels
  for (int x=0; x < width; x++)
  {
    for (int y=0; y < height; y++)
    {
      numPixel++;
      // get color
      //color c = imgPal.get(x, y);
      int c = imgPal.pixels[y*width+x];
      boolean exists = false;
      // check if color exists
      for (int n=0; n<numCol; n++)
      {
        //if (c == palette[n])
        if (deltaE(c, palette[n], distMethod) < maxDist)
        {
          //println(deltaE(c, palette[n]));
          exists = true;
          break;
        }
      }
      // add color to paletteA6693A
      if (!exists)
      {
        // palette is full ?
        if (numCol<maxCol)
        {
          palette[numCol] = c;
          numCol++;
        }
        // exit function
        else
        {
          //println("More than " +maxCol+ " colors...");
          overColor = true;
          x = width;
          y = height;
          numPixel = totPixels;
          //println(numCol);
          break;
        }
      }
    }
  }
  pCol = palette[0];

  //println(hex(pCol,6));

  //println(numCol);
}
// Button[] buttons = new Button[N];
// buttons[0] = new Button( 20, 20, 60, 30, "btn00Method", "btn_00", color(128), color(255) );
class Button 
{
  float x, y, w, h;
  String s, t;
  int c1, c2;

  // constructor
  Button(float ix, float iy, float iw, float ih, String is, String it, int ic1, int ic2) 
  {
    x=ix; // start x
    y=iy; // start y
    w=iw; // width
    h=ih; // height
    s=is; // method
    t=it; // text
    c1=ic1; // fill color
    c2=ic2; // stroke color
  }
  public boolean isOver() 
  {
    return(mouseX>=x&&mouseX<=x+w&&mouseY>=y&&mouseY<=y+h);
  }
  public void onClick() 
  {
    if (isOver() && !s.equals("")) method(s);
  }
  public void show() 
  {
    pushStyle();
    fill(c1);
    stroke(c2);
    rect(x, y, w, h);
    fill(0);
    textAlign(CENTER,CENTER);
    text(t,x+w/2.0f+1,y+h/2.0f-1);
    popStyle();
  }
}

// Spinner[] spinners = new Spinner[N];
class Spinner 
{
  float x, y, w, h;
  float v, s;
  int c, k, q;
  String m;

  Spinner(float ix, float iy, float iw, float ih, float iv, float is, int ic, String im) 
  {
    x=ix; // start x
    y=iy; // start y
    w=iw; // width
    h=ih; // height
    v=iv; // start value
    s=is; // step value
    c=ic; // color
    m=im; // method
  }
  
  public boolean isOver() 
  {
    return(mouseX>=x&&mouseX<=x+w&&mouseY>=y&&mouseY<=y+h);
  }
  
  public void onClick() 
  {
    //if (isOver() && !s.equals("")) method(s);
    if (isOver() && !m.equals(""))
    {
      if ((mouseX>x)&&(mouseX<x+w/4)&&(mouseY>=y)&&(mouseY<=y+h)) //left press
      {
        v=v-s; method(m);
      }
      if ((mouseX>x+3*w/4)&&(mouseX<x+w)&&(mouseY>=y)&&(mouseY<=y+h)) //right press
      {
        v=v+s; method(m);
      }        
    }
  }
  public void setValue(float u)
  {
    v = u;
  }
  
  public float getValue()
  {
    return(v);
  }  
  
  public void show() 
  {
    pushStyle();
    stroke(0);
    fill(c);
    rect(x, y, w, h);
    line(x+w/4,y,x+w/4,y+h);
    line(x+3*w/4,y,x+3*w/4,y+h);
    fill(0);
    textAlign(CENTER, CENTER);
    text(nf(v,0,0),x+w/2.0f,y+h/2.0f-1);
    fill(20);
    text("-",x+w/8.0f,y+h/2.0f-2);
    text("+",x+7*w/8.0f,y+h/2.0f-2);
    popStyle();
  }
}  
  

// SpinBound[] spinB = new SpinBound[N];
class SpinBound 
{
  float x, y, w, h;
  float v, s;
  float minv, maxv;
  int c, k, q;
  String m;

  SpinBound(float ix, float iy, float iw, float ih, float iv, float is, float imin, float imax, int ic, String im) 
  {
    x=ix; // start x
    y=iy; // start y
    w=iw; // width
    h=ih; // height
    v=iv; // start value
    s=is; // step value
    minv = imin; // min value
    maxv = imax; // max value
    c=ic; // color
    m=im; // method
  }
  public boolean isOver() 
  {
    return(mouseX>=x&&mouseX<=x+w&&mouseY>=y&&mouseY<=y+h);
  }

  public void onClick() 
  {
    //if (isOver() && !s.equals("")) method(s);
    if (isOver() && !m.equals(""))
    {
      if ((mouseX>x)&&(mouseX<x+w/4)&&(mouseY>=y)&&(mouseY<=y+h)) //left press
      {
        v = constrain(v-s, minv, maxv);
        method(m);
      }
      if ((mouseX>x+3*w/4)&&(mouseX<x+w)&&(mouseY>=y)&&(mouseY<=y+h)) //right press
      {
        v = constrain(v+s, minv, maxv);
        method(m);
      }        
    }
  }
 
  public void setValue(float u)
  {
    //u = constrain(u, minv, maxv);
    v = u;
  }
  
  public float getValue()
  {
    return(v);
  }  
  
  public void show() 
  {
    pushStyle();
    stroke(0xff93a1a1);
    fill(c);
    rect(x, y, w, h);
    line(x+w/4,y,x+w/4,y+h);
    line(x+3*w/4,y,x+3*w/4,y+h);
    fill(0);
    textAlign(CENTER, CENTER);
    text(nf(v,0,0),x+w/2.0f,y+h/2.0f-1);
    fill(20);
    text("-",x+w/8.0f,y+h/2.0f-2);
    text("+",x+7*w/8.0f,y+h/2.0f-2);
    popStyle();
  }
}

class Checkbox
{
  float x, y, w, h;
  String t;  
  boolean s;
  int c, k;
  // constructor
  Checkbox(float ix, float iy, float iw, float ih, String it, boolean is, int ic, int ik) 
  {
    x=ix; // start x
    y=iy; // start y
    w=iw; // width
    h=ih; // height
    t=it; // text    
    s=is; // status
    c=ic; // unactive color
    k=ik; // active color
  }
  public boolean isOver() 
  {
    return(mouseX>=x&&mouseX<=x+w&&mouseY>=y&&mouseY<=y+h);
  }
  public void onClick() 
  {
    if (isOver())
    {
       s=!(s);
    }   
  }
  public void draw() 
  {
    pushStyle();
    stroke(0);
    fill(isOver()?k:c);
    rect(x, y, w, h);
    if (s==true)
    {
      stroke(255,128,0);
      line(x+1,y+1,x+w-1,y+h-1);
      line(x+w-1,y+1,x+1,y+h-1);
    }
    //fill(0);
    fill(0xffeee8d5);
    textAlign(LEFT,CENTER);
    text(t,x+w+3,y+h/2-2);
    popStyle();
  }
}
// https://en.wikipedia.org/wiki/Color_difference
//*********************************
public double deltaE(int col1, int col2, int m)
{
  double result = 0.0f;
  if (col1 == col2) { return result; }
  //if (col1==color(0,0,0)) {col1=color(1,1,1);}
  //if (col2==color(0,0,0)) {col2=color(1,1,1);}
  double[] xyz1 = rgb2xyz(col1);
  double[] lab1 = xyz2lab(xyz1);

  double[] xyz2 = rgb2xyz(col2);
  double[] lab2 = xyz2lab(xyz2);

  double c1 = Math.sqrt(lab1[1]*lab1[1]+lab1[2]*lab1[2]);
  double c2 = Math.sqrt(lab2[1]*lab2[1]+lab2[2]*lab2[2]);
  double dc = c1-c2;
  double dl = lab1[0]-lab2[0]; // lightness difference
  double da = lab1[1]-lab2[1]; // color difference
  double db = lab1[2]-lab2[2]; // opponent difference

  double dh = Math.sqrt((da*da)+(db*db)-(dc*dc));

  // color distance CIE76
  // deltaE = 2.3 correspond to JND (just noticeable difference)
  if (m == 1)
  {
    result = Math.sqrt((da*da)+(db*db)+(dc*dc));
    println(result + " " + ((da*da)+(db*db)+(dc*dc)));
    if (isNaN(result)) { println("is NaN"); };
  }

  double primo, secondo, terzo;

  // color distance CIE94 (graphic arts)
  if (m == 2)
  {
    primo = dl;
    secondo = dc / (1.0f + 0.045f*c1);
    terzo = dh / (1.0f + 0.015f*c1);
    result = (Math.sqrt(primo*primo + secondo*secondo + terzo*terzo));
    if (isNaN(result)) { println("is NaN"); };

  }

  // color distance CIE94 (textiles)
  if (m == 3)
  {
    primo = dl / 2.0f;
    secondo = dc / (1.0f + 0.048f*c1);
    terzo = dh / (1.0f + 0.014f*c1);
    result = (Math.sqrt(primo*primo + secondo*secondo + terzo*terzo));
    if (isNaN(result)) { println("is NaN"); };
  }
  return result;
}

//*********************************
public double [] rgb2xyz(int rgb) {

  double[] result = new double[3];

  //double rr = red(rgb)/255.0;
  //double gg = green(rgb)/255.0;
  //double bb = blue(rgb)/255.0;
  double rr = ((rgb >> 16) & 0xFF) / 255.0f;
  double gg = ((rgb >> 8) & 0xFF) / 255.0f;
  double bb = (rgb & 0xFF) / 255.0f;

  if (rr > 0.04045f) {
    rr = (rr + 0.055f) / 1.055f;
    rr = Math.pow(rr, 2.4f);
  } else {
    rr = rr / 12.92f;
  }
  if (gg > 0.04045f) {
    gg = (gg + 0.055f) / 1.055f;
    gg = Math.pow(gg, 2.4f);
  } else {
    gg = gg / 12.92f;
  }
  if (bb > 0.04045f) {
    bb = (bb + 0.055f) / 1.055f;
    bb = Math.pow(bb, 2.4f);
  } else {
    bb = bb / 12.92f;
  }

  bb *= 100.0f;
  rr *= 100.0f;
  gg *= 100.0f;

  result[0] = rr * 0.4124f + gg * 0.3576f + bb * 0.1805f;
  result[1] = rr * 0.2126f + gg * 0.7152f + bb * 0.0722f;
  result[2] = rr * 0.0193f + gg * 0.1192f + bb * 0.9505f;

  return result;
}

//*********************************
public double [] xyz2lab(double[] xyz) {

  double[] result = new double[3];

  // luminance values
  double x = xyz[0] / 95.047f;
  double y = xyz[1] / 100.0f;
  double z = xyz[2] / 108.8900f;

  if (x > 0.008856f) {
    x = Math.pow(x, 1.0f/3.0f);
  } else {
    x = 7.787f*x + 16.0f/116.0f;
  }
  if (y > 0.008856f) {
    y = Math.pow(y, 1.0f/3.0f);
  } else {
    y = (7.787f*y) + (16.0f/116.0f);
  }
  if (z > 0.008856f) {
    z = Math.pow(z, 1.0f/3.0f);
  } else {
    z = 7.787f*z + 16.0f/116.0f;
  }

  result[0] = 116.0f*y - 16.0f;
  result[1] = 500.0f*(x-y);
  result[2] = 200.0f*(y-z);

  return result;
}

public boolean isNaN(double x)
{
  return (x != x);
}
  public void settings() {  size(400, 400);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dominantcolor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
