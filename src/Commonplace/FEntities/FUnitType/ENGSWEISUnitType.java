package Commonplace.FEntities.FUnitType;

import Commonplace.FAI.StrongBoostAI;
import Commonplace.FContent.SpecialContent.CStats;
import mindustry.content.Fx;
import mindustry.entities.Effect;

public class ENGSWEISUnitType extends UpGradeUnitType {
    public float damage = 1;
    public float percent = 1;
    public boolean firstPercent = false;
    public float changeHel = -1;
    public float hitReload = 3600;
    public float minSpeed = Float.MAX_VALUE;
    public float defend = 1.0F / 3;
    public int power = 1;
    public long reload = 1800;
    public float delay = 90;
    public Effect boostEffect = Fx.missileTrailSmoke;
    public float speed1 = 0.5F;
    public float health2 = 70;
    public int exchangeTime = 120;
    public int number = 0;
    public ENGSWEISUnitType(String name) {
        super(name);
        aiController = StrongBoostAI::new;
    }
    @Override
    public void setStats() {
        super.setStats();
        stats.add(CStats.speed1, speed1);
        stats.add(CStats.health2, health2);
        stats.add(CStats.change_time,exchangeTime);
        stats.add(CStats.activate_number,number);
        stats.add(CStats.boost_reload,reload);
        stats.add(CStats.hit_damage,damage);
        stats.add(CStats.hit_percent,percent);
        stats.add(CStats.first_percent,firstPercent);
        stats.add(CStats.change_hel,changeHel);
    }
}