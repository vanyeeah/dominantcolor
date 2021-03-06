import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class dominantcolor extends PApplet {

PImage img;


public void setup()
{
  
  colorMode(HSB, 360, 100, 100);
  img = loadImage("data/demo1.jpg");
}


public void draw()
{
  image(img, 0, 0);




  IntDict pixelBuckets = new IntDict();

  // Loop through the images pixels
    for(int i = 0; i < img.pixels.length;i++) {

    // Get the hue as we are keying the dictionary by the hue
    float pixelHue = hue(img.pixels[i]);
    String key = String.valueOf(pixelHue);



    if (pixelBuckets.hasKey(key))
    {
      // Hue already seen so increment count
      int currentCount = pixelBuckets.get(key);
      currentCount = currentCount + 1;
      pixelBuckets.set(key, currentCount);
    }
    else
    {
      // New hue. Start at 1
      String newKey = String.valueOf(pixelHue);
      pixelBuckets.set(newKey, 1);
    }
  }

  // IntDict has a sort function
  pixelBuckets.sortValues();

  Integer index = 0;
  short[] pieAngles = new short[pixelBuckets.size()];
  int[] colors = new int[pixelBuckets.size()];
  Integer angleTotal = 0;

  for (int i : pixelBuckets.values()) {

     String key =  pixelBuckets.keyArray()[index];
     float hue = Float.parseFloat(key);
     int theColor = color(Math.round(hue), 100, 75);

  println("Color: " + theColor + " has been scanned " + i + " times");

      fill(theColor);
      rect(20,20,20,20);




  noLoop();
}
}
  public void settings() {  size(400,400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dominantcolor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
