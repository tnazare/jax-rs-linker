package com.vidal.oss.jax_rs_linker.parser;

import com.google.common.base.Optional;
import com.vidal.oss.jax_rs_linker.functions.ClassToName;
import com.vidal.oss.jax_rs_linker.model.ApiPath;
import com.vidal.oss.jax_rs_linker.model.ClassName;
import com.vidal.oss.jax_rs_linker.model.PathParameter;
import com.vidal.oss.jax_rs_linker.model.TemplatedPath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;


@Generated("com.vidal.oss.jax_rs_linker.LinkerAnnotationProcessor")
public class BrandResourceLinker {

    private final Map<ClassName, ApiPath> relatedMappings = new HashMap<>();
    private final String contextPath;

    public BrandResourceLinker() {
        this("");
    }

    public BrandResourceLinker(String contextPath) {
        this.contextPath = contextPath;
    }

    public TemplatedPath self() {
        return new TemplatedPath(contextPath + "/brand/{id}", Arrays.asList(new PathParameter(ClassName.valueOf("int"), "id")));
    }

    public Optional<TemplatedPath> related(Class<?> resourceClass) {
        ApiPath path = relatedMappings.get(ClassName.valueOf(ClassToName.INSTANCE.apply(resourceClass)));
        if (path == null) {
            return Optional.<TemplatedPath>absent();
        }
        return Optional.of(new TemplatedPath(contextPath + path.getPath(), path.getPathParameters()));
    }
}