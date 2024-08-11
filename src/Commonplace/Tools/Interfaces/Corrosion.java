package Commonplace.Tools.Interfaces;

public interface Corrosion {
    float baseDamage();

    default float corrosionLevel() {
        return 1;
    }
}
