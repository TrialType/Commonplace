package Commonplace.Utils.Classes;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.content.Bullets;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Buildingc;
import mindustry.gen.Bullet;
import mindustry.gen.Unitc;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public abstract class Lightning2 {
    private static final Rand random = new Rand();
    private static final Rect rect = new Rect();
    private static final Seq<Unitc> entities = new Seq<>();
    private static final Seq<Buildingc> entitiesB = new Seq<>();
    private static final Seq<Unitc> entitiesU = new Seq<>();
    private static final IntSet hit = new IntSet();
    private static final int maxChain = 8;
    private static final float hitRange = 30f;
    private static boolean bHit = false;
    private static int lastSeed = 0;

    public static void createConnect(Team team, Color color, float damage, float x, float y, float targetAngle, int length, int connectLength) {
        createLightningInternalConnect(null, lastSeed++, team, color, damage, x, y, targetAngle, length, connectLength);
    }

    public static void createConnect(Bullet bullet, Color color, float damage, float x, float y, float targetAngle, int length, int connectLength) {
        createLightningInternalConnect(bullet, lastSeed++, bullet.team, color, damage, x, y, targetAngle, length, connectLength);
    }

    private static void createLightningInternalConnect(@Nullable Bullet hitter, int seed, Team team, Color color, float damage, float x, float y, float rotation, int length, int connectLength) {
        random.setSeed(seed);
        hit.clear();

        BulletType hitCreate = hitter == null || hitter.type.lightningType == null ? Bullets.damageLightning : hitter.type.lightningType;
        Seq<Vec2> lines = new Seq<>();
        bHit = false;
        final int[] size = new int[]{
                length / 2
        };

        entitiesB.clear();
        entitiesU.clear();
        for (int i = 0; i < size[0]; i++) {
            hitCreate.create(null, team, x, y, rotation, damage * (hitter == null ? 1f : hitter.damageMultiplier()), 1f, 1f, hitter);
            lines.add(new Vec2(x + Mathf.range(3f), y + Mathf.range(3f)));

            if (lines.size > 1) {
                bHit = false;
                Vec2 from = lines.get(lines.size - 2);
                Vec2 to = lines.get(lines.size - 1);
                World.raycastEach(World.toTile(from.getX()), World.toTile(from.getY()), World.toTile(to.getX()), World.toTile(to.getY()), (wx, wy) -> {

                    Tile tile = world.tile(wx, wy);
                    if (tile != null && (tile.build != null && tile.build.isInsulated()) && tile.team() != team) {
                        bHit = true;
                        //snap it instead of removing
                        lines.get(lines.size - 1).set(wx * tilesize, wy * tilesize);
                        return true;
                    } else if (tile != null && tile.build != null && !entitiesB.contains(tile.build) && tile.team() != team) {
                        entitiesB.add(tile.build);
                        size[0] += connectLength;
                    }
                    return false;
                });
                if (bHit) break;
            }

            rect.setSize(hitRange).setCenter(x, y);
            entities.clear();
            if (hit.size < maxChain) {
                Units.nearbyEnemies(team, rect, u -> {
                    if (!hit.contains(u.id()) && (hitter == null || u.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround))) {
                        entities.add(u);
                        if (!entitiesU.contains(u)) {
                            entitiesU.add(u);
                            size[0] += connectLength;
                        }
                    }
                });
            }

            Unitc furthest = Geometry.findFurthest(x, y, entities);

            if (furthest != null) {
                hit.add(furthest.id());
                x = furthest.x();
                y = furthest.y();
            } else {
                rotation += random.range(20f);
                x += Angles.trnsx(rotation, hitRange / 2f);
                y += Angles.trnsy(rotation, hitRange / 2f);
            }
        }

        Fx.lightning.at(x, y, rotation, color, lines);
    }
}
