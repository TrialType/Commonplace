package Commonplace.FEntities.FUnitType;

import Commonplace.FAI.BoostFlyingAI;
import Commonplace.FContent.SpecialContent.CStats;
import mindustry.content.Fx;
import mindustry.entities.Effect;

public class BoostUnitType extends UpGradeUnitType {
    public float hitChangeHel = -1;
    public float hitDamage = 1;
    public float hitPercent = 1;
    public boolean hitFirstPercent = false;
    public float hitReload = 3600;
    public float boostLength = 50;
    public float boostDuration = 3;
    public long boostReload = 1800;
    public float boostDelay = 0;
    public Effect boostEffect = Fx.missileTrailSmoke;
    public Effect boostDelayEffect = Fx.missileTrailSmoke;

    public float speed1 = 0.5F;
    public float health2 = 70;
    public int number = 0;
    public int exchangeTime = 120;
    public Effect changeEffect = Fx.missileTrailSmoke;

    public BoostUnitType(String name) {
        super(name);
        aiController = BoostFlyingAI::new;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(CStats.speed1, speed1);
        stats.add(CStats.health2, health2);
        stats.add(CStats.change_time, exchangeTime);
        stats.add(CStats.activate_number, number);
        stats.add(CStats.boost_reload, boostReload);
        stats.add(CStats.hit_damage, hitDamage);
        stats.add(CStats.hit_percent, hitPercent);
        stats.add(CStats.first_percent, hitFirstPercent);
        stats.add(CStats.change_hel, hitChangeHel);
    }
}