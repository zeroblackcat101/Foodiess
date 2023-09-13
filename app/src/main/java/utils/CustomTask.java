package utils;

public interface CustomTask<T> {
    void complete(CustomTaskResult<T> customTaskResult);
}