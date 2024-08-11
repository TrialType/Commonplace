package Commonplace.Content.ProjectContent;

public class Sign {
    public static Commonplace.FType.New.Sign sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5;
    public static Commonplace.FType.New.Sign[] allSize;

    public static void load() {
        sizeProject1 = new Commonplace.FType.New.Sign("project1");
        sizeProject2 = new Commonplace.FType.New.Sign("project2");
        sizeProject3 = new Commonplace.FType.New.Sign("project3");
        sizeProject4 = new Commonplace.FType.New.Sign("project4");
        sizeProject5 = new Commonplace.FType.New.Sign("project5");

        allSize = new Commonplace.FType.New.Sign[]{sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5};
    }
}
