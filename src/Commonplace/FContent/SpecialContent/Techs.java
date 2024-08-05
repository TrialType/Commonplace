package Commonplace.FContent.SpecialContent;

import Commonplace.FContent.DefaultContent.FBlocks;
import arc.struct.Seq;
import mindustry.content.*;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

import static Commonplace.FContent.DefaultContent.FBlocks.*;
import static Commonplace.FContent.ProjectContent.FSign.*;
import static Commonplace.FContent.DefaultContent.FPlanetGenerators.fullWater;
import static Commonplace.FContent.DefaultContent.FPlanetGenerators.longestDown;
import static Commonplace.FContent.DefaultContent.FUnits.*;
import static Commonplace.FContent.ProjectContent.UnitProjects.*;
import static mindustry.content.Blocks.*;
import static mindustry.content.Items.*;
import static mindustry.content.SectorPresets.*;
import static mindustry.content.TechTree.node;
import static mindustry.type.ItemStack.with;

public class Techs {
    public static TechTree.TechNode wes;
    public static TechTree.TechNode tf;
    public static TechTree.TechNode head;

    public static void load() {
        wes = TechTree.nodeRoot("wes", buildCore, () -> {
            node(unitUpper, ItemStack.with(Items.copper, 20000, Items.lead, 20000, Items.graphite, 20000, Items.silicon, 20000, Items.titanium, 20000), () -> {
                node(sizeProject1, ItemStack.with(silicon, 100, titanium, 100), () -> {
                    node(sizeProject2, ItemStack.with(silicon, 300, titanium, 300), () -> {
                        node(sizeProject3, ItemStack.with(silicon, 900, titanium, 900), () -> {
                            node(sizeProject4, ItemStack.with(silicon, 2700, titanium, 2700), () -> {
                                node(sizeProject5, ItemStack.with(silicon, 8100, titanium, 8100), () -> {
                                });
                            });
                        });
                    });
                });

                node(handTurret, ItemStack.with(silicon, 100, titanium, 100), () -> {
                    node(fragsAdd, ItemStack.with(silicon, 100, titanium, 100), () -> {
                        node(damageAdder, ItemStack.with(silicon, 100, titanium, 100), () -> {
                            node(fragDamageAdder, ItemStack.with(silicon, 100, titanium, 100), () -> {
                            });
                        });
                    });
                });
            });
            node(autoWall, ItemStack.with(Items.copper, 1450, Items.graphite, 350), () -> {
                node(edge, ItemStack.with(Items.copper, 3000, Items.graphite, 1200, Items.thorium, 350), () -> {
                });
            });
            node(decoyLarge, ItemStack.with(Items.copper, 3500, Items.silicon, 2500, Items.graphite, 2500, Items.titanium, 3000, Items.thorium, 3000), () -> {
            });
            node(FBlocks.primarySolidification, ItemStack.with(Items.metaglass, 350, Items.copper, 400, Items.lead, 250), Seq.with(new Objectives.SectorComplete(longestDown)), () -> {
                node(FBlocks.intermediateSolidification, ItemStack.with(Items.metaglass, 1250, Items.copper, 1500, Items.lead, 1000, Items.graphite, 1400), () -> {
                    node(FBlocks.advancedSolidification, ItemStack.with(Items.metaglass, 5000, Items.copper, 4500, Items.lead, 4000, Items.graphite, 3500, Items.titanium, 3000), () -> {
                        node(FBlocks.ultimateSolidification, ItemStack.with(Items.metaglass, 15000, Items.copper, 14500, Items.lead, 14000, Items.graphite, 13500, Items.titanium, 14000, Items.thorium, 14500, Items.surgeAlloy, 5000), () -> {
                        });
                    });
                });
            });
            node(mountain, ItemStack.with(Items.titanium, 3400, Items.copper, 3000, Items.graphite, 3500), Seq.with(new Objectives.SectorComplete(stainedMountains)), () -> {
            });
            node(eleFenceII, ItemStack.with(Items.titanium, 3500, Items.copper, 6000, Items.silicon, 3000), Seq.with(new Objectives.SectorComplete(saltFlats)), () -> {
                node(eleFenceIII, ItemStack.with(Items.titanium, 4500, Items.copper, 10000, Items.silicon, 5000), Seq.with(new Objectives.SectorComplete(nuclearComplex)), () -> {
                });
            });
            node(longestDown, Seq.with(new Objectives.SectorComplete(impact0078)), () -> {
                node(fullWater, Seq.with(new Objectives.SectorComplete(longestDown)), () -> {
                });
            });
        });
        head = Planets.serpulo.techTree;
        //blocks
        head.each(t -> {
            if (t.content == tsunami) {
                tf = node(fourNet, ItemStack.with(Items.titanium, 49990, Items.copper, 49990, Items.thorium, 49990, Items.silicon, 49990, Items.phaseFabric, 49990), Seq.with(new Objectives.SectorComplete(fullWater)), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == scorch) {
                tf = node(fireBoost, ItemStack.with(Items.titanium, 15000, Items.graphite, 15000, Items.graphite, 20000, Items.silicon, 15000, Items.phaseFabric, 15000, Items.plastanium, 9000), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == overdriveProjector) {
                tf = node(slowProject, ItemStack.with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == additiveReconstructor) {
                tf = node(outPowerFactory, ItemStack.with(Items.copper, 5000, Items.lead, 6000, Items.silicon, 8000, titanium, 4000, thorium, 4000), Seq.with(new Objectives.SectorComplete(planetaryTerminal)), () -> {
                    node(inputPowerFactory, ItemStack.with(Items.copper, 5000, Items.lead, 6000, Items.silicon, 8000, titanium, 4000, thorium, 4000), () -> {
                    });
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == conveyor) {
                tf = node(toHome, ItemStack.with(Items.copper, 500, silicon, 200, graphite, 300), Seq.with(new Objectives.Research(silicon)), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == forceProjector) {
                tf = node(reflective, ItemStack.with(titanium, 4000, silicon, 4000, phaseFabric, 1200), Seq.with(new Objectives.Research(phaseFabric)), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == salvo) {
                tf = node(residual, with(Items.copper, 1000, Items.graphite, 800, Items.titanium, 250, Items.silicon, 250), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == foreshadow) {
                tf = node(windTurret, ItemStack.with(Items.titanium, 20000, Items.copper, 26000, Items.graphite, 18000, Items.silicon, 16500, Items.surgeAlloy, 3000), Seq.with(new Objectives.SectorComplete(planetaryTerminal)), () -> {
                });
                tf.parent = t;
                t.children.add(tf);
            } else if (t.content == surgeWall) {
                tf = node(polymerizationWall, () -> node(polymerizationWallLarge, () -> {
                }));
                tf.parent = t;
                t.children.add(tf);
            }
        });
        //units
        head.each(t -> {
            if (t.content == UnitTypes.dagger) {
                tf = node(barb, () -> node(hammer, () -> node(buying, () -> node(crazy, () -> node(transition, () -> node(shuttle, () -> {
                }))))));
                tf.parent = t;
                t.children.add(tf);
            }
        });
        //items
    }
}
