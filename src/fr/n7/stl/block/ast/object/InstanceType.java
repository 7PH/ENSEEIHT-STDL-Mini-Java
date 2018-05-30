package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class InstanceType implements Type {

    public static final int OBJECT_ADDR_LENGTH = 1;

    private String name;

	/* Types génériques : <String> etc */
	private List<InstanceType> typeInstantiations;

	private ProgramDeclaration declaration;
	
	private GenericType genericDeclaration;

    public InstanceType(String name, List<InstanceType> instantiations) {
        this.name = name;
        this.typeInstantiations = instantiations;
    }

	public InstanceType(String name) {
		this(name, new LinkedList<>());
	}

	@Override
	public boolean equalsTo(Type other) {
        if (! (other instanceof InstanceType))
            return false;

        InstanceType instanceType = (InstanceType) other;
        if (declaration != null) {
            // this one is a class/interface type, like 'Box'. Other has to be too
            if (declaration != instanceType.getDeclaration())
                return false;
        } else {
            if (genericDeclaration != instanceType.getGenericDeclaration())
                return false;
        }

        Logger.warning("@TODO InstanceTYpe: double check");
        return name.equals(instanceType.name);
	}
	
	@Override
	public boolean compatibleWith(Type other) {
        if (! (other instanceof InstanceType))
            return false;


        InstanceType instanceType = (InstanceType) other;
        if (declaration != null) {
            // this one is a class/interface type, like 'Box'. Other has to be too
            Declaration declaration2 = instanceType.getDeclaration();

            // could be improved
            if (declaration2 != declaration) {
                // if a superclass is compatible with the other, it's OK
                for (InstanceType superClassT: declaration.getExtendsList())
                    if (superClassT.compatibleWith(other))
                        return true;
                // idem for an interface
                for (InstanceType implemented: declaration.getImplementsList())
                    if (implemented.compatibleWith(other))
                        return true;
                // nope
                return false;
            }
        } else {
            if (instanceType.getGenericDeclaration() != genericDeclaration) {
                // does 'this' extends in some manner the other type?
                // for instance:
                //   class Box<T extends A> { }
                //   -> T is compatible with A
                boolean ok = false;
                for (InstanceType type: getGenericDeclaration().getExtendedTypes()) {
                    // @TODO: how it should be done:
                    //if (instanceType.compatibleWith(type))
                    if (instanceType.name.equals(type.name))
                        ok = true;
                }
                return ok;
            } else {
                return true;
            }
        }

        return true;
	}

	@Override
	public Type merge(Type other) {
		throw new SemanticsUndefinedException("merge method is undefined for InstanceType.");
	}

	@Override
	public int length() {
	    return 1; // @TODO
    }

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(name)) {
			Logger.error("InstanceType: Unknown reference to '" + name + "'");
			return false;
		}

		Declaration scopeDeclaration = scope.get(name);
        if (scopeDeclaration instanceof ProgramDeclaration) {
            this.declaration = (ProgramDeclaration) scopeDeclaration;

            // check generic types instantiated with class one
            /* @TODO we can't do here because of the constructor. "class Box<T> { public Box() { } }"
            if (! checkClassGenericsMatch())
                return false;
            */

        } else if (scope.get(this.name) instanceof GenericType) {
            // This is an instance of the generic type.
            this.genericDeclaration = (GenericType) scopeDeclaration;

            /* @TODO make this or not ??
            for (Type type: genericDeclaration.getExtendedTypes()) {
                if (! compatibleWith(type)) {
                    Logger.error("Could not instantiate generic type '" + this + "' because it does not extends '" + type + "'");
                    return false;
                }
            }
            */
        } else {
            Logger.error(this.name + " is not a class nor an interface and cannot be instantiated. " + scope.get(this.name).getClass().getName() );
            return false;
        }

        return true;
	}

	public boolean checkClassGenericsMatch() {
        if (declaration == null) return true;
        List<GenericType> genericTypes = declaration.getClassName().getGenerics();
        if (genericTypes.size() != typeInstantiations.size()) {
            Logger.error("Wrong number of generic type instantiated for class '" + declaration.getName() + "' " + genericTypes.size() + " - " + typeInstantiations.size());
            return false;
        }
        for (int i = 0; i < typeInstantiations.size(); ++ i) {

            // @TODO: Remove the following comment

            // TODO : Verifier que les types generiques sont compatibles.
            // ex : si déclaré MyClass<T extends X>
            // declaration[i].compatibleWith(typeInstantiations[i])

            Type type = typeInstantiations.get(i);
            GenericType genericType = genericTypes.get(i);
            // 'type' doit "matcher" 'genericType'
            for (InstanceType constraint: genericType.getExtendedTypes()) {
                // 'type' doit être enfant de 'constraint'
                if (! type.compatibleWith(constraint)) {
                    // nop
                    Logger.error("Unable to set '" + type + "' as generic type for '" + declaration.getName() + "'");
                    return false;
                }
            }
        }

        return true;
    }

	@Override
	public String toString() {
		return this.name + (this.typeInstantiations.size() > 0 ? ("<" + this.typeInstantiations + ">") : "");
	}


	public ProgramDeclaration getDeclaration() {
		return declaration;
	}

	public GenericType getGenericDeclaration() {
		return genericDeclaration;
	}

	public List<InstanceType> getTypeInstantiations() {
		return typeInstantiations;
	}

	/** Say if the object contains this attribute.
	 * @param name the searched attribute
	 * @return true if contains, false if not
	 */
	public boolean contains(String name) {
		if (this.getDeclaration() instanceof InterfaceDeclaration) {
			Logger.error("Interface " + this.name + " does not contain attribute " + name + " because " + this.name + " is an interface.");
			return false;
		} else if (this.getDeclaration() instanceof ClassDeclaration) {
			ClassDeclaration _classe = (ClassDeclaration) this.getDeclaration();
			// TODO : check if attribute is present
		} else {
			Logger.error("This message should not appear. Contains method was called on a InstanceType that was initiated with a declaration that is not a ProgramDeclaration");
			return false;
		}
		return false;
	}

	/** Get an attribute of the object.
	 * @param attributeIdentificateur the attribute we ask.
	 * @return the attribute
	 */
	public AttributeDefinition get(String attributeIdentificateur) {
		// TODO :
		// Get the objet : ClassDeclaration
		//	Get the attribute list of it
		//	  Get the correct attribute thanks to his name field
		return null;
	}

}
