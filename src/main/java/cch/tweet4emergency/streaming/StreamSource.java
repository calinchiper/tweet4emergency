package cch.tweet4emergency.streaming;

public interface StreamSource<T> {

    T stream() throws InterruptedException;

    void stopStreaming();
}
