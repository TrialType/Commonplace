package Commonplace.Entities.FUnit.Override;

import Commonplace.Tools.Interfaces.PeculiarityC;
import arc.math.Mathf;
import arc.struct.Bits;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.gen.CrawlUnit;
import mindustry.io.TypeIO;

public class FCrawlUnit extends CrawlUnit implements PeculiarityC {
    protected IntSeq pes = new IntSeq();

    protected FCrawlUnit() {
        this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
        this.crawlTime = Mathf.random(100.0F);
        this.lastCrawlSlowdown = 1.0F;
        this.resupplyTime = Mathf.random(10.0F);
        this.statuses = new Seq<>();
    }

    public static FCrawlUnit create() {
        return new FCrawlUnit();
    }

    @Override
    public int classId() {
        return 110;
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        pes = TypeIO.readIntSeq(read);
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
