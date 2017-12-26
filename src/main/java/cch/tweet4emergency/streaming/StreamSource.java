package cch.tweet4emergency.streaming;

public interface StreamSource<T> {

    T streamData();

    void startStreaming();

    void stopStreaming();

}
