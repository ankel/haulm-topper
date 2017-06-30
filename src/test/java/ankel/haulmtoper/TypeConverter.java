package ankel.haulmtoper;

/**
 * @author Binh Tran
 */
public class TypeConverter
{
  public static PropertyInterface.Foo someRandomName(final String s)
  {
    return new PropertyInterface.Foo(s);
  }

  public PropertyInterface.Bar invalidMethodBecauseItsNotStatic(final String s)
  {
    throw new RuntimeException("baaad");
  }
}
