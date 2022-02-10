package com.benardmathu.hfms.api.base;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;

/**
 * @author bernard
 */
public abstract class BaseController {
    protected final String TAG = this.getClass().getSimpleName();

    protected Gson gson = new Gson();
    protected PrintWriter writer;
}