package fr.vidal.oss.jax_rs_linker.parser;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import fr.vidal.oss.jax_rs_linker.model.HttpVerb;
import fr.vidal.oss.jax_rs_linker.predicates.AnnotationMatchesElement;

import javax.lang.model.element.ExecutableElement;
import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.util.Map;

class HttpVerbVisitor {

    private static final Map<Class<? extends Annotation>, HttpVerb> JAX_RS_HTTP_VERBS =
        ImmutableMap.<Class<? extends Annotation>, HttpVerb>builder()
            .put(OPTIONS.class, HttpVerb.OPTIONS)
            .put(HEAD.class, HttpVerb.HEAD)
            .put(GET.class, HttpVerb.GET)
            .put(POST.class, HttpVerb.POST)
            .put(PUT.class, HttpVerb.PUT)
            .put(DELETE.class, HttpVerb.DELETE)
            .build();

    public Optional<HttpVerb> visit(final ExecutableElement methodElement) {
        Optional<Class<? extends Annotation>> annotation = matchingAnnotation(methodElement);
        if (!annotation.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(JAX_RS_HTTP_VERBS.get(annotation.get()));
    }

    private static Optional<Class<? extends Annotation>> matchingAnnotation(ExecutableElement methodElement) {
        return FluentIterable.from(JAX_RS_HTTP_VERBS.keySet())
            .firstMatch(AnnotationMatchesElement.BY_ELEMENT(methodElement));
    }
}
