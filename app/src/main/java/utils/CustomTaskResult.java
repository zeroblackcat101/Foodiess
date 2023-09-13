package utils;

public class CustomTaskResult<T> {
    private boolean isSuccessFull;
    private String errorMessage;

    private T data;


    public CustomTaskResult(boolean isSuccessFull, String errorMessage, T data) {
        this.isSuccessFull = isSuccessFull;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public boolean isSuccessFull() {
        return isSuccessFull;
    }

    public String errorMessage() {
        return errorMessage;
    }



    public T getData() {
        return data;
    }




}
