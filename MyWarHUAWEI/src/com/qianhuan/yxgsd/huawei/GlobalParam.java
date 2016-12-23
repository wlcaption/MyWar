package com.qianhuan.yxgsd.huawei;

public class GlobalParam
{
    /**
     * APP ID
     */
    public static final String APP_ID = "10468700";
    
    /**
     * private key for buoy, the CP need to save the key value on the server for security
     */
    public static String BUO_SECRET = "";
    
    /**
     * CPID
     */
    public static final String CP_ID = "890086000001006890";
    
    /**
     * 鏀粯ID
     */
	 /**
     * Pay ID
     */
    public static final String PAY_ID = "890086000001006890";
    
    /**
     * private key for pay, the CP need to save the key value on the server for security
     */
    public static String PAY_RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCtTz9MQIBdXc2Ge3bicEZUYio2+TAwKT4VzSbsgxvmHCTYx/KQyRmsBtyQTxhZyExqg3248tVPB6o+oCh5cvqiZK+0/vHbbeeVR2zLt1a1mPrmw2mixvDJICGojCA0zkM0YY46PMuwvjNu1cuyl7hiht3lGSMfA2qzvJkzIIX6Z3Nlc0vXQVgUA9kow4oRFghnP9TnM6RsmSp4rJdnDFFxblBO+iibFLvHrbT6aThlsgqdFkn0z+DIB2vr5edGYgrBq1lQVfvzVQVf/0EayELsBWFlNOI6KMGRcaqoSMZPZjjkpt33uV9IQCtkPnks1HyTt0IiImBDXpiiowHBD9WfAgMBAAECggEBAKz3PWYdrb8BAkDoccMWaKqI+ja5ReWbE7JweBtt0mc6yW9tRmJHwg5VHsYLmnLom5NmPhBakpb4QGmWMC7dmNSABnhuRPVJX+o1SZibFrfQwP+UIZZqVB92qQDtMKti++GIR4lJ6cQwX2aLi2tnTodOsKCYENw/7bsKTB+hovU/qEtpJwcLPdoGTSOHCKinh6vhSkf7Oyz5h+PQ+c7l2XnzmKarSXdmnovLB9sBAwNrJcepZ8BPXL10bv6AoXbzqgC+DrnsT3n9O1dVOQWSQ9FvFQ38vi3Q9akk2ckdOhYhqofg256KmUvjvGyenu41KzePeCAtXwLjSKkdI28ULAECgYEA7qZkargoXYbz5YgYJzqtO8dd5b3NbDf0WJjIvKAE4iP/wLFMucm3UVyDQ+DxyE6z92J8wEarrYvrC9QOjHE3IlrQOUYyimLBqcGui7ahKA0IB8Rm+7OW4LeWrNDBZndfzyTf5DmVGfX6CfvILnyyMLcUJ747pDxxzKqMqzV9dgECgYEAuejHdLFDXQNpb45J842k8ZsbkQqzPNvIVegTHPeCNMsl1l4QcJKPrs02RWlq27vN9sl1Vwbm0EBqXvJT05ZvKtfuCxqbtMQjof019o8ygjpff5jeGoY2j1uv5zMniFkTsMcGPR+yNgIRrqpJaoZZJBLbvT5RTIl8SrqQiZMRi58CgYEAv89YsjWlq9ZFvVwvHYiZp4xLudVdf/dRGsxhuslaY2/PpU5bfo/UGT6j+jCX5AjtuI2d+uRSI8BrgCxGLTbpu2EGLqJvCK7rPMeAxKZazNf8dlGy++aSA7dLEUcPyo1zogffM43ceusqtk95y3NJvMHJH1BUm2JBjOAfA5SQbAECgYA/Si703BAJz0qKrs8gOh1oHxzgYNsqIcxu6oXvO5e5L1ufQgCowkxl/vi14rB9Q89Xb7ghu3jCdtt/nVHKW5FW7ZHdd96ASLG0yQYg/Rj92q9+OeWK9BwI6/bTZ8fSlDiu2uKV1n+OAWBRrSk3OauJK15ha6CzxK5qpl7kZwv3EwKBgCrkqiCqXrTSoME0tLfGivOdx09rigeWXcUyIBDqz9iSWCffw+wVZeDGHjU2ptZBYZxYBtQeXVpNXTGoAfJ8dmFAiMLjOD5yp7I/qAs4yErPKvuLdDX3zwtSHVnOnNbfZPj3i3QibD6WDr/dmCL9VM7DW9ccKj+VsKOEf74Jjc2c";
    
    /**
     * public key for pay
     */
    public static final String PAY_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArU8/TECAXV3Nhnt24nBGVGIqNvkwMCk+Fc0m7IMb5hwk2MfykMkZrAbckE8YWchMaoN9uPLVTweqPqAoeXL6omSvtP7x223nlUdsy7dWtZj65sNposbwySAhqIwgNM5DNGGOOjzLsL4zbtXLspe4Yobd5RkjHwNqs7yZMyCF+mdzZXNL10FYFAPZKMOKERYIZz/U5zOkbJkqeKyXZwxRcW5QTvoomxS7x620+mk4ZbIKnRZJ9M/gyAdr6+XnRmIKwatZUFX781UFX/9BGshC7AVhZTTiOijBkXGqqEjGT2Y45Kbd97lfSEArZD55LNR8k7dCIiJgQ16YoqMBwQ/VnwIDAQAB";

    // portrait view for pay UI
	public static final int PAY_ORI = 1;
	// landscape view for pay UI
	public static final int PAY_ORI_LAND = 2;
    

    
     public static final String VALID_TOKEN_ADDR = "http://123.207.146.159/huaweisdk/login.php";
    
     public static final String GET_PAY_PRIVATE_KEY = "http://123.207.146.159/huaweisdk/pay.php";
    
     public static final String GET_BUOY_PRIVATE_KEY = "http://123.207.146.159/huaweisdk/iconkey.php";
    
    public interface PayParams
    {
        public static final String USER_ID = "userID";
        
        public static final String APPLICATION_ID = "applicationID";
        
        public static final String AMOUNT = "amount";
        
        public static final String PRODUCT_NAME = "productName";
        
        public static final String PRODUCT_DESC = "productDesc";
        
        public static final String REQUEST_ID = "requestId";
        
        public static final String USER_NAME = "userName";
        
        public static final String SIGN = "sign";
        
        public static final String NOTIFY_URL = "notifyUrl";
        
        public static final String SERVICE_CATALOG = "serviceCatalog";
        
        public static final String SHOW_LOG = "showLog";
        
        public static final String SCREENT_ORIENT = "screentOrient";
        
        public static final String SDK_CHANNEL = "sdkChannel";
        
        public static final String URL_VER = "urlver";
    }
    
}
