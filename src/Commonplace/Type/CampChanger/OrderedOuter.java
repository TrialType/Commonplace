package Commonplace.Type.CampChanger;

import mindustry.gen.Entityc;

public class OrderedOuter<T> extends OrderedChanger {
    public T outer;

    public OrderedOuter(float value, T outer) {
        this.value = value;
        this.outer = outer;
    }

    @Override
    public boolean valid(Entityc e) {
        return false;
    }

    @Override
    public void applyCamp(float[] values, boolean[] bools, Object[] objects) {

    }

    @Override
    public void apply(Entityc e) {

    }
}
