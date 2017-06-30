package ankel.haulmtoper;

import ankel.haulmtoper.api.PropertyKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Binh Tran
 */
interface PropertyInterface
{
  @PropertyKey("AnInt")
  int getInt();

  @PropertyKey("AnLong")
  long getInvalidMethod(Object foo);

  @PropertyKey("ALong")
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

  @RequiredArgsConstructor
  class Bar
  {
    private final String s;
  }
}
