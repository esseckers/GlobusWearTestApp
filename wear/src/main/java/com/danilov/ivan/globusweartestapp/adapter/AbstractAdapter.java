package com.danilov.ivan.globusweartestapp.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public abstract class AbstractAdapter<T> extends ArrayAdapter<T> {
    protected int resource;
    protected Context context;

    public AbstractAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
    }
}
