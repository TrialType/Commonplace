package Commonplace.FContent.ProjectContent;

import Commonplace.FType.New.Sign;

public class FSign {
    public static Sign sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5;
    public static Sign[] allSize;

    public static void load() {
        sizeProject1 = new Sign("project1");
        sizeProject2 = new Sign("project2");
        sizeProject3 = new Sign("project3");
        sizeProject4 = new Sign("project4");
        sizeProject5 = new Sign("project5");

        allSize = new Sign[]{sizeProject1, sizeProject2, sizeProject3, sizeProject4, sizeProject5};
    }
}
