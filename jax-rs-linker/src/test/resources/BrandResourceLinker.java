package fr.vidal.oss.jax_rs_linker.parser;

import fr.vidal.oss.jax_rs_linker.model.ClassName;
import fr.vidal.oss.jax_rs_linker.model.PathParameter;
import fr.vidal.oss.jax_rs_linker.model.TemplatedPath;

import javax.annotation.Generated;
import java.util.Arrays;

@Generated("fr.vidal.oss.jax_rs_linker.LinkerAnnotationProcessor")
public class BrandResourceLinker {

    private final String contextPath;

    public BrandResourceLinker() {
        this("");
    }

    public BrandResourceLinker(String contextPath) {
        this.contextPath = contextPath;
    }

    public TemplatedPath<BrandResourcePathParameters> self() {
        return new TemplatedPath<BrandResourcePathParameters>(contextPath + "/brand/{id}", Arrays.<PathParameter>asList(new PathParameter(ClassName.valueOf("int"), "id")));
    }

}
