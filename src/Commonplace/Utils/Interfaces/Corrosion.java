package Commonplace.Utils.Interfaces;

public interface Corrosion {
    float baseDamage();

    default float corrosionLevel() {
        return 1;
    }
}
