package Floor.FContent;

import arc.struct.Seq;
import mindustry.content.*;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

import static Floor.FContent.FBlocks.*;
import static Floor.FContent.FItems.*;
import static Floor.FContent.FPlanetGenerators.fullWater;
import static Floor.FContent.FPlanetGenerators.longestDown;
import static Floor.FContent.FPlanets.ENGSWEIS;
import static Floor.FContent.FUnits.*;
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
                node(blueprint1, () -> {
                    node(blueprint2, ItemStack.with(), () -> {
                        node(blueprint3, ItemStack.with(), () -> {
                            node(blueprint4, ItemStack.with(), () -> {
                                node(blueprint5, ItemStack.with(), () -> {
                                });
                            });
                        });
                    });
                    node(sizeProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(sizeProject2, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                            node(sizeProject3, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                node(sizeProject4, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(sizeProject5, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                        node(sizeProject6, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                            node(sizeProject7, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                                node(sizeProject8, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                                    node(sizeProject9, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                                        node(sizeProject10, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                                                        });
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                    node(healthProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(healthProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(healthProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(healthProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(healthProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(speedProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(speedProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(speedProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(speedProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(speedProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(bulletProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(bulletProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(bulletProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(bulletProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(bulletProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(reloadProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(reloadProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(reloadProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(reloadProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(reloadProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(shieldProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(shieldProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(shieldProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(shieldProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(shieldProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(splashProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(splashProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(splashProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(splashProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(splashProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(knockProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(knockProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(knockProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(knockProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(knockProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(percentProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(percentProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(percentProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(percentProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(percentProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(lightningProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(lightningProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(lightningProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(lightningProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(lightningProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(empProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(empProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(empProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(empProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(empProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(fragProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(fragProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(fragProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(fragProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(fragProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(targetIntervalProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(targetIntervalProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(targetIntervalProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(targetIntervalProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(targetIntervalProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(bulletNumberProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(bulletNumberProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(bulletNumberProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(bulletNumberProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(bulletNumberProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(suppressionProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(suppressionProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(suppressionProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(suppressionProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(suppressionProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(puddlesProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(puddlesProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(puddlesProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(puddlesProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(puddlesProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(powerProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(powerProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(powerProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(powerProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(powerProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(boostProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(boostProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(boostProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(boostProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(boostProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(statusProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(statusProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(statusProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(statusProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(statusProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(boostBaseProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(boostBaseProject5, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(boostBaseProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(boostBaseProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(boostBaseProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                    node(boostReloadProject1, ItemStack.with(blueprint1, 1000, silicon, 1000, copper, 1000), () -> {
                        node(boostReloadProject2, ItemStack.with(blueprint2, 1000, silicon, 1000, copper, 1000), () -> {
                            node(boostReloadProject3, ItemStack.with(blueprint3, 1000, silicon, 1000, copper, 1000), () -> {
                                node(boostReloadProject4, ItemStack.with(blueprint4, 1000, silicon, 1000, copper, 1000), () -> {
                                    node(boostReloadProject5, ItemStack.with(blueprint5, 1000, silicon, 1000, copper, 1000), () -> {
                                    });
                                });
                            });
                        });
                    });
                });
            });
            node(autoWall, ItemStack.with(Items.copper, 1450, Items.graphite, 350), () -> {
                node(edge, ItemStack.with(Items.copper, 3000, Items.graphite, 1200, Items.thorium, 350), () -> {
                });
            });
            node(decoy, ItemStack.with(Items.copper, 3500, Items.silicon, 2500, Items.graphite, 2500, Items.titanium, 3000, Items.thorium, 3000), () -> {
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
