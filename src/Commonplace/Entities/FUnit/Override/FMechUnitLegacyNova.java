package Commonplace.Entities.FUnit.Override;

import Commonplace.Tools.Classes.UnitPeculiarity;
import Commonplace.Tools.Interfaces.PeculiarityC;
import arc.struct.IntSeq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.MechUnitLegacyNova;
import mindustry.io.TypeIO;

public class FMechUnitLegacyNova extends MechUnitLegacyNova implements PeculiarityC {
    protected boolean uploaded = false;
    protected IntSeq pes = new IntSeq();

    public static FMechUnitLegacyNova create() {
        return new FMechUnitLegacyNova();
    }

    @Override
    public int classId() {
        return 111;
    }

    @Override
    public void update() {
        if (!uploaded) {
            UnitPeculiarity.load(this, pes.items);
            uploaded = true;
        }
        super.update();
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        pes = TypeIO.readIntSeq(read);
        uploaded = false;
    }

    public void write(Writes write) {
        super.write(write);
        TypeIO.writeIntSeq(write, pes);
    }

    @Override
    public IntSeq getPes() {
        return pes;
    }
}
