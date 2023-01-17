package hypermart.hypermart.orders.util;

import hypermart.hypermart.item.model.Item;
import hypermart.hypermart.member.model.Member;
import hypermart.hypermart.member.model.MemberGrade;

public class DiscountPolicy {

    public static int calculateDiscount(Item item, Member member, int orderCount) {
        int totalPrice = item.getPrice() * orderCount;
        MemberGrade memberGrade = member.getMemberGrade();

        if (memberGrade == MemberGrade.BRONZE) {
            return totalPrice;
        }
        if (memberGrade == MemberGrade.SILVER) {
            return totalPrice * 99 / 100;
        }
        if (memberGrade == MemberGrade.GOLD) {
            return totalPrice * 97 / 100;
        }
        if (memberGrade == MemberGrade.PLATINUM) {
            return totalPrice * 95 / 100;
        }
        return totalPrice * 92 / 100;
    }

    public static int calculateSpecialDiscount(Item item, int orderCount) {
        int totalPrice = item.getPrice() * orderCount;

        return totalPrice * 80 / 100;
    }
}
