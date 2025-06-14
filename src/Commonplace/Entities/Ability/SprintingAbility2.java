package Commonplace.Entities.Ability;

import arc.Core;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.world.Tile;

public class SprintingAbility2 extends Ability {
    private final static Seq<Tile> tiles = new Seq<>();
    private float timer = 0;
    private float reloadTimer = 0;
    private float face = -1;

    public boolean rotate = false;
    public float sprintingLength = 10;
    public float sprintingDuration = 300;
    public float sprintingDelay = 0;
    public float sprintingReload = 300;
    public float sprintingDamage = 100;
    public float sprintingRadius = 300;
    public Effect sprintingDelayEffect = Fx.none;
    public StatusEffect status = null;
    public float statusDuration = 180;

    public void update(Unit unit) {
        if (reloadTimer >= sprintingReload) {
            timer += Time.delta;
            if (timer >= sprintingDelay + sprintingDuration) {
                reloadTimer = 0;
                timer = 0;
                face = -1;

                if (status != null) {
                    unit.apply(status, statusDuration);
                }
            } else if (timer > sprintingDelay) {
                Teamc t = Units.closestTarget(unit.team, unit.x, unit.y, sprintingRadius,
                        u -> Angles.angleDist(unit.angleTo(u), unit.rotation) < 15,
                        b -> Angles.angleDist(unit.angleTo(b), unit.rotation) < 15);

                if (t != null) {
                    if (rotate || face < 0) {
                        face = unit.angleTo(t);
                    }
                } else if (rotate) {
                    face = -1;
                }

                if (face >= 0) {
                    Units.nearbyEnemies(unit.team, unit.x, unit.y, sprintingLength, u -> {
                        float angle = Angles.angleDist(face, unit.angleTo(u));
                        if (angle < 90 && Mathf.sinDeg(angle) * unit.dst(u) < (u.hitSize + unit.hitSize) / 2) {
                            u.damage(sprintingDamage);
                        }
                    });
                    Units.nearbyBuildings(unit.x, unit.y, sprintingLength, b -> {
                        if (b.team != unit.team) {
                            final float[] angle = new float[1];
                            b.tile.getLinkedTilesAs(b.block, tiles);
                            if (tiles.contains(tile -> (angle[0] = Angles.angleDist(face, unit.angleTo(b))) <= 90 &&
                                    Mathf.sinDeg(angle[0]) * unit.dst(tile) <= unit.hitSize / 2)) {
                                b.damage(sprintingDamage);
                            }
                        }
                    });
                    unit.rotation = face;
                    unit.vel.trns(face, sprintingLength);
                }
            } else {
                sprintingDelayEffect.at(unit);
            }
        } else {
            reloadTimer = Math.min(reloadTimer + Time.delta, sprintingReload);
        }
    }

    @Override
    public String localized() {
        return Core.bundle.get(getBundle() + ".name");
    }

    @Override
    public String getBundle() {
        return "ability.sprinting2";
    }
}
