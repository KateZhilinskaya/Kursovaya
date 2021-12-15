public class Query {

    final public static String SHOW_ALL_USERS = "SELECT * FROM users";
    final public static String SHOW_TRANSFER_MONEY = "SELECT * FROM transfer_money";
    final public static String SHOW_TYPE_DEPOSIT = "SELECT * FROM deposit_type";
    final public static String SHOW_ALL_DEPOSIT = "SELECT * FROM deposit";
    final public static String DEPOSIT_WHERE_USER_ID = "SELECT * FROM deposit WHERE user_id = ";
    final public static String DEPOSIT_WHERE_SUM_LESS = "SELECT * FROM deposit WHERE sum <= ";
    final public static String TRANSFER_MONEY_WHERE_SENDER_ID =  "SELECT * FROM transfer_money WHERE sender_id = ";
    final public static String TRANSFER_MONEY_WHERE_ACOOUNT_NUM = "SELECT * FROM transfer_money WHERE receive_num = ";
}
