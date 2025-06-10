package Commonplace.Type.CampChanger;

import Commonplace.Entities.Unit.CampUnit;
import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Floatf;
import mindustry.gen.Entityc;

public class OrderedFloat extends OrderedChanger {
    public Floatf<Float> fValue;

    public OrderedFloat(float value, Floatf<Float> fValue) {
        this.value = value;
        this.fValue = fValue;
    }

    @Override
    public void applyCamp(float[] values, boolean[] bools, Object[] objects) {
        float value = values[0];
        float rangeC = values[1];
        boolean lastOver = bools[0];
        Floatc a = (Floatc) objects[0];
        Floatc c = (Floatc) objects[1];
        Boolc b = (Boolc) objects[2];

        if (this.value > value) {
            c.get(this.value);
            a.get(fValue.get(rangeC));
            b.get(cover);
        } else if (this.value == value) {
            if (cover) {
                a.get(fValue.get(rangeC));
                b.get(true);
            } else if (!lastOver) {
                a.get(fValue.get(rangeC));
            }
        }
    }

    @Override
    public void apply(Entityc e) {
        if (e instanceof CampUnit u) {
            if (cover) {
                u.valueOR = value;
                u.changeR = fValue.get(u.ovrR ? u.valueR : -1);
            } else {
                u.changeR = fValue.get(u.ovrR ? 1 : u.valueR);
            }
            u.valueR = value;
            u.ovrR = cover;
        }
    }
}
