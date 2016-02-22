
/**
 * Class to print debugging information to stdout
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class Debug
{
  private static boolean debug = true;

  /**
   * Turns printing of debugging information on/ off.
   * @param state Print debugging information set to true/false
   * @return The old state
   */
  public static synchronized boolean set( boolean state )
  {
    boolean oldState = debug;
    debug = state;
    return oldState;
  }

  /**
   * Display text for debugging purposes
   * @param fmt  The same as printf etc
   * @param params The parameters to fmt
   */
  public static void trace(String fmt, Object... params )
  {
    if ( debug )
    {
      synchronized( Debug.class )
      {
        System.out.printf( fmt, params );
        System.out.println();
      }
    }
  }

  /**
   * Display a fatal message if the assertion fails
   * @param ok true if all is ok
   * @param fmt The same as printf etc
   * @param params The parameters to fmt
   */
  public static void assertTrue( boolean ok, String fmt, 
                                 Object... params )
  {
    if ( ! ok )
    {
      error( "Assert - " + fmt, params );
    }
  }

  /**
   * Display a fatal message
   * @param fmt The same as printf etc
   * @param params The parameters to fmt
   */
  public static synchronized void error(String fmt, Object... params )
  {
    System.out.printf( "ERROR: " + fmt, params );
    System.out.println();
    System.out.flush();
  }

}
