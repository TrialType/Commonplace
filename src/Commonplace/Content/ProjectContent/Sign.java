package Commonplace.Content.ProjectContent;

public class Sign {
    public static Commonplace.Type.New.Sign sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5;
    public static Commonplace.Type.New.Sign[] allSize;

    public static void load() {
        sizeProject1 = new Commonplace.Type.New.Sign("project1");
        sizeProject2 = new Commonplace.Type.New.Sign("project2");
        sizeProject3 = new Commonplace.Type.New.Sign("project3");
        sizeProject4 = new Commonplace.Type.New.Sign("project4");
        sizeProject5 = new Commonplace.Type.New.Sign("project5");

        allSize = new Commonplace.Type.New.Sign[]{sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5};
    }
}
