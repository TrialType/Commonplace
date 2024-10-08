package Commonplace.Entities.FUnit.Override;

import Commonplace.Utils.Classes.UnitPeculiarity;
import Commonplace.Utils.Interfaces.PeculiarityC;
import arc.struct.IntSeq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.*;
import mindustry.io.TypeIO;

public class FElevationMoveUnit extends ElevationMoveUnit implements PeculiarityC {
    protected boolean uploaded = true;
    protected IntSeq pes = new IntSeq();

    public static FElevationMoveUnit create() {
        return new FElevationMoveUnit();
    }

    @Override
    public int classId() {
        return 109;
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
