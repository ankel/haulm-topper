package ankel.haulmtoper.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this annotation to indicate that it is to read from a property mapping.
 *
 * @author Binh Tran
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyKey
{
  /**
   * The name of the key that contains this configuration value in the mapping
   */
  String value();
}
