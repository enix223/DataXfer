package com.cel.dataxfer.controller;

import com.cel.dataxfer.interceptor.Auth;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

@Before(Auth.class)
public class AppController extends Controller {

}
