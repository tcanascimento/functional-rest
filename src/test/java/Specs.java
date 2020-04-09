public class Specs {

    private String scene;
    private Integer returnCode;
    private String errorCode;
    private String message;


    public Specs(String scene, Integer returnCode, String errorCode, String message) {
        this.scene = scene;
        this.returnCode = returnCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Specs() {
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Specs{" +
                "scene='" + scene + '\'' +
                ", returnCode=" + returnCode +
                ", errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
