package com.shinydev.ripple.model.common;

import java.util.List;

public abstract class AbstractRequest {

    public abstract String getMethod();

    public abstract List<?> getParams();

}
