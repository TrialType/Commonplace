package Commonplace.Entities.Unit;

import Commonplace.Loader.DefaultContent.Units2;
import Commonplace.Loader.Special.Events;
import Commonplace.Loader.DefaultContent.StatusEffects2;
import arc.math.Mathf;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.ctype.ContentType;
import mindustry.entities.Units;
import mindustry.gen.Unit;
import mindustry.gen.UnitWaterMove;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;

import java.util.Random;

import static java.lang.Math.max;
import static mindustry.Vars.world;

public class CaveUnit extends UnitWaterMove {
    private static final Seq<UnitType> u1 = new Seq<>(new UnitType[]{
            UnitTypes.dagger,
            UnitTypes.nova,
            UnitTypes.flare,
            UnitTypes.retusa,
            UnitTypes.risso,
            Units2.barb
    });
    private static final Seq<UnitType> u2 = new Seq<>(new UnitType[]{
            UnitTypes.mace,
            UnitTypes.pulsar,
            UnitTypes.atrax,
            UnitTypes.horizon,
            UnitTypes.poly,
            UnitTypes.minke,
            UnitTypes.oxynoe,
            Units2.hammer
    });
    private static final Seq<UnitType> u3 = new Seq<>(new UnitType[]{
            UnitTypes.fortress,
            UnitTypes.quasar,
            UnitTypes.spiroct,
            UnitTypes.zenith,
            UnitTypes.mega,
            UnitTypes.bryde,
            UnitTypes.cyerce,
            UnitTypes.cyerce,
            Units2.buying
    });
    private static final Seq<UnitType> u4 = new Seq<>(new UnitType[]{
            UnitTypes.scepter,
            UnitTypes.vela,
            UnitTypes.arkyid,
            UnitTypes.antumbra,
            UnitTypes.quad,
            UnitTypes.sei,
            UnitTypes.aegires,
            Units2.crazy
    });
    private static final Seq<UnitType> u5 = new Seq<>(new UnitType[]{
            UnitTypes.reign,
            UnitTypes.corvus,
            UnitTypes.toxopid,
            UnitTypes.eclipse,
            UnitTypes.omura,
            UnitTypes.navanax,
            Units2.transition
    });
    private float length = -1;
    private float allTimer = -1;
    private boolean back = false;
    private float backTimer = 0;
    private float summonTimer = 0;
    private int time = 1;

    protected CaveUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
        this.tleft = new Trail(1);
        this.trailColor = Blocks.water.mapColor.cpy().mul(1.5F);
        this.tright = new Trail(1);
    }

    public static CaveUnit create() {
        return new CaveUnit();
    }

    @Override
    public int classId() {
        return 118;
    }

    @Override
    public void update() {
        if (allTimer >= 21000) {
            Fx.unitDrop.at(x, y, 0, this);
            if (allTimer >= 21600) {
                back = true;
                time = 0;
                kill();
                return;
            }
        }

        super.update();

        if (back) {
            backTimer += Time.delta;
            summonTimer = summonTimer + Time.delta;
            if (backTimer >= 481) {
                time++;
                back = false;
                backTimer = 0;
                summonTimer = 0;
            } else if (summonTimer >= 60) {
                summonTimer = 0;
                summonUnit();
            }
        }
    }

    public void applyDamage() {
        float boost = allTimer >= max(world.width(), world.height()) * 13.44f ? 1 : allTimer * 0.00695f / max(world.width(), world.height()) * 13.44f;
        Units.nearbyEnemies(team, x, y, length * boost, u -> {
            if (u.shield > 0.01) {
                u.damage(u.shield() * 2);
                u.damage(u.maxHealth * 0.001f);
            } else {
                u.damage(u.maxHealth * 0.001f);
            }
            u.apply(StatusEffects.slow, 2);
            u.apply(StatusEffects2.abyss, 2);
        });
        Units.nearbyBuildings(x, y, length * boost, b -> {
            b.applySlowdown(0.8f, 3);
            if (b.team != team) {
                b.damage(b.maxHealth * 0.001f);
            }
        });
    }

    public void summonUnit() {
        Random ra = new Random();
        int power = ra.nextInt(time * 15) + 1;
        while (power > 1) {
            if (power >= 32) {
                power = power - 32;
                summonUnit(u5.get(ra.nextInt(u5.size)));
            } else if (power >= 16) {
                power = power - 16;
                summonUnit(u4.get(ra.nextInt(u4.size)));
            } else if (power >= 8) {
                power = power - 8;
                summonUnit(u3.get(ra.nextInt(u3.size)));
            } else if (power >= 4) {
                power = power - 4;
                summonUnit(u2.get(ra.nextInt(u2.size)));
            } else {
                power = power - 2;
                summonUnit(u1.get(ra.nextInt(u1.size)));
            }
        }
    }

    public void summonUnit(UnitType u) {
        Random ra = new Random();
        float x = 0, y = 0, rotate = 45;

        switch (ra.nextInt(4)) {
            case 0: {
                y = 0;
                x = ra.nextInt(world.width() * 8 + 1);
                rotate = 90;
                break;
            }
            case 1: {
                x = world.width() * 8;
                y = ra.nextInt(world.height() * 8 + 1);
                rotate = 180;
                break;
            }
            case 2: {
                y = world.height() * 8;
                x = ra.nextInt(world.width() * 8 + 1);
                rotate = 270;
                break;
            }
            case 3: {
                x = 0;
                y = ra.nextInt(world.height() * 8 + 1);
                rotate = 0;
            }
        }
        Unit unit = u.create(team);
        unit.set(x, y);
        unit.rotation(rotate);
        unit.add();
        unit.apply(StatusEffects2.tension);
        arc.Events.fire(new Events.GetPowerEvent(unit, time * 2, false));
        Fx.unitSpawn.at(x, y, rotate);
    }
}
