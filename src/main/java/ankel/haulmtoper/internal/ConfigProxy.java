package ankel.haulmtoper.internal;

import ankel.haulmtoper.api.InvalidConfigurationMethod;
import ankel.haulmtoper.api.MissingPropertyKey;
import ankel.haulmtoper.api.PropertyKey;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Binh Tran
 */
@Slf4j
public class ConfigProxy implements InvocationHandler
{
  private final AtomicReference<ImmutableMap<String, String>> properties;
  private final List<Class<?>> valueConverters;

  public ConfigProxy(
      final Map<String, String> properties,
      final Class<?>... valueConverter)
  {
    this.properties = new AtomicReference<>(ImmutableMap.copyOf(properties));
    this.valueConverters = Lists.newArrayList(valueConverter);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
  {
    if (method.getParameterCount() != 0)
    {
      throw new InvalidConfigurationMethod(String.format("Method [%s] has non-zero parameter count", method.getName()));
    }

    final PropertyKey propertyKey = method.getDeclaredAnnotation(PropertyKey.class);

    if (propertyKey == null || Strings.isNullOrEmpty(propertyKey.value()))
    {
      throw new InvalidConfigurationMethod(
          "Method [%s] must contains a non-null, non-empty ProperyKey annotation", method.getName());
    }

    final String key = propertyKey.value();
    final String value;
    ImmutableMap<String, String> props = properties.get();

    final boolean optional;
    final Class<?> returnClass;

    if (method.getReturnType().isAssignableFrom(Optional.class))
    {
      optional = true;
      try
      {
        returnClass = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
      }
      catch (Exception e)
      {
        log.debug("Exception from readding inner type", e);
        throw new IllegalArgumentException("Cannot return raw java.util.Optional type");
      }
      log.debug("Optional property for [{}] inner type [{}]", method.getName(), returnClass.getTypeName());
    }
    else
    {
      optional = false;
      returnClass = method.getReturnType();
      log.debug("Required property for [{}] inner type [{}]", method.getName(), returnClass.getTypeName());
    }

    if (!props.containsKey(key))
    {
      if (optional)
      {
        return Optional.empty();
      }
      else
      {
        throw new MissingPropertyKey("Cannot find key [%s] in the provided mapping", key);
      }
    }
    else
    {
      value = props.get(key);
    }

    if (returnClass.getClass().isAssignableFrom(String.class))
    {
      return wrapIfOptional(optional, value);
    }
    else
    {
      return wrapIfOptional(optional, findMethodAndInvoke(returnClass, value));
    }
  }

  private Object wrapIfOptional(final boolean optional, final Object obj)
  {
    if (optional)
    {
      return Optional.ofNullable(obj);
    }
    else
    {
      return obj;
    }
  }

  private Object findMethodAndInvoke(final Class<?> returnClass, final String arg) throws InvocationTargetException, IllegalAccessException
  {
    Method convertMethod = null;

    classLoop: for (final Class<?> valueConverter : valueConverters)
    {
      for (final Method m : valueConverter.getDeclaredMethods())
      {
        log.debug("Evaluating method [{}] from [{}]", m.getName(), m.getDeclaringClass().getCanonicalName());
        if (
            ClassUtils.isAssignable(
                new Class[]{returnClass},
                new Class[]{m.getReturnType()},
                true)
            && m.getParameterCount() == 1
            && m.getParameterTypes()[0].isAssignableFrom(String.class)
            && Modifier.isStatic(m.getModifiers()))
        {
          convertMethod = m;
          log.debug("Found method [{}] from [{}]", m.getName(), m.getDeclaringClass().getCanonicalName());
          break classLoop;
        }
      }
    }

    if (convertMethod == null)
    {
      final String converterNames = valueConverters.stream()
          .map(Class::getCanonicalName)
          .collect(Collectors.joining(", "));

      throw new InvalidConfigurationMethod(
          "Cannot find any suitable method to convert String to [%s] from [%s]",
          returnClass.getCanonicalName(),
          converterNames);
    }

    return convertMethod.invoke(null, arg);
  }
}
