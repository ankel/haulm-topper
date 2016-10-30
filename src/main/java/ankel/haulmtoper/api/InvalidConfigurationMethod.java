package ankel.haulmtoper.api;

/**
 * @author Binh Tran
 */
public class InvalidConfigurationMethod extends RuntimeException
{
  public InvalidConfigurationMethod(final String message, final Object... args)
  {
    super(String.format(message, args));
  }
}
