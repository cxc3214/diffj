package org.incava.diffj;

import java.text.MessageFormat;
import org.incava.analysis.FileDiff;
import org.incava.analysis.FileDiffChange;
import org.incava.ijdk.text.Location;
import org.incava.java.Java;

public class TestCtorDiff extends AbstractTestItemDiff {
    protected final static String[] PARAMETER_MSGS = new String[] {
        CtorDiff.PARAMETER_REMOVED,
        null,
        CtorDiff.PARAMETER_ADDED,
    };

    protected final static String[] THROWS_MSGS = new String[] {
        CtorDiff.THROWS_REMOVED,
        null,
        CtorDiff.THROWS_ADDED,
    };

    public TestCtorDiff(String name) {
        super(name);
    }

    public void xtestAccessAdded() {
        evaluate(new Lines("class Test {",
                           "    Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    public Test() {}",
                           "}"),
                 
                 makeAccessRef(null, "public", loc(2, 5), loc(2, 8), loc(3, 5), loc(3, 10)));
    }

    public void xtestAccessRemoved() {
        evaluate(new Lines("class Test {",
                           "    public Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() {}",
                           "}"),
                 
                 makeAccessRef("public", null, loc(2, 5), loc(2, 10), loc(3, 5), loc(3, 8)));
    }

    // public void xtestAccessRemoved() {
    //     Ref ref = removedAccess("public");
        
    //     evaluate(new Lines("class Test {",
    //                        "    " + str(ref, "public") + " Test() {}",
    //                        "",
    //                        "}"),

    //              new Lines("class Test {",
    //                        "",
    //                        "    " + str(ref) + "Test() {}",
    //                        "}"),
                 
    //              makeAccessRef("public", null, loc(2, 5), loc(2, 10), loc(3, 5), loc(3, 8)));
    // }

    public void xtestAccessChanged() {
        evaluate(new Lines("class Test {",
                           "    private Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    public Test() {}",
                           "}"),
                 
                 makeAccessRef("private", "public", loc(2, 5), loc(3, 5)));
    }

    public void xtestParameterAddedNoneToOne() {
        evaluate(new Lines("class Test {",
                           "    Test() {}",
                           "",
                           "}"),
                 
                 new Lines("class Test {",
                           "",
                           "    Test(Integer i) {}",
                           "}"),

                 makeCodeChangedRef(CtorDiff.PARAMETER_ADDED, "i", loc(2, 9), loc(2, 10), loc(3, 18), loc(3, 18)));
    }

