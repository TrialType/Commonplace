package Commonplace.Entities.Ability;

import arc.func.Floatf;
import arc.func.Intf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Building;
import mindustry.gen.Unit;

import static mindustry.Vars.content;

public class PowerItemTakeAbility extends Ability {
    public float powerTake = 150;
    public float powerRange = 200;
    public float powerReload = 90;
    public float produceDamage = 70;
    public Floatf<Float> powerHeal = f -> f;
    public Floatf<Float> powerReversal = f -> f;

    public int itemTake = 15;
    public float itemRange = 200;
    public float itemReload = 90;
    public Floatf<Integer> itemHeal = i -> 10 * i;
    public Intf<Float> itemReversal = f -> (int) (1 + f / 10f);

    protected float powerTimer = 0;
    protected float itemTimer = 0;

    @Override
    public void update(Unit unit) {
        itemTimer -= Time.delta;
        powerTimer -= Time.delta;

        if ((itemTimer <= 0 && unit.maxHealth - unit.health > 1) || (unit.damaged() && powerTimer <= 0)) {
            Seq<Building> powers = new Seq<>();
            Seq<Building> items = new Seq<>();
            Units.nearbyBuildings(unit.x, unit.y, Math.max(powerRange, itemRange), b -> {
                if (b.team != unit.team) {
                    if (b.block.hasPower && b.within(unit, powerRange) && (b.block.consPower != null && b.block.consPower.buffered || b.power.graph.producers.any() || b.power.graph.batteries.any()) && b.power.graph.getLastPowerStored() > 0) {
                        powers.add(b);
                    }
                    if (b.block.hasItems && b.within(unit, itemRange) && b.items.any()) {
                        items.add(b);
                    }
                }
            });

            if (powers.any() && powerTimer <= 0) {
                powerTimer = powerReload;
                powers.each(b -> {
                    if (unit.damaged()) {
                        float taken = Math.min(powerReversal.get(unit.maxHealth - unit.health), Math.min(powerTake, b.power.graph.getLastPowerStored()));
                        unit.heal(powerHeal.get(taken));
                        b.power.graph.useBatteries(taken);
                        Fx.itemTransfer.at(b.x, b.y, 0, unit);
                    }
                    if (!b.block.consumesPower && !b.block.outputsPower || b.block.outputsPower && (!b.block.consumesPower || !b.block.consPower.buffered)) {
                        b.damage(produceDamage);
                    }
                });
            }

            if (unit.maxHealth - unit.health > 1 && items.any() && itemTimer <= 0) {
                itemTimer = itemReload;
                items.each(b -> {
                    if (unit.maxHealth - unit.health > 1) {
                        int[] take = {0};
                        int[] taken = {Math.min(itemReversal.get(unit.maxHealth - unit.health), Math.min(itemTake, b.items.total()))};
                        unit.heal(itemHeal.get(taken[0]));
                        content.items().each(i -> {
                            if (taken[0] > 0 && b.items.has(i)) {
                                int num = Math.min(taken[0], b.items.get(i));
                                b.items.remove(i, num);
                                taken[0] -= num;
                                take[0] += num;
                            }
                        });
                        if (take[0] > 0) {
                            Fx.itemTransfer.at(b.x, b.y, 0, unit);
                        }
                    }
                });
            }
        }
    }
}
