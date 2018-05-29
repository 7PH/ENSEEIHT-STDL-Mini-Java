package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.object.AbstractThisUse;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.object.ProgramDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public abstract class DefinitionAccess extends AbstractUse implements Instruction, Expression {

    protected String target;

    protected VariableDeclaration declaration;

    protected String name;

    protected Expression object;
    
    protected InstanceType objectType;

    protected ProgramDeclaration programDeclaration;

    public DefinitionAccess(String target, String name) {
        this.target = target;
        this.name = name;
    }

    public DefinitionAccess(Expression object, String name) {
        this.object = object;
        this.name = name;
    }

    protected abstract boolean subResolve(HierarchicalScope<Declaration> scope);

    @Override
    public final boolean resolve(HierarchicalScope<Declaration> scope) {

        if (object == null) {
            if (! scope.knows(target)) {
                Logger.error("MethodAccess: Unknown identifier: " + target);
                return false;
            }

            Declaration decl = scope.get(target);
            if (decl instanceof AbstractThisUse) {
                this.programDeclaration = ((AbstractThisUse)decl).programDeclaration;
            } else if (decl instanceof VariableDeclaration) {
                if (! (decl.getType() instanceof  InstanceType)) {
                    // appel de méthode sur une variable non objet
                    Logger.error("Calling a method on a non-object " + target);
                    return false;
                }
                InstanceType type = (InstanceType)decl.getType();
                this.programDeclaration = type.getDeclaration();
            } else {
                Logger.error("DefinitionAccess: trying to get a non-object property");
                return false;
            }
            this.declaration = (VariableDeclaration) decl;
            this.objectType = (InstanceType) programDeclaration.getType();
        } else {
            if (! object.resolve(scope)) {
                Logger.error("Could not resolve attribute assignment because of: " + object + ".");
                return false;
            }
            Type type = object.getType();
            if (! (type instanceof InstanceType)) {
                Logger.error(object + " is not a InstanceType.");
                return false;
            }
            this.objectType = (InstanceType) type;
            this.programDeclaration = objectType.getDeclaration();
        }

        return subResolve(scope);
    }

    @Override
    public String toString() {
        return (object == null ? target : object) + "." + name;
    }
}