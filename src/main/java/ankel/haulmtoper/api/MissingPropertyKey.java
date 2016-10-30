package ankel.haulmtoper.api;

/**
 * @author Binh Tran
 */
public class MissingPropertyKey extends RuntimeException
{
  public MissingPropertyKey(final String message, final Object... args)
  {
    super(String.format(message, args));
  }
}
