package ankel.haulmtoper;

import ankel.haulmtoper.PropertyInterface.Bar;
import ankel.haulmtoper.PropertyInterface.Foo;
import ankel.haulmtoper.api.InvalidConfigurationMethod;
import ankel.haulmtoper.api.MissingPropertyKey;
import ankel.haulmtoper.api.PropertyKey;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Binh Tran
 */
@Slf4j
public class ConfigurationGeneratorTest
{
  private final ImmutableMap<String, String> props = ImmutableMap.<String, String> builder()
      .put("AnInt", "42")
      .put("foo", "srsly foo")
      .put("bar", "not too bar")
      .build();

  private PropertyInterface pi = ConfigurationGenerator.createFor(PropertyInterface.class, props, Foo.class);

  @Test
  public void testGetInt()
  {
    assertEquals(42, pi.getInt());
  }

  @Test
  public void testInvalidMethod()
  {
    try
    {
      pi.getInvalidMethod(new Object());
      fail();
    }
    catch (final InvalidConfigurationMethod e)
    {
      log.error("Expected exception",e);
    }
    catch (final Exception e)
    {
      log.error("Unexpected exception", e);
      fail();
    }
  }

  @Test
  public void testMissingPropertyThrowsException()
  {
    try
    {
      pi.getAnLong();
      fail();
    }
    catch (final MissingPropertyKey e)
    {
      log.error("Expected exception",e);
    }
    catch (final Exception e)
    {
      log.error("Unexpected exception", e);
      fail();
    }
  }

  @Test
  public void testMissingPropertyReturnOptionalEmpty()
  {
    assertFalse(pi.getString().isPresent());
  }

  @Test
  public void testProvidedConversionMethodIsUSed()
  {
    Optional<Foo> foo = pi.getFoo();

    assertEquals("srsly foo", foo.get().getS());
  }

  @Test
  public void testNoSuitableMethodFound()
  {
    try
    {
      pi.getBar();
      fail();
    } catch (final InvalidConfigurationMethod e)
    {
      log.error("Expected exception", e);
    } catch (final Exception e)
    {
      log.error("Unexpected exception", e);
      fail();
    }
  }

  @Test
  public void testIgnoreNonStaticMethods()
  {
    pi = ConfigurationGenerator.createFor(PropertyInterface.class, props, Bar.class);
    try
    {
      pi.getBar();
      fail();
    }
    catch (final InvalidConfigurationMethod e)
    {
      log.error("Expected exception",e);
    }
    catch (final Exception e)
    {
      log.error("Unexpected exception", e);
      fail();
    }
  }
}