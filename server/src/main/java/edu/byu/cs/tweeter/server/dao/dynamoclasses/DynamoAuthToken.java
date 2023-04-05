package edu.byu.cs.tweeter.server.dao.dynamoclasses;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoAuthToken {
        private String authtoken;
        private String user_handle;
        private String timestamp;


        @DynamoDbPartitionKey
        @DynamoDbAttribute("authtoken")

        public String getAuthtoken() {
            return authtoken;
        }

        public void setAuthtoken(String authtoken) {
            this.authtoken = authtoken;
        }

        @DynamoDbAttribute("user_handle")
        public String getUser_handle() {
            return user_handle;
        }

        public void setUser_handle(String user) {
            this.user_handle = user;
        }
        @DynamoDbAttribute("timestamp")
        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }



        @Override
        public String toString() {
            return "DynamoAuthToken{" +
                    "authtoken='" + authtoken + '\'' +
                    ", user='" + user_handle +
                    ", timestamp='" + timestamp +
                    '}';
        }
}
