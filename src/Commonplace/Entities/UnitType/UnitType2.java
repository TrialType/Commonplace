package Commonplace.Entities.UnitType;

import Commonplace.Type.CampChanger.OrderedFloat;
import Commonplace.Utils.Classes.Camp;
import arc.struct.Seq;
import mindustry.type.UnitType;

public class UnitType2 extends UnitType {
    public Camp camp;
    public boolean canApply = true;
    public boolean setDefault = true;
    public boolean rangeMulSuper = false;
    public Seq<OrderedFloat> campRangeChanger = new Seq<>();

    public UnitType2(String name, Camp camp) {
        super(name);
        this.camp = camp;
    }

    @Override
    public void init() {
        super.init();

        if (setDefault) {
            if (campRangeChanger.any()) {
                canApply = false;
            }
        }

        campRangeChanger.sort(o -> -o.value);
    }
}
