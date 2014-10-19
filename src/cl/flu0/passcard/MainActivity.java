package cl.flu0.passcard;

import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import org.pepsoft.passwordcard.*;
import org.pepsoft.util.*;

public class MainActivity extends ActionBarActivity {
	
	private final TextWatcher cardIdTextWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			// From http://www.41post.com/5108/programming/android-hexadecimal-color-input-using-an-edittext
			//Store the text on a String  
            String text = s.toString();  

            //Get the length of the String  
            int length = s.length();  

            /*If the String length is bigger than zero and it's not 
            composed only by the following characters: A to F and/or 0 to 9 */  
            if(!text.matches("[a-fA-F0-9]+") && length > 0) {  
                //Delete the last character  
                s.delete(length - 1, length);
            }
            
            genPassCard();
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	};

	private PasswordCard pCard;
	
	private long pCardId;
	private boolean incDigit = false;
	private boolean incSymbols = false;
	
	private TextView txvCardContent;
	
	private EditText etxCardId;
	private Button btnRndId;
	private CheckBox cbxIncNumbers;
	private CheckBox cbxIncSymbols;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getViews();
		genRndPassCardId();
		genPassCard();
	}

	private void getViews() {
		if (txvCardContent == null)
			txvCardContent = (TextView) findViewById(R.id.txvCardContent);
		
		if (etxCardId == null) {
			etxCardId = (EditText) findViewById(R.id.etxCardId);
			etxCardId.addTextChangedListener(cardIdTextWatcher);
		}
		
		if (btnRndId == null) {
			btnRndId = (Button) findViewById(R.id.btnRndId);
			btnRndId.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					genRndPassCardId();
				}
			});
		}
		
		if (cbxIncNumbers == null) {
			cbxIncNumbers = (CheckBox) findViewById(R.id.cbxIncNumbers);
			cbxIncNumbers.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					genPassCard();
				}
			});
		}
		
		if (cbxIncSymbols == null) {
			cbxIncSymbols = (CheckBox) findViewById(R.id.cbxIncSymbols);
			cbxIncSymbols.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					genPassCard();
				}
			});
		}
	}
	
	private void genPassCard() {
		if (etxCardId.getText().toString().isEmpty()) {
			txvCardContent.setText("");
			return;
		}
		
		pCardId = Tool.parseUnsignedHexLong(etxCardId.getText().toString());
		
		incDigit = cbxIncNumbers.isChecked();
		incSymbols = cbxIncSymbols.isChecked();
		
		pCard = new PasswordCard(pCardId, incDigit, incSymbols);
		txvCardContent.setText(pCard.getAsString("\n"));
	}
	
	private void genRndPassCardId() {
		etxCardId.setText(getRandomHexString(16));
	}
	
	// From http://stackoverflow.com/questions/14622622 
	private String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
