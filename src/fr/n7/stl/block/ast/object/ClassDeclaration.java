package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
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

import java.util.*;

public class ClassDeclaration extends ProgramDeclaration {
	
	private ClassModifier modifier;

	private List<Definition> definitions;

	public final Map<AttributeDefinition, Integer> attrRelativeOffset = new HashMap<>();
	
	public ClassDeclaration(ClassModifier modifier, ClassName name, List<InstanceType> extension, List<InstanceType> implementation, List<Definition> definitions) {
		this.modifier = modifier;
		this.className = name;
		this.extendsList = extension;
		this.implementsList = implementation;
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
        if (extendsList.size() > 0) {
            return ((ClassDeclaration) extendsList.get(0).getDeclaration()).getAttributeDeclaration(name, false);
        }
        return null;
    }

    public AttributeDefinition getAttributeDeclaration(String name) {
        return getAttributeDeclaration(name, true);
    }

    public boolean hasSuper() { return extendsList.size() > 0; }

    public ClassDeclaration getSuper() { return ((ClassDeclaration) extendsList.get(0).getDeclaration()); }

    public int getAttributeAbsoluteOffset(AttributeDefinition attr) {
	    if (! attrRelativeOffset.containsKey(attr)) {
	        // attr is not here: maybe in super?
	        if (! hasSuper()) {
	            // error
	            return -1;
            } else {
                return getSuper().getAttributeAbsoluteOffset(attr);
            }
        } else {
            // attr is here
            int size = hasSuper() ? getSuper().getAllAttributesSizes() : 0;
            size += attrRelativeOffset.get(attr);
            return size;
        }
	}

	private void assignAttributesRelativeOffsets() {
	    int offset = 0;
	    for (Definition definition: definitions) {
	        if (! (definition instanceof AttributeDefinition)) continue;
            AttributeDefinition attr = (AttributeDefinition) definition;
            attrRelativeOffset.put(attr, offset);
            offset += attr.getType().length();
        }
    }

    public int getAllAttributesSizes() {
	    int size = 0;
	    if (extendsList.size() > 0)
            size += ((ClassDeclaration) extendsList.get(0).getDeclaration()).getAllAttributesSizes();
	    for (Definition definition: definitions) {
	        if (definition instanceof AttributeDefinition)
	            size += definition.getType().length();
        }
        return size;
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
        if (recursive && extendsList.size() > 0)
            matches.addAll(((ClassDeclaration) extendsList.get(0).getDeclaration()).getMethodDefinitionsByMethodName(name, true));
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

    public List<MethodDefinition> getConstructors() {
	    // @TODO get also the parent constructors??
        return getMethodDefinitionsByMethodName("", false);
    }

    public boolean definesMethod(String name, boolean recursive) {
        return getMethodDefinitionsByMethodName(name, recursive).size() > 0;
    }

    public boolean definesMethod(Signature signature, boolean recursive) {
        return definesMethod(signature.getMethodName(), recursive);
    }

    @Override
    public boolean subResolve(HierarchicalScope<Declaration> scope) {
        if (extendsList.size() > 0) {
            InstanceType superClass = extendsList.get(0);
            if (superClass.getDeclaration().getName().equals(this.getName())) {
                Logger.error(this.getName() + " cannot extend self");
                return false;
            }
        }

        for (InstanceType tp: this.implementsList) {
            if (! tp.resolve(scope)) {
            	Logger.error("Could not resolve the instance type " + tp.toString() + " in class " + this.className.getName());
                return false;
            }
        }

        // Verify if every superclass abstract methods are implemented if the class is not abstract
        if ((this.extendsList.size() > 0) && ! isAbstract()) {
            InstanceType tp = this.extendsList.get(0);
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
            if (classDeclaration.getExtendsList().size() > 0) {
                Logger.error("" + classDeclaration.getExtendsList());
                InstanceType superClass = classDeclaration.getExtendsList().get(0);
                while (superClass != null) {
                    ClassDeclaration cd = (ClassDeclaration) superClass.getDeclaration();
                    boolean ret2 = checkForSuperClassMethods(cd);
                    if (!ret2) {
                        return false;
                    }
                    superClass = ((cd.getExtendsList().size() > 0) ? cd.getExtendsList().get(0) : null);
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

            // assign parameters offset for getCode
            if (definition instanceof MethodDefinition)
                ((MethodDefinition)definition).getSignature().assignParametersRelativeOffset();
        }
        if (getMethodDefinitionsByMethodName("", false).size() == 0) {
            this.definitions.add(new Constructor(this.getName(), new LinkedList<>(), new Block(new LinkedList<>())));
        }

        assignAttributesRelativeOffsets();
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

        // Verify that extendsClass contains only class
        for (InstanceType tp: extendsList) {
            if (! (tp.getDeclaration() instanceof ClassDeclaration)) {
                Logger.error("The class " + this.getName() + " extends " + tp.getDeclaration().getName() + " which is not a class.");
                b = false;
            }

            ClassDeclaration superClass = (ClassDeclaration) tp.getDeclaration();
            if (superClass.isFinal()) {
                Logger.error("Cannot extend final class");
                b = false;
            }
        }

        // Verify that each interface method is implemented
        for (InstanceType tp: implementsList) {
            if (! (tp.getDeclaration() instanceof InterfaceDeclaration)) {
                Logger.error("The class " + getName() + " implements " + tp.getDeclaration().getName() + " which is not an interface.");
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
        if (this.extendsList.size() > 1 ) {
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
				Logger.error("ClassDeclaration: checkType error in " + d.getName());
				b = false;
			}
		}

		return b;
	}

	@Override
	public int allocateMemory(Register register, int offset) {
        int oldOffset = offset;
        for (Definition definition: definitions)
            offset += definition.allocateMemory(register, offset);
        return offset - oldOffset;
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

		if (extendsList.size() > 0)
		    result += " extends " + extendsList.get(0);

		if (implementsList.size() > 0) {
		    result += " implements ";
		    for (int i = 0; i < implementsList.size() - 1; ++ i)
                result += implementsList.get(i) + ", ";
		    result += implementsList.get(implementsList.size() - 1);
        }

        result += " {" + "\n";

        for (Definition d : this.definitions)
            result += d + "\n";

		result += "\n" + "}" + "\n";
		return result;
	}

}