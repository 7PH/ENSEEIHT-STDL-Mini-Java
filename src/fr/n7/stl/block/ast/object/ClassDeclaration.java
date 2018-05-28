package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.ArrayList;
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

    public List<AttributeDefinition> getAttributes() {
        List<AttributeDefinition> attributes = new LinkedList<>();
        for (Definition d : definitions) {
            if (d instanceof AttributeDefinition) {
                attributes.add((AttributeDefinition) d);
            }
        }
        return attributes;
    }

    public boolean hasAttribute(String name) {
        return getAttributeDeclaration(name) != null;
    }

    public boolean hasAttribute(String name, boolean returnPrivates) {
        return getAttributeDeclaration(name, returnPrivates) != null;
    }

    // will check for: name
    public List<MethodDefinition> getMethodDefinitionsByMethodName(String name, boolean recursive) {
        List<MethodDefinition> matches = new ArrayList<>();
        for (Definition definition: definitions) {

            if (! (definition instanceof MethodDefinition))
                continue;

            MethodDefinition method = (MethodDefinition) definition;
            if (method.getSignature().getMethodName().equals(name))
                matches.add(method);
        }
        // go check superclass
        if (recursive && extendedClass.size() > 0)
            matches.addAll(((ClassDeclaration)extendedClass.get(0).getDeclaration()).getMethodDefinitionsByMethodName(name, true));
        return matches;
    }

    // will check for: name + params
    public List<MethodDefinition> getMethodDefinitionsByMethodNameAndParams(String name, List<ParameterDeclaration> params, boolean returnIfCompatible, boolean recursive) {
        List<MethodDefinition> matches = getMethodDefinitionsByMethodName(name, recursive);
        matches.removeIf(definition -> {
            List<ParameterDeclaration> defParams = definition.getSignature().getParameters();
            if (defParams.size() != params.size()) return true;
            for (int i = 0; i < params.size(); ++ i) {
                if (! returnIfCompatible && ! params.get(i).getType().equalsTo(defParams.get(i).getType()))
                    return true;
                if (! params.get(i).getType().compatibleWith(defParams.get(i).getType()))
                    return true;
            }
            return false;
        });
        return matches;
    }

    // will check for: return type + name + params
    public List<MethodDefinition> getMethodDefinitionsBySignature(Signature signature, boolean recursive) {
        List<MethodDefinition> matches = getMethodDefinitionsByMethodNameAndParams(signature.getMethodName(), signature.getParameters(), true, recursive);
        matches.removeIf(definition -> ! definition.getSignature().getType().equalsTo(signature.getType()));
        return matches;
    }

    public MethodDefinition getMethodDefinitionBySignature(Signature signature, boolean recursive) {
        List<MethodDefinition> matches = getMethodDefinitionsBySignature(signature, recursive);
        return matches.size() > 0 ? matches.get(0) : null;
    }

    public boolean definesMethod(String name, boolean recursive) {
        return getMethodDefinitionsByMethodName(name, recursive).size() > 0;
    }

    public boolean definesMethod(Signature signature, boolean recursive) {
        return definesMethod(signature.getMethodName(), recursive);
    }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {
        if (extendedClass.size() > 0 && extendedClass.get(0).getDeclaration().getName().equals(this.getName())) {
            Logger.error(this.getName() + " cannot extend self");
            return false;
        }

        for (InstanceType tp: this.implementedClasses) {
            if (! tp.resolve(scope))
                return false;
        }

        // Verify if every superclass abstract methods are implemented if the class is not abstract
        if ((this.extendedClass.size() > 0) && !this.isAbstract()) {
            InstanceType tp = this.extendedClass.get(0);
            // Check if the superclass is well a simple class
            if (tp.getDeclaration() instanceof InterfaceDeclaration) {
                Logger.error("The class " + this.getName() + " extends the interface "+ tp.getDeclaration().getName() + " which is not correct.");
                return false;
            }
            ClassDeclaration classDeclaration = (ClassDeclaration) tp.getDeclaration();
            boolean ret = checkForSuperClassMethods(classDeclaration);
            if (!ret) {
                return false;
            }
            if (classDeclaration.getExtendedClass().size() > 0) {
                Logger.error("" + classDeclaration.getExtendedClass());
                InstanceType superClass = classDeclaration.getExtendedClass().get(0);
                while (superClass != null) {
                    ClassDeclaration cd = (ClassDeclaration) superClass.getDeclaration();
                    boolean ret2 = checkForSuperClassMethods(cd);
                    if (!ret2) {
                        return false;
                    }
                    superClass = ((cd.getExtendedClass().size() > 0) ? cd.getExtendedClass().get(0) : null);
                }
            }

        }

        HierarchicalScope<Declaration> subScope = new SymbolTable(scope);

        // Resolve for each definition in the new scope
        for (Definition definition: definitions) {
            definition.setParent(this);
            if (! definition.resolve(subScope)) {
                Logger.error("Could not resolve class " + getName() + " because of an unresolvable definition: " + definition.getClass().getCanonicalName());
                return false;
            }
        }

        return true;
    }

    private boolean checkForSuperClassMethods(ClassDeclaration cd) {
        for (Definition d : cd.getDefinitions()) {
            if (d instanceof MethodDefinition) {
                MethodDefinition md = (MethodDefinition) d;
                // For each abstract method of the superclass, we check if it is implemented
                if (md.isAbstract() && ! definesMethod(md.getSignature().getMethodName(), false)) {
                    Logger.error("The class " + this.getName() + ", by extending " + cd.getName() + ", needs to implement the method " + md.getName() + ".");
                    return false;
                }
            }
        }
        return true;
    }
    /*
	private List<Signature> getClassSignatures() {
		List<Signature> classSignatures = new LinkedList<>();
		for (Definition d : this.definitions) {
			if (d instanceof MethodDefinition) {
				classSignatures.add(((MethodDefinition) d).getSignature());
			}
		}
		return classSignatures;
	}*/

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
                if (! definesMethod(signature, false)) {
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
        for (Definition definition: definitions)
            offset = definition.allocateMemory(register, offset);
        return offset;
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
	    for (Definition definition: definitions)
	        fragment.append(definition.getCode(factory));
	    return fragment;
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