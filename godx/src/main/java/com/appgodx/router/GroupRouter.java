package com.appgodx.router;

import java.util.Map;

public interface GroupRouter {
    Map<String,Class<? extends PathRouter>> getGroupRouter();

}
