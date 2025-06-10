package Commonplace.Type.CampChanger;

import Commonplace.Entities.Unit.CampUnit;
import mindustry.gen.Entityc;

public abstract class OrderedChanger {
    protected float x = -1, y = -1;

    public float value;
    public float radius;
    public float width, height;

    public boolean team = false;
    public boolean cover = false;
    public boolean circle = true;

    public <T extends OrderedChanger> T team() {
        team = true;
        //noinspection unchecked
        return (T) this;
    }

    public <T extends OrderedChanger> T cover() {
        cover = true;
        //noinspection unchecked
        return (T) this;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean inRange(Entityc e) {
        if (e instanceof CampUnit u) {
            if (x < 0 || y < 0) {
                return false;
            }
            if (circle) {
                return u.within(x, y, radius + u.hitSize() / 2f);
            } else {
                float hw = width / 2f + u.hitSize() / 2;
                float hh = height / 2f + u.hitSize() / 2;
                return Math.abs(u.x() - x) <= hw && Math.abs(u.y() - y) <= hh;
            }
        }
        return false;
    }

    public boolean valid(Entityc e) {
        if (e instanceof CampUnit u && value >= u.valueOR) {
            if (cover) {
                return value >= u.valueR;
            }
            return value > u.valueOR;
        }
        return false;
    }

    public abstract void applyCamp(float[] values, boolean[] bools, Object[] objects);

    public abstract void apply(Entityc e);
}
