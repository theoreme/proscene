/**
 * Octree Node. 
 * by Jean Pierre Charalambos.
 * 
 * This class is part of the View Frustum Culling example.
 * This class holds the octree hierarchy culled against the main viewer camera.
 *
 * Press 'h' to toggle the mouse and keyboard navigation help.
 */

public class OctreeNode {
  Vec p1, p2;
  OctreeNode child[];
  int level;

  OctreeNode(Vec P1, Vec P2) {
    p1 = P1;
    p2 = P2;
    child = new OctreeNode[8];
  }

  public void draw(PGraphicsOpenGL pg3d) {
    pg3d.stroke(color(0.3f*level*255, 0.2f*255, (1.0f-0.3f*level)*255));
    pg3d.strokeWeight(level + 1);

    pg3d.beginShape();
    pg3d.vertex(p1.x(), p1.y(), p1.z());
    pg3d.vertex(p1.x(), p2.y(), p1.z());
    pg3d.vertex(p2.x(), p2.y(), p1.z());
    pg3d.vertex(p2.x(), p1.y(), p1.z());
    pg3d.vertex(p1.x(), p1.y(), p1.z());
    pg3d.vertex(p1.x(), p1.y(), p2.z());
    pg3d.vertex(p1.x(), p2.y(), p2.z());
    pg3d.vertex(p2.x(), p2.y(), p2.z());
    pg3d.vertex(p2.x(), p1.y(), p2.z());
    pg3d.vertex(p1.x(), p1.y(), p2.z());
    pg3d.endShape();
    // pg3d.endShape(CLOSE);

    pg3d.beginShape(PApplet.LINES);
    pg3d.vertex(p1.x(), p2.y(), p1.z());
    pg3d.vertex(p1.x(), p2.y(), p2.z());
    pg3d.vertex(p2.x(), p2.y(), p1.z());
    pg3d.vertex(p2.x(), p2.y(), p2.z());
    pg3d.vertex(p2.x(), p1.y(), p1.z());
    pg3d.vertex(p2.x(), p1.y(), p2.z());
    pg3d.endShape();
  }

  public void drawIfAllChildrenAreVisible(PGraphicsOpenGL pg3d, Camera camera) {
    Camera.Visibility vis = camera.boxIsVisible(p1, p2);
    if (vis == Camera.Visibility.VISIBLE)
      draw(pg3d);
    else if (vis == Camera.Visibility.SEMIVISIBLE)
      if (child[0] != null)
        for (int i = 0; i < 8; ++i)
          child[i].drawIfAllChildrenAreVisible(pg3d, camera);
      else
        draw(pg3d);
  }

  public void buildBoxHierarchy(int l) {
    level = l;
    Vec middle = Vec.multiply(Vec.add(p1, p2), 1 / 2.0f);
    for (int i = 0; i < 8; ++i) {
      // point in one of the 8 box corners
      Vec point = new Vec(((i & 4) != 0) ? p1.x() : p2.x(), ((i & 2) != 0) ? p1.y() : p2.y(), ((i & 1) != 0) ? p1.z() : p2.z());			
      if (level > 0) {
        child[i] = new OctreeNode(point, middle);
        child[i].buildBoxHierarchy(level - 1);
      } 
      else
        child[i] = null;
    }
  }
}