    public void xtestParameterAddedOneToTwo() {
        evaluate(new Lines("class Test {",
                           "    Test(String s) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(String s, Integer i) {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_ADDED, "i", loc(2, 9), loc(2, 18), loc(3, 20), loc(3, 28)));
    }

    public void xtestParameterAddedOneToThree() {
        evaluate(new Lines("class Test {",
                           "    Test(String s) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(List[] ary, String s, Integer i) {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_ADDED, "ary", loc(2,  9), loc(2, 18), loc(3, 10), loc(3, 19)),
                 makeCodeChangedRef(CtorDiff.PARAMETER_ADDED, "i",   loc(2,  9), loc(2, 18), loc(3, 32), loc(3, 40)),
                 new FileDiffChange(paramReordMsg("s", 0, 1), loc(2, 17), loc(2, 17), loc(3, 29), loc(3, 29)));
    }

    public void xtestParameterRemovedOneToNone() {
        evaluate(new Lines("class Test {",
                           "    Test(Integer i[][][][]) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_REMOVED, "i", loc(2, 18), loc(2, 18), loc(3, 9), loc(3, 10)));
    }

    public void xtestParameterRemovedTwoToOne() {
        evaluate(new Lines("class Test {",
                           "    Test(String s, Integer i) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "    Test(String s) {}",
                           "",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_REMOVED, "i", loc(2, 20), loc(2, 28), loc(2, 9), loc(2, 18)));
    }

    public void xtestParameterRemovedThreeToOne() {
        evaluate(new Lines("class Test {",
                           "    Test(List[] ary, String s, Integer i) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "    Test(String s) {}",
                           "",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_REMOVED, "ary", loc(2, 10), loc(2, 19), loc(2,  9), loc(2, 18)),
                 new FileDiffChange(paramReordMsg("s", 1, 0), loc(2, 29), loc(2, 29), loc(2, 17), loc(2, 17)),
                 makeCodeChangedRef(CtorDiff.PARAMETER_REMOVED, "i",   loc(2, 32), loc(2, 40), loc(2,  9), loc(2, 18)));
    }

    public void xtestParameterChangedType() {
        evaluate(new Lines("class Test {",
                           "    Test(int i) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(Integer i) {}",
                           "}"),
                 
                 new FileDiffChange(getMessage(null, null, CtorDiff.PARAMETER_TYPE_CHANGED, "int", "Integer"), 
                                          loc(2, 10), loc(2, 14), loc(3, 10), loc(3, 18)));
    }

    public void xtestParameterChangedName() {
        evaluate(new Lines("class Test {",
                           "    Test(int i) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(int x) {}",
                           "}"),
                 
                 makeChangedRef(CtorDiff.PARAMETER_NAME_CHANGED, "i", "x", loc(2, 14), loc(3, 14)));
    }

    public void xtestParameterReordered() {
        evaluate(new Lines("class Test {",
                           "    Test(int i, double d) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(double d, int i) {}",
                           "}"),
                 
                 new FileDiffChange(paramReordMsg("i", 0, 1), loc(2, 14), loc(2, 14), loc(3, 24), loc(3, 24)),
                 new FileDiffChange(paramReordMsg("d", 1, 0), loc(2, 24), loc(2, 24), loc(3, 17), loc(3, 17)));
    }

    public void xtestParameterReorderedAndRenamed() {
        evaluate(new Lines("class Test {",
                           "    Test(int i, double d) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(double dbl, int i2) {}",
                           "}"),
                 
                 new FileDiffChange(paramReordRenamedMsg("i", 0, "i2",  1), loc(2, 14), loc(2, 14), loc(3, 26), loc(3, 27)),
                 new FileDiffChange(paramReordRenamedMsg("d", 1, "dbl", 0), loc(2, 24), loc(2, 24), loc(3, 17), loc(3, 19)));
    }

    public void xtestParameterOneAddedOneReordered() {
        evaluate(new Lines("class Test {",
                           "    Test(int i) {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(int i2, int i) {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.PARAMETER_ADDED, "i2", loc(2,  9), loc(2, 15), loc(3, 10), loc(3, 15)),
                 new FileDiffChange(paramReordMsg("i", 0, 1), loc(2, 14), loc(2, 14), loc(3, 22), loc(3, 22)));
    }

    public void xtestThrowsAddedNoneToOne() {
        evaluate(new Lines("class Test {",
                           "    Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() throws Exception {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.THROWS_ADDED, "Exception", loc(2, 5), loc(2, 13), loc(3, 19), loc(3, 27)));
    }

    public void xtestThrowsAddedOneToTwo() {
        evaluate(new Lines("class Test {",
                           "    Test() throws IOException {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() throws IOException, NullPointerException {}",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.THROWS_ADDED, "NullPointerException", loc(2, 19), loc(2, 29), loc(3, 32), loc(3, 51)));
    }

    public void xtestThrowsAddedOneToThree() {
        evaluate(new Lines("class Test {",
                           "    Test() throws IOException {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() throws ArrayIndexOutOfBoundsException, IOException, NullPointerException {}",
                           "}"),

                 makeCodeChangedRef(CtorDiff.THROWS_ADDED, "ArrayIndexOutOfBoundsException", loc(2, 19), loc(2, 29), loc(3, 19), loc(3, 48)),
                 new FileDiffChange(throwsReordMsg("IOException", 0, 1), loc(2, 19), loc(2, 29), loc(3, 51), loc(3, 61)),
                 makeCodeChangedRef(CtorDiff.THROWS_ADDED, "NullPointerException", loc(2, 19), loc(2, 29), loc(3, 64), loc(3, 83)));
    }

    public void xtestThrowsRemovedOneToNone() {
        evaluate(new Lines("class Test {",
                           "    Test() throws IOException {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() {}",
                           "}"),

                 makeCodeChangedRef(CtorDiff.THROWS_REMOVED, "IOException", loc(2, 19), loc(2, 29), loc(3, 5), loc(3, 13)));
    }

    public void xtestThrowsRemovedTwoToOne() {
        evaluate(new Lines("class Test {",
                           "    Test() throws IOException, NullPointerException {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "    Test() throws IOException {}",
                           "",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.THROWS_REMOVED, "NullPointerException", loc(2, 32), loc(2, 51), loc(2, 19), loc(2, 29)));
    }

    public void xtestThrowsRemovedThreeToOne() {
        evaluate(new Lines("class Test {",
                           "    Test() throws ArrayIndexOutOfBoundsException, IOException, NullPointerException {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "    Test() throws IOException {}",
                           "",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.THROWS_REMOVED, "ArrayIndexOutOfBoundsException", loc(2, 19), loc(2, 48), loc(2, 19), loc(2, 29)),
                 new FileDiffChange(throwsReordMsg("IOException", 1, 0),  loc(2, 51), loc(2, 61), loc(2, 19), loc(2, 29)),
                 makeCodeChangedRef(CtorDiff.THROWS_REMOVED, "NullPointerException", loc(2, 64), loc(2, 83), loc(2, 19), loc(2, 29)));
    }

    public void xtestThrowsReordered() {
        evaluate(new Lines("class Test {",
                           "    Test() throws ArrayIndexOutOfBoundsException, IOException {}",
                           "",
                           "}"),
                 new Lines("class Test {",
                           "    Test() throws IOException, ArrayIndexOutOfBoundsException {}",
                           "",
                           "}"),
                 
                 new FileDiffChange(throwsReordMsg("ArrayIndexOutOfBoundsException", 0, 1), loc(2, 19), loc(2, 48), loc(2, 32), loc(2, 61)),
                 new FileDiffChange(throwsReordMsg("IOException",                    1, 0), loc(2, 51), loc(2, 61), loc(2, 19), loc(2, 29)));
    }

    public void xtestCodeNotChanged() {
        evaluate(new Lines("class Test {",
                           "    Test() { i = -1; }",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() { ",
                           "        i = -1;",
                           "    }",
                           "",
                           "}"),
                 
                 NO_CHANGES);
    }

    public void xtestCodeChanged() {
        evaluate(new Lines("class Test {",
                           "    Test() { int i = -1; }",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() { ",
                           "        int i = -2;",
                           "    }",
                           "}"),
                 
                 makeCodeChangedRef(CtorDiff.CODE_CHANGED, "Test()", loc(2, 23), loc(2, 23), loc(4, 18), loc(4, 18)));
    }
    
    public void testCodeInsertedSameLine() {
        evaluate(new Lines("class Test {",
                           "    Test() { int i = -1; }",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() { ",
                           "        int j = 0;",
                           "        int i = -1;",
                           "    }",
                           "}"),
                 
                 makeCodeAddedRef(CtorDiff.CODE_ADDED, "Test()", loc(2, 18), loc(2, 18), loc(4, 13), loc(5, 11)));
    }

    public void xtestCodeAddedOwnLine() {
        evaluate(new Lines("class Test {",
                           "    Test() { ",
                           "        char ch;",
                           "        int i = -1;",
                           "    }",
                           "",
                           "}"),
                 
                 new Lines("class Test {",
                           "",
                           "    Test() { ",
                           "        char ch;",
                           "        if (true) { }",
                           "        int i = -1;",
                           "    }",
                           "}"),
                 
                 makeCodeAddedRef(CtorDiff.CODE_ADDED, "Test()", loc(4, 9), loc(4, 11), loc(5, 9), loc(5, 21)));
    }

    public void xtestCodeDeleted() {
        evaluate(new Lines("class Test {",
                           "    Test() { ",
                           "        int j = 0;",
                           "        int i = -1;",
                           "    }",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() { int i = -1; }",
                           "}"),
                 
                 makeCodeDeletedRef(CtorDiff.CODE_REMOVED, "Test()", loc(3, 13), loc(4, 11), loc(3, 18), loc(3, 18)));
    }
    
    public void xtestCodeInsertedAndChanged() {
        evaluate(new Lines("class Test {",
                           "    Test(int i) { i = 1; }",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test(int i) { ",
                           "        int j = 0;",
                           "        i = 2;",
                           "    }",
                           "}"),

                 makeCodeChangedRef(CtorDiff.CODE_CHANGED, "Test(int)", loc(2, 19), loc(2, 23), loc(4,  9), loc(5, 13)));
    }

    public void testCtorWithParameterizedTypeDump() {
        Lines lines = new Lines("public class LogIterator {",
                                "    public <T> LogIterator() {",
                                "    }",
                                "}");
        
        tr.Ace.setVerbose(true);
        tr.Ace.yellow("this", this);

        try {
            JavaFile jf = new JavaFile("name", lines.toString(), "1.6");
            org.incava.pmdx.SimpleNodeUtil.dump(jf.compile());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        evaluate(lines, lines, NO_CHANGES);
    }

    public void testCtorWithoutParameterizedTypeDump() {
        Lines lines = new Lines("public class LogIterator {",
                                "    public LogIterator() {",
                                "    }",
                                "}");
        
        tr.Ace.setVerbose(true);
        tr.Ace.yellow("this", this);

        try {
            JavaFile jf = new JavaFile("name", lines.toString(), "1.6");
            org.incava.pmdx.SimpleNodeUtil.dump(jf.compile());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        evaluate(lines, lines, NO_CHANGES);
    }
        
    public void testCtorWithParameterizedType() {
        tr.Ace.setVerbose(true);
        tr.Ace.yellow("this", this);
        evaluate(new Lines("public class LogIterator {",
                           "    public <T> LogIterator() {",
                           "    }",
                           "}"),
                 new Lines("public class LogIterator {",
                           "    public <T> LogIterator() {",
                           "    }",
                           "}"),
                 Java.SOURCE_1_6,
                 NO_CHANGES);
    }

    protected String paramReordMsg(String paramName, int oldPosition, int newPosition) {
        return MessageFormat.format(CtorDiff.PARAMETER_REORDERED, paramName, Integer.valueOf(oldPosition), Integer.valueOf(newPosition));
    }

    protected String paramReordRenamedMsg(String oldName, int oldPosition, String newName, int newPosition) {
        return MessageFormat.format(CtorDiff.PARAMETER_REORDERED_AND_RENAMED, oldName, Integer.valueOf(oldPosition), Integer.valueOf(newPosition), newName);
    }

    protected String throwsReordMsg(String throwsName, int oldPosition, int newPosition) {
        return MessageFormat.format(CtorDiff.THROWS_REORDERED, throwsName, Integer.valueOf(oldPosition), Integer.valueOf(newPosition));
    }

    protected FileDiff makeChangedRef(String msg, 
                                      String fromStr, String toStr,
                                      Location fromStart, Location toStart) {
        return new FileDiffChange(getMessage(null, null, msg, fromStr, toStr), fromStart, loc(fromStart, fromStr), toStart, loc(toStart, toStr));
    }
}