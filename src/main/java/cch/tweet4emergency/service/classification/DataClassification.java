package cch.tweet4emergency.service.classification;

public interface DataClassification<R> {
    R classify(String data);
}
