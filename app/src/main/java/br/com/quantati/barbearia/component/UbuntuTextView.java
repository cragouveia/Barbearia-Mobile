package br.com.quantati.barbearia.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by carlos on 11/6/15.
 */
public class UbuntuTextView extends TextView {
    public UbuntuTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }
    public UbuntuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }
    public UbuntuTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/Ubuntu-M.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
    }
}
