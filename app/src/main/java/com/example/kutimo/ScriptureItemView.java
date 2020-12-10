package com.example.kutimo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 */
public class ScriptureItemView extends LinearLayout {

    TextView titleText;
    TextView quoteText;
    Button linkButton;

    // For Static view
    @StyleableRes
    int title_index = 0;
    @StyleableRes
    int quote_index = 1;
    @StyleableRes
    int link_index = 2;

    /**
     * Dynamically create ScriptureItemView object to be appended to a parent.
     *
     * @param context Pass in from current activity.
     */
    public ScriptureItemView(Context context) {
        super(context);
        initializeViewById(context);
    }

    /**
     * Created for convenience of setting all text on one line.
     *
     * @param context  Pass in from current activity.
     * @param setTitle Set the resource title from Gospel Library.
     * @param setQuote Set the quote from Gospel Library.
     * @param setLink  Set the link from Gospel Library.
     */
    public ScriptureItemView(Context context, String setTitle, String setQuote, String setLink) {
        super(context);
        initializeViewById(context);

        setTitleText(setTitle);
        setQuoteText(setQuote);
        setButton(setLink);
    }

    /**
     * Created to handle incoming JSONObject that contains "title", "content", and "link", all as
     * strings.
     *
     * @param context   Pass in from current activity
     * @param scripture JSONObject containing "title", "content", "link" as string objects.
     */
    public ScriptureItemView(Context context, JSONObject scripture) {
        this(context, (String) scripture.get("title"), (String) scripture.get("content"), (String) scripture.get("link"));
    }

    /**
     * Automatically used by static in activity_<name>.xml
     */
    public ScriptureItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViewById(context);

        int[] sets = {R.attr.title, R.attr.quote, R.attr.link};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);

        setTitleText(typedArray.getText(title_index));
        setQuoteText(typedArray.getText(quote_index));
        setButton(typedArray.getText(link_index));
        typedArray.recycle();
    }

    /**
     * Sets up connection between View objects and findViewById function.
     * Used for both static and dynamic constructors.
     */
    private void initializeViewById(Context context) {
        inflate(context, R.layout.scripture_view_layout, this);
        titleText = (TextView) findViewById(R.id.title);
        quoteText = (TextView) findViewById(R.id.quote);
        linkButton = (Button) findViewById(R.id.link);
    }

    /**
     * @return text from titleText TextView object
     */
    public CharSequence getTitle() {
        return titleText.getText();
    }

    /**
     * @param new_title_text sets text for titleText TextView object
     */
    public void setTitleText(CharSequence new_title_text) {
        titleText.setText(new_title_text);
    }

    /**
     * @return text from titleText TextView object
     */
    public CharSequence getQuoteText() {
        return quoteText.getText();
    }

    /**
     * @param new_quote_text sets text for quoteText TextView object
     */
    public void setQuoteText(CharSequence new_quote_text) {
        quoteText.setText(new_quote_text);
    }

    /**
     * @return text from titleText TextView object
     */
    public CharSequence getButton() {
        // TODO: update properly to return link
        return linkButton.getText();
    }

    /**
     * @param new_button_link sets text for titleText TextView object
     */
    public void setButton(CharSequence new_button_link) {
        // TODO: update properly to launch link by pressing a button.
        linkButton.setText(new_button_link);
    }
}