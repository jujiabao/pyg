package com.pyg.utils;

import java.io.Serializable;

/**
 * @Title PygResult
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-07-17 22:32
 */
public class PygResult implements Serializable {
    private boolean success;
    private String message;

    public PygResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PygResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
