package fr.vidal.oss.jax_rs_linker.writer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.squareup.javapoet.*;
import com.squareup.javawriter.StringLiteral;
import fr.vidal.oss.jax_rs_linker.LinkerAnnotationProcessor;
import fr.vidal.oss.jax_rs_linker.api.NoPathParameters;
import fr.vidal.oss.jax_rs_linker.model.*;
import fr.vidal.oss.jax_rs_linker.model.ClassName;
import fr.vidal.oss.jax_rs_linker.predicates.MappingByApiLinkTargetPredicate;

import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.google.common.base.Throwables.propagate;
import static com.squareup.javapoet.ClassName.bestGuess;
import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static java.lang.String.format;
import static javax.lang.model.element.Modifier.*;

public class LinkerWriter {

    private final Filer filer;

    public LinkerWriter(Filer filer) {
        this.filer = filer;
    }

    public void write(ClassName generatedClass, Collection<Mapping> mappings) throws IOException {
        Optional<Mapping> selfMapping = FluentIterable.from(mappings)
            .firstMatch(new Predicate<Mapping>() {
                @Override
                public boolean apply(@Nullable Mapping input) {
                    return input.getApi().getApiLink().getApiLinkType() == ApiLinkType.SELF;
                }
            });

        ApiPath selfApiPath = selfMapping.get().getApi().getApiPath();

        com.squareup.javapoet.ClassName templatedPathClass = bestGuess(
            parameterizedTemplatedPath(
                selfApiPath,
                generatedClass.className())
        );

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(generatedClass.className())
            .addAnnotation(
                AnnotationSpec.builder(Generated.class)
                    .addMember("value", LinkerAnnotationProcessor.class.getName())
                    .build()
            )
            .addField(FieldSpec.builder(String.class, "contextPath", PRIVATE, FINAL).build())
            .addMethod(
                constructorBuilder()
                    .addModifiers(PUBLIC)
                    .addCode("this(\"\")")
                    .build()
            )
            .addMethod(
                constructorBuilder()
                    .addModifiers(PUBLIC)
                    .addParameter(
                        ParameterSpec.builder(String.class, "contextPath", FINAL).build())
                    .addCode("this.contextPath = contextPath")
                    .build()
            )
            .addMethod(
                MethodSpec.methodBuilder("self")
                    .addModifiers(PUBLIC)
                    .returns(templatedPathClass)
                    .addCode(
                        "return new $T(contextPath + $S, $T.<$T>asList($S))",
                        templatedPathClass,
                        selfApiPath.getPath(),
                        Arrays.class,
                        PathParameter.class,
                        parameters(selfApiPath.getPathParameters())
                    )
                    .build()
            );

        for (Mapping mapping : linked(mappings)) {
            Api api = mapping.getApi();
            ClassName target = api.getApiLink().getTarget().get();
            ApiPath apiPath = api.getApiPath();
            typeBuilder.addMethod(
                MethodSpec.methodBuilder(format("related%s", target.className()))
                    .returns(templatedPathClass)
                    .addModifiers(PUBLIC, FINAL)
                    .addCode(
                        "$T path = new $T($S,$T.<$T>asList($S)",
                        ApiPath.class,
                        ApiPath.class,
                        apiPath.getPath(),
                        Arrays.class,
                        PathParameter.class,
                        parameters(apiPath.getPathParameters())
                    )
                    .addCode(
                        "return new %T(contextPath + path.getPath(), path.getPathParameters())",
                        templatedPathClass
                    )
                    .build()
            );
        }

        JavaFile.builder(generatedClass.packageName(), typeBuilder.build())
            .indent("\t")
            .build()
            .writeTo(filer);

    }

    private String parameterizedTemplatedPath(ApiPath apiPath, String generatedClass) {
        if (apiPath.getPathParameters().isEmpty()) {
            return format("TemplatedPath<%s>", NoPathParameters.class.getSimpleName());
        }
        return format("TemplatedPath<%s>", generatedClass.replace("Linker", "PathParameters"));
    }

    private Iterable<Mapping> linked(Collection<Mapping> mappings) {
        return FluentIterable.from(mappings)
            .filter(MappingByApiLinkTargetPredicate.BY_API_LINK_TARGET_PRESENCE)
            .toList();
    }


    private String parameters(Collection<PathParameter> pathParameters) {
        return FluentIterable.from(pathParameters)
            .transform(new Function<PathParameter, String>() {
                @Nullable
                @Override
                public String apply(PathParameter input) {
                    return format(
                            "new PathParameter(ClassName.valueOf(%s), %s)",
                            StringLiteral.forValue(input.getType().fullyQualifiedName()).literal(),
                            StringLiteral.forValue(input.getName()).literal()
                    );
                }
            })
            .join(Joiner.on(", "));
    }

}
