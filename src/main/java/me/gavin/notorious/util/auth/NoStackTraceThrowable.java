package me.gavin.notorious.util.auth;

public class NoStackTraceThrowable extends RuntimeException {

    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "Ratted by Notorious!";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
