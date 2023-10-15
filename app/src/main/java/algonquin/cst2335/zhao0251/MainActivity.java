package algonquin.cst2335.zhao0251;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

/** This page is the first page of the application.
 * @author yingyi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
        /** This represents the string message at the top. */
        private TextView tv = null;
        /** This holds the text field to write password. */
        private EditText et = null;
        /** This holds the login button object. */
        private Button btn = null;

        /** This is the starting point for MainActivity
         *
         * @param savedInstanceState If the activity is being re-initialized after previously being shut
         *                           down then this Bundle contains the data it most recently supplied
         *                           in {@link #onSaveInstanceState}.     *
         */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            tv = findViewById(R.id.textView);
            et = findViewById(R.id.editText);
            btn = findViewById(R.id.button);

            btn.setOnClickListener( clk -> {
                String password = et.getText().toString();

                checkPasswordComplexity(password);

            });
        }

        /** This is function a string and returns true if it has meets the password requirements.
         *
         * @param pw The string object that we are checking
         * @return  Returns true if pw meets password requirements.
         */
        private boolean checkPasswordComplexity(String pw){
            boolean result = false;
            boolean foundUpperCase = false, foundLowerCase = false, foundNumber = false,
                    foundSpecial = false;


            for (int i = 0; i < pw.length(); i++) {
                char c = pw.charAt(i);
                if (Character.isUpperCase(c)) {
                    foundUpperCase = true;
                } else if (Character.isLowerCase(c)) {
                    foundLowerCase = true;
                } else if (Character.isDigit(c)) {
                    foundNumber = true;
                } else if (isSpecialCharacter(c)) {
                    foundSpecial = true;
                }
            }


            if (!foundUpperCase) {
                tv.setText("You are missing an upper case letter");
                //Toast.makeText(this, "You are missing an upper case letter", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!foundLowerCase) {
                tv.setText("You are missing a lower case letter");
                //Toast.makeText(this, "You are missing a lower case letter", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!foundNumber) {
                tv.setText("You are missing a number");
                //Toast.makeText(this, "You are missing a number", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!foundSpecial) {
                tv.setText("You are missing a special character");
                //Toast.makeText(this, "You are missing a special character", Toast.LENGTH_SHORT).show();
                return false;
            }
            tv.setText("Password meets complexity requirements");
            //Toast.makeText(this, "Password meets complexity requirements", Toast.LENGTH_SHORT).show();
            return foundUpperCase && foundLowerCase && foundNumber && foundSpecial;
        }

        /** Checks if the given character is a special character.
         *
         * @param c The character to be checked for special character status.
         * @return true if the character is a special character, false otherwise.
         */
        private boolean isSpecialCharacter(char c) {
            switch (c) {
                case '#':
                case '?':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '!':
                case '@':
                    return true;
                default:
                    return false;
            }
        }
    }