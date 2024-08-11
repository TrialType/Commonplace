package Commonplace.FTools.classes;

import Commonplace.FContent.DefaultContent.FStatusEffects;
import Commonplace.FContent.DefaultContent.FUnits;
import Commonplace.FContent.SpecialContent.MEvents;
import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.struct.IntFloatMap;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Fires;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.type.StatusEffect;
import mindustry.world.Tile;

import static java.lang.Math.*;
import static mindustry.Vars.*;

public class FDamage extends Damage {
    private static final IntFloatMap damages = new IntFloatMap();
    private static final EventType.UnitDamageEvent bulletDamageEvent = new EventType.UnitDamageEvent();
    private static final Rect rect = new Rect();
    private static final Vec2 vec = new Vec2();

    private FDamage() {
    }

    public static void damage(Team team, float x, float y, float radius, float percent, boolean complete, boolean air, boolean ground, boolean scaled, @Nullable Bullet source) {
        Cons<Unit> cons = unit -> {
            if (unit.team == team || !unit.checkTarget(air, ground) || !unit.hittable() || !unit.within(x, y, radius + (scaled ? unit.hitSize / 2f : 0f))) {
                return;
            }
            boolean dead = unit.dead;
            float amount = calculateDamage(scaled ? Math.max(0, unit.dst(x, y) - unit.type.hitSize / 2) : unit.dst(x, y), radius, percent * unit.maxHealth);
            float shield = unit.shield;
            unit.shield = 0;
            unit.damagePierce(amount);
            unit.shield = shield;
            if (unit.health() <= 0) unit.dead(true);
            if (source != null) {
                Events.fire(bulletDamageEvent.set(unit, source));
                unit.controller().hit(source);
                if (!dead && unit.dead) {
                    Events.fire(new EventType.UnitBulletDestroyEvent(unit, source));
                }
            }
            float dst = vec.set(unit.x - x, unit.y - y).len();
            unit.vel.add(vec.setLength((1f - dst / radius) * 2f / unit.mass()));

            if (complete && percent * unit.maxHealth >= 9999999f && unit.isPlayer()) {
                Events.fire(EventType.Trigger.exclusionDeath);
            }
        };
        rect.setSize(radius * 2).setCenter(x, y);
        if (team != null) {
            Units.nearbyEnemies(team, rect, cons);
        } else {
            Units.nearby(rect, cons);
        }
        if (ground) {
            if (!complete) {
                tileDamage(team, World.toTile(x), World.toTile(y), radius / tilesize, percent, source);
            } else {
                completeDamage(team, x, y, radius, percent);
            }
        }

    }

    private static void completeDamage(Team team, float x, float y, float radius, float percent) {
        int trad = (int) (radius / tilesize);
        for (int dx = -trad; dx <= trad; dx++) {
            for (int dy = -trad; dy <= trad; dy++) {
                Tile tile = world.tile(Math.round(x / tilesize) + dx, Math.round(y / tilesize) + dy);
                if (tile != null && tile.build != null && (team == null || team != tile.team()) && dx * dx + dy * dy <= trad * trad) {
                    tile.build.damage(team, percent * tile.build.maxHealth);
                }
            }
        }
    }

