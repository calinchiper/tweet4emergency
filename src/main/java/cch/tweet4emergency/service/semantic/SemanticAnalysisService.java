package cch.tweet4emergency.service.semantic;


public interface SemanticAnalysisService<T, R> {
    R analyze(T content);
}
