package com.example.kutimo;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Timothy
 */
public class ScriptureItemView extends LinearLayout {

    TextView titleText;
    TextView quoteText;
    Button linkButton;
    Button shareButton;

    private String gospel_link = "";

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

        shareButton.setOnClickListener(this::onShareButton);
        linkButton.setOnClickListener(this::onLinkButton);
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
        gospel_link = setLink;

        shareButton.setOnClickListener(this::onShareButton);
        linkButton.setOnClickListener(this::onLinkButton);
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
        gospel_link = (String) typedArray.getText(link_index);
        typedArray.recycle();

        shareButton.setOnClickListener(this::onShareButton);
        linkButton.setOnClickListener(this::onLinkButton);
    }

    public void onShareButton(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Send message");
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s\n\"%s\"\n\n%s", getTitle(), getQuoteText(), gospel_link));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        getContext().startActivity(shareIntent);

    }
    public void onLinkButton(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gospel_link));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(intent);
        }
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
        shareButton = (Button) findViewById(R.id.share);
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
}