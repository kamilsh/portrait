package net.suncaper.tag_backend.result;

public class Result {
    private int code;
    private String message;
    private Object result;

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.result = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
