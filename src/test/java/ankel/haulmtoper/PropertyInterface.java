package ankel.haulmtoper;

import ankel.haulmtoper.api.PropertyKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Binh Tran
 */
public interface PropertyInterface
{
  @PropertyKey("AnInt")
  int getInt();

  @PropertyKey("AnLong")
  long getInvalidMethod(Object foo);

  @PropertyKey("AnLong")
  Long getAnLong();

  @PropertyKey("AString")
  Optional<String> getString();

  @PropertyKey("foo")
  Optional<Foo> getFoo();

  @PropertyKey("bar")
  Bar getBar();

  @RequiredArgsConstructor
  @Getter
  class Foo
  {
    private final String s;
  }

  class TypeConverter
  {
    public static Foo someRandomName(final String s)
    {
      return new Foo(s);
    }

    public Bar invalidMethodBecauseItsNotStatic(final String s)
    {
      return null;
    }
  }

  @RequiredArgsConstructor
  class Bar
  {
    private final String s;
  }
}
