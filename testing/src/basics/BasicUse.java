package basics;

import processing.core.PApplet;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.geom.Vec;
import remixlab.proscene.Scene;

/**
 * Created by pierre on 11/15/16.
 */
public class BasicUse extends PApplet {
  Scene scene;
  float radians = 0;

  public void settings() {
    size(640, 360, P3D);  // your size() command here
  }

  public void setup() {
    println("Version: " + Scene.prettyVersion);
    scene = new Scene(this);
    scene.enableBoundaryEquations();
    scene.loadConfig();
    /*
    scene.mouseAgent().addGrabber(scene);
    scene.mouseAgent().setDefaultGrabber(scene);
    scene.setBinding(new ClickShortcut(LEFT, 2), "sceneCustomClick");
    */
  }

  public void keyPressed() {
    if (key == 'x') scene.shiftTimers();
    if (key == 'f') scene.flip();
  }

  public void draw() {
    background(0);
  /*
  if (frame != null) {
   frame.setResizable(true);
   PApplet.println("set size");
  }
  */
    noStroke();
    if (scene.camera().ballVisibility(new Vec(0, 0, 0), 40) == Camera.Visibility.SEMIVISIBLE)
      fill(255, 0, 0);
    else
      fill(0, 255, 0);
    sphere(40);
  }

  public void sceneCustomClick(Scene scn) {
    scn.setAxesVisualHint(!scn.axesVisualHint());
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"basics.BasicUse"});
  }
}