    public static void tileDamage(Team team, int x, int y, float baseRadius, float percent, @Nullable Bullet source) {
        Core.app.post(() -> {
            var in = world.build(x, y);
            if (in != null && in.team != team && in.block.size > 1 && in.health > percent * in.maxHealth) {
                in.damage(team, percent * in.maxHealth * Math.min((in.block.size), baseRadius * 0.4f));
                return;
            }
            float radius = Math.min(baseRadius, 100), rad2 = radius * radius;
            int rays = Mathf.ceil(radius * 2 * Mathf.pi);
            double spacing = Math.PI * 2.0 / rays;
            damages.clear();
            for (int i = 0; i <= rays; i++) {
                float dealt = 0f;
                int startX = x;
                int startY = y;
                int endX = x + (int) (Math.cos(spacing * i) * radius), endY = y + (int) (Math.sin(spacing * i) * radius);

                int xDist = Math.abs(endX - startX);
                int yDist = -Math.abs(endY - startY);
                int xStep = (startX < endX ? +1 : -1);
                int yStep = (startY < endY ? +1 : -1);
                int error = xDist + yDist;

                while (startX != endX || startY != endY) {
                    var build = world.build(startX, startY);
                    if (build != null && build.team != team) {
                        float edgeScale = 0.6f;
                        float mult = (1f - (Mathf.dst2(startX, startY, x, y) / rad2) + edgeScale) / (1f + edgeScale);
                        float next = percent * (in == null ? 1 : in.maxHealth) * mult - dealt;
                        int p = Point2.pack(startX, startY);
                        damages.put(p, Math.max(damages.get(p), next));
                        dealt += build.health;
                        if (next - dealt <= 0) {
                            break;
                        }
                    }
                    if (2 * error - yDist > xDist - 2 * error) {
                        error += yDist;
                        startX += xStep;
                    } else {
                        error += xDist;
                        startY += yStep;
                    }
                }
            }
            for (var e : damages) {
                int cx = Point2.x(e.key), cy = Point2.y(e.key);
                var build = world.build(cx, cy);
                if (build != null) {
                    if (source != null) {
                        build.damage(source, team, e.value);
                    } else {
                        build.damage(team, e.value);
                    }
                }
            }
        });
    }

    private static float calculateDamage(float dist, float radius, float damage) {
        float falloff = 0.4f;
        float scaled = Mathf.lerp(1f - dist / radius, 1f, falloff);
        return damage * scaled;
    }

    public static void damageInterp(Team team, float x, float y, float radius, float damage, Interp interp, boolean complete, boolean air, boolean ground, boolean scaled, @Nullable Bullet source) {
        Cons<Unit> cons = unit -> {
            if (unit.team == team || !unit.checkTarget(air, ground) || !unit.hittable() || !unit.within(x, y, radius + (scaled ? unit.hitSize / 2f : 0f))) {
                return;
            }
            boolean dead = unit.dead;
            float amount = calculateDamage(scaled ? Math.max(0, unit.dst(x, y) - unit.type.hitSize / 2) : unit.dst(x, y), radius, damage * interp.apply(unit.dst(x, y) / radius));
            float shield = unit.shield;
            unit.shield = 0;
            unit.damagePierce(amount);
            unit.shield = shield;
            if (unit.health() <= 0) unit.dead(true);
            if (source != null) {
                Events.fire(bulletDamageEvent.set(unit, source));
                unit.controller().hit(source);
                if (!dead && unit.dead) {
                    Events.fire(new EventType.UnitBulletDestroyEvent(unit, source));
                }
            }
            float dst = vec.set(unit.x - x, unit.y - y).len();
            unit.vel.add(vec.setLength((1f - dst / radius) * 2f / unit.mass()));

            if (complete && damage * interp.apply(unit.dst(x, y) / radius) * unit.maxHealth >= 9999999f && unit.isPlayer()) {
                Events.fire(EventType.Trigger.exclusionDeath);
            }
        };
        rect.setSize(radius * 2).setCenter(x, y);
        if (team != null) {
            Units.nearbyEnemies(team, rect, cons);
        } else {
            Units.nearby(rect, cons);
        }
        if (ground) {
            if (!complete) {
                tileDamageInterp(team, World.toTile(x), World.toTile(y), radius / tilesize, damage, interp, source);
            } else {
                completeDamageInterp(team, x, y, radius, damage, interp);
            }
        }

    }

    private static void completeDamageInterp(Team team, float x, float y, float radius, float damage, Interp interp) {
        int trad = (int) (radius / tilesize);
        for (int dx = -trad; dx <= trad; dx++) {
            for (int dy = -trad; dy <= trad; dy++) {
                Tile tile = world.tile(Math.round(x / tilesize) + dx, Math.round(y / tilesize) + dy);
                if (tile != null && tile.build != null && (team == null || team != tile.team()) && dx * dx + dy * dy <= trad * trad) {
                    tile.build.damage(team, damage * interp.apply(tile.build.dst(x, y) / radius));
                }
            }
        }
    }

