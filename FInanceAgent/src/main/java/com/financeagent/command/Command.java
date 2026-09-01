package com.financeagent.command;

public interface Command<R> {

    R execute();

    String getName();
}
