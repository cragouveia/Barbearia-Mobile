package br.com.quantati.barbearia.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by carlos on 11/6/15.
 */
public class UbuntuLightTextView extends TextView {

    public UbuntuLightTextView(Context context) {
        super(context);
    }

    public UbuntuLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UbuntuLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/Ubuntu-L.ttf", context);
        setTypeface(customFont);
    }
}