    public static void tileDamageInterp(Team team, int x, int y, float baseRadius, float damage, Interp interp, @Nullable Bullet source) {
        Core.app.post(() -> {
            var in = world.build(x, y);
            if (in != null && in.team != team && in.block.size > 1 && in.health > damage * interp.apply(in.dst(x, y) / tilesize / baseRadius)) {
                in.damage(team, damage * interp.apply(in.dst(x, y) / tilesize / baseRadius));
                return;
            }
            float radius = Math.min(baseRadius, 100), rad2 = radius * radius;
            int rays = Mathf.ceil(radius * 2 * Mathf.pi);
            double spacing = Math.PI * 2.0 / rays;
            damages.clear();
            for (int i = 0; i <= rays; i++) {
                float dealt = 0f;
                int startX = x;
                int startY = y;
                int endX = x + (int) (Math.cos(spacing * i) * radius), endY = y + (int) (Math.sin(spacing * i) * radius);

                int xDist = Math.abs(endX - startX);
                int yDist = -Math.abs(endY - startY);
                int xStep = (startX < endX ? +1 : -1);
                int yStep = (startY < endY ? +1 : -1);
                int error = xDist + yDist;

                while (startX != endX || startY != endY) {
                    var build = world.build(startX, startY);
                    if (build != null && build.team != team) {
                        float edgeScale = 0.6f;
                        float mult = (1f - (Mathf.dst2(startX, startY, x, y) / rad2) + edgeScale) / (1f + edgeScale);
                        float next = damage * interp.apply((in == null ? 0 : in.dst(x, y)) / tilesize / baseRadius) * mult - dealt;
                        int p = Point2.pack(startX, startY);
                        damages.put(p, Math.max(damages.get(p), next));
                        dealt += build.health;
                        if (next - dealt <= 0) {
                            break;
                        }
                    }
                    if (2 * error - yDist > xDist - 2 * error) {
                        error += yDist;
                        startX += xStep;
                    } else {
                        error += xDist;
                        startY += yStep;
                    }
                }
            }
            for (var e : damages) {
                int cx = Point2.x(e.key), cy = Point2.y(e.key);
                var build = world.build(cx, cy);
                if (build != null) {
                    if (source != null) {
                        build.damage(source, team, e.value);
                    } else {
                        build.damage(team, e.value);
                    }
                }
            }
        });
    }


    public static void SqrtDamage(@Nullable Bullet bullet, Team team, float damage, float x, float y, float rotation, float length, float width) {
        float angle = rotation + 90;
        float len = width * 2 / 9;
        float len2 = width;

        int num = 0;
        Seq<Unit> units = new Seq<>();
        Seq<Building> buildings = new Seq<>();
        Units.nearbyEnemies(team, x, y, max(length, width / 2) * 1.4f, units::add);
        Units.nearbyBuildings(x, y, max(length, width / 2) * 1.4f, b -> {
            if (b.team != team) {
                buildings.add(b);
            }
        });
        while (len2 > -width) {
            num++;
            float ox = (float) (x + cos(toRadians(angle)) * (len2 - len / 2));
            float oy = (float) (y + sin(toRadians(angle)) * (len2 - len / 2));
            for (Unit u : units) {
                float angleO = Angles.angleDist(rotation, Angles.angle(ox, oy, u.x, u.y));
                if (angleO <= 90) {
                    float len3 = (float) sqrt((u.x - ox) * (u.x - ox) + (u.y - oy) * (u.y - oy));
                    if (cos(toRadians(angleO)) * len3 <= length && sin(toRadians(angleO)) * len3 <= len / 2) {
                        boolean dead = u.dead;
                        u.damage(damage * (num == 5 ? 1 : num < 5 ? (float) num / 5.0f : (float) (5 - (num - 5)) / 5.0f));
                        if (!dead && u.dead && bullet != null) {
                            Events.fire(new EventType.UnitBulletDestroyEvent(u, bullet));
                        }
                    }
                }
            }
            for (Building b : buildings) {
                float angleO = Angles.angleDist(rotation, Angles.angle(ox, oy, b.x, b.y));
                if (angleO <= 90) {
                    float len3 = (float) sqrt((b.x - ox) * (b.x - ox) + (b.y - oy) * (b.y - oy));
                    if (cos(toRadians(angleO)) * len3 <= length && sin(toRadians(angleO)) * len3 < len / 2) {
                        b.damage(damage * (num == 5 ? 1 : num < 5 ? (float) num / 5 : (float) (5 - (num - 5)) / 5));
                    }
                }
            }
            len2 = len2 - len;
        }

    }

