package com.devfiles.enterprise.infrastructure.adapter.controller;

public interface AbstractLinks {
    String getRel();
    String getHref();
    String getTitle();
    String getMethod();
    boolean isRequiresAuth();
    boolean isTemplated();
}
