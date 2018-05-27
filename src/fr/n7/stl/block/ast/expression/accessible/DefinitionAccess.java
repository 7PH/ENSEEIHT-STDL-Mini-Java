package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.object.AbstractThisUse;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.object.ProgramDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public abstract class DefinitionAccess implements Instruction, Expression {

    protected String target;

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
            // Recover declaration from scope
            if (!scope.knows(target)) {
                Logger.error("MethodAccess: Unknown identifier: " + target);
                return false;
            }
            Declaration declaration = scope.get(target);
            
            if (declaration instanceof AbstractThisUse) {
                this.programDeclaration = ((AbstractThisUse) declaration).programDeclaration;
            } else if (declaration instanceof VariableDeclaration) {
                if (!(declaration.getType() instanceof InstanceType)) {
                    // Method call on a non-object variable
                    Logger.error("Calling a method on a non-object " + target);
                    return false;
                }
                InstanceType type = (InstanceType) declaration.getType();
                this.programDeclaration = type.getDeclaration();
            } else {
                this.programDeclaration = (ProgramDeclaration) scope.get(target);
            }
            this.objectType = (InstanceType) programDeclaration.getType();
        } else {
            if (!object.resolve(scope)) {
                Logger.error("Could not resolve attribute assignment because of: " + object + ".");
                return false;
            }
            Type type = object.getType();
            if (!(type instanceof InstanceType)) {
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