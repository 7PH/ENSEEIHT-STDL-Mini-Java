package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.LinkedList;
import java.util.List;

public class ClassDeclaration extends ProgramDeclaration {
	
	private ClassModifier modifier;

	private List<Definition> definitions;
	
	public ClassDeclaration(ClassModifier modifier, ClassName name, List<InstanceType> extension, List<InstanceType> implementation, List<Definition> definitions) {
		this.modifier = modifier;
		this.className = name;
		this.extendedClass = extension;
		this.implementedClasses = implementation;
		this.definitions = definitions;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}
	
	public boolean isAbstract() {
		return this.modifier == ClassModifier.ABSTRACT;
	}

	public boolean isFinal() {
		return (this.modifier == ClassModifier.FINAL);
	}

    public AttributeDefinition getAttributeDeclaration(String name, boolean returnPrivates) {
        for (Definition definition: definitions) {
            if (definition instanceof AttributeDefinition
                    && definition.getName().equals(name)
                    && (returnPrivates || definition.getAccessModifier() == AccessModifier.PUBLIC))
                return (AttributeDefinition) definition;
        }
        if (extendedClass.size() > 0) {
            return ((ClassDeclaration) extendedClass.get(0).getDeclaration()).getAttributeDeclaration(name, false);
        }
        return null;
    }

    public AttributeDefinition getAttributeDeclaration(String name) {
        return getAttributeDeclaration(name, true);
    }

    public boolean hasAttribute(String name) {
        return getAttributeDeclaration(name) != null;
    }

    public boolean hasAttribute(String name, boolean returnPrivates) {
        return getAttributeDeclaration(name, returnPrivates) != null;
    }

    // @TODO check also parameters
    public boolean definesMethod(String name, List<Expression> parameters) {
        for (Definition definition: definitions) {
            if (! (definition instanceof MethodDefinition))
                continue;
            MethodDefinition method = (MethodDefinition) definition;
            if (method.getSignature().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public boolean definesMethod(String name) { return definesMethod(name, new LinkedList<>()); }
    public boolean definesMethod(Signature signature) { return definesMethod(signature.getName()); }

    public boolean hasMethod(String name, List<Expression> parameters) {
        return definesMethod(name, parameters)
                || (extendedClass.size() > 0 && ((ClassDeclaration)extendedClass.get(0).getDeclaration()).definesMethod(name, parameters));
    }
    public boolean hasMethod(String name) { return hasMethod(name, new LinkedList<>()); }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {
        if (extendedClass.size() > 0 && extendedClass.get(0).getDeclaration().getName().equals(this.getName())) {
            Logger.error(this.getName() + " cannot extend self");
            return false;
        }

        for (InstanceType tp: this.implementedClasses) {
            if (!tp.resolve(scope))
                return false;
        }

        // Define a new scope for methods/attributes
        HierarchicalScope<Declaration> newScope = new SymbolTable(scope);

        // Resolve for each definition in the new scope
        for (Definition definition: definitions) {
            if (! definition.resolve(newScope)) {
                Logger.error("Could not resolve class " + getName() + " because of an unresolvable definition: " + definition.getClass().getCanonicalName());
                return false;
            }
        }

        return true;
    }

	private List<Signature> getClassSignatures() {
		List<Signature> classSignatures = new LinkedList<>();
		for (Definition d : this.definitions) {
			if (d instanceof MethodDefinition) {
				classSignatures.add(((MethodDefinition) d).getSignature());
			}
		}
		return classSignatures;
	}

	@Override
	public boolean checkType() {
		boolean b = true;

        // Verify that implementedClass contains only interfaces
        for (InstanceType tp: extendedClass) {
            if (! (tp.getDeclaration() instanceof ClassDeclaration)) {
                Logger.error("The class " + this.getName() + " implements " + tp.getDeclaration().getName() + " which is not a interface.");
                b = false;
            }
        }

        // Verify that each interface method is implemented
        for (InstanceType tp: implementedClasses) {
            if (! (tp.getDeclaration() instanceof InterfaceDeclaration)) {
                Logger.error("Class " + getName() + " cannot implements " + tp + ". This is not an interface.");
                return false;
            }

            InterfaceDeclaration interfaceDeclaration = (InterfaceDeclaration) tp.getDeclaration();
            for (Signature signature: interfaceDeclaration.getSignatures()) {
                if (! definesMethod(signature)) {
                    Logger.error("The class, by implementing " + tp.getDeclaration().getName() + ", needs to implement the method " + signature.getName().split(" ")[0] + ".");
                    return false;
                }
            }
        }

        // Verify that there is no more one superclass
        if (this.extendedClass.size() > 1 ) {
            Logger.error("A simple class like " + this.getName() + " can not have more than one superclass.");
            return false;
        }

        // TODO: Remonter la chaîne des extends/implements pour checker les methodes abstraites

        // Verify if every superclass abstract methods are implemented
        if (this.extendedClass.size() > 0) {
            InstanceType tp = this.extendedClass.get(0);
            // Check if the superclass is well a simple class
            if (tp.getDeclaration() instanceof InterfaceDeclaration) {
                Logger.error("The class " + this.getName() + " extends the interface "+ tp.getDeclaration().getName() + " which is not correct.");
                return false;
            }
            ClassDeclaration classDeclaration = (ClassDeclaration) tp.getDeclaration();
            for (Definition d : classDeclaration.getDefinitions()) {
                if (d instanceof MethodDefinition) {
                    MethodDefinition md = (MethodDefinition) d;

                    // For each abstract method of the superclass, we check if it is implemented
                    if (md.isAbstract() && ! definesMethod(md.getSignature().getName())) {
                        Logger.error("The class, by extending " + classDeclaration.getName() + ", needs to implement the method " + md.getName() + ".");
                        return false;
                    }
                }
            }
        }

        // Verify if a method is abstract and accord it with class modifier
        for (Definition definition: definitions) {
            if (definition instanceof MethodDefinition) {
                if (((MethodDefinition) definition).isAbstract() && ! isAbstract()) {
                    Logger.error("Non abstract class" + getName() + " cannot contain abstract method " + definition.getName() + ".");
                    return false;
                }
            } else if (definition instanceof AttributeDefinition) {
                // ok
            } else {
                Logger.error("ClassDeclaration: Wrong kind of definition for: " + definition);
            }
        }

		for (Definition d : this.definitions) {
			if (! d.checkType()) {
				Logger.error(d.getName() + " definition raised false checktype");
				b = false;
			}
		}

		return b;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
		throw new SemanticsUndefinedException("allocateMemory method not implemented yet in ClassDeclaration");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method not implemented yet in ClassDeclaration");
	}

	@Override
	public Type getReturnType() {
		return AtomicType.VoidType;
	}

	@Override
	public String toString() {
		String result = "class " + className.toString();

		if (extendedClass.size() > 0)
		    result += " extends " + extendedClass.get(0);

		if (implementedClasses.size() > 0) {
		    result += " implements ";
		    for (int i = 0; i < implementedClasses.size() - 1; ++ i)
                result += implementedClasses.get(i) + ", ";
		    result += implementedClasses.get(implementedClasses.size() - 1);
        }

        result += " {" + "\n";

        for (Definition d : this.definitions)
            result += d + "\n";

		result += "\n" + "}" + "\n";
		return result;
	}

}
