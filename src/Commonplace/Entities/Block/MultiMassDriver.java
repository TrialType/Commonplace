package Commonplace.Entities.Block;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.util.pooling.Pools;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.distribution.MassDriver;

import static mindustry.Vars.*;
import static mindustry.Vars.content;

public class MultiMassDriver extends MassDriver {
    public int maxLinks = 3;

    public MultiMassDriver(String name) {
        super(name);

        config(Point2.class, (MultiMassDriverBuild tile, Point2 point) -> {
            int pos = Point2.pack(point.x + tile.tileX(), point.y + tile.tileY());

            if (tile.links.contains(pos)) {
                tile.links.remove((Integer) pos);
            } else if (world.build(pos) instanceof MultiMassDriverBuild) {
                tile.links.add(pos);
            }
        });
        config(Integer.class, (MultiMassDriverBuild tile, Integer point) -> {
            if (point == -1) {
                tile.links.remove((Integer) tile.link);
            } else if (point == -2) {
                tile.links.clear();
            } else if (point >= 0) {
                tile.links.add(point);
            }
        });
    }

    public class MultiMassDriverBuild extends MassDriverBuild {
        Seq<Integer> links = new Seq<>(maxLinks);
        public float acceptCounter = 0f;

        @Override
        public void updateTile() {
            Seq<Building> buildings = new Seq<>(maxLinks);

            updateLinks();
            boolean hasLink = linkValid();

            if (hasLink) {
                for (int i = 0; i < links.size; i++) {
                    buildings.add(world.build(links.get(i)));
                }
            }

            //reload regardless of state
            if (reloadCounter > 0f) {
                reloadCounter = Mathf.clamp(reloadCounter - edelta() / reload);
            }

            if (acceptCounter > 0f) {
                acceptCounter = Mathf.clamp(acceptCounter - edelta() / reload);
            }

            var current = currentShooter();

            //cleanup waiting shooters that are not valid
            if (current != null && !shooterValid(current)) {
                waitingShooters.remove(current);
            }

            //switch states
            if (state == DriverState.idle) {
                //start accepting when idle and there's space
                if (!waitingShooters.isEmpty() && (itemCapacity - items.total() >= minDistribute)) {
                    state = DriverState.accepting;
                } else if (hasLink) { //switch to shooting if there's a valid link.
                    state = DriverState.shooting;
                }
            }

            //dump when idle or accepting
            if (state == DriverState.idle || state == DriverState.accepting) {
                dumpAccumulate();
            }

            //skip when there's no power
            if (efficiency <= 0f) {
                return;
            }

            if (state == DriverState.accepting) {
                //if there's nothing shooting at this, bail - OR, items full
                if (currentShooter() == null || (itemCapacity - items.total() < minDistribute)) {
                    state = DriverState.idle;
                    return;
                }

                //align to shooter rotation
                rotation = Angles.moveToward(rotation, angleTo(currentShooter()) + 180, rotateSpeed * efficiency);
            } else if (state == DriverState.shooting) {
                //if there's nothing to shoot at OR someone wants to shoot at this thing, bail
                if (!hasLink || (!waitingShooters.isEmpty() && (itemCapacity - items.total() >= minDistribute))) {
                    state = DriverState.idle;
                    return;
                }

                float targetRotation = buildings.sumf(b -> {
                    float angle = angleTo(b) % 360;
                    return angle > 180 ? angle - 360 : angle;
                }) / buildings.size;

                //must shoot minimum amount of items
                //must have minimum amount of space
                if (items.total() >= minDistribute && buildings.contains(b -> b.block.itemCapacity - b.items.total() >= minDistribute)) {
                    Seq<Building> fires = new Seq<>(maxLinks);

                    if (reloadCounter <= 0.0001f) {
                        //align to target location
                        rotation = Angles.moveToward(rotation, targetRotation, rotateSpeed * efficiency);
                    }

                    for (int i = 0; i < buildings.size; i++) {
                        MassDriverBuild other = (MassDriverBuild) buildings.get(i);
                        other.waitingShooters.add(this);

                        if (reloadCounter <= 0.0001f) {
                            //fire when it's the first in the queue and angles are ready.
                            if (other.currentShooter() == this &&
                                    other.state == DriverState.accepting &&
                                    Angles.near(rotation, targetRotation, 2f) &&
                                    Angles.near(other.rotation, angleTo(other), 2f)
                            ) {
                                float timeToArrive = Math.min(bulletLifetime, dst(other) / bulletSpeed);
                                Time.run(timeToArrive, () -> {
                                    //remove waiting shooters, it's done firing
                                    other.waitingShooters.remove(this);
                                    other.state = DriverState.idle;
                                });

                                fires.add(other);
                            }
                        }
                    }

                    if (!fires.isEmpty()) {
                        //actually fire
                        fire(fires);
                        //driver is immediately idle
                        state = DriverState.idle;
                    }
                }
            }
        }