    public static void triangleDamage(Bullet bullet, Team team, float damage, float x, float y, float rotation, float length, float width, float power, StatusEffect statusEffect, float time, float boss, Effect effect) {
        float maxLen = max(length, width) * 1.5f;
        Units.nearbyEnemies(team, x, y, maxLen, u -> {
            float angle = Angles.angleDist(rotation, Angles.angle(x, y, u.x, u.y));
            float len = (float) sqrt((u.x - x) * (u.x - x) + (u.y - y) * (u.y - y));
            if (angle <= 90 && (width / length) - tan(toRadians(angle)) >= -0.01f && len * cos(toRadians(angle)) - length <= 0) {
                boolean dead = u.dead;
                u.damage(damage);
                u.apply(statusEffect, time);
                if (effect != null) {
                    effect.at(x, y, Angles.angle(x, y, u.x, u.y), u);
                }

                vec.set(u.x - x, u.y - y);
                if (FUnits.boss.contains(u.type)) {
                    vec.setLength(power * boss);
                } else {
                    vec.setLength(power);
                }
                u.moveAt(vec);

                if (!dead && u.dead) {
                    if (bullet != null) {
                        Events.fire(new EventType.UnitBulletDestroyEvent(u, bullet));
                    }
                }
            }
        });
        Units.nearbyBuildings(x, y, maxLen, b -> {
            if (b.team != team) {
                float angle = Angles.angleDist(rotation, Angles.angle(x, y, b.x, b.y));
                float len = (float) sqrt((b.x - x) * (b.x - x) + (b.y - y) * (b.y - y));
                if (angle <= 90 && (width / length) - tan(toRadians(angle)) >= -0.01f && len * cos(toRadians(angle)) - length <= 0) {
                    boolean dead = b.dead;
                    b.damage(damage);
                    if (effect != null) {
                        effect.at(x, y, Angles.angle(x, y, b.x, b.y), b);
                    }

                    if (FStatusEffects.burnings.indexOf(statusEffect) >= 0) {
                        Fires.create(b.tile);
                    }

                    if (!dead && b.dead) {
                        if (bullet != null) {
                            Events.fire(new EventType.BuildingBulletDestroyEvent(b, bullet));
                        }
                    }
                }
            }
        });
    }

    public static void percentDamage(Unit killer, Healthc u, float percent, float damage, boolean firstPercent, float changeHel) {
        boolean dead = u.dead();
        if (firstPercent && u.health() > changeHel || (!firstPercent && u.health() <= changeHel)) {
            if (u instanceof Shieldc s) {
                float shield = s.shield();
                s.shield(0);
                u.damagePierce(u.health() - u.maxHealth() * percent / 100);
                s.shield(shield);
            } else {
                u.damagePierce(u.health() - u.maxHealth() * percent / 100);
            }
        } else {
            u.damage(damage);
        }
        if (!dead && u.dead() && killer != null) {
            Events.fire(new MEvents.UnitDestroyOtherEvent(killer, u));
        }
    }
}
