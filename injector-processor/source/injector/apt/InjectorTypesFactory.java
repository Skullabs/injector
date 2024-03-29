package injector.apt;

import generator.apt.SimplifiedAST;
import injector.apt.utils.DuplicatedConstructorRemover;
import injector.apt.utils.DuplicatedMethodsRemover;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.HashSet;

@UtilityClass
class InjectorTypesFactory {

    InjectorTypes from(SimplifiedAST.Type type) {
        val regular = createNewType(type);
        val listOfProducers = new HashSet<InjectorType>();

        DuplicatedConstructorRemover.removeFrom(type);
        DuplicatedMethodsRemover.removeFrom(type);

        for (val method : type.getMethods()) {
            val injectorMethod = InjectorMethod.from( method );
            if ( injectorMethod.isProducer() )
                listOfProducers.add( createTypeWithSingleMethod( type, injectorMethod ) );
            else
                regular.getMethods().add( injectorMethod );
        }

        regular.computeUniqueIdentifier();
        return new InjectorTypes( regular, listOfProducers );
    }

    private InjectorType createTypeWithSingleMethod( SimplifiedAST.Type type, SimplifiedAST.Method method ) {
        val iType = (InjectorType)new InjectorType()
                .setCanonicalName( method.getType() )
                .setType( type.getCanonicalName() )
                .setAnnotations( type.getAnnotations() )
                .setName( type.getName() );

        iType.getMethods().add( method );
        iType.computeUniqueIdentifier();

        return iType;
    }

    private InjectorType createNewType( SimplifiedAST.Type type ) {
        return (InjectorType)new InjectorType()
                .setCanonicalName( type.getCanonicalName() )
                .setFields( type.getFields() )
                .setInterfaces( type.getInterfaces() )
                .setInterface( type.isInterface() )
                .setType( type.getType() )
                .setAnnotations( type.getAnnotations() )
                .setName( type.getName() );
    }
}
