package com.example.securestoragelabjava;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.securestoragelabjava.cache.CacheStore;
import com.example.securestoragelabjava.files.InternalTextStore;
import com.example.securestoragelabjava.files.StudentsJsonStore;
import com.example.securestoragelabjava.model.Student;
import com.example.securestoragelabjava.prefs.AppPrefs;
import com.example.securestoragelabjava.prefs.SecurePrefs;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SecureStorageLab";
    private final List<String> langs = Arrays.asList("fr", "en", "ar");

    private EditText etName;
    private EditText etToken;
    private Spinner spLang;
    private SwitchCompat swDark;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etToken = findViewById(R.id.etToken);
        spLang = findViewById(R.id.spLang);
        swDark = findViewById(R.id.swDark);
        tvResult = findViewById(R.id.tvResult);

        setupLangSpinner();

        Button btnSavePrefs = findViewById(R.id.btnSavePrefs);
        Button btnLoadPrefs = findViewById(R.id.btnLoadPrefs);
        Button btnSaveJson = findViewById(R.id.btnSaveJson);
        Button btnLoadJson = findViewById(R.id.btnLoadJson);
        Button btnClear = findViewById(R.id.btnClear);

        btnSavePrefs.setOnClickListener(v -> savePrefs());
        btnLoadPrefs.setOnClickListener(v -> loadPrefsToUi());
        btnSaveJson.setOnClickListener(v -> saveJsonFile());
        btnLoadJson.setOnClickListener(v -> loadJsonFile());
        btnClear.setOnClickListener(v -> clearAll());

        loadPrefsToUi();
    }

    private void setupLangSpinner() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, langs);
        spLang.setAdapter(adapter);
    }

    private void savePrefs() {
        String name = etName.getText().toString().trim();
        String lang = langs.get(Math.max(0, spLang.getSelectedItemPosition()));
        String theme = swDark.isChecked() ? "dark" : "light";

        boolean ok = AppPrefs.save(this, name, lang, theme, false);

        String token = etToken.getText().toString();
        if (!token.isEmpty()) {
            try {
                SecurePrefs.saveToken(this, token);
            } catch (Exception e) {
                tvResult.setText("Erreur chiffrement token : " + e.getMessage());
                return;
            }
        }

        Log.d(TAG, "Prefs sauvegardées ok=" + ok + ", name=" + name + ", lang=" + lang + ", theme=" + theme);

        try {
            CacheStore.write(this, "last_ui.txt", "name=" + name + ", lang=" + lang + ", theme=" + theme);
        } catch (Exception ignored) {}

        tvResult.setText(
                "Sauvegarde terminée.\n" +
                "Nom : " + name + "\n" +
                "Langue : " + lang + "\n" +
                "Thème : " + theme + "\n" +
                "Token : Chiffré et horodaté."
        );
    }

    private void loadPrefsToUi() {
        AppPrefs.Triple triple = AppPrefs.load(this);

        etName.setText(triple.name);
        swDark.setChecked("dark".equals(triple.theme));

        int idx = langs.indexOf(triple.lang);
        spLang.setSelection(idx >= 0 ? idx : 0);

        String tokenStatus;
        try {
            String token = SecurePrefs.loadToken(this);
            if (token != null) {
                tokenStatus = "Présent (Long: " + token.length() + ")";
            } else if (SecurePrefs.isTokenExpired(this)) {
                tokenStatus = "Expiré (Invalide)";
            } else {
                tokenStatus = "Absent";
            }
        } catch (Exception e) {
            tokenStatus = "Erreur lecture";
        }

        tvResult.setText(
                "Données chargées :\n" +
                "Nom : " + triple.name + "\n" +
                "Langue : " + triple.lang + "\n" +
                "Thème : " + triple.theme + "\n" +
                "Token : " + tokenStatus
        );

        Log.d(TAG, "Chargement - Nom: " + triple.name + ", Token: " + tokenStatus);
    }

    private void saveJsonFile() {
        List<Student> students = Arrays.asList(
                new Student(1, "Amina", 20),
                new Student(2, "Omar", 21),
                new Student(3, "Sara", 19)
        );

        try {
            StudentsJsonStore.save(this, students);
            InternalTextStore.writeUtf8(this, "note.txt", "Sauvegarde JSON effectuée.");
        } catch (Exception e) {
            tvResult.setText("Erreur sauvegarde JSON : " + e.getMessage());
            return;
        }

        tvResult.setText("Fichier students.json créé avec " + students.size() + " entrées.");
    }

    private void loadJsonFile() {
        List<Student> students = StudentsJsonStore.load(this);
        String note;
        try {
            note = InternalTextStore.readUtf8(this, "note.txt");
        } catch (Exception e) {
            note = "(note.txt absent)";
        }

        StringBuilder sb = new StringBuilder("Contenu JSON :\n");
        sb.append("Note : ").append(note).append("\n");
        for (Student s : students) {
            sb.append("• ").append(s.name).append(" (").append(s.age).append(" ans)\n");
        }
        tvResult.setText(sb.toString());
    }

    private void clearAll() {
        AppPrefs.clear(this);
        try { SecurePrefs.clear(this); } catch (Exception ignored) {}
        StudentsJsonStore.delete(this);
        InternalTextStore.delete(this, "note.txt");
        int purged = CacheStore.purge(this);

        etName.setText("");
        etToken.setText("");
        swDark.setChecked(false);
        spLang.setSelection(0);

        tvResult.setText("Stockage intégralement nettoyé.\nCache : " + purged + " fichier(s) supprimé(s).");
    }
}
