package ankel.haulmtoper;

import ankel.haulmtoper.internal.ConfigProxy;
import ankel.haulmtoper.api.ConfigUtils;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Binh Tran
 */
public class ConfigurationGenerator
{
  @SuppressWarnings("unchecked")
  public static <T> T createFor(final Class<T> configInterface, final Map<String, String> properties)
  {
    return (T) Proxy.newProxyInstance(
        configInterface.getClassLoader(),
        new Class[] {configInterface},
        new ConfigProxy(properties, ConfigUtils.class));
  }

  @SuppressWarnings("unchecked")
  public static <T> T createFor(final Class<T> configInterface, final Map<String, String> properties, final Class<?> convertClass)
  {
    return (T) Proxy.newProxyInstance(
        configInterface.getClassLoader(),
        new Class[] {configInterface},
        new ConfigProxy(properties, convertClass, ConfigUtils.class));
  }
}
