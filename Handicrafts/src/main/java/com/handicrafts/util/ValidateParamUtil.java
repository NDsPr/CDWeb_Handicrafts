package com.handicrafts.util;

import java.util.ArrayList;
import java.util.List;

public class ValidateParamUtil {
    public static boolean isInteger(String param) {
        if (param == null || param.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String param) {
        if (param == null && param.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<String> checkEmptyParam(String ...fields) {
        ArrayList<String> errors = new ArrayList<>();
        for (String field : fields) {
            if (BlankInputUtil.isBlank(field)) {
                errors.add("e");
            } else {
                errors.add(null);
            }
        }
        return errors;
    }
}
