package cch.tweet4emergency.classification;

public interface ClassifiedData<T, R> {
    R result(T data);
}
