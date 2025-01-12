package owl.home.dnd.util.exception;


import java.util.function.Supplier;


public class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static <T> T wrapAndGetResultOrNull(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            return null;
        }
    }
}