        protected void fire(Seq<Building> buildings) {
            //reset reload, use power.
            reloadCounter = 1f;

            int[] max = new int[content.items().size];
            for (int i = 0; i < content.items().size; i++) {
                max[i] = items.get(content.item(i)) / buildings.size;
            }

            for (var build : buildings) {
                MassDriverBuild target = (MassDriverBuild) build;

                DriverBulletData data = Pools.obtain(DriverBulletData.class, DriverBulletData::new);
                data.from = this;
                data.to = target;
                int totalUsed = 0;
                for (int i = 0; i < content.items().size; i++) {
                    int maxTransfer = Math.min(max[i], tile.block().itemCapacity - totalUsed);
                    data.items[i] = maxTransfer;
                    totalUsed += maxTransfer;
                    items.remove(content.item(i), maxTransfer);
                }

                float angle = tile.angleTo(target);

                bullet.create(this, team,
                        x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation),
                        angle, -1f, bulletSpeed, bulletLifetime, data);

                shootEffect.at(x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation), angle);
                smokeEffect.at(x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation), angle);
            }
            Effect.shake(shake, shake, this);

            shootSound.at(tile, Mathf.random(0.9f, 1.1f));
        }

        protected boolean shooterValid(Building other) {
            return other instanceof MultiMassDriverBuild entity && other.isValid() && other.efficiency > 0 && entity.block == block && entity.links.contains(pos()) && within(other, range);
        }

        @Override
        protected boolean linkValid() {
            return links.contains(pos -> world.build(pos) instanceof MultiMassDriverBuild build && build.block == block && build.team == team && within(build, range));
        }

        @Override
        public void draw() {
            Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            float counter = knockback * (acceptCounter > 0 ? -acceptCounter : reloadCounter);
            Drawf.shadow(region,
                    x + Angles.trnsx(rotation + 180, counter) - (size / 2f),
                    y + Angles.trnsy(rotation + 180, counter) - (size / 2f), rotation - 90);
            Draw.rect(region,
                    x + Angles.trnsx(rotation + 180, counter),
                    y + Angles.trnsy(rotation + 180, counter), rotation - 90);
        }

        @Override
        public void drawConfigure() {
            float sin = Mathf.absin(Time.time, 6f, 1f);

            Draw.color(Pal.accent);
            Lines.stroke(1f);
            Drawf.circles(x, y, (tile.block().size / 2f + 1) * tilesize + sin - 2f, Pal.accent);

            for (var shooter : waitingShooters) {
                Drawf.circles(shooter.x, shooter.y, (tile.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
                Drawf.arrow(shooter.x, shooter.y, x, y, size * tilesize + sin, 4f + sin, Pal.place);
            }

            if (linkValid()) {
                Building target;
                for (int pos : links) {
                    if ((target = world.build(pos)) != null) {
                        Drawf.circles(target.x, target.y, (target.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
                        Drawf.arrow(x, y, target.x, target.y, size * tilesize + sin, 4f + sin);
                    }
                }
            }

            Drawf.dashCircle(x, y, range, Pal.accent);
        }

        public void handlePayload(Bullet bullet, DriverBulletData data) {
            super.handlePayload(bullet, data);
            acceptCounter = 1f;
        }

        @Override
        public Point2 config() {
            if (tile == null) return null;
            for (int pos : links) {
                if (pos >= 0) {
                    return Point2.unpack(pos).sub(tile.x, tile.y);
                }
            }
            return null;
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if (this == other) {
                if (link == -1) deselect();
                configure(-2);
                return false;
            }

            updateLinks();

            if (links.contains(other.pos())) {
                link = other.pos();
                configure(-1);
                return false;
            } else if (other.block == block && other.dst(tile) <= range && other.team == team && links.size < maxLinks) {
                if (links.isEmpty()) {
                    configure(other.pos());
                    return false;
                } else {
                    float min = links.min(pos -> angleTo(world.build(pos))) % 360;
                    float max = links.min(pos -> angleTo(world.build(pos))) % 360;
                    float angle = angleTo(other) % 360;
                    if ((angle > min && Angles.angleDist(min, angle) <= 120) || (angle < max && Angles.angleDist(max, angle) <= 120)) {
                        configure(other.pos());
                        return false;
                    }
                }
            }

            return true;
        }

        public void updateLinks() {
            links.remove(pos -> !(world.build(pos) instanceof MultiMassDriverBuild build) || !build.isValid() || build.team != team || build.block != block);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(links.size);
            for (int pos : links) {
                write.i(pos);
            }
            write.f(acceptCounter);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            int num = read.i();
            for (int i = 0; i < num; i++) {
                links.add(read.i());
            }
            this.acceptCounter = read.f();
        }
    }
}
