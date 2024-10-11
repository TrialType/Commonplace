package Commonplace.Type.New;

import Commonplace.Utils.Classes.ProjectUtils;
import arc.func.Cons;
import arc.struct.Seq;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class UnitProject extends UnlockableContent {
    public final static Seq<UnitProject> all = new Seq<>();

    public final int sid;
    public final int max;
    public final float heavy;
    public final Cons<Unit> applier;
    public Runnable init = () -> {
    };

    public UnitProject(float heavy, int max, String name, Cons<Unit> applier) {
        super(name);
        this.max = max;
        this.applier = applier;
        this.heavy = heavy;
        sid = all.size;
        all.add(this);
    }

    public static UnitProject get(int id) {
        return all.get(id);
    }

    public static void weaponApply(Weapon weapon, Unit u) {
        WeaponMount[] mount;
        if (weapon.mirror) {
            mount = new WeaponMount[u.mounts.length + 2];
            System.arraycopy(u.mounts, 0, mount, 0, u.mounts.length);
            Weapon w = weapon.copy();
            w.bullet = w.bullet.copy();
            w.flip();
            w.recoilTime *= 2f;
            w.reload *= 2f;
            w.otherSide = mount.length - 1;
            if (w.recoilTime < 0) w.recoilTime = w.reload;
            BoostProject.weaponBoos.get(w);
            ProjectUtils.init(w.bullet);
            mount[mount.length - 2] = new WeaponMount(w);
            w = weapon.copy();
            w.bullet = w.bullet.copy();
            w.flip();
            w.flip();
            w.recoilTime *= 2f;
            w.reload *= 2f;
            w.otherSide = mount.length - 2;
            if (w.recoilTime < 0) w.recoilTime = w.reload;
            BoostProject.weaponBoos.get(w);
            ProjectUtils.init(w.bullet);
            mount[mount.length - 1] = new WeaponMount(w);
        } else {
            mount = new WeaponMount[u.mounts.length + 1];
            System.arraycopy(u.mounts, 0, mount, 0, u.mounts.length);
            Weapon w = weapon.copy();
            w.bullet = w.bullet.copy();
            if (w.recoilTime < 0) w.recoilTime = w.reload;
            BoostProject.weaponBoos.get(w);
            w.bullet.init();
            mount[mount.length - 1] = new WeaponMount(w);
        }
        u.mounts = mount;
    }

    public static void abilityApply(Ability ability, Unit u) {
        Ability[] abilities = new Ability[u.abilities.length + 1];
        System.arraycopy(u.abilities, 0, abilities, 0, u.abilities.length);
        abilities[abilities.length - 1] = ability.copy();
        abilities[abilities.length - 1].init(u.type);
        BoostProject.abilityBoos.get(ability);
        u.abilities = abilities;
    }

    @Override
    public void init() {
        super.init();
        init.run();
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.typeid_UNUSED;
    }
}
