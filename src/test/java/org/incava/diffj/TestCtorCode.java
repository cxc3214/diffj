package org.incava.diffj;

import java.text.MessageFormat;

public class TestCtorCode extends ItemsTest {
    public TestCtorCode(String name) {
        super(name);
        tr.Ace.setVerbose(true);
    }

    public void testCodeNotChanged() {
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

    public void testCodeChanged() {
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
                 
                 makeCodeChangedRef(Messages.CODE_CHANGED, "Test()", loc(2, 23), loc(2, 23), loc(4, 18), loc(4, 18)));
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
                 
                 makeCodeAddedRef(Messages.CODE_ADDED, "Test()", loc(2, 18), loc(2, 18), loc(4, 13), loc(5, 11)));
    }

    public void testCodeAddedOwnLine() {
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
                 
                 makeCodeAddedRef(Messages.CODE_ADDED, "Test()", loc(4, 9), loc(4, 11), loc(5, 9), loc(5, 21)));
    }

    public void testCodeDeleted() {
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
                 
                 makeCodeDeletedRef(Messages.CODE_REMOVED, "Test()", loc(3, 13), loc(4, 11), loc(3, 18), loc(3, 18)));
    }
    
    public void testCodeInsertedAndChanged() {
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

                 makeCodeChangedRef(Messages.CODE_CHANGED, "Test(int)", loc(2, 19), loc(2, 23), loc(4,  9), loc(5, 13)));
    }
}
