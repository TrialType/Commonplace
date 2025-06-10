package Commonplace.Utils.Classes;

import arc.math.geom.Position;
import mindustry.Vars;

public class VectorQuadTree {
    VectorQuadTreeNode root;

    public void addNode(Position pos) {
        if (root == null) {
            root = new VectorQuadTreeNode(pos);
        } else {
            float hx = Vars.world.width() * Vars.tilesize / 2f;
            float hy = Vars.world.height() * Vars.tilesize / 2f;
            float px = pos.getX(), py = pos.getY();
        }
    }

    static class VectorQuadTreeNode {
        Position pos;
        VectorQuadTreeNode parent;
        VectorQuadTreeNode luChild, ldChild, ruChild, rdChild;

        public VectorQuadTreeNode(Position pos) {
            this.pos = pos;
        }

        public VectorQuadTreeNode(Position pos, VectorQuadTreeNode node) {
            this.pos = pos;
            this.parent = node;
        }
    }
}
