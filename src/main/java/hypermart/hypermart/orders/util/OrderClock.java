package hypermart.hypermart.orders.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OrderClock {
    public static boolean isSpecialDiscountTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nineClock = LocalDateTime.now().with(LocalTime.NOON).plusHours(9);
        LocalDateTime tenClock = LocalDateTime.now().with(LocalTime.NOON).plusHours(10);

        return now.isAfter(nineClock) && now.isBefore(tenClock);
    }
}
