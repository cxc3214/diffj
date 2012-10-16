package org.incava.diffj.compunit;

import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.ast.SimpleNode;
import org.incava.diffj.Messages;
import org.incava.diffj.element.Differences;
import org.incava.pmdx.CompilationUnitUtil;
import org.incava.pmdx.SimpleNodeUtil;

public class Package {
    private final ASTCompilationUnit compUnit;
    private final ASTPackageDeclaration pkg;

    public Package(ASTCompilationUnit compUnit) {
        this.compUnit = compUnit;
        this.pkg = CompilationUnitUtil.getPackage(compUnit);
    }

    public boolean isEmpty() {
        return pkg == null;
    }

    public void diff(Package toPackage, Differences differences) {
        if (isEmpty()) {
            if (!toPackage.isEmpty()) {
                ASTName    name    = toPackage.getPackageName();
                SimpleNode fromPos = getChild();
                differences.added(fromPos, name, Messages.PACKAGE_ADDED);
            }
        }
        else if (toPackage.isEmpty()) {
            ASTName    name  = getPackageName();
            SimpleNode toPos = toPackage.getChild();
            differences.deleted(name, toPos, Messages.PACKAGE_REMOVED);
        }
        else {
            ASTName fromName = getPackageName();
            String  fromStr  = SimpleNodeUtil.toString(fromName);
            ASTName toName   = toPackage.getPackageName();
            String  toStr    = SimpleNodeUtil.toString(toName);

            if (!fromStr.equals(toStr)) {
                differences.changed(fromName, toName, Messages.PACKAGE_RENAMED);
            }
        }
    }

    protected SimpleNode getChild() {
        SimpleNode child = SimpleNodeUtil.findChild(compUnit);
        return child == null ? compUnit : child;
    }

    public ASTName getPackageName() {
        return SimpleNodeUtil.findChild(pkg, net.sourceforge.pmd.ast.ASTName.class);
    }
}
