package at.binter.gcd.util;

import static at.binter.gcd.GCDApplication.app;

public record ValidationError(boolean skippable, String message, Object... params) {
    public String getMessage() {
        if (params() != null && params().length > 0) {
            return app.getString(message(), params());
        }
        return app.getString(message());
    }
}
