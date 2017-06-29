package ankel.haulmtoper;

import ankel.haulmtoper.api.ConfigUtils;
import ankel.haulmtoper.internal.ConfigProxy;
import com.google.common.collect.Lists;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Binh Tran
 */
public class ConfigurationGenerator
{
  @SuppressWarnings("unchecked")
  public static <T> T createFor(
      final Class<T> configInterface,
      final Map<String, String> properties,
      final Class<?>... convertClasses)
  {
    List<Class<?>> convertClassList = Lists.newArrayList(convertClasses);

    convertClassList.add(ConfigUtils.class);

    return (T) Proxy.newProxyInstance(
        configInterface.getClassLoader(),
        new Class[] {configInterface},
        new ConfigProxy(properties, convertClassList));
  }
}
