
package fr.vidal.oss.jax_rs_linker;

import fr.vidal.oss.jax_rs_linker.servlet.ContextPaths;
import javax.annotation.Generated;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import fr.vidal.oss.jax_rs_linker.parser.BrandResourceLinker;
import fr.vidal.oss.jax_rs_linker.parser.PersonResourceLinker;
import fr.vidal.oss.jax_rs_linker.parser.ProductResourceLinker;

@WebListener
@Generated("fr.vidal.oss.jax_rs_linker.LinkerAnnotationProcessor")
public class Linkers
        implements ServletContextListener {

    private static String contextPath = "";

    private static String applicationName = "fr.vidal.oss.jax_rs_linker.it.Configuration";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        contextPath = ContextPaths.contextPath(sce.getServletContext(), applicationName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    public static ProductResourceLinker productResourceLinker() {
        return new ProductResourceLinker(contextPath);
    }

    public static BrandResourceLinker brandResourceLinker() {
        return new BrandResourceLinker(contextPath);
    }

    public static PersonResourceLinker personResourceLinker() {
        return new PersonResourceLinker(contextPath);
    }
}
