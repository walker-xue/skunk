package commons.test;

import java.util.UUID;

import com.skunk.core.utils.UUIDUtils;

public class UUIDUtilTest {

    public static void main(String[] args) {

        for (int i = 0; i < 20000; i++) {
            String randomUUID = UUIDUtils.get8UUID();

            System.out.println("randomUUID:" + randomUUID + ";" + randomUUID.length());
        }

        UUID uuid = UUID.randomUUID();

        System.out.println("getLeastSignificantBits:" + uuid.getLeastSignificantBits());
        System.out.println("getMostSignificantBits:" + uuid.getMostSignificantBits());
        System.out.println("version:" + uuid.version());
        //        System.out.println("node:" + uuid.node());
        //        System.out.println("timestamp:" + uuid.timestamp());
        for (int i = 0; i < 12; i++) {
            //            UUID uuid2 = UUID.nameUUIDFromBytes(new String("abc").getBytes());
            UUID uuid2 = UUID.randomUUID();
            System.out.println("version:" + uuid2.toString());
        }

    }

}
