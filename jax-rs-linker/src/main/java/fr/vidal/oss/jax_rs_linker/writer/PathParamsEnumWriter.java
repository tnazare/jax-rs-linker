package fr.vidal.oss.jax_rs_linker.writer;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.squareup.javawriter.JavaWriter;
import fr.vidal.oss.jax_rs_linker.LinkerAnnotationProcessor;
import fr.vidal.oss.jax_rs_linker.api.PathParameters;
import fr.vidal.oss.jax_rs_linker.model.ClassName;
import fr.vidal.oss.jax_rs_linker.model.Mapping;
import fr.vidal.oss.jax_rs_linker.functions.MappingToPathParameters;
import fr.vidal.oss.jax_rs_linker.functions.PathParameterToString;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Sets.immutableEnumSet;
import static javax.lang.model.element.Modifier.*;

public class PathParamsEnumWriter implements AutoCloseable {

    private final JavaWriter javaWriter;

    public PathParamsEnumWriter(JavaWriter javaWriter) {
        this.javaWriter = javaWriter;
    }

    public void write(ClassName generatedClass, Collection<Mapping> mappings) throws IOException {
        javaWriter.setIndent("\t");
        JavaWriter writer = javaWriter
            .emitPackage(generatedClass.packageName())
            .emitImports(Generated.class, PathParameters.class)
            .emitEmptyLine()
            .emitAnnotation(Generated.class, LinkerAnnotationProcessor.processorQualifiedName())
            .beginType(generatedClass.fullyQualifiedName(), "enum", EnumSet.of(PUBLIC), null, PathParameters.class.getSimpleName());

        writeEnumeration(mappings, writer);

        writer.emitEmptyLine()
            .emitField("String", "placeholder", immutableEnumSet(PRIVATE, FINAL))
            .emitEmptyLine()
            .beginConstructor(Collections.<Modifier>emptySet(), "String", "placeholder")
                .emitStatement("this.placeholder = placeholder")
            .endConstructor()
            .emitEmptyLine()
            .emitAnnotation(Override.class)
            .beginMethod("String", "placeholder", immutableEnumSet(PUBLIC))
            .emitStatement("return this.placeholder")
            .endMethod()
            .endType();

    }

    private void writeEnumeration(Collection<Mapping> mappings, JavaWriter writer) throws IOException {
        Collection<String> apiPathsEnums = getApiPathsEnums(mappings);
        Iterator<String> it = apiPathsEnums.iterator();
        while(it.hasNext()) {
            writer.emitEnumValue(it.next(), !it.hasNext());
        }
    }

    private Collection<String> getApiPathsEnums(Collection<Mapping> mappings) {
        return FluentIterable.from(mappings)
                .transformAndConcat(MappingToPathParameters.TO_PATH_PARAMETERS)
                .transform(PathParameterToString.TO_STRING)
                .toSortedSet(Ordering.natural());
    }

    @Override
    public void close() {
        try {
            javaWriter.close();
        } catch (IOException e) {
            throw propagate(e);
        }
    }

}
