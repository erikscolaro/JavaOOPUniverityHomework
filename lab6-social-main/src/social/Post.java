package social;

public class Post {
    private String message;
    
    private Long time;

    static private Integer sharedSerial=0;
    private Integer serial;

    public String getMessage() {
        return message;
    }
   
    public Integer getSerial() {
        return serial;
    }

    public Post(String message, Long time) {
        this.message = message;
        this.time = time;
        this.serial=sharedSerial;
        sharedSerial++;
    }
    public Long getTime() {
        return time;
    }
}
