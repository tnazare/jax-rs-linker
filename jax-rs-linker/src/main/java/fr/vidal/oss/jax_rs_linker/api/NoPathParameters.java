package fr.vidal.oss.jax_rs_linker.api;

public enum NoPathParameters implements PathParameters {
    ;

    @Override
    public String placeholder() {
        throw new UnsupportedOperationException();
    }
}
