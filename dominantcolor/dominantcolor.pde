PImage img;


void setup()
{
  size(400,400);
  colorMode(HSB, 360, 100, 100);
  img = loadImage("data/demo.jpg");
}


void draw()
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
  color[] colors = new color[pixelBuckets.size()];
  Integer angleTotal = 0;

  for (int i : pixelBuckets.values()) {

     String key =  pixelBuckets.keyArray()[index];
     float hue = Float.parseFloat(key);
     color theColor = color(Math.round(hue), 100, 75);

      println("Color: " + theColor + " occurs " + i + " times");

      fill(theColor);
      rect(20,20,20,20);

     float percentage =  (i * 100.0f) / img.pixels.length;
     //short percentageAsShort = (short) Math.round(percentage);

     // println("Percentage is:" + percentage + "%");
     // println("Percentage (short) is:" + percentageAsShort + "%");

     //println(" | hsb: " + int(hue(c)) + "," + int(saturation(c)) + "," + int(brightness(c)));

     short angle = (short)Math.round((360 * (percentage / 100)));
     angleTotal = angleTotal + angle;

     if (index == pixelBuckets.size() - 1)
     {
       // Last peice of pie to be calculated. Ensure ou total sum of angles adds up to 360
       // to avoid gaps. The reason it does not always round up is likely to be rounding issues

       Integer diff = 360 - angleTotal;
       pieAngles[index] = (short)(angle + diff);
     }
     else
     {
       pieAngles[index] = angle;
     }

     if (angle >= 2)
     {
       colors[index] = theColor;
     }
     else
     {
       // Anything less than a certain angle ignore. Set to -1 and later those colors to be the most common color
       colors[index] = -1;
     }

     index++;
  }

  //println("HashMap Size " + pixelBuckets.size());

  for (int colorIndex = 0; colorIndex < colors.length; colorIndex++)
  {
    if (colors[colorIndex] == -1)
    {
      colors[colorIndex] = colors[colors.length - 1];
    }
  }


  noLoop();
}

void drawPieChart(short[] angles, color[] colors, int diam) {
}
