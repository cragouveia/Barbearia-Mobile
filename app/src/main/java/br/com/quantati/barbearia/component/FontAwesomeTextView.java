package br.com.quantati.barbearia.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by carlos on 6/26/16.
 */
public class FontAwesomeTextView extends TextView {
    public FontAwesomeTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/fontawesome-webfont.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
    }
}
