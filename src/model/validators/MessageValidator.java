package model.validators;

import java.util.ArrayList;
import java.util.List;

import model.Task;

public class MessageValidator {
    // validationを実行する
    public static List<String> validate(Task t) {
        List<String> errors = new ArrayList<String>();
        String content_error = _validateContent(t.getContent());
        if(!content_error.equals("")) {
            errors.add(content_error);
        }

        return errors;
    }

    // 内容の必須入力チェック
    private static String _validateContent(String content) {
        if(content == null || content.equals("")) {
            return "内容を入力してください";
        }
        return "";
    }
}
