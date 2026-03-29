package com.example.securestoragelabjava.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class SecurePrefs {

    private static final String PREFS_NAME = "secure_prefs";
    private static final String KEY_API_TOKEN = "secure_api_token";
    private static final String KEY_TOKEN_TIMESTAMP = "token_timestamp";
    
    // Durée de validité du token : 1 heure (en millisecondes)
    private static final long TOKEN_TTL = 3600 * 1000;

    private SecurePrefs() {}

    private static SharedPreferences getSecurePrefs(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void saveToken(Context context, String token) throws Exception {
        getSecurePrefs(context).edit()
                .putString(KEY_API_TOKEN, token)
                .putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis())
                .apply();
    }

    /**
     * Charge le token uniquement s'il n'est pas expiré.
     */
    public static String loadToken(Context context) throws Exception {
        SharedPreferences prefs = getSecurePrefs(context);
        long timestamp = prefs.getLong(KEY_TOKEN_TIMESTAMP, 0);
        
        if (System.currentTimeMillis() - timestamp > TOKEN_TTL) {
            // Token expiré : on le supprime par sécurité
            prefs.edit().remove(KEY_API_TOKEN).remove(KEY_TOKEN_TIMESTAMP).apply();
            return null;
        }
        
        return prefs.getString(KEY_API_TOKEN, null);
    }

    public static boolean isTokenExpired(Context context) throws Exception {
        long timestamp = getSecurePrefs(context).getLong(KEY_TOKEN_TIMESTAMP, 0);
        return timestamp != 0 && (System.currentTimeMillis() - timestamp > TOKEN_TTL);
    }

    public static void clear(Context context) throws Exception {
        getSecurePrefs(context).edit().clear().apply();
    }
}
