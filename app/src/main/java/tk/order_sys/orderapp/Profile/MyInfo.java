package tk.order_sys.orderapp.Profile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class MyInfo {
    public static String getGoogleMail(Context context,String mType) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager, mType);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager, String mType) {
        Account[] accounts = accountManager.getAccountsByType(mType);
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String getPhoneNumber(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
}
