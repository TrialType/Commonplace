package Floor.FType.New;

import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;

public class Sign extends UnlockableContent {
    public Sign(String name) {
        super(name);
    }

    @Override
    public ContentType getContentType() {
        return ContentType.typeid_UNUSED;
    }
}
