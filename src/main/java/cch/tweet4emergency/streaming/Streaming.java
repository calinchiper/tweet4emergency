package cch.tweet4emergency.streaming;

import java.util.function.Consumer;

public interface Streaming<T, F> {

    void start();

    void stop();

    void onArrival(Consumer<T> consumerFn);

    F onFiltering();

}
