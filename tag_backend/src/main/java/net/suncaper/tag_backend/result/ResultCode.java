package net.suncaper.tag_backend.result;

public enum ResultCode {
    SUCCESS(200),
    FAIL(400);
    private int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() { return code; }
}
