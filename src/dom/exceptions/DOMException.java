package dom.exceptions;

public class DOMException extends Throwable {
  protected DOMException(String message)  {
    super(message);
  }

  public static boolean checkInstance(Throwable throwable) {
    return throwable instanceof DOMException;
  }
}
