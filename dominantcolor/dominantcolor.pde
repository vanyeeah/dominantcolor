import java.lang.Math;

PFont font;
// max colors
int maxCol = 256;
// number of palette colors
int numCol;
// more than maxCol on palette
boolean overColor;
// define palette
color[] palette = new color[maxCol];
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

color pCol;
//GUI

//double delta;

void setup()
{
  size(400, 400);
  smooth();

  // set globar var
  maxDist = 10;
  distMethod = 2;
  // load image
  imgName = "demo2.jpg";
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

void init()
{

    numCol = 0;
  overColor = false;
  pCol = color(0);
  numPixel = 0;

  for (int i=0; i < maxCol; i++)
  {
    //palette[i] = (int)sqrt(-1);
    palette[i] = color(255);

    dist[i] = 0.0;
  }

}

//*********************************
void draw()
{
  image(img, 0, 0);

    // draw palette on canvas
    drawPalette();
    // draw picked color
    stroke(#93a1a1);
    fill(pCol);
    rect(width-46, imgY, 20, 20);
    fill(0);
    text("#" + hex(pCol,6), width-60, imgY + 32);
}


//*********************************
void drawPalette()
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
color somecolor()
{
  return palette[int(random(0, numCol))];
}

//*********************************



void createPalette(PImage imgPal)
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
      color c = imgPal.pixels[y*width+x];
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
  pCol = palette[1];

  //println(hex(pCol,6));

  //println(numCol);
}
