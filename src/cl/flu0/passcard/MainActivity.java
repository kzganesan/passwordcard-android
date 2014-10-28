package cl.flu0.passcard;

import java.util.Locale;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.Typeface;
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
	private Typeface freeMono;
	private EditText etxCardId;
	private Button btnRndId;
	private CheckBox cbxIncNumbers;
	private CheckBox cbxIncSymbols;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setUp();
	}

	private void setUp() {
		if (txvCardContent == null) {
			txvCardContent = (TextView) findViewById(R.id.txvCardContent);
			freeMono = Typeface.createFromAsset(getAssets(),"fonts/FreeMono.ttf");
			txvCardContent.setTypeface(freeMono);
		}
		
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
		etxCardId.setText(getRandomHexString(16).toUpperCase(Locale.US));
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
