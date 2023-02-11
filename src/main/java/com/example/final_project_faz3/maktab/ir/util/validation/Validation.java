package com.example.final_project_faz3.maktab.ir.util.validation;

import com.google.common.io.Files;

import javax.validation.ValidationException;
import java.io.File;

public class Validation {

    public static TriConsumer<String, String, String> validate = (s, r, m) -> {
        if (s.equals("") || !s.matches(r))
            throw new ValidationException(m);
    };

    public static void validatePassword(String password) {
        validate.accept(password, "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).{8,}$",
                "Invalid Password( 8 characters,contain at least one uppercase,one lowercase,one digit,one special character @#$%& )");
    }

    public static void validateEmail(String email) {
        validate.accept(email, "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", "Invalid email format! ");
    }

    public static void validateImageFormat(File file) {
        String fileExtension = Files.getFileExtension(file.getName());
        validate.accept(fileExtension, "jpg", "format not acceptable it should be jpg ");
    }

    public static void validatePhotoSize(byte[] bytes) {//TODO: write check size joda
        int length = bytes.length;
        int permittedLength = 300000;
        String value = String.valueOf(length);
        String value1 = String.valueOf(permittedLength);
        validate.accept(value, value1, "Photo size limited");
    }

}
