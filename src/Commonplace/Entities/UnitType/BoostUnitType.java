package Commonplace.Entities.UnitType;

import Commonplace.AI.BoostFlyingAI;
import Commonplace.Loader.Special.Effects;
import arc.graphics.g2d.Draw;
import arc.math.Interp;
import mindustry.entities.Effect;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;

import static arc.graphics.g2d.Draw.*;

public class BoostUnitType extends UnitType {
    public String icon1 = "error";
    public String icon2 = "error";
    public float hitChangeHel = -1;
    public float hitDamage = 1;
    public float hitPercent = 1;
    public boolean hitFirstPercent = false;
    public float hitReload = 60;
    public float boostLength = 7.5f;
    public float boostDuration = 20;
    public long boostReload = 1800;
    public float boostDelay = 60;
    public Effect boostEffect = Effects.boosting;
    public Effect boostDelayEffect = Effects.boostDelay;

    public float searchRange = 200;
    public float speed1 = 0.5F;
    public float health2 = 70;
    public int number = 0;
    public int exchangeTime = 120;
    public Effect changeEffect = new Effect(exchangeTime, e -> {
        if (e.data instanceof Unit u) {
            mixcol(Pal.accent, 1f);
            if (e.fin() < 0.35) {
                rect(icon1, u.x, u.y, u.rotation - 90f);
            } else if (e.fin() < 0.65) {
                float length = Math.max(icon1.length(), icon2.length());
                rect("error", u.x, u.y, length, length);
            } else {
                float scl = e.fout(Interp.pow2Out);
                float p = Draw.scl;
                Draw.scl *= scl;

                rect(icon2, u.x, u.y, u.rotation - 90f);

                reset();
                Draw.scl = p;
            }
        }
    });

    public BoostUnitType(String name) {
        super(name);
        aiController = BoostFlyingAI::new;
    }
